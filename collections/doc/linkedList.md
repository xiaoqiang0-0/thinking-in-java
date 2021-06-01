# LinkedList

## 定义
```java
public class LinkedList<E>
    extends AbstractSequentialList<E>
    implements List<E>, Deque<E>, Cloneable, java.io.Serializable
{
//...
}
```
linkedList实现为双向链表  
节点定义：
```java
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
```
从类的签名中就可以看到linkedList继承自`java.util.AbstractSequentialList`，而`java.util.AbstractSequentialList`继承自`java.util.AbstractList`，也就意味着它和ArraysList具备类许多的公共方法实现
同时，实现了`java.util.Deque`,所以LinkedList不仅仅具有链表的，功能，同样可以直接拿来当`Stack`和`Queue`使用。
