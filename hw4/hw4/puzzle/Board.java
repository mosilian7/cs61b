package hw4.puzzle;

import java.util.ArrayList;

public class Board implements WorldState {
    private final String estimation;
    private final int[][] tiles;
    private final int N;
    private int[] zeroLocation;


    public Board(int[][] tiles) {
        N = tiles.length;
        this.tiles = new int[N][N];
        for (int row = 0; row < N; row += 1) {
            System.arraycopy(tiles[row], 0, this.tiles[row], 0, N);
        }
        estimation = "manhattan";

        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                if (tiles[i][j] == 0) {
                    zeroLocation = new int[]{i, j};
                    return;
                }
            }
        }
    }

    private Board(int[][] tiles, int[] zeroLocation) {
        N = tiles.length;
        this.tiles = new int[N][N];
        for (int row = 0; row < N; row += 1) {
            System.arraycopy(tiles[row], 0, this.tiles[row], 0, N);
        }
        estimation = "manhattan";
        this.zeroLocation = zeroLocation;
    }

    public int tileAt(int i, int j) {
        if (i < 0 || i > N - 1 || j < 0 || j > N - 1) {
            throw (new IndexOutOfBoundsException("i or j is too big or too small!"));
        }
        return tiles[i][j];
    }

    public int size() {
        return N;
    }

    @Override
    public Iterable<WorldState> neighbors() {
        ArrayList<WorldState> out = new ArrayList<>();
        int[][] directions = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        int i = zeroLocation[0];
        int j = zeroLocation[1];
        for (int[] d : directions) {
            int iNew = i + d[0];
            int jNew = j + d[1];
            if (iNew < 0 || iNew > N - 1 || jNew < 0 || jNew > N - 1) {
                continue;
            }
            int[][] newTiles = new int[N][N];
            for (int row = 0; row < N; row += 1) {
                System.arraycopy(tiles[row], 0, newTiles[row], 0, N);
            }
            newTiles[i][j] = tiles[iNew][jNew];
            newTiles[iNew][jNew] = 0;
            out.add(new Board(newTiles, new int[]{iNew, jNew}));
        }
        return out;
    }

    private int hammingToGoal(int i, int j) {
        int target = tiles[i][j];
        if (target == 0) {
            return 0;
        }
        int targetRow = (target - 1) / N;
        int targetCol = (target - 1) % N;
        if (targetRow == i && targetCol == j) {
            return 0;
        } else {
            return 1;
        }
    }

    public int hamming() {
        int out = 0;
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                out += hammingToGoal(i, j);
            }
        }
        return out;
    }

    private int manhattanToGoal(int i, int j) {
        int target = tiles[i][j];
        if (target == 0) {
            return 0;
        }
        int targetRow = (target - 1) / N;
        int targetCol = (target - 1) % N;
        return Math.abs(targetRow - i) + Math.abs(targetCol - j);
    }

    public int manhattan() {
        int out = 0;
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                out += manhattanToGoal(i, j);
            }
        }
        return out;
    }

    @Override
    public int estimatedDistanceToGoal() {
        switch (estimation) {
            case ("manhattan"):
                return manhattan();
            case ("hamming"):
                return hamming();
            default:
                return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this.N != ((Board) o).N) {
            return false;
        }
        for (int row = 0; row < N; row += 1) {
            for (int col = 0; col < N; col += 1) {
                if (!(tiles[row][col] == (((Board) o).tiles[row][col]))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int out = 0;
        for (int row = 0; row < N; row += 1) {
            for (int col = 0; col < N; col += 1) {
                out = out * 31;
                out += tiles[row][col];
            }
        }
        return out;
    }

    /**
     * Returns the string representation of the board.
     * Uncomment this method.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
}
