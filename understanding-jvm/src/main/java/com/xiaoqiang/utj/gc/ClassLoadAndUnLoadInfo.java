package com.xiaoqiang.utj.gc;

/**
 * -Xnoclassgc 可用于关闭类型的加载与卸载
 * -verbose:class -XX:+TraceClassLoading 显示类加载信息
 * -XX:TraceClassUnLoading  显示类卸载信息（此参数需要FastDebug版的虚拟机支持没有测试）
 * 大量使用反射动态代理的时候需要使用jvm具备类型卸载能力，以保证不会对方法区造成过大的内存压力
 *
 * ```
 * [0.002s][warning][arguments] -XX:+TraceClassLoading is deprecated. Will use -Xlog:class+load=info instead.
 * [0.006s][info   ][class,load] opened: C:\Program Files\Java\jdk-11.0.5\lib\modules
 * [0.015s][info   ][class,load] java.lang.Object source: jrt:/java.base
 * ...
 * [0.100s][info   ][class,load] java.lang.Shutdown$Lock source: jrt:/java.base
 * [0.100s][info   ][class,load] java.lang.invoke.VarHandleObjects$FieldInstanceReadOnly source: jrt:/java.base
 * [0.100s][info   ][class,load] java.lang.invoke.VarHandleObjects$FieldInstanceReadWrite source: jrt:/java.base
 * ```
 */
public class ClassLoadAndUnLoadInfo {
    public static void main(String[] args) {
        //Do something...
    }
}
