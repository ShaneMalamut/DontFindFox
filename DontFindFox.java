public class DontFindFox {
    public static void main (String[] args)
    {
        TilesLeft tiles = new TilesLeft();
        tiles.add('F', 5);
        tiles.add('O', 6);
        tiles.add('X', 5);

        PuzzleState board = new PuzzleState(null, 4, 4, "FOX", tiles, true, 0);
        AlphaBeta ab = new AlphaBeta();
        board.printPuzzle();
	}
}