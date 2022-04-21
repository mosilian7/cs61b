import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome1() {
        assertFalse(palindrome.isPalindrome("cat"));
    }

    @Test
    public void testIsPalindrome2() {
        assertTrue(palindrome.isPalindrome("racecar"));
    }

    @Test
    public void testIsPalindrome3() {
        assertTrue(palindrome.isPalindrome("r"));
    }

    @Test
    public void testIsPalindrome4() {
        assertTrue(palindrome.isPalindrome(""));
    }

    @Test
    public void testIsPalindrome5() {
        assertFalse(palindrome.isPalindrome("horse"));
    }

    /**Testing OffByOne*/
    @Test
    public void testIsPalindrome6() {
        CharacterComparator obo = new OffByOne();
        assertFalse(palindrome.isPalindrome("horse",obo));
    }

    @Test
    public void testIsPalindrome7() {
        CharacterComparator obo = new OffByOne();
        assertTrue(palindrome.isPalindrome("h",obo));
    }

    @Test
    public void testIsPalindrome8() {
        CharacterComparator obo = new OffByOne();
        assertTrue(palindrome.isPalindrome("flake",obo));
    }

    /**Testing OffByN*/
    @Test
    public void testIsPalindrome9() {
        CharacterComparator obn = new OffByN(5);
        assertFalse(palindrome.isPalindrome("flake",obn));
    }

    @Test
    public void testIsPalindrome10() {
        CharacterComparator obn = new OffByN(5);
        assertTrue(palindrome.isPalindrome("gafb",obn));
    }

    @Test
    public void testIsPalindrome11() {
        CharacterComparator obn = new OffByN(5);
        assertTrue(palindrome.isPalindrome("gahfb",obn));
    }
}
