package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

public class Solver {
    private final int moves;
    private final Iterable<WorldState> solution;
    private final HashMap<WorldState, Integer> cacheHeuristics = new HashMap<>();

    public Solver(WorldState initial) {
        MinPQ<SearchNode> pq = new MinPQ<>();
        HashSet<WorldState> done = new HashSet<>();
        pq.insert(new SearchNode(initial, 0, null));

        while (!pq.isEmpty()) {
            SearchNode activate = pq.delMin();
            if (done.contains(activate.content)) {
                continue;
            }
            done.add(activate.content);
            if (activate.content.isGoal()) {
                moves = activate.movesFromInit;
                solution = activate.pathToInit();
                return;
            }
            for (WorldState w : activate.content.neighbors()) {
                SearchNode s = new SearchNode(w, activate.movesFromInit + 1, activate);
                if (s.equals(activate.parent)) {
                    continue;
                }
                pq.insert(s);
            }
        }

        throw (new IllegalArgumentException("Path do not exist!"));
    }

    public int moves() {
        return moves;
    }

    public Iterable<WorldState> solution() {
        return solution;
    }

    private class SearchNode implements Comparable<SearchNode> {
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
            return this.content.equals(((SearchNode) o).content);
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

}
