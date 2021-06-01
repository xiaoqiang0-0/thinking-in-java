# Unsafe

## 获取unsafe实例
在通过调用`Unsafe.getUnsafe()`方法时，会校验调用类是否为`Bootstrap Classloader`加载的类，否则抛出异常
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
### 通过反射获取
```java
public class UnsafeUtils{
    public Unsafe reflectUnsafe(){
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
> `-Xbootclasspath/a`,` -Xbootclasspath/p`, `-Xbootclasspath` 区别
