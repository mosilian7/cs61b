package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private final int N;
    private final boolean[] opened;
    private int openedSize;

    private final WeightedQuickUnionUF percolatesChecker;
    private final WeightedQuickUnionUF isFullChecker;

    public Percolation(int N) {

        this.N = N;
        percolatesChecker = new WeightedQuickUnionUF(N * N + 2);
        isFullChecker = new WeightedQuickUnionUF(N * N + 1);
        opened = new boolean[N * N];
        for (int i = 0; i < N * N; i += 1) {
            opened[i] = false;
        }
       /* Node N*N is for the top and Node N*N+1 is for the bottom. */
    }

    private int position(int row, int col) {
        return N * row + col;
    }
    private boolean isIllegal(int row, int col) {
        return (row < 0 || row > N - 1 || col < 0 || col > N - 1);
    }


    public void open(int row, int col) {
        if (isIllegal(row, col)) {
            throw (new IndexOutOfBoundsException("Boom!"));
        }
        int position = position(row, col);
        open(position);

    }

    private void open(int position) {
        if (isOpen(position)) {
            return;
        }
        opened[position] = true;
        unionBorder(position);
        unionNeighbor(position);
        openedSize += 1;
    }

    private void unionBorder(int position) {
        if (position < N) {
            percolatesChecker.union(position, N * N);
            isFullChecker.union(position, N * N);
        }
        if (position >= N * (N - 1)) {
            percolatesChecker.union(position, N * N + 1);
        }
    }

    private void unionNeighbor(int position) {
        for (int i:new int[]{-1, 1, -N, N}) {
            try {
                if (position % N == N - 1 && i == 1) {
                    continue;
                }
                if (position % N == 0 && i == -1) {
                    continue;
                }
                if (opened[position + i]) {
                    percolatesChecker.union(position, position + i);
                    isFullChecker.union(position, position + i);
                }
            } catch (ArrayIndexOutOfBoundsException x) {
                continue;
            }
        }
    }

    public boolean isOpen(int row, int col) {
        if (isIllegal(row, col)) {
            throw (new IndexOutOfBoundsException("Boom!"));
        }
        int position = N * row + col;
        return opened[position];
    }

    private boolean isOpen(int position) {
        return opened[position];
    }

    public boolean isFull(int row, int col) {
        if (isIllegal(row, col)) {
            throw (new IndexOutOfBoundsException("Boom!"));
        }
        int position = N * row + col;
        return isFull(position);
    }

    private boolean isFull(int position) {
        return isFullChecker.find(position) == isFullChecker.find(N * N);
    }

    public int numberOfOpenSites() {
        return openedSize;
    }

    public boolean percolates() {
        return percolatesChecker.find(N * N + 1) == percolatesChecker.find(N * N);
    }
    public static void main(String[] args) {
        //Testing
    }
}
