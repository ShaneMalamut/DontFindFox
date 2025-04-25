import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class representing the tiles left to be played.
 */
public class TilesLeft {
    protected HashMap<Character, Integer> tiles;

    public TilesLeft() {
        tiles = new HashMap<>();
    }

    /** 
     * Copy constructor.
     */
    @SuppressWarnings("unchecked")
    public TilesLeft(TilesLeft original) {
        tiles = (HashMap<Character, Integer>) original.tiles.clone();
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