package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;
public class HexWorldTest {
    @Test
    public void testNextX() {
        int x0 = 3;
        int size = 3;
        int actual = HexWorld.nextX(x0,2,size);
        assertEquals(8,actual);
    }
}
