import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DontFindFox {
    /**
     * Class representing the tiles left to be played.
     */
    public class TilesLeft {
        protected HashMap<Character, Integer> tiles = new HashMap<>();

        /** 
         * Copy constructor.
         */
        @SuppressWarnings("unchecked")
		public TilesLeft(TilesLeft original) {
            this.tiles = (HashMap<Character, Integer>) original.tiles.clone();
        }

        /**
         * Print the tiles and their counts.
         */
        public void printTiles() {
            for (Map.Entry<Character, Integer> entry : tiles.entrySet()) {
                System.out.print(entry.getKey() + "=" + entry.getValue() + " ");
            }

            System.out.println();
        }
        

        /**
         * Add a tile and its count to the pieces remaining.
         * This function should be used during construction.
         */
        public void add(char tile, int count) {
            tiles.put(tile, count);
        }

        /**
         * Decrement the count of the given tile by 1.
         */
        public void decrementPiece(char tile) {
            tiles.put(tile, tiles.get(tile) - 1);
        }

        /**
         * Get the set of characters which have a count greater than zero.
         */
        public Set<Character> getAvailableCharacters() {
            Set<Character> available = new HashSet<>();

            for (Map.Entry<Character, Integer> entry : tiles.entrySet()) {
                if (entry.getValue() > 0) {
                    available.add(entry.getKey());
                }
            }

            return available;
        }
    }

    /**
     * Class representing a single world state, defined by the placement of tiles in the grid, 
     * the count of tiles yet to be placed, and which player's turn it is.
     * Initialized with an arbitrary grid size, special word and letter counts.
     */
    public class PuzzleState {
        private final int[][]   grid;
        private final int       row_count;
        private final int       column_count;
        private final String    word;
        private final TilesLeft tiles;
        private final boolean   turn;
        private final int       moves_made;
        private int[]           last_cell_position = null;

        /**
         * Constructor.
         * If grid is null, constructs an empty PuzzleState with the given number of rows and columns.
         * @param grid          A two-dimensional array of the rows and columns
         * @param row_count     The number of rows in the grid
         * @param column_count  The number of columns in the grid
         * @param tiles         The count of each tile or "letter" not yet placed in the grid
         * @param turn          True when it is player 1's turn (MAX), false when player 2's turn (MIN)
         */
        public PuzzleState(int[][] grid, int row_count, int column_count, String word, 
        TilesLeft tiles, boolean turn, int moves_made) {
            if (grid == null) {
                grid = new int[row_count][column_count];
            }
            
            this.row_count = row_count;
            this.column_count = column_count;
            this.grid = grid;
            this.word = word;
            this.tiles = tiles;
            this.turn = turn;
            this.moves_made = moves_made;
        }

        /**
         * Print the tiles and the grid.
         */
        public void printPuzzle() {
            tiles.printTiles();

            for (int[] row : grid) {
                for (int cell : row) {
                    System.out.print((cell == 0 ? '.' : cell) + ' ' );
                }

                System.out.println();
            }
        }

        /**
         * Clones the current PuzzleState with a deep copy of the grid and tiles. Also changes the turn and increases moves made.
         */
        public PuzzleState constructNeighbor() {
            // Make a copy of the grid by copying each row into a new grid
            int[][] newGrid = new int[row_count][column_count];
            for (int i = 0; i < row_count; i++) {
                newGrid[i] = Arrays.copyOf(grid[i], row_count);
            }

            // Make a copy of the tiles
            TilesLeft newTiles = new TilesLeft(tiles);

            PuzzleState neighbor = new PuzzleState(newGrid, row_count, column_count, word, newTiles, !turn, moves_made + 1);
            return neighbor;
        }

        /**
         * Create an array of all possible moves that can be made
         * @return Array of PuzzleState neighbors
         */
        public PuzzleState[] neighbors() {
            Set<Character> available = tiles.getAvailableCharacters();
            int neighborCount = available.size() * (row_count * column_count - moves_made);
            PuzzleState[] neighbors = new PuzzleState[neighborCount];
            int count = 0;

            // Loop through the grid and create a neighbor for every tile that could be inserted into each cell.
            for (int i = 0; i < row_count; i++) {
                for (int j = 0; j < column_count; j++) {
                    if (grid[i][j] == 0) {
                        for (char tile : available) {
                            PuzzleState neighbor = constructNeighbor();
                            
                            neighbor.insertTile(i, j, tile);
                            neighbor.tiles.decrementPiece(tile);
                            int[] position = {i, j};
                            neighbor.last_cell_position = position;
                            
                            neighbors[count] = neighbor;
                            count++;
                        }
                    }
                }
            }

            return neighbors;
        }

        /**
         * Insert a tile into the grid at a given row and column.
         */
        public void insertTile(int row, int col, char tile) {
            grid[row][col] = tile;
        }
    }

    /**
     * AlphaBeta pruning algorithm and heuristic.
     */
    public class AlphaBeta {
        private String   word;
        private String[] combinations;
        private Double   max_value;
        private Double   min_value;

        protected class PuzzleScore {
            private final PuzzleState state;
            private final Double      value;

            public PuzzleScore(PuzzleState state, Double value) {
                this.state = state;
                this.value = value;
            }
        }

        /**
         * Prepare values and begin the AlphaBeta pruning algorithm.
         * Returns the best neighbor and its value.
         */
        public PuzzleScore start(PuzzleState state, int depth, String word) {
            combinations = generateCombinations(word);

            return h_alphabeta(state, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }

        /**
         * Recursive AlphaBeta pruning algorithm.
         */
        private PuzzleScore h_alphabeta(PuzzleState state, int depth, Double alpha, Double beta) {
            int leaf_value = isLeaf(state);
            
            if (leaf_value == max_value) {
                return new PuzzleScore(null, max_value);
            } else if (leaf_value == min_value) {
                return new PuzzleScore(null, min_value);
            } else if (depth == 0) {
                return new PuzzleScore(null, heuristic(state));
            } else if (state.turn) { // MAX node
                Double value = Double.NEGATIVE_INFINITY;
                PuzzleState best_neighbor = null;

                for (PuzzleState neighbor : state.neighbors()) {
                    PuzzleScore child_value = h_alphabeta(neighbor, depth - 1, alpha, beta);

                    if (child_value.value > value) {
                        value = child_value.value;
                        best_neighbor = neighbor;
                    }
                    if (value > alpha) {
                        alpha = value;
                    }
                    if (alpha >= beta) {
                        break; // Pruning
                    }
                }

                // Return value and best neighbor for MAX
                return new PuzzleScore(best_neighbor, value);
            } else { // MIN node
                Double value = Double.POSITIVE_INFINITY;
                PuzzleState best_neighbor = null;

                for (PuzzleState neighbor : state.neighbors()) {
                    PuzzleScore child_value = h_alphabeta(neighbor, depth - 1, alpha, beta);

                    if (child_value.value < value) {
                        value = child_value.value;
                        best_neighbor = neighbor;
                    }
                    if (value < beta) {
                        beta = value;
                    }
                    if (beta <= alpha) {
                        break; // Pruning
                    }
                }

                // Return value and best neighbor for MIN
                return new PuzzleScore(best_neighbor, value);
            }
        }

        private Double heuristic(PuzzleState state) {
            return 0.0;
        }

        /**
         * Generate the two-letter combinations from the word, used by the heuristic.
         */
        private String[] generateCombinations(String word) {
            return null;
        }

        /**
         * Return the max or min value if it is a leaf node. Otherwise, return 0.
         */
        private int isLeaf(PuzzleState state) {
            return 0;
        }
    }

    public static void main (String[] args)
    {
        System.out.println("test");
	}
}