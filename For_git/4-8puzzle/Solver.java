import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Solver {
    private int moves = 0; // moves to solve the initial board
    private Node finalSolutionNode;
    private boolean solvable;

    private class Node implements Comparable<Node> {
        private Board board;
        private Node prev;
        private int boardMoves;
        private int manhattanDist;
        private int manhattanPriority;

        public Node(Board inputBoard) {
            board = inputBoard;
            manhattanDist = inputBoard.manhattan();
        }

        public int compareTo(Node other) {
            if (this.manhattanPriority > other.manhattanPriority) {
                return 1;
            }
            else if (this.manhattanPriority < other.manhattanPriority) {
                return -1;
            }
            else return 0;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Cannot input null board");
        }
        Node initialBoardNode = new Node(initial);
        initialBoardNode.prev = null;
        initialBoardNode.boardMoves = 0;
        initialBoardNode.manhattanPriority = initialBoardNode.boardMoves
                + initialBoardNode.manhattanDist;

        // for twin board instance
        Node twinBoardNode = new Node(initial.twin());
        twinBoardNode.prev = null;
        twinBoardNode.boardMoves = 0;
        twinBoardNode.manhattanPriority = twinBoardNode.manhattanDist + twinBoardNode.boardMoves;

        // insert the initial search node into the MinPQ
        MinPQ solverQueue = new MinPQ();
        MinPQ twinSolverQueue = new MinPQ(); // to solve the twin
        solverQueue.insert(initialBoardNode);
        twinSolverQueue.insert(twinBoardNode);

        int printCount = 0;
        while (!solverQueue.isEmpty() || !twinSolverQueue
                .isEmpty()) { // not all the Nodes are popped out
            Node dequed = solverQueue
                    .delMin(); // need to take note, for the exchange part in delMin()
            Node twinDequed = twinSolverQueue.delMin();

            /* if (printCount < 10) {
                StdOut.println("Dequed:" + "Count:" + printCount + " " + dequed.board.toString());
                StdOut.println("Twin:" + "Count:" + printCount + " " + twinDequed.board.toString());
                printCount++;
            } */

            if (dequed.board.isGoal()) {
                solvable = true;
                finalSolutionNode = dequed;
                moves = dequed.boardMoves;
                break;
            } // goal Board is reached
            else if (twinDequed.board.isGoal()) {
                solvable = false;
                break;
            }
            for (Board b : dequed.board.neighbors()) {
                /* if (printCount < 10) {
                    StdOut.println("b Board:" + b.toString());
                } */
                printCount++;
                Node childNode = new Node(b);
                /* for (Board neighB : b.neighbors()) {
                    if (printCount < 10) {
                        StdOut.println("Neighbor of b:" + neighB.toString());
                    }
                } */
                if (dequed.prev == null || !b.equals(dequed.prev.board)) {
                    childNode.prev = dequed; // all the childNode point to the parent node (dequed)
                    childNode.boardMoves = dequed.boardMoves + 1;
                    childNode.manhattanPriority = childNode.boardMoves + childNode.manhattanDist;
                    solverQueue.insert(childNode);
                    /* if (printCount < 10) {
                        StdOut.println("ChildNode:" + childNode.board.toString());
                    } */
                    printCount++;
                }
            }
            for (Board b1 : twinDequed.board.neighbors()) {
                Node twinChildNode = new Node(b1);
                if (twinDequed.prev == null || !b1.equals(twinDequed.prev.board)) {
                    twinChildNode.prev
                            = twinDequed; // all the childNode point to the parent node (dequed)
                    twinChildNode.boardMoves = twinDequed.boardMoves + 1;
                    twinChildNode.manhattanPriority = twinChildNode.boardMoves
                            + twinChildNode.manhattanDist;
                    twinSolverQueue.insert(twinChildNode);
                }
            }
        }
    }

    // Need to create a few methods for the use of MinPQ class
    private class MinPQ {
        private Node[] pq;
        private int N = 0;

        public MinPQ() { // need to implement resize
            pq = new Node[2]; // initially set to 2 for 1st Node since index start at 1;
        }

        public boolean isEmpty() {
            return N == 0;
        }

        public void insert(Node node) { // implement later
            if (N == pq.length - 1) {
                pq = resize(pq, (N + 1) * 2);
            }
            pq[++N] = node;
            swim(N);
        }

        private Node[] resize(Node[] array, int capacity) {
            Node[] copy = new Node[capacity];
            for (int i = 1; i < Math.min(pq.length, capacity); i++) {
                copy[i] = array[i];
            }
            array = copy;
            return array;
        }

        public Node delMin() { // remove the node with min mahattan distance
            Node min = pq[1];
            if (N == 1) {
                pq[N--] = null;
                return min;
            }
            else {
                exch(1, N--);
                sink(1);
                pq[N + 1] = null;
                return min;
            }
        }

        private void swim(int k) {
            while (k > 1 && !less(k / 2, k)) {
                exch(k / 2, k);
                k = k / 2;
            }
        }

        private void sink(int k) {
            while (2 * k <= N) {
                int j = 2 * k;
                if (j < N && !less(j, j + 1)) {
                    j++; // j + 1 is smaller than j, update j as j + 1
                }
                if (less(k, j)) { // if k is already less than j
                    break;
                }
                exch(k, j);
                k = j;
            }
        }

        private boolean less(int i, int j) {
            return pq[i].compareTo(pq[j]) < 0;
        }

        private void exch(int i, int j) {
            Node temp = pq[j];
            pq[j] = pq[i];
            pq[i] = temp;
        }
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return new SolutionIterable();
    }

    private class SolutionIterable implements Iterable<Board> {

        public Iterator<Board> iterator() {
            return new SolutionIterator();
        }

        private class SolutionIterator implements Iterator<Board> {
            private Board[] solution;
            private int count;

            public SolutionIterator() {
                int solutionMoves = moves();
                count = solutionMoves;
                solution = new Board[solutionMoves + 1];
                Node end = finalSolutionNode;
                for (int i = 0; i < solutionMoves + 1; i++) {
                    solution[i] = end.board;
                    end = end.prev;
                }
            }

            public boolean hasNext() {
                return count >= 0;
            }

            public Board next() {
                Board solutionBoard = solution[count];
                count--;
                return solutionBoard;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
