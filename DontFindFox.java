public class DontFindFox {
    public static void main (String[] args)
    {
        TilesLeft tiles = new TilesLeft();
        tiles.add('F', 5);
        tiles.add('O', 6);
        tiles.add('X', 5);

        PuzzleState board = new PuzzleState(null, 4, 4, 
        "FOX", tiles, true, 0);
        AlphaBeta ab = new AlphaBeta();

        System.out.println("Move 0\nStarting board:");
        board.printPuzzle();
        System.out.println("MAX is attempting to spell the word \"FOX\", while MIN avoids spelling the word.");
        PuzzleScore next_state;

        int move = 1;
        while (true) {
            System.out.println("\nMove " + move + "\nWaiting for computer...");

            next_state = ab.start(board, 4, "FOX");

            System.out.println("MAX made this move with alpha value " + next_state.value);
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
            next_state = ab.start(board, 4, "FOX");

            System.out.println("MIN made this move with alpha value " + next_state.value);
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
}