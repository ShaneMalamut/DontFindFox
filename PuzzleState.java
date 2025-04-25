import java.util.Arrays;
import java.util.Set;

/**
 * Class representing a single world state, defined by the placement of tiles in the grid, 
 * the count of tiles yet to be placed, and which player's turn it is.
 * Initialized with an arbitrary grid size, special word and letter counts.
 */
public class PuzzleState {
    final char[][]  grid;
    final int       row_count;
    final int       column_count;
    final String    word;
    final TilesLeft tiles;
    boolean         turn;
    int             moves_made;
    int[]           last_cell_position = null;

    /**
     * Constructor.
     * If grid is null, constructs an empty PuzzleState with the given number of rows and columns.
     * @param grid          A two-dimensional array of the rows and columns
     * @param row_count     The number of rows in the grid
     * @param column_count  The number of columns in the grid
     * @param tiles         The count of each tile or "letter" not yet placed in the grid
     * @param turn          True when it is player 1's turn (MAX), false when player 2's turn (MIN)
     */
    public PuzzleState(char[][] grid, int row_count, int column_count, String word, 
    TilesLeft tiles, boolean turn, int moves_made) {
        if (grid == null)
            grid = new char[row_count][column_count];
        
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

        for (char[] row : grid) {
            for (char cell : row) {
                System.out.print((cell == 0 ? "." : cell) + " ");
            }

            System.out.println();
        }
    }

    /**
     * Clones the current PuzzleState with a deep copy of the grid and tiles. 
     */
    public PuzzleState constructNeighbor() {
        // Make a copy of the grid by copying each row into a new grid
        char[][] newGrid = new char[row_count][column_count];
        for (int i = 0; i < row_count; i++) {
            newGrid[i] = Arrays.copyOf(grid[i], row_count);
        }

        // Make a copy of the tiles
        TilesLeft newTiles = new TilesLeft(tiles);

        PuzzleState neighbor = new PuzzleState(newGrid, row_count, column_count, word, 
            newTiles, turn, moves_made);
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
        int[] position = {row, col};
        last_cell_position = position;
        moves_made++;
        turn = !turn;
        tiles.decrementPiece(tile);
    }
}