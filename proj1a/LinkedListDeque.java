
public class LinkedListDeque<T> {
    private class Node{
        public T item;
        public Node prev;
        public Node next;

        /**Initialize a node.*/
        public Node(T i,Node p,Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node(null,null,null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    public LinkedListDeque(T i) {
        sentinel = new Node(null,null,null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        sentinel.next = new Node(i,sentinel,sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size = 1;
    }

    public void addFirst(T i) {
        sentinel.next = new Node(i,sentinel,sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size += 1;
    }

    public void addLast(T i) {
        sentinel.prev = new Node(i,sentinel.prev,sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node n = sentinel;
        while (n.next != sentinel) {
            System.out.print(n.next.item);
            System.out.print(' ');
            n = n.next;
        }
    }

    public T removeFirst() {
        T firstItem = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size = size - 1;
        return firstItem;
    }

    public T removeLast() {
        T lastItem = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size = size - 1;
        return lastItem;
    }

    public T get(int index) {
        if (index > size - 1) {
            return null;
        } else {
            Node n = sentinel;
            while (index > 0) {
                n = n.next;
                index = index - 1;
            }
            return n.next.item;
        }
    }

    public static void main(String[] args) {
        LinkedListDeque<Integer> l = new LinkedListDeque<>();
        l.addFirst(8);
        l.addLast(7);
        l.addLast(5);
        l.addLast(6);
        int last = l.removeLast();
        System.out.println(last);
        l.printDeque();
        System.out.println("");
        System.out.println(l.get(3));
    }
}
