/*
This is Percolation class with attributes: open, isOpen, isFull, Percolates
*/

public class Percolation {
    // creates n-by-n grid, with all sites initially blocked
    private int N;
    private int[][] grid;
    private int[][] id;
    private int[][] size;
    private int[][] minId;
    private int[][] maxId;
    private int openSites;
    private boolean percolationStatus;

    /******************* PUBLIC INTERFACE ********************/

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        this.N = n;
        this.grid = new int[n][n]; // very good!
        this.id = new int[n][n];
        this.size = new int[n][n];
        this.minId = new int[n][n];
        this.maxId = new int[n][n];
        this.openSites = 0;
        this.percolationStatus = false;

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                grid[row][col] = 0; // 0 means blocked
                id[row][col] = row * N + col; // Initialize the id of each site
                size[row][col] = 1; // when no sites are connected initially, size = 1
                minId[row][col] = row * N
                        + col; // Initialize the minId of the connected sites to be individual id
                maxId[row][col] = row * N + col;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int rowBaseOne, int colBaseOne) {
        if (!isOpen(rowBaseOne, colBaseOne)) {
            int row = rowBaseOne;
            int col = colBaseOne;
            grid[row][col] = 1; // 1 means open
            openSites += 1;
            union(row, col);
            if (isFull(rowBaseOne, colBaseOne)) {
                int rootSite = Root(row, col);
                if ((maxId[rootSite / N][rootSite % N] / N) == (N - 1)) {
                    percolationStatus = true;
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int rowBaseOne, int colBaseOne) {
        int row = rowBaseOne;
        int col = colBaseOne;
        return grid[row][col] == 1;
    }

    // is the site (row, col) full?
    // full when the site is connected to the top i.e row of root is 0
    public boolean isFull(int rowBaseOne, int colBaseOne) {
        int row = rowBaseOne;
        int col = colBaseOne;
        int root = Root(row, col); // return id of the root
        return (isOpen(rowBaseOne, colBaseOne) && minId[root / N][root % N] / N == 0);
    }


    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolationStatus;
    }

    /******************* PRIVATE METHODS ********************/

    // find the top sites
    private int Root(int row, int col) {
        int idSite = (row) * N + (col);
        if (id[row][col] == idSite) {
            return idSite;
        }
        else {
            // recursively find the root of parent cell
            int idCheck = id[row][col];
            int root = Root(idCheck / N, idCheck % N);
            // flatten the tree
            id[row][col] = root;
            return root;
        }
    }

    // connect the open sites, they only connect if they share the col or row no
    private void unionWeighted(int a, int b) {
        // a is id of a site
        // b is id of the site to connected
        int rowA = a / N;
        int colA = a % N;
        int rowB = b / N;
        int colB = b % N;
        if (size[rowA][colA] > size[rowB][colB]) {
            // connect the smaller tree to the larger tree
            id[rowB][colB] = id[rowA][colA];
            size[rowA][colA] += size[rowB][colB];
            minId[rowA][colA] = Math.min(minId[rowA][colA],
                                         minId[rowB][colB]); // Store the minimum ids of the connected sites
            maxId[rowA][colA] = Math.max(maxId[rowA][colA], maxId[rowB][colB]);
        }
        else { // size a <= size b
            id[rowA][colA] = id[rowB][colB];
            size[rowB][colB] += size[rowA][colA];
            minId[rowB][colB] = Math.min(minId[rowA][colA], minId[rowB][colB]);
            maxId[rowB][colB] = Math.max(maxId[rowA][colA], maxId[rowB][colB]);
        }
    }

    private void union(int row, int col) {
        int rootSite = Root(row, col);
        if (row >= 1 && grid[row - 1][col] == 1) {
            int rootTop = Root(row - 1, col);
            unionWeighted(rootSite, rootTop);
            rootSite = Root(row, col);
        }
        if (row <= N - 2 && grid[row + 1][col] == 1) {
            int rootBottom = Root(row + 1, col);
            unionWeighted(rootSite, rootBottom);
            rootSite = Root(row, col);
        }
        if (col >= 1 && grid[row][col - 1] == 1) {
            int rootLeft = Root(row, col - 1);
            unionWeighted(rootSite, rootLeft);
            rootSite = Root(row, col);
        }
        if (col <= N - 2 && grid[row][col + 1] == 1) {
            int rootRight = Root(row, col + 1);
            unionWeighted(rootSite, rootRight);
        }
    }
}
