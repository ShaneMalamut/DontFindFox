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
    
    public static void main (String[] args)
    {
        TilesLeft tiles = new TilesLeft();
        // tiles.add('F', 5);
        // tiles.add('O', 6);
        // tiles.add('X', 5);

        // computerVsComputer(tiles, "FOX", 4, 4, 4, false);

        // tiles = new TilesLeft();
        // tiles.add('F', 21);
        // tiles.add('O', 22);
        // tiles.add('X', 21);

        // computerVsComputer(tiles, "FOX", 8, 8, 4, false);

        // tiles = new TilesLeft();
        // tiles.add('P', 21);
        // tiles.add('O', 22);
        // tiles.add('L', 21);

        // computerVsComputer(tiles, "POLO", 8, 8, 4, true);


        tiles.add('P', 16);
        tiles.add('O', 16);
        tiles.add('L', 16);
        tiles.add('E', 16);

        computerVsComputer(tiles, "POLO", 8, 8, 4, true);
	}
}