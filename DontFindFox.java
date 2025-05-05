import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DontFindFox {
    public static void computerVsComputer(TilesLeft tiles, String word, int row_count, int column_count, int depth, boolean turn) {
        PuzzleState board = new PuzzleState(null, row_count, column_count, 
        word, tiles, turn, 0);
        AlphaBeta ab = new AlphaBeta();

        System.out.println("Starting with depth " + depth + " and starting turn with " + (turn ? "MAX" : "MIN"));
        board.printPuzzle();
        System.out.println("MAX is attempting to spell the word \"" + word + "\", while MIN avoids spelling the word.");
        PuzzleScore next_state;

        int move = 1;
        while (true) {
            System.out.println("\nMove " + move + "\nWaiting for computer...");

            next_state = ab.start(board, depth, word);

            System.out.println((board.turn ? "MAX" : "MIN") + " made this move with alpha value " + next_state.value);
            board = next_state.state;
            board.printPuzzle();

            if (ab.isLeaf(board) > 0) {
                System.out.println("Game over: MAX wins!");
                break;
            }
            if (ab.isLeaf(board) < 0) {
                System.out.println("Game over: MIN wins!");
                break;
            }

            System.out.println("Waiting for computer...");
            next_state = ab.start(board, depth, word);

            System.out.println((board.turn ? "MAX" : "MIN") + " made this move with alpha value " + next_state.value);
            board = next_state.state;
            board.printPuzzle();

            if (ab.isLeaf(board) > 0) {
                System.out.println("Game over: MAX wins!");
                break;
            }
            if (ab.isLeaf(board) < 0) {
                System.out.println("Game over: MIN wins!");
                break;
            }

            move++;
        }
    }

    public static void computerVsPlayer(TilesLeft tiles, String word, int row_count, int column_count, int depth, boolean turn) 
    throws IOException {
        PuzzleState board = new PuzzleState(null, row_count, column_count, 
        word, tiles, turn, 0);
        AlphaBeta ab = new AlphaBeta();

        System.out.println("Starting with depth " + depth + " and starting turn with " + (turn ? "MAX" : "MIN"));
        board.printPuzzle();
        System.out.println("MAX is attempting to spell the word \"" + word + "\", while MIN avoids spelling the word.");
        System.out.println("You play as " + (turn ? "MIN" : "MAX"));
        PuzzleScore next_state;

        BufferedReader r = new BufferedReader(
            new InputStreamReader(System.in));

        int move = 1;
        while (true) {
            System.out.println("\nMove " + move + "\nWaiting for computer...");

            next_state = ab.start(board, depth, word);

            System.out.println((board.turn ? "MAX" : "MIN") + " made this move with alpha value " + next_state.value);
            board = next_state.state;
            board.printPuzzle();

            if (ab.isLeaf(board) > 0) {
                System.out.println("Game over: MAX wins!");
                break;
            }
            if (ab.isLeaf(board) < 0) {
                System.out.println("Game over: MIN wins!");
                break;
            }

            System.out.println("What row? Enter a number between 0 and " + (row_count - 1));
            int row = Integer.parseInt(r.readLine());
            System.out.println("What column? Enter a number between 0 and " + (column_count - 1));
            int column = Integer.parseInt(r.readLine());
            System.out.println("What character?");
            char c = r.readLine().charAt(0);
            board.insertTile(row, column, c);

            System.out.println("Player made this move");
            board.printPuzzle();

            if (ab.isLeaf(board) > 0) {
                System.out.println("Game over: MAX wins!");
                break;
            }
            if (ab.isLeaf(board) < 0) {
                System.out.println("Game over: MIN wins!");
                break;
            }

            move++;
        }
    }
    
    public static void main (String[] args) throws IOException
    {
        while (true) {
            BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
            
            System.out.println("How many rows?");
            int rows = Integer.parseInt(r.readLine());
            System.out.println("How many columns?");
            int columns = Integer.parseInt(r.readLine());

            TilesLeft tiles = new TilesLeft();
            int tiles_count = 0;
            while (tiles_count < rows * columns) {
                System.out.print(rows * columns - tiles_count + " tiles still needed. Enter next character: ");
                char c = r.readLine().charAt(0);
                System.out.println("How many of that tile? ");
                int count = Integer.parseInt(r.readLine());
                if (tiles_count + count > rows * columns) {
                    System.out.println("That's too many tiles. Please try again.");
                } else {
                    tiles_count += count;
                    tiles.add(c, count);
                }
            }

            System.out.println("What is the special word?");
            String word = r.readLine();

            System.out.println("Does MAX go first? [Y/n]");
            boolean turn = 'Y' == r.readLine().charAt(0);

            System.out.println("What depth should be used for the algorithm?");
            int depth = Integer.parseInt(r.readLine());

            System.out.println("Do you want to play against the computer? [Y/n]");
            boolean PvC = 'Y' == r.readLine().charAt(0);

            if (PvC) {
                computerVsPlayer(tiles, word, rows, columns, depth, turn);
            }
            else {
                computerVsComputer(tiles, word, rows, columns, depth, turn);
            }
        }
    }
}