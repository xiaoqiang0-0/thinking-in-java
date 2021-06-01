package org.xq.thinkinginjava.unsafestudy;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeUtils {

    public static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        ClassLoader loader = UnsafeUtils.class.getClassLoader();
        if (loader == null || loader == ClassLoader.getPlatformClassLoader()) {
            System.out.println("---------------直接获取----------------");
            return Unsafe.getUnsafe();
        }
        return reflectUnsafe();
    }

    private static Unsafe reflectUnsafe() throws NoSuchFieldException, IllegalAccessException {
        System.out.println("---------------通过反射获取----------------");
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return (Unsafe) field.get(Unsafe.class);
    }
}
