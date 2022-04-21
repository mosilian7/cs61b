import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {

    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void testEqualChars1() {
        CharacterComparator cc = new OffByOne();
        assertFalse(cc.equalChars('c','i'));
    }

    @Test
    public void testEqualChars2() {
        CharacterComparator cc = new OffByOne();
        assertFalse(cc.equalChars('i','i'));
    }

    @Test
    public void testEqualChars3() {
        CharacterComparator cc = new OffByOne();
        assertTrue(cc.equalChars('i','j'));
    }

    @Test
    public void testEqualChars4() {
        CharacterComparator cc = new OffByOne();
        assertTrue(cc.equalChars('i','h'));
    }


}
