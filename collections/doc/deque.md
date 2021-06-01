# Deque
## 定义
```java
public interface Deque<E> extends Queue<E> {
    
    void addFirst(E e);
    void addLast(E e);
    boolean offerFirst(E e);
    boolean offerLast(E e);
    E removeFirst();
    E removeLast();
    E pollFirst();
    E pollLast();
    E getFirst();
    E getLast();
    E peekFirst();
    E peekLast();
    boolean removeFirstOccurrence(Object o);
    boolean removeLastOccurrence(Object o);
    boolean add(E e);
    boolean offer(E e);
    E remove();
    E poll();
    E element();
    E peek();
    boolean addAll(Collection<? extends E> c);
    void push(E e);
    E pop();
    boolean remove(Object o);
    boolean contains(Object o);
    int size();
    Iterator<E> iterator();

}
```
首先命名上就是一个双端队列，然后从接口的定义中可以看到，不仅包括了队列的操作，同时包括了栈的操作，`push`和`pop`
## 具体实现
### [LinkedList](linkedList.md)
### ArrayDeque
对比ArrayList扩容上
```java
    private void grow(int needed) {
        final int oldCapacity = elements.length;
        int newCapacity;
        int jump = (oldCapacity < 64) ? (oldCapacity + 2) : (oldCapacity >> 1);
        if (jump < needed
            || (newCapacity = (oldCapacity + jump)) - MAX_ARRAY_SIZE > 0)
            newCapacity = newCapacity(needed, jump);
        final Object[] es = elements = Arrays.copyOf(elements, newCapacity);
        if (tail < head || (tail == head && es[head] != null)) {
            int newSpace = newCapacity - oldCapacity;
            System.arraycopy(es, head,
                             es, head + newSpace,
                             oldCapacity - head);
            for (int i = head, to = (head += newSpace); i < to; i++)
                es[i] = null;
        }
    }
    private int newCapacity(int needed, int jump) {
        final int oldCapacity = elements.length, minCapacity;
        if ((minCapacity = oldCapacity + needed) - MAX_ARRAY_SIZE > 0) {
            if (minCapacity < 0)
                throw new IllegalStateException("Sorry, deque too big");
            return Integer.MAX_VALUE;
        }
        if (needed > jump)
            return minCapacity;
        return (oldCapacity + jump - MAX_ARRAY_SIZE < 0)
            ? oldCapacity + jump
            : MAX_ARRAY_SIZE;
    }
```
默认初始化大小为16，与ArrayList的10不同，且直接初始化大小为16的数组，并非使用时才扩容

