package hw4.puzzle;

public class PuzzleSolver {
    public static void main(String[] args) {
        Board b = TestSolver.readBoard("input/puzzle13.txt");
        Solver s = new Solver(b);
        for (WorldState w:s.solution()) {
            System.out.println((Board) w);
        }
    }
}
