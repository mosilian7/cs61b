package byog.lab6;

import org.junit.Test;
import org.junit.Assert.*;

public class MemoryGameTest {
    public static void main(String[] args) {
        MemoryGameSolution gameS = new MemoryGameSolution(40, 40,34);
        MemoryGame game = new MemoryGame(40, 40,3423);
        game.startGame();
        //gameS.startGame();
    }
}
