/* To perform a series of computational experiments, create a data type PercolationStats with the following API. */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] fractionOpenSites;
    private double Mean;
    private double stdDev;
    private int Trials;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("must be positive values");
        }
        this.fractionOpenSites = new double[trials];
        this.Trials = trials;
        // 1. Initialise the grid
        // 2. while (grid hasn't percolated)
        //      2.1 generate random row and col values
        //          while site is open, generate again
        //      2.2 update the site to open
        //  3. update the fractionOpenSites;
        for (int i = 0; i < trials; i++) {
            Percolation grid = new Percolation(n);
            while (!grid.percolates()) {
                int row = StdRandom.uniform(0, n);// random int from [0,n) or [0,n-1]
                int col = StdRandom.uniform(0, n);
                while (grid.isOpen(row, col)) {
                    row = StdRandom.uniform(0, n);// random int from [0,n) or [0,n-1]
                    col = StdRandom.uniform(0, n);
                }
                grid.open(row, col);
            }
            // when the system percolates
            fractionOpenSites[i] = ((double) grid.numberOfOpenSites()) / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        this.Mean = StdStats.mean(fractionOpenSites);
        return Mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        this.stdDev = StdStats.stddev(fractionOpenSites);
        return stdDev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() // (mean - 1.96*stddev/sqrt(T))
    {
        return (Mean - 1.96 * stdDev / java.lang.Math.sqrt(Trials));
    }


    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (Mean + 1.96 * stdDev / java.lang.Math.sqrt(Trials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.valueOf(args[0]);
        int trials = Integer.valueOf(args[1]);
        PercolationStats percolationTest = new PercolationStats(n, trials);
        System.out.printf("%-30s = %.10f%n", "mean", percolationTest.mean());
        System.out.printf("%-30s = %.10f%n", "stddev", percolationTest.stddev());
        System.out.printf(
                "%-30s = [%.10f, %.10f]%n",
                "95% confidence interval",
                percolationTest.confidenceLo(),
                percolationTest.confidenceHi());
    }
}

