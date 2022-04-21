
public class LinkedListDeque<Item> implements Deque<Item>{
    private class Node{
        public Item item;
        public Node prev;
        public Node next;

        /**Initialize a node.*/
        public Node(Item i,Node p,Node n) {
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

    public LinkedListDeque(Item i) {
        sentinel = new Node(null,null,null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        sentinel.next = new Node(i,sentinel,sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size = 1;
    }

    @Override
    public void addFirst(Item i) {
        sentinel.next = new Node(i,sentinel,sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size += 1;
    }

    @Override
    public void addLast(Item i) {
        sentinel.prev = new Node(i,sentinel.prev,sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size += 1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node n = sentinel;
        while (n.next != sentinel) {
            System.out.print(n.next.item);
            System.out.print(' ');
            n = n.next;
        }
    }

    @Override
    public Item removeFirst() {
        Item firstItem = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size = size - 1;
        return firstItem;
    }

    @Override
    public Item removeLast() {
        Item lastItem = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size = size - 1;
        return lastItem;
    }

    @Override
    public Item get(int index) {
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
}
