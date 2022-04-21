package hw2;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;
public class PercolationStats {
    private final PercolationFactory pf;
    private final int N;
    private final int T;
    private double[] xT;
    private boolean xTInit = false;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw (new IllegalArgumentException("fadafada"));
        }
        this.N = N;
        this.T = T;
        this.pf = pf;
    }
    public double mean(){
        if (!xTInit) {
            doStat();
        }
        return StdStats.mean(xT);
    }
    public double stddev() {
        if (!xTInit) {
            doStat();
        }
        return StdStats.stddev(xT);
    }
    public double confidenceLow(){
        return (mean() - 1.96 * stddev() / Math.sqrt(T));
    }
    public double confidenceHigh(){
        return (mean() + 1.96 * stddev() / Math.sqrt(T));
    }
    private int oneStep() {
        Percolation p = pf.make(N);
        while (true) {
            int position = StdRandom.uniform(N * N);
            int row = position / N;
            int col = position % N;
            p.open(row,col);
            if (p.percolates()) {
                return p.numberOfOpenSites();
            }
        }
    }
    private void doStat() {
        xT = new double[T];

        for (int i = 0;i < T;i += 1) {
            xT[i] = (double) oneStep() / (double) (N*N);
        }
        xTInit = true;
    }

}
