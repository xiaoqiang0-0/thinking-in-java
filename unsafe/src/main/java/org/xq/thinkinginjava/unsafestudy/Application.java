package org.xq.thinkinginjava.unsafestudy;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class Application {
    public static void main(String[] args) throws InterruptedException {

        try {
            Unsafe unsafe = UnsafeUtils.getUnsafe();
            System.out.println(unsafe);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println("Unsafe study!");
        Thread.sleep(200*1000);
    }
}
