package lab11.graphs;

import java.util.ArrayDeque;
/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private final int sourceX;
    private final int sourceY;
    private final int targetX;
    private final int targetY;
    private final int source;
    private final int target;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.targetX = targetX;
        this.targetY = targetY;
        source = m.xyTo1D(sourceX,sourceY);
        target = m.xyTo1D(targetX,targetY);
        marked[source] = true;
        distTo[source] = 0;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        ArrayDeque<Integer> fringe = new ArrayDeque<>();
        fringe.addLast(source);
        while (!fringe.isEmpty()) {
            int activate = fringe.removeFirst();
            for(int neighbor : maze.adj(activate)) {
                if (!marked[neighbor]) {
                    fringe.addLast(neighbor);
                    marked[neighbor] = true;
                    edgeTo[neighbor] = activate;
                    distTo[neighbor] = distTo[activate] + 1;
                    announce();
                    if (neighbor == target) {
                        return;
                    }
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs();
    }
}

