# Unsafe

## 获取unsafe实例
Unsafe部分源码
```java
public final class Unsafe {
    //...
    @CallerSensitive
    public static Unsafe getUnsafe() {
        Class<?> caller = Reflection.getCallerClass();
        if (!VM.isSystemDomainLoader(caller.getClassLoader()))
            throw new SecurityException("Unsafe");
        return theUnsafe;
    }
    //...
}
```
很明显在通过调用`Unsafe.getUnsafe()`方法时，会校验调用类是否为`Bootstrap Classloader`加载的类，否则抛出异常  
因此在获取Unsafe实例是就只能解决这个验证问题，或者是通过反射获取，因此可以有如下实现：
```java
public class UnsafeUtils {

    public static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        ClassLoader loader = UnsafeUtils.class.getClassLoader();
        if (loader == null || loader == ClassLoader.getPlatformClassLoader()) {
            return Unsafe.getUnsafe();
        }
        return reflectUnsafe();
    }

    private static Unsafe reflectUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return (Unsafe) field.get(Unsafe.class);
    }
}
```
### 通过参数使得当前`jar`包通过`Bootstrap ClassLoader`加载

```bash
java -Xbootclasspath/a:target/unsafe-1.0-SNAPSHOT.jar -jar target/unsafe-1.0-SNAPSHOT.jar
```
如果将此`Jar`加入-`Xbootclasspath`加载,则通过反射获取，否则直接调用`Unsafe.getUnsafe()`
> `-Xbootclasspath/a`,` -Xbootclasspath/p`, `-Xbootclasspath` 区别,参考[Oracle文档](https://docs.oracle.com/cd/E15289_01/JRCLR/optionx.htm).
> The -Xbootclasspath option specifies a list of directories, JAR files, and ZIP archives to search for bootstrap classes and resources. These are used in place of the bootstrap class files included in the Java SE JDK.  
> The -Xbootclasspath/a option is similar to -Xbootclasspath in that it specifies a list of directories, JAR files, and ZIP archives; however, the list is appended to the default bootstrap class path.  
> The -Xbootclasspath/p option is similar to -Xbootclasspath in that it specifies a list of directories, JAR files, and ZIP archives; however, the list is prepended to the default bootstrap class path.  
> 其中`-Xbootclasspath`会替代所有的包括jdk所包含的classpath，`-Xbootclasspath/a`后边追加，`-Xbootclasspath/p`前边插入，明显根据常识，参考环境变量的配置如果前边插入则会覆盖原有的一些配置，因此，优先使用`-Xbootclasspath/a`。至于完全替换所有的`-Xbootclasspath`自然更不可能推介使用。
