import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class SeamCarver {
    Picture picture;

    public SeamCarver(Picture picture) {
        this.picture = picture;
    }

    private static int sqr(int x) {
        return x * x;
    }

    private static int delta_sqr(Color c1, Color c2) {
        int c1red = c1.getRed();
        int c1green = c1.getGreen();
        int c1blue = c1.getBlue();
        int c2red = c2.getRed();
        int c2green = c2.getGreen();
        int c2blue = c2.getBlue();

        return sqr(c1red - c2red) + sqr(c1green - c2green) + sqr(c1blue - c2blue);
    }

    public static void main(String[] args) {
        Picture p = new Picture(3, 4);
        int[][][] exampleArray = {{{255, 101, 51}, {255, 101, 153}, {255, 101, 255}},
                {{255, 153, 51}, {255, 153, 153}, {255, 153, 255}},
                {{255, 203, 51}, {255, 204, 153}, {255, 205, 255}},
                {{255, 255, 51}, {255, 255, 153}, {255, 255, 255}}};
        double[][] exampleEnergy = {{20808.0, 52020.0, 20808.0},
                {20808.0, 52225.0, 21220.0},
                {20809.0, 52024.0, 20809.0},
                {20808.0, 52225.0, 21220.0}};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                int[] colorVals = exampleArray[j][i];
                p.set(i, j, new Color(colorVals[0], colorVals[1], colorVals[2]));
            }
        }

        SeamCarver sc = new SeamCarver(p);

        for (int i : sc.findVerticalSeam()) {
            //System.out.println(i);
        }

        //System.out.println(sc.validSeam(new int[]{4,2,3,4}));
    }

    public Picture picture() {
        return picture;
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException();
        }
        int leftX = x == 0 ? width() - 1 : x - 1;
        int rightX = x == width() - 1 ? 0 : x + 1;
        int upY = y == 0 ? height() - 1 : y - 1;
        int downY = y == height() - 1 ? 0 : y + 1;

        Color left = picture.get(leftX, y);
        Color right = picture.get(rightX, y);
        Color up = picture.get(x, upY);
        Color down = picture.get(x, downY);

        return delta_sqr(left, right) + delta_sqr(up, down);
    }

    private void printArr(double[][] arr) {
        for (double[] a : arr) {
            for (double d : a) {
                System.out.print(d + " ");
            }
            System.out.print("\n");
        }
    }

    public int[] findVerticalSeam() {
        double[][] energyCache = new double[height()][width()];
        double[][] M = new double[height()][width()];
        int[][] route = new int[height()][width()];
        for (int i = 0; i < width(); i += 1) {
            for (int j = 0; j < height(); j += 1) {
                energyCache[j][i] = energy(i, j);
            }
        }

        calculateM(M, energyCache, route);
        //printArr(M);
        int i = minIndex(M[height() - 1]);
        int[] out = new int[height()];
        for (int j = height() - 1; j >= 0; j -= 1) {
            i = route[j][i];
            out[j] = i;
        }
        return out;
    }

    private int minIndex(double[] arr) {
        double minValue = Double.MAX_VALUE;
        int minIndex = -1;
        for (int i = 0; i < arr.length; i += 1) {
            if (arr[i] < minValue) {
                minValue = arr[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    private double calculateMij(int i, int j, double[][] M, double[][] energyCache, int[][] route) {
        if (j == 0) {
            route[j][i] = i;
            return energyCache[j][i];
        }
        int left = i == 0 ? i : i - 1;
        int right = i == width() - 1 ? i : i + 1;
        int[] choices = new int[]{left, i, right};
        double[] possibleM = new double[]{M[j - 1][left], M[j - 1][i], M[j - 1][right]};
        int minI = minIndex(possibleM);
        route[j - 1][i] = choices[minI];
        //System.out.println(String.format("i: %1$d, j: %2$d, possible M: %3$f, %4$f, %5$f, best choice: %6$d", i, j,
        //                                possibleM[0], possibleM[1], possibleM[2], choices[minI]));
        route[j][i] = i;
        return energyCache[j][i] + possibleM[minI];
    }

    private void calculateM(double[][] M, double[][] energyCache, int[][] route) {
        for (int j = 0; j < height(); j += 1) {
            for (int i = 0; i < width(); i += 1) {
                M[j][i] = calculateMij(i, j, M, energyCache, route);
            }
        }
    }

    private void transpose() {
        Picture pT = new Picture(height(), width());
        for (int i = 0; i < height(); i += 1) {
            for (int j = 0; j < width(); j += 1) {
                pT.set(i, j, picture.get(j, i));
            }
        }
        picture = pT;
    }

    public int[] findHorizontalSeam() {
        transpose();
        int[] out = findVerticalSeam();
        transpose();
        return out;
    }

    private boolean validSeam(int[] seam) {
        int lastItem = -1;
        for (int i = 0; i < seam.length; i += 1){
            if (lastItem != -1 && Math.abs(seam[i] - lastItem) > 1) {
                return false;
            }
            lastItem = seam[i];
        }
        return true;
    }
    public void removeHorizontalSeam(int[] seam) {
        if (seam.length != width() || !validSeam(seam)) {
            throw new IllegalArgumentException();
        }
        picture = SeamRemover.removeHorizontalSeam(picture, seam);
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height() || !validSeam(seam)) {
            throw new IllegalArgumentException();
        }
        picture = SeamRemover.removeVerticalSeam(picture, seam);
    }
}
