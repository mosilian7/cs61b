package lab11.graphs;

import java.util.ArrayDeque;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int cycleStart;
    private int[] parentOf;
    private boolean terminateDFS;

    public MazeCycles(Maze m) {
        super(m);
        parentOf = new int[maze.V()];
        for (int i = 0; i < maze.V(); i += 1) {
            parentOf[i] = Integer.MAX_VALUE;
        }
        terminateDFS = false;
    }

    private void dfs(int v) {
        if (terminateDFS) {
            return;
        }
        marked[v] = true;
        announce();
        for (int n : maze.adj(v)) {
            if (terminateDFS) {
                return;
            }
            if (!marked[n]) {
                parentOf[n] = v;
                dfs(n);
            } else if (marked[n] && n != parentOf[v]){
                int pointer = v;
                while (pointer != n){
                    int parent = parentOf[pointer];
                    edgeTo[pointer] = parent;
                    pointer = parent;
                }
                edgeTo[n] = v;
                announce();
                terminateDFS = true;
                return;
            }
        }
    }

    private int indexOfFalse(boolean[] marked) {
        for (int i=0;i<maze.V();i+=1) {
            if (!marked[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void solve() {
        int iof = indexOfFalse(marked);
        while (!(terminateDFS || iof == -1)) {
            dfs(iof);
            iof = indexOfFalse(marked);
        }

    }

    // Helper methods go here
}

