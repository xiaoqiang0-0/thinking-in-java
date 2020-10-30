package com.xiaoqiang.cp.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MCSLock implements Lock {
    private final ThreadLocal<Node> node = ThreadLocal.withInitial(Node::new);
    private final AtomicReference<Node> tail = new AtomicReference<>(null);

    private static class Node {
        private volatile boolean locked;
        private volatile Node next;
    }

    @Override
    public void lock() {
        Node node = this.node.get();
        node.locked = true;
        Node pre = this.tail.getAndSet(node);
        if (pre!=null) {
            pre.next = node;
            while (node.locked);
        }
    }

    @Override
    public void unlock() {
        Node node = this.node.get();
        if (node.next == null) {
            if (tail.compareAndSet(node, null)) {
                return;
            }
            while (node.next == null) ;
        }
        node.next.locked = false;
        node.next = null;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
    }

    @Override
    public boolean tryLock() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }
}
