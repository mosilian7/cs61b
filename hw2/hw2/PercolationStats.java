package hw2;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;
public class PercolationStats {
    PercolationFactory pf;
    int N;
    int T;
    double[] xT;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        this.N = N;
        this.T = T;
        this.pf = pf;
        xT = new double[T];
    }
    public double mean(){
        return StdStats.mean(xT);
    }
    public double stddev() {
        return StdStats.stddev(xT);
    }
    public double confidenceLow(){
        return (mean() - 1.96 * stddev()/Math.sqrt(T));
    }
    public double confidenceHigh(){
        return (mean() + 1.96 * stddev()/Math.sqrt(T));
    }
    private int oneStep() {
        Percolation p = pf.make(N);
        while (true) {
            int position = StdRandom.uniform(N*N);
            int row = position/N;
            int col = position%N;
            p.open(row,col);
            if (p.percolates())
                return p.numberOfOpenSites();
        }
    }
    public void doStat() {
        for (int i=0;i<T;i+=1) {
            xT[i] = (double) oneStep()/(double) (N*N);
        }
    }
    public static void main(String[] args) {
        int N = Integer.valueOf(args[0]);
        int T = Integer.valueOf(args[1]);
        PercolationFactory pf = new PercolationFactory();
        PercolationStats p = new PercolationStats(N,T,pf);
        p.doStat();
        System.out.println("mean = "+p.mean());
        System.out.println("stddev = "+p.stddev());
        System.out.println("95% confidence interval = ["+p.confidenceLow() + ", "
                +p.confidenceHigh()+"]");
    }
}
