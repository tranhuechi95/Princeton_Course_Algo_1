import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class Board {
    private int[][] board;
    private int boardSize;
    private int blankRow;
    private int blankCol;
    private int twinRow = -1;
    private int twinCol = -1;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        boardSize = tiles.length;
        board = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = tiles[i][j];
            }
        }
        int blankLocation = blankSquare();
        // StdOut.println("Creating a new board with blank location = " + blankLocation);
        blankRow = blankLocation / boardSize;
        blankCol = blankLocation % boardSize;
        // StdOut.println("Blank row = " + blankRow);
        // StdOut.println("Blank col = " + blankCol);
    }

    // string representation of this board
    public String toString() {
        String boardElement = new String();
        boardElement += String.valueOf(boardSize) + "\n";
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                boardElement += " " + String.valueOf(board[i][j]) + " ";
            }
            boardElement += "\n";
        }
        return boardElement;
    }

    // board dimension n
    public int dimension() {
        return boardSize;
    }

    // number of tiles out of place
    public int hamming() {
        int countTiles = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] != 0 && board[i][j] != i * boardSize + j + 1) {
                    countTiles++;
                }
            }
        }
        return countTiles;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int countSteps = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] != 0 && board[i][j] != i * boardSize + j + 1) {
                    int index = board[i][j];
                    countSteps += Math.abs((index - 1) / boardSize - i) + Math
                            .abs((index - 1) % boardSize - j);
                }
            }
        }
        return countSteps;
    }

    // is this board the goal board?
    public boolean isGoal() {
        /* there are two types of goal boards
        one is 1 -> 8 0
        two is 0 1 -> 8
         */
        for (int i = 0; i < boardSize * boardSize - 1; i++) {
            if (board[i / boardSize][i % boardSize] != i + 1) {
                return false;
            }
        }
        return board[boardSize - 1][boardSize - 1] == 0;

        /* for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (i == boardSize - 1 && j == boardSize - 1)
                    return board[i][j] == 0;
                else if (board[i][j] != i * boardSize + j + 1)
                    return false;
            }
        } */
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            throw new IllegalArgumentException("Argument to equals() cannot be null!");
        }
        if (y.getClass() == this.getClass() && ((Board) y).boardSize == this.boardSize) {
            // logic to compare 2 Board instances go here
            /* A y;
            ((B)y).methodNameOfClassB */
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (board[i][j] != ((Board) y).board[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
        else
            return false;
    }

    // to locate the blank square
    private int blankSquare() {
        int blankLocation = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == 0) {
                    blankLocation = i * boardSize + j;
                    break;
                }
            }
        }
        return blankLocation;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new BoardIterable();
    }

    private class BoardIterable implements Iterable<Board> {

        public Iterator<Board> iterator() {
            return new BoardIterator();
        }

        private class BoardIterator implements Iterator<Board> {
            private int countTile = 0;
            private int count = 0;
            private int[] moves = new int[4];

            public BoardIterator() {
                /* StdOut.println("Creating an iterator: blankRow = " + blankRow + ", blankCol = "
                                       + blankCol); */
                if (blankRow + 1 < boardSize) {
                    moves[countTile++] = (blankRow + 1) * boardSize + blankCol;
                }
                if (blankRow - 1 >= 0) {
                    moves[countTile++] = (blankRow - 1) * boardSize + blankCol;
                }
                if (blankCol + 1 < boardSize) {
                    moves[countTile++] = blankRow * boardSize + (blankCol + 1);
                }
                if (blankCol - 1 >= 0) {
                    moves[countTile++] = blankRow * boardSize + (blankCol - 1);
                }
            }

            public boolean hasNext() {
                return count < countTile;
            }

            public Board next() {
                int[][] neighArr = new int[boardSize][boardSize];
                for (int i = 0; i < boardSize; ++i) {
                    for (int j = 0; j < boardSize; ++j) {
                        neighArr[i][j] = board[i][j];
                    }
                }
                int blankValue = neighArr[blankRow][blankCol];
                int row = moves[count] / boardSize;
                int col = moves[count] % boardSize;
                neighArr[blankRow][blankCol] = neighArr[row][col];
                neighArr[row][col] = blankValue;
                Board neighborBoard = new Board(neighArr);
                count++;
                return neighborBoard;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (twinRow == -1 && twinCol == -1) {
            int i = StdRandom.uniform(boardSize);
            int j = StdRandom.uniform(boardSize - 1);
            while (i == blankRow && (j == blankCol || (j + 1) == blankCol)) {
                i = StdRandom.uniform(boardSize);
                j = StdRandom.uniform(boardSize - 1);
            }
            twinRow = i;
            twinCol = j;
        }
        Board twinBoard = new Board(board);
        exchange(twinBoard, twinRow, twinCol);
        return twinBoard;
    }

    // to exchange two tiles on the same row
    private void exchange(Board twinBoard, int i, int j) {
        int a = twinBoard.board[i][j];
        twinBoard.board[i][j] = twinBoard.board[i][j + 1];
        twinBoard.board[i][j + 1] = a;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // for each command-line argument
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // solve the slider puzzle
            Board initial = new Board(tiles);
            StdOut.println(initial.toString());
            //StdOut.println("Manhattan:" + " " + initial.manhattan());
            //StdOut.println("Hamming:" + " " + initial.hamming());
            for (Board b : initial.neighbors()) {
                StdOut.println("Neighbor:" + " " + b.toString());
                for (Board neighB : b.neighbors()) {
                    StdOut.println("Neighbor of b:" + neighB.toString());
                }
            }
            //StdOut.println("Twin Board:" + " " + initial.twin().toString());

        }

    }
}
