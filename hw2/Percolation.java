import java.util.ArrayList;
import java.util.Objects;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.HashMap;
import java.util.TreeSet;

public class Percolation {
    private int N;
    private int size;
    private TreeSet<Integer> opened;
    private TreeSet<Integer> top;
    private TreeSet<Integer> bottom;
    private WeightedQuickUnionUF WQU;

    public Percolation(int N) {
        this.N = N;
        size = 0;
        WQU = new WeightedQuickUnionUF(N*N);
    }

    private class Coordinate {
        private int row;
        private int col;
        public Coordinate(int x, int y) {
            row = x;
            col = y;
        }

        public boolean ConnectsWith(Coordinate cd) {
            return (row+1==cd.row || row-1==cd.row || col+1==cd.col || col-1==cd.col);
        }

        @Override
        public boolean equals(Object cd) {
            if (this.getClass() != cd.getClass())
                return false;
            if (this == cd) return true;
            if (cd == null) return false;
            return ((Coordinate) cd).row == row && ((Coordinate) cd).col == col;
        }
        @Override
        public int hashCode() {
            return Objects.hash(row,col);
        }
    }

    private boolean isIllegal(int row, int col) {
        return (row<0 || row>N-1 || col<0 || col>N-1);
    }


    public void open(int row,int col) {
        if (isIllegal(row,col)) {
            throw (new IndexOutOfBoundsException("Boom!"));
        }
        int position = N*row + col;
        if (isOpen(row,col))
            return;
        opened.add(position);
        if (position<N)
            top.add(position);
        if (position>N*(N-1))
            bottom.add(position);

        for (int i:new int[]{-1,1,-N,N}) {
            if (opened.contains(position+i)) {
                WQU.union(position,position+i);
            }
        }

    }

    public boolean isOpen(int row,int col) {
        if (isIllegal(row,col)) {
            throw (new IndexOutOfBoundsException("Boom!"));
        }
        int position = N*row + col;
        return opened.contains(position);
    }

    public boolean isFull(int row,int col) {
        int position = N*row + col;
        return isFull(position);
    }

    private boolean isFull(int position) {
        for(int i:top) {
            if (WQU.find(i) == WQU.find(position))
                return true;
        }
        return false;
    }

    public int numberOfOpenSites() {
        return opened.size();
    }

    public boolean percolates() {
        for(int i:bottom) {
            if (isFull(i)) {
                return true;
            }
        }
        return false;
    }
}
