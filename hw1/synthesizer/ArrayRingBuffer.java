package synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T>  extends AbstractBoundedQueue<T>{
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        fillCount = 0;
        this.capacity = capacity;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(T x) {
        if (isFull()) {
            throw new RuntimeException("Ring Buffer Overflow");
        }
        if (last == capacity) {
            rb[0] = x;
            last = 1;
        } else {
            rb[last] = x;
            last += 1;
        }
        fillCount += 1;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer Underflow");
        }
        T output = rb[first];
        if (first == capacity - 1) {
            first = 0;
        } else {
            first += 1;
        }
        fillCount -= 1;
        return output;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    @Override
    public T peek() {
        return rb[first];
    }

    public T peek(int i) {
        if (i >= fillCount) {
            throw new IndexOutOfBoundsException();
        }
        if (first + i < capacity) {
            return rb[first + i];
        } else {
            return rb[first + i - capacity];
        }

    }

    @Override
    public Iterator<T> iterator() {
        return new ARBIterator();
    }

    private class ARBIterator implements Iterator<T>{
        private int pointer;
        public ARBIterator() {
            pointer = first;
        }

        public boolean hasNext() {
            return !(pointer + 1 == last);
        }

        public T next() {
            T out = rb[pointer];
            if (pointer == capacity - 1) {
                pointer = 0;
            } else {
                pointer += 1;
            }
            return out;
        }
    }

}
