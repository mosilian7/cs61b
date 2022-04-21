public class Palindrome {
    /**Return a Deque where the characters appear in the same order as in the String*/
    public Deque<Character> wordToDeque(String word){
        ArrayDeque<Character> AList = new ArrayDeque<>();
        for (int i=0;i<word.length();i++) {
            AList.addLast(word.charAt(i));
        }
        return AList;
    }

    private boolean isPalindromeList(Deque<Character> WordList,CharacterComparator cc) {
        if (WordList.size() == 1 || WordList.isEmpty()) {
            return true;
        }
        if (!cc.equalChars(WordList.removeFirst(), WordList.removeLast())) {
            return false;
        } else {
            return isPalindromeList(WordList,cc);
        }
    }

    public boolean isPalindrome(String word) {
        CharacterComparator de = new DefaultEqual();
        Deque<Character> WordList = wordToDeque(word);
        return isPalindromeList(WordList,de);
    }

    public boolean isPalindrome(String word,CharacterComparator cc) {
        Deque<Character> WordList = wordToDeque(word);
        return isPalindromeList(WordList,cc);
    }
}
