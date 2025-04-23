import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
        private int[] nextPosition;
        private Random random = new Random();

        /**
         * Default Value Constructor.
         * Constructs an empty PuzzleState with the given number of rows and columns.
         * All cells are set to the value 0.
         * @param row_count The number of rows in the grid
         * @param column_count The number of columns in the grid
         * @param pieces The count of pieces not yet placed in the grid
         * @param nextPosition A 2-element array containing the row and column of the next piece.
         */
        public PuzzleState(final int row_count, final int column_count, 
        final PiecesRemaining pieces, int[] nextPosition) {
            this.row_count = row_count;
            this.column_count = column_count;
            this.grid = new int[row_count][column_count];
            this.pieces = pieces;
            this.nextPosition = nextPosition;
        }
        
        /**
         * Explicit Value Constructor.
         * @param grid A two-dimensional array of the rows and columns.
         * 0 represents an empty cell, and positive integers each represent a letter.
         * @param pieces The count of pieces not yet placed in the grid
         * @param nextPosition A 2-element array containing the row and column of the next piece.
         */
        public PuzzleState(final int[][] grid, PiecesRemaining pieces, int[] nextPosition) {
            this.grid = grid;
            this.row_count = grid.length;
            this.column_count = grid[0].length;
            this.pieces = pieces;
            this.nextPosition = nextPosition;
        }

        public PuzzleState(final int[][] grid, PiecesRemaining pieces) {
            this(grid, pieces, null);
        }

        /**
         * Create an array of all possible moves that can be made
         * @return Array of PuzzleState neighbors
         */
        public PuzzleState[] neighbors() {
            PuzzleState[] neighbors = new PuzzleState[row_count * column_count];
            int count = 0;

            for (int i = 0; i < row_count; i++) {
                for (int j = 0; j < column_count; j++) {
                    if (grid[i][j] == 0) {
                        int[] position = new int[2];
                        position[0] = i;
                        position[1] = j;
                        neighbors[count] = new PuzzleState(grid, pieces, position);
                        count++;
                    }
                }
            }

            return neighbors;
        }

        /**
         * Insert a given piece into the grid at nextPosition.
         * Also removes that piece from the pieces remaining
         */
        public void addPiece(int piece) {
            grid[nextPosition[0]][nextPosition[1]] = piece;
            pieces.decrementPiece(piece);
        }

        /**
         * Insert a randomly chosen piece into the grid at nextPosition.
         * Also removes that piece from the pieces remaining
         */
        public void addPiece() {
            int piece = -1;

            // Calculate the total count (used for weighting)
            int total = 0;
            for (int count : pieces.counts.values()) {
                total += count;
            }

            if (total == 0) {
                throw new IllegalStateException("No pieces available to choose from.");
            }

            // Pick a random number between 0 and total - 1
            int target = random.nextInt(total);

            // Loop through the counts and find the corresponding piece ID
            int runningSum = 0;
            for (Integer pieceId : pieces.counts.keySet()) {
                int count = pieces.counts.get(pieceId);
                runningSum += count;
            
                if (target < runningSum) {
                    piece = pieceId;
                }
            }

            addPiece(piece);
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