import org.junit.Test;
import static org.junit.Assert.*;

public class DequeTest {
    @Test
    public void testGet() {
        LinkedListDeque<Integer> l = new LinkedListDeque<>();
        l.addFirst(8);
        l.addLast(7);
        l.addLast(5);
        int expect = 5;
        int actual = l.get(2);
        assertEquals(expect, actual);
    }
}
