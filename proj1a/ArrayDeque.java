public class ArrayDeque<Item> {
    private Item[] items;
    private int size;
    private int pointer;

    public ArrayDeque() {
        items = (Item[]) new Object[8];
        size = 0;
        pointer = 0;
    }

    private void resize(int i) {
        Item[] newItems = (Item[]) new Object[i];
        if (pointer+size>items.length) {
            System.arraycopy(items, pointer, newItems, 0, items.length - pointer);
            System.arraycopy(items, 0, newItems, items.length - pointer, size + pointer - items.length);
        } else {
            System.arraycopy(items, pointer, newItems, 0, size);
        }
        items = newItems;
        pointer = 0;
    }

    public void addFirst(Item i) {
        if (size == items.length) {
            resize(size*2);
        }

        if (pointer == 0) {
            pointer = items.length - 1;
        } else {
            pointer = pointer -1;
        }
        items[pointer] = i;
        size += 1;
    }

    public void addLast(Item i) {
        if (size == items.length) {
            resize(size*2);
        }
        if (pointer+size>items.length-1) {
            items[pointer + size - items.length] = i;
        } else {
            items[pointer + size] = i;
        }
        size += 1;
    }

    public boolean isEmpty() {
        return size==0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int p = pointer;
        for (int i=0;i<size;i++) {
            System.out.print(items[p]);
            System.out.print(" ");
            if (p==items.length-1) {
                p = 0;
            } else {
                p += 1;
            }
        }
    }

    public void printDequeMod() {
        System.out.print("Members of the list:");
        int p = pointer;
        for (int i=0;i<size;i++) {
            System.out.print(items[p]);
            System.out.print(" ");
            if (i%30 == 29) {
                System.out.print("\n");
            }
            if (p==items.length-1) {
                p = 0;
            } else {
                p += 1;
            }
        }
        System.out.print("\n");
        System.out.print("size:");
        System.out.print(size);
        System.out.print(" item_number:");
        System.out.println(items.length);
    }

    public Item removeFirst() {
        Item a = items[pointer];
        if (pointer==items.length-1) {
            pointer = 0;
        } else {
            pointer += 1;
        }
        size -= 1;

        if ((float) size/(float) items.length<0.3) {
            resize(items.length/2);
        }
        return a;
    }

    public Item removeLast() {
        int end;
        if (pointer + size>items.length) {
            end = pointer + size - items.length - 1;
        } else {
            end = pointer + size - 1;
        }
        Item a = items[end];
        size -= 1;

        if ((float) size/(float) items.length<0.3) {
            resize(items.length/2);
        }
        return a;
    }

    public Item get(int i) {
        if (i>size-1) {
            return null;
        } else if (pointer+i>items.length-1) {
            return items[pointer+i-items.length];
        } else {
            return items[pointer+i];
        }
    }
}
