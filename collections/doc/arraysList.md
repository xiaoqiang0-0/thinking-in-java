# ArraysList

## 数据结构
```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    transient Object[] elementData; 
}
```
内部依赖Object数组存储数据

## 实例化
```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    private static final int DEFAULT_CAPACITY = 10;
    transient Object[] elementData; 
    private int size;
    private static final Object[] EMPTY_ELEMENTDATA = {};
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        }
    }

    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    public ArrayList(Collection<? extends E> c) {
        elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            if (elementData.getClass() != Object[].class)
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }
}
```
默认构造函数调用后直接将`elementData`指向默认空的`DEFAULTCAPACITY_EMPTY_ELEMENTDATA`,
如果指明初始化大小，则根据初始化大小情况创建，为0指向默认控数组，大于0创建目标大小数组，否则抛出异常。
传入集合，则调用原集合的toArray()将数据转入elementData，检查elementData数据长度，修改size大小，如果size==0，则将this.elementData指回EMPTY_ELEMENTDATA，新的临时数组等待回收，否则再根据数据类型是否为`Object[].class`进行数据内容的拷贝。
> toArray()实现
> ```java
>     //ArraysList
>     public Object[] toArray() {
>         return Arrays.copyOf(elementData, size);
>     }
>     //LinkedList
>     public Object[] toArray() {
>         Object[] result = new Object[size];
>         int i = 0;
>         for (Node<E> x = first; x != null; x = x.next)
>             result[i++] = x.item;
>         return result;
>     }
>     //Set没有单独实现，直接继承自AbstractCollection
>     public Object[] toArray() {
>         Object[] r = new Object[size()];
>         Iterator<E> it = iterator();
>         for (int i = 0; i < r.length; i++) {
>             if (! it.hasNext()) // fewer elements than expected
>                 return Arrays.copyOf(r, i);
>             r[i] = it.next();
>         }
>         return it.hasNext() ? finishToArray(r, it) : r;
>     }
> ```
## 扩容
```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    private static final int DEFAULT_CAPACITY = 10;
    
    private int size;

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    /**
    * 扩容实现
    */
    private Object[] grow(int minCapacity) {
        return elementData = Arrays.copyOf(elementData,
                                           newCapacity(minCapacity));
    }
    /**
    * 扩容新容量计算
    */
    private int newCapacity(int minCapacity) {
        
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity <= 0) {
            if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
                return Math.max(DEFAULT_CAPACITY, minCapacity);
            if (minCapacity < 0) 
                throw new OutOfMemoryError();
            return minCapacity;
        }
        return (newCapacity - MAX_ARRAY_SIZE <= 0)
            ? newCapacity
            : hugeCapacity(minCapacity);
    }
}
```
扩容触发点
`java.util.ArrayList.add(int, E)`,  
`java.util.ArrayList.add(E, java.lang.Object[], int)`,  
`java.util.ArrayList.addAll(java.util.Collection<? extends E>)`,  
`java.util.ArrayList.addAll(int, java.util.Collection<? extends E>)`,  
`java.util.ArrayList.ensureCapacity`,  
前四个均为添加元素时，自动触发
最后一个为手动初始化，可用于预测会有大量插入操作的时候，提前直接初始化一步到位，减少多次的扩容，提高性能。

## `java.util.Arrays.ArrayList`
上文主要内容都是在对于`java.util.ArrayList`的分析。  

对于`java.util.Arrays.ArrayList`与`java.util.ArraysList`对比，略有不同，前者更像是后者的简化版，同样是对于`java.util.AbstractList`的实现，但是前者并没有扩展像扩容之类的操作。
也就意味着通过`Arrays.asList()`创建的`ArraysList`，一旦创建，添加数据，也无法进行扩容。
```java
private static class ArrayList<E> extends AbstractList<E>
        implements RandomAccess, java.io.Serializable
    {
        private static final long serialVersionUID = -2764017481108945198L;
        private final E[] a;

        ArrayList(E[] array) {
            a = Objects.requireNonNull(array);
        }

        @Override
        public int size() {
            return a.length;
        }

        @Override
        public Object[] toArray() {
            return Arrays.copyOf(a, a.length, Object[].class);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T[] toArray(T[] a) {
            int size = size();
            if (a.length < size)
                return Arrays.copyOf(this.a, size,
                                     (Class<? extends T[]>) a.getClass());
            System.arraycopy(this.a, 0, a, 0, size);
            if (a.length > size)
                a[size] = null;
            return a;
        }

        @Override
        public E get(int index) {
            return a[index];
        }

        @Override
        public E set(int index, E element) {
            E oldValue = a[index];
            a[index] = element;
            return oldValue;
        }

        @Override
        public int indexOf(Object o) {
            E[] a = this.a;
            if (o == null) {
                for (int i = 0; i < a.length; i++)
                    if (a[i] == null)
                        return i;
            } else {
                for (int i = 0; i < a.length; i++)
                    if (o.equals(a[i]))
                        return i;
            }
            return -1;
        }

        @Override
        public boolean contains(Object o) {
            return indexOf(o) >= 0;
        }

        @Override
        public Spliterator<E> spliterator() {
            return Spliterators.spliterator(a, Spliterator.ORDERED);
        }

        @Override
        public void forEach(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            for (E e : a) {
                action.accept(e);
            }
        }

        @Override
        public void replaceAll(UnaryOperator<E> operator) {
            Objects.requireNonNull(operator);
            E[] a = this.a;
            for (int i = 0; i < a.length; i++) {
                a[i] = operator.apply(a[i]);
            }
        }

        @Override
        public void sort(Comparator<? super E> c) {
            Arrays.sort(a, c);
        }

        @Override
        public Iterator<E> iterator() {
            return new ArrayItr<>(a);
        }
    }
```
对照查看`java.util.AbstractList`中的定义
可以很明显看到`java.util.AbstractList.remove`,`java.util.AbstractList.add(int, E)`的实现都是直接抛出`java.lang.UnsupportedOperationException`.
