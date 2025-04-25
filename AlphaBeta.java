import java.util.HashSet;
import java.util.Set;

/**
* AlphaBeta pruning algorithm and heuristic.
*/
public class AlphaBeta {
    private String      word;
    private Set<String> combinations;
    private int         max_value;
    private int         min_value;
    private int         row_count;
    private int         column_count;

    /**
        * Prepare values and begin the AlphaBeta pruning algorithm.
        * Returns the best neighbor and its value.
        */
    public PuzzleScore start(PuzzleState state, int depth, String word) {
        row_count = state.row_count;
        column_count = state.column_count;
        this.word = word;
        combinations = generateCombinations(word);
        
        max_value = 0;
        // Calculate min and max heuristic values
        for (String combination : combinations) {
            // Get the count for the left letter and the right letter
            int left_count = state.tiles.tiles.get(combination.charAt(0));
            int right_count = state.tiles.tiles.get(combination.charAt(1));

            // Add the minimum to max_value
            max_value += Math.min(left_count, right_count);
        }
        min_value = 0; // Min value is just 0 because the heuristic can't go negative.

        return h_alphabeta(state, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
        * Recursive AlphaBeta pruning algorithm.
        */
    private PuzzleScore h_alphabeta(PuzzleState state, int depth, int alpha, int beta) {
        int leaf_value = isLeaf(state);
        
        if (leaf_value > 0) { // Win for MAX
            return new PuzzleScore(null, max_value);
        } else if (leaf_value < 0) { // Win for MIN
            return new PuzzleScore(null, min_value);
        } else if (depth == 0) {
            return new PuzzleScore(null, heuristic(state));
        } else if (state.turn) { // MAX node
            int value = Integer.MIN_VALUE;
            PuzzleState best_neighbor = null;

            for (PuzzleState neighbor : state.neighbors()) {
                PuzzleScore child_value = h_alphabeta(neighbor, depth - 1, alpha, beta);

                if (child_value.value > value) {
                    value = child_value.value;
                    best_neighbor = neighbor;
                }

                alpha = Math.max(alpha, value);
                
                if (alpha >= beta)
                    break; // Pruning
            }

            // Return value and best neighbor for MAX
            return new PuzzleScore(best_neighbor, value);
        } else { // MIN node
            int value = Integer.MAX_VALUE;
            PuzzleState best_neighbor = null;

            for (PuzzleState neighbor : state.neighbors()) {
                PuzzleScore child_value = h_alphabeta(neighbor, depth - 1, alpha, beta);

                if (child_value.value < value) {
                    value = child_value.value;
                    best_neighbor = neighbor;
                }

                beta = Math.min(beta, value);

                if (beta <= alpha)
                    break; // Pruning
            }

            // Return value and best neighbor for MIN
            return new PuzzleScore(best_neighbor, value);
        }
    }

    /**
        * Counts and totals how often each two-letter combination from the word appears in the grid.
        * Only counts if the word can actually be completed. (i.e. isn't being blocked by an invalid tile 
        * or the edge of the grid.)
        */
    private int heuristic(PuzzleState state) {
        int matches = 0;

        // Check down
        for (int row = 0; row < row_count - 1; row++) {
            for (int column = 0; column < column_count; column++) {
                if (combinations.contains("" + state.grid[row][column] + state.grid[row + 1][column]))
                    matches++;
            }
        }

        // Check to the right
        for (int row = 0; row < row_count; row++) {
            for (int column = 0; column < column_count - 1; column++) {
                if (combinations.contains("" + state.grid[row][column] + state.grid[row][column + 1]))
                    matches++;
            }
        }

        // Check up
        for (int row = 1; row < row_count; row++) {
            for (int column = 0; column < column_count; column++) {
                if (combinations.contains("" + state.grid[row][column] + state.grid[row - 1][column]))
                    matches++;
            }
        }

        // Check to the right
        for (int row = 0; row < row_count; row++) {
            for (int column = 1; column < column_count; column++) {
                if (combinations.contains("" + state.grid[row][column] + state.grid[row][column - 1]))
                    matches++;
            }
        }

        return matches;
    }

    /**
        * Generate the two-letter combinations from the word, used by the heuristic. 
        */
    private Set<String> generateCombinations(String word) {
        combinations = new HashSet<>();

        for (int i = 0; i < word.length() - 1; i++) {
            combinations.add(word.substring(i, i + 2));
        }

        return combinations;
    }

    /**
        * Return 1 if it is a win for MAX, -1 if it is a win for MIN, or 0 if it is not a leaf node.
        */
    public int isLeaf(PuzzleState state) {
        if (state.last_cell_position == null)
            return 0;

        int row = state.last_cell_position[0];
        int column = state.last_cell_position[1];

        // Check if the word is present, checking every position in the word
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            
            // Skip this letter if it's not the right one
            if (c != state.grid[row][column])
                continue;
            
            // Determine which directions the word can fit in the grid
            boolean left_to_right = column + word.length() - i <= column_count && column - i >= 0;
            boolean right_to_left = column - word.length() + i + 1 >= 0 && column + i + 1 <= column_count;
            boolean top_to_bottom = row + word.length() - i <= row_count && row - i >= 0;
            boolean bottom_to_top = row - word.length() + i + 1 >= 0 && row + i + 1 <= row_count;

            // Check for the word in the horizontal axis
            if (left_to_right) {
                int leftmost_column = column - i;

                boolean found = true;
                for (int j = leftmost_column; j < word.length() + leftmost_column; j++) {
                    if (state.grid[row][j] != word.charAt(j - leftmost_column)) {
                        found = false;
                        break;
                    }
                }

                if (found) {
                    return 1;
                }
            }
            if (right_to_left) {
                int rightmost_column = column + i; //2

                boolean found = true;
                for (int j = rightmost_column; j > rightmost_column - word.length(); j--) {
                    if (state.grid[row][j] != word.charAt(-(j - rightmost_column))) {
                        found = false;
                        break;
                    }
                }

                if (found) {
                    return 1;
                }
            }

            // Check for the word in the vertical axis
            if (top_to_bottom) {
                int topmost_row = row - i;

                boolean found = true;
                for (int j = topmost_row; j < word.length() + topmost_row; j++) {
                    if (state.grid[j][column] != word.charAt(j - topmost_row)) {
                        found = false;
                        break;
                    }
                }

                if (found) {
                    return 1;
                }
            }
            if (bottom_to_top) {
                int bottommost_row = row + i;

                boolean found = true;
                for (int j = bottommost_row; j > bottommost_row - word.length(); j--) {
                    if (state.grid[j][column] != word.charAt(-(j - bottommost_row))) {
                        found = false;
                        break;
                    }
                }

                if (found) {
                    return 1;
                }
            }

            // Check for the word in the diagonals
            // if (top_to_bottom && left_to_right) {
            // }
            // if (top_to_bottom && right_to_left) {
            // }
            // if (bottom_to_top && left_to_right) {
            // }
            // if (bottom_to_top && right_to_left) {
            // }
        }

        // If it is a full board, it is a win for MAX
        if (state.moves_made == row_count * column_count) {
            return -1;
        }

        // Not a leaf node
        return 0;
    }
}