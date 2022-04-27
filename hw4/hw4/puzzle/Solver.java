package hw4.puzzle;
import edu.princeton.cs.algs4.MinPQ;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

public class Solver {
    private MinPQ<SearchNode> pq = new MinPQ<>();
    private HashSet<WorldState> marked = new HashSet<>();
    private int moves;
    private Iterable<WorldState> solution;
    private HashMap<WorldState,Integer> cacheHeuristics = new HashMap<>();


    private class SearchNode implements Comparable<SearchNode>{
        protected WorldState content;
        protected int movesFromInit;
        protected SearchNode parent;
        protected int heuristics;
        protected ArrayList<WorldState> pathToInit = new ArrayList<>();

        public SearchNode(WorldState c, int m, SearchNode p) {
            content = c;
            movesFromInit = m;
            parent = p;
            if (cacheHeuristics.containsKey(content)) {
                heuristics = cacheHeuristics.get(content);
            } else {
                heuristics = content.estimatedDistanceToGoal();
                cacheHeuristics.put(content, heuristics);
            }

        }

        protected void setMovesFromInit(int m) {
            movesFromInit = m;
        }

        private void pathToInitHelper(SearchNode s) {
            if (s.parent == null) {
                pathToInit.add(s.content);
                return;
            }
            pathToInitHelper(s.parent);
            pathToInit.add(s.content);
        }

        protected Iterable<WorldState> pathToInit() {
            pathToInit.clear();
            pathToInitHelper(this);
            return pathToInit;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            if (this.content ==((SearchNode) o).content) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return content.hashCode();
        }

        @Override
        public int compareTo(SearchNode other) {
            return this.movesFromInit + this.heuristics - other.movesFromInit - other.heuristics;
        }
    }


    public Solver(WorldState initial) {
        pq.insert(new SearchNode(initial,0,null));

        while (!pq.isEmpty()) {
            SearchNode activate = pq.min();
            if (activate.content.isGoal()) {
                moves = activate.movesFromInit;
                solution = activate.pathToInit();
                return;
            }
            if (marked.contains(activate.content)) {
                pq.delMin();
                continue;
            }
            for (WorldState w : activate.content.neighbors()) {
                if (marked.contains(w)) {
                    continue;
                }
                pq.insert(new SearchNode(w,activate.movesFromInit+1,activate));
            }
            marked.add(activate.content);
            pq.delMin();
        }

        throw (new IllegalArgumentException("Path do not exist!"));
    }

    public int moves() {
        return moves;
    }

    public Iterable<WorldState> solution() {
        return solution;
    }
}
