# 虚拟机类的加载

## 类的加载过程

### 加载
1) 加载类的二进制流
2) 将字节流转化为方法区的运行时数据结构
3) 在内存中生成class对象，作为方法区这个类的数据访问入口
> 

### 验证
校验class字节流中包含的信息符合虚拟机规范
1) 文件格式验证    
基于二进制进行，通过校验后，将会写入方法区  
2) 元数据验证 （是否有父类，是否有不允许的继承，非抽象类的方法实现是否完整，是否与父类冲突等等）  
主要是对于累的元数据信息进行语义校验
3) 字节码验证  
在第二阶段校验基础上对累的方法体进行校验分析，保证方法运行时不会危害到虚拟机
4) 应用符号验证  
检查是否缺少或者被禁止访问他以来的某些外部类，方法字段等资源。

### 准备
正式为类中的定义的变量（静态变量）分配内存并设置变量初始值（并非初始化，只是设置零值）
> 常量会被直接初始化


### 解析
将常量池内的符号引用替换为直接引用
1) 类活接口的解析
2) 字段解析
3) 方法解析
4) 接口方法解析

### 初始化
先执行父类静态代码的初始化，然后执行子类中静态代码块的初始化

## 类加载器
将类加载到虚拟机中的代码，叫做类加载器

`java.lang.ClassLoader`
核心方法：`java.lang.ClassLoader.loadClass(java.lang.String, boolean)`

### 启动类加载器
负责加载`<JAVA_HOME>\lib`目录，或者被`Xbootclasspath`指定的路径，而且是虚拟机能识别的（类似rt.jar,tools.jar，名字不符合及时在lib下也不会被加载）
无法通过应用层控制

### 扩展类加载器
`sun.misc.Launcher.ExtClassLoader`

### 应用程序类加载器
`sun.misc.Launcher.AppClassLoader`

`Java9`之前都是有这三种类加载器配合完成加载，有必要的时候也可以自定义

### 双亲委派模型
`Java`的类加载器分启动类加载器，和其他类加载器
启动类加载器由虚拟机实现，是虚拟机的一部分。其他类加载器均继承自`java.lang.ClassLoader`
自`1.2`一来，一直保持三层加载器，双骑委派的类加载机制。
双亲委派机制：
类加载器收到加载亲求后并不会直接去自己尝试加载，而是吧请求委托给父类，如果父类无法完成，则子类尝试自己加载，每一层加载器都是如此。如此一来，加载器之间有了优先关系，就保证了加载的类的唯一性
实现：
```java
protected Class<?> loadClass(String name, boolean resolve)
        throws ClassNotFoundException
    {
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();
                try {
                    if (parent != null) {
                        c = parent.loadClass(name, false);
                    } else {
                        c = findBootstrapClassOrNull(name);
                    }
                } catch (ClassNotFoundException e) {
                    // ClassNotFoundException thrown if class not found
                    // from the non-null parent class loader
                }

                if (c == null) {
                    // If still not found, then invoke findClass in order
                    // to find the class.
                    long t1 = System.nanoTime();
                    c = findClass(name);

                    // this is the defining class loader; record the stats
                    PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                    PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                    PerfCounter.getFindClasses().increment();
                }
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }
```
因此如果要破坏这种双亲委派只需直接去自定义类加载器，`@Override` `java.lang.ClassLoader.loadClass(java.lang.String, boolean)`方法即，直接自定义加载流程，跳过先去委托父类处理的逻辑即可


