package com.xiaoqiang.tij.async;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureDemo {
    public static void main(String[] args) {
        combine();
        exceptionHandler();
    }

    /**
     * 合并两个异步操作的结果
     */
    public static void combine() {
        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "world";
        }), (s1, s2) -> s1 + " " + s2).join();
        System.out.println(result);
    }

    /**
     * 异常处理
     */
    public static void exceptionHandler() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> Integer.parseInt("ILLEGAL"))
                .thenApply(r -> r * 2 * Math.PI)
                .thenApply(s -> "apply>> " + s)
                .exceptionally(ex -> "Error: " + ex.getMessage());
        System.out.println(future.join());
    }
}
