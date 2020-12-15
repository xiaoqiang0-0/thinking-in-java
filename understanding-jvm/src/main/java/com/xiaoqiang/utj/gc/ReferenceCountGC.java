package com.xiaoqiang.utj.gc;

/**
 * 判定对象是否存活
 *  1) 引用计数法
 *      原理很简单，就是引用上增加一个计数器，每次被引用则计数器加一，失效或释放减一，为零即不可能再被使用。
 *  2) 可达性分析法
 *      通过可达性分析算法判定对象是否存活，原理：通过 `GC Roots` 的对象作为起始节点集，从这些节点开始根据引用关系向下搜索，索索过程中走过
 *      的路径称为“引用链”,如果某个对象到 `GC Roots` 之间没有任何“引用链”，则此正面此对象不可能再被使用，也就可以判定其为可回收对象。
 *      GC Roots:
 *      a) 虚拟机栈中引用的对象，比如各个线程被对用的方法对战中使用到的参数局部变量临时变量等。
 *      b) 在方法区中类静态属性引用的对象，比如java类的引用类型静态变量
 *      c) 在方法区中常量引用的对象，比如字符串常量池（String table）里的引用。
 *      d) 在本地方法栈中JNI引用的对象。
 *      e) jvm内部的引用，如基本数据类型对应的Class对象，一些常驻的异常对象等，患有系统类加载器。
 *      f) 所有被同步锁持有的对象。
 *      g) 反映jvm内部情况的JMXBean，JVMTI 中注册的回调，本地代码缓存等。
 *
 * 分析jvm采用何种方式：
 *     如果采用引用计数法，如下示例中则不可能回收objA和objB，且将堆内存最大设置为`-Xmx7M`时应该直接出现OOM，但是实际上并不会出现，
 * 而且通过添加`-XX:+PrintGCDetails`，可以从日志确认对象的回收。
 */
public class ReferenceCountGC {
    public Object instance = null;

    private static final int _1MB = 1024 * 1024;

    private byte[] bigSize = new byte[2 * _1MB];

    public static void testGC() {
        ReferenceCountGC objA = new ReferenceCountGC();
        ReferenceCountGC objB = new ReferenceCountGC();
        objA.instance = objB;
        objB.instance = objA;

        objA = null;
        objB = null;

//        System.gc();
    }

    public static void main(String[] args) {
        testGC();
        testGC();
    }
}
