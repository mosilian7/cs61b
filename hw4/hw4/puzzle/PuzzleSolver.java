package hw4.puzzle;

public class PuzzleSolver {
    public static void main(String[] args) {
        //Board b = TestSolver.readBoard("input/puzzle13.txt");
        Board b = new Board(new int[][]{{14,7,5,1},{11,10,8,3},{15,9,4,6},{2,12,13,0}});
        Solver s = new Solver(b);
        for (WorldState w:s.solution()) {
            System.out.println((Board) w);
        }
        System.out.println("moves: "+s.moves());
    }
}
