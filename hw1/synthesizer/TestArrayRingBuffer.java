package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer(3);
        arb.enqueue(1);
        arb.enqueue(2);
        arb.enqueue(3);
        arb.dequeue();
        arb.enqueue(4);
        arb.dequeue();
        arb.enqueue(5);
        arb.dequeue();
        arb.enqueue(6);
        assertEquals(4,arb.peek());
        assertEquals(4,arb.dequeue());
        assertEquals(5,arb.peek());
        assertEquals(5,arb.dequeue());
        assertEquals(6,arb.peek());
        assertEquals(6,arb.dequeue());

    }

    @Test
    public void testIterator() {
        ArrayRingBuffer arb = new ArrayRingBuffer(3);
        arb.enqueue(1);
        arb.enqueue(2);
        arb.enqueue(3);
        arb.dequeue();
        arb.enqueue(4);
        arb.dequeue();
        arb.enqueue(5);
        arb.dequeue();
        arb.enqueue(6);
        arb.dequeue();
        arb.enqueue(7);
        int expect = 5;
        for (Object item : arb) {
            assertEquals(expect,item);
            expect += 1;
        }
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
