package edu.cmu.cs.cs214.hw4.core;

import org.junit.*;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Test class that especially for testing Tile auto-generation and Tile
 * object class methods
 */
public class TileTest {
    private ParseTile parseTile;
    private List<Tile> tiles;
    private final static int[] TILESIZE = new int[]{2, 4, 1, 4, 5, 2, 1, 3, 2
            , 3, 3, 3, 2, 3, 2, 3, 1, 3, 2, 1, 8, 9, 4, 1};

    @Before
    public void setUp() {
        parseTile = new ParseTile();
        tiles = parseTile.generateTiles();
    }

    /**
     * Should be exactly 72
     */
    @Test
    public void testSize() {
        // Test if it generate exactly 72 tiles
        assertEquals(tiles.size(), 72);
    }

    /**
     * Start tile should be D
     */
    @Test
    public void testStartTile() {
        //Test if the list of tiles start with the start tile
        Tile startTile = tiles.get(0);
        assertEquals(startTile.getTag(), 'D');
    }

    /**
     * Test if each type of tile has generated correct amount
     */
    @Test
    public void testNumOfEachKindTile() {
        // Test generate the correct number of each type tile
        int[] countNum = new int[24];
        for (int i = 0; i < tiles.size(); i++) {
            countNum[tiles.get(i).getTag() - 'A']++;
        }
        for (int i = 0; i < countNum.length; i++) {
            assertEquals(countNum[i], TILESIZE[i]);
        }
    }

    /**
     *
     */
    @Test
    public void testTileSetOrientation() {
        // First tile should be D type
        Tile tile = tiles.get(0);
        // Rotate the tile 90 degrees clockwise
        tile.rotate();
        assertEquals(tile.getEdgeSegment(0).getType(), 0);
        assertEquals(tile.getEdgeSegment(1).getType(), 2);
        assertEquals(tile.getEdgeSegment(2).getType(), 3);
        assertEquals(tile.getEdgeSegment(3).getType(), 2);
        assertEquals(tile.getEdgeSegment(4).getType(), 2);

        // Rotate the tile 180 degrees clockwise
        tile.rotate();
        assertEquals(tile.getEdgeSegment(0).getType(), 2);
        assertEquals(tile.getEdgeSegment(1).getType(), 0);
        assertEquals(tile.getEdgeSegment(2).getType(), 2);
        assertEquals(tile.getEdgeSegment(3).getType(), 3);
        assertEquals(tile.getEdgeSegment(4).getType(), 2);

        // Rotate the tile 270 degrees clockwise
        tile.rotate();
        assertEquals(tile.getEdgeSegment(0).getType(), 3);
        assertEquals(tile.getEdgeSegment(1).getType(), 2);
        assertEquals(tile.getEdgeSegment(2).getType(), 0);
        assertEquals(tile.getEdgeSegment(3).getType(), 2);
        assertEquals(tile.getEdgeSegment(4).getType(), 2);

        // Rotate the tile 360 degrees clockwise
        tile.rotate();
        assertEquals(tile.getEdgeSegment(0).getType(), 2);
        assertEquals(tile.getEdgeSegment(1).getType(), 3);
        assertEquals(tile.getEdgeSegment(2).getType(), 2);
        assertEquals(tile.getEdgeSegment(3).getType(), 0);
        assertEquals(tile.getEdgeSegment(4).getType(), 2);
    }

    @Test
    public void testSetPosition() {
        Tile tile = tiles.get(0);
        assertNull(tile.getPosition());
        Position position = new Position(0, 0);
        tile.setPosition(position);
        assertEquals(tile.getPosition(), position);
    }

    @Test
    public void testGetMonastery() {
        Tile tile = tiles.get(0);
        assertFalse(tile.getHasMonastery());
    }
}
