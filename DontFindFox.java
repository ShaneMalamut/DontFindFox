import java.util.HashMap;
import java.util.Map;

public class DontFindFox {
    public class PiecesRemaining {
        protected final Map<String, Integer> pieces = new HashMap<>();
        protected final Map<Integer, Integer> counts = new HashMap<>();
        private int nextId = 1;

        /**
         * Add a piece and its count to the pieces remaining.
         */
        public void add(final String piece, final int count) {
            pieces.put(piece, nextId);
            counts.put(nextId, count);
            nextId++;
        }

        /**
         * Decrements the count of the appropriate piece by 1
         * @param id the Id of the piece
         */
        public void decrementPiece(int id) {
            counts.put(id, counts.get(id) - 1);
        }
    }

    /**
     * Class representing a single world state, defined by the placement of pieces in the grid, and the pieces remaining.
     */
    public class PuzzleState {
        private int row_count;
        private int column_count;
        private int[][] grid;
        private PiecesRemaining pieces;

        /**
         * Default Value Constructor.
         * Constructs an empty PuzzleState with the given number of rows and columns.
         * All cells are set to the value 0.
         * @param row_count The number of rows in the grid
         * @param column_count The number of columns in the grid
         * @param pieces The count of pieces not yet placed in the grid
         */
        public PuzzleState(final int row_count, final int column_count, PiecesRemaining pieces) {
            this.row_count = row_count;
            this.column_count = column_count;
            this.grid = new int[row_count][column_count];
            this.pieces = pieces;
        }

        /**
         * Explicit Value Constructor.
         * @param grid A two-dimensional array of the rows and columns.
         * 0 represents an empty cell, and positive integers each represent a letter.
         * @param pieces The count of pieces not yet placed in the grid
         */
        public PuzzleState(final int[][] grid, PiecesRemaining pieces) {
            this.grid = grid;
            this.row_count = grid.length;
            this.column_count = grid[0].length;
            this.pieces = pieces;
        }

        /**
         * Create an array of all possible moves that can be made
         * @return Array of PuzzleState neighbors
         */
        public PuzzleState[] neighbors() {
            return null;
        }
    }

    /**
     * Algorithm for finding the best possible move.
     */
    public class Solver {

    }

    public static void main (String[] args)
    {
        System.out.println("test");
	}
}