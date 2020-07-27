package edu.cmu.cs.cs214.hw4.core;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Test class for testing GameBoard class and its methods
 */
public class GameBoardTest {
    private ParseTile parseTile;
    private List<Tile> tiles;
    private GameBoard gameBoard;
    private final static int NUMPLAYERS = 5;

    @Before
    public void setUp() {
        gameBoard = new GameBoard(NUMPLAYERS);
        // Tiles for testing
        parseTile = new ParseTile();
        // For better testing, this tile list is not randomly shuffle, so
        // that a specific type of tile can be found easily
        tiles = parseTile.generateSortedTiles();
    }

    /**
     * This function test the constructor of the GameBoard
     */
    @Test
    public void testConstructor() {
        assertNotNull(gameBoard.getFeatureList());
        assertNotNull(gameBoard.getPlayer(3));
    }

    /**
     * This function not only test place tile, but also the condition of
     * merge two road features into one and other supposed behaviors
     */
    @Test
    public void testPlaceTile_RoadMerge() {
        //At the beginning of the game, the tile can only be placed on (0,0)
        assertFalse(gameBoard.placeTile(tiles.get(0), new Position(1, 1)));
        assertTrue(gameBoard.placeTile(tiles.get(0), new Position(0, 0)));
        assertEquals(gameBoard.getFeatureList().size(), 2);

        // Not we get type A tile placed on (0,0) point

        // Test case 1: put type D tile on (0,-1) point -> valid placement
        assertEquals(tiles.get(7).getTag(), 'D');
        assertTrue(gameBoard.placeTile(tiles.get(7), new Position(0, 1)));
        assertEquals(gameBoard.getFeatureList().size(), 3);

        // Test case 2: put type F tile on (0,-1) point -> invalid placement,
        // position is occupied!
        assertEquals(tiles.get(15).getTag(), 'F');
        assertFalse(gameBoard.placeTile(tiles.get(15), new Position(0, 1)));

        // Test case 3: put type F tile on (10,-10) point -> invalid
        // placement, there must be at least one  tile around this tile
        assertFalse(gameBoard.placeTile(tiles.get(15), new Position(10, 10)));

        // Test case 4: put type F tile on (1,-1) point -> valid placement
        assertTrue(gameBoard.placeTile(tiles.get(15), new Position(1, 1)));

        // Test case 5: put type J tile on (0,-2) -> invalid placement, need
        // to rotate 270 degrees
        Tile J = tiles.get(23);
        assertEquals(J.getTag(), 'J');
        assertFalse(gameBoard.placeTile(J, new Position(0, 2)));
        // rotate 90 degrees
        J.rotate();
        // rotate 180 degrees
        J.rotate();
        // rotate 270 degrees
        J.rotate();
        assertTrue(gameBoard.placeTile(J, new Position(0, 2)));


        // Now we should have one road feature, two city feature, one
        // monastery feature
        assertEquals(gameBoard.getFeatureList().size(), 4);


        ///////////////////////////Partition Line////////////////////////////
        // Here we start to test merge two feature, put D type on (2,-1),
        // rotate 180 degree, one city feature is complete!
        Tile D = tiles.get(7);
        assertEquals(D.getTag(), 'D');
        D.rotate();
        D.rotate();
        assertTrue(gameBoard.placeTile(D, new Position(2, 1)));
        gameBoard.calculateCityScore(D);
        assertEquals(gameBoard.getFeatureList().size(), 5);

        // put O type on (2, -2), rotate 180 degree
        Tile O = tiles.get(37);
        assertEquals(O.getTag(), 'O');
        O.rotate();
        O.rotate();
        assertTrue(gameBoard.placeTile(O, new Position(2, 2)));
        // Now we have two road feature, both of them are not complete!
        assertEquals(gameBoard.getFeatureList().size(), 6);

        //Now put a U type tile to connect these two road feature! (rotate 90
        // degree)
        Tile U = tiles.get(49);
        assertEquals(U.getTag(), 'U');
        U.rotate();
        assertTrue(gameBoard.placeTile(U, new Position(1, 2)));

        //Now we have three road feature in roadList, but the previous two
        // were set to be complete, and they are merged to be a new one
        assertEquals(gameBoard.getFeatureList().size(), 7);


        // Finally we put a W type tile on (2, 0) to complete the this
        // feature while
        // introduce another two new road feature
        Tile W = tiles.get(66);
        assertEquals(W.getTag(), 'W');
        assertTrue(gameBoard.placeTile(W, new Position(2, 0)));
        gameBoard.calculateRoadScore(W);
        assertEquals(gameBoard.getFeatureList().size(), 9);
    }

    /**
     * This function not only test place tile, but also the condition of
     * merge two city features into one and other supposed behaviors
     */
    @Test
    public void testPlaceTile_CityMerge() {
        // To simply the test, suppose we start with H type tile (normally
        // the game start with D type)
        Tile H = tiles.get(18);
        assertEquals(H.getTag(), 'H');
        assertTrue(gameBoard.placeTile(H, new Position(0, 0)));
        gameBoard.calculateCityScore(H);
        // Put a E type, rotate 270 degree on (0, -1)
        Tile E = tiles.get(10);
        assertEquals(E.getTag(), 'E');
        E.rotate();
        E.rotate();
        E.rotate();
        assertTrue(gameBoard.placeTile(E, new Position(0, 1)));
        // So far, we should have three city features
        assertEquals(gameBoard.getFeatureList().size(), 3);
        gameBoard.calculateCityScore(E);

        // Put a P type, rotate 90 degree on (-1, -1)
        Tile P = tiles.get(39);
        assertEquals(P.getTag(), 'P');
        P.rotate();
        assertTrue(gameBoard.placeTile(P, new Position(-1, 1)));
        // So far, we should have three city features and one road feature
        assertEquals(gameBoard.getFeatureList().size(), 4);
        gameBoard.calculateCityScore(P);

        // Put a U type, rotate 90 degree on (-2, -1)
        Tile U = tiles.get(49);
        assertEquals(U.getTag(), 'U');
        U.rotate();
        assertTrue(gameBoard.placeTile(U, new Position(-2, 1)));
        // So far, we still only get one road
        assertEquals(gameBoard.getFeatureList().size(), 4);
        gameBoard.calculateCityScore(U);

        // Put a I type, rotate 270 degree on (-2, 0)
        Tile I = tiles.get(21);
        I.rotate();
        I.rotate();
        I.rotate();
        assertEquals(I.getTag(), 'I');
        assertTrue(gameBoard.placeTile(I, new Position(-2, 0)));
        // So far we should have 5 city features
        assertEquals(gameBoard.getFeatureList().size(), 6);
        gameBoard.calculateCityScore(I);

        // Finally put a Q type, rotate 180 degree, on (-1, 0) to merge three
        // city features together
        Tile Q = tiles.get(42);
        assertEquals(Q.getTag(), 'Q');
        Q.rotate();
        Q.rotate();
        assertTrue(gameBoard.placeTile(Q, new Position(-1, 0)));
        // So far, we should have 7 city features in total
        assertEquals(gameBoard.getFeatureList().size(), 8);
        // The merged city should have 5 segments to count scores
        assertEquals(gameBoard.getFeatureList().size(), 8);
        // And this merged city is finished
        gameBoard.calculateCityScore(Q);
        assertEquals(gameBoard.getFeatureList().size(), 8);
    }

    /**
     * This function not only test place tile, but also the condition of
     * a monastery is complete
     */
    @Test
    public void testPlaceTile_Monastery() {
        // For simply the testing, we start with B tile with a monastery
        // segment at (0,0)
        Tile B = tiles.get(2);
        assertEquals(B.getTag(), 'B');
        assertTrue(gameBoard.placeTile(B, new Position(0, 0)));
        assertEquals(gameBoard.getFeatureList().size(), 1);
        // Assume the player1's meeple occupy this monastery
        gameBoard.placeMeeple(B.getEdgeSegment(4), 1);
        // Now the player1 has 6 meeples left
        assertEquals(gameBoard.getPlayer(1).getLeftMeeple(), 6);

        // Put a U type on (1,0), now the monastery feature has 2 segments
        Tile U = tiles.get(49);
        assertTrue(gameBoard.placeTile(U, new Position(1, 0)));
        assertEquals(gameBoard.getFeatureList().size(), 2);

        // Put a W type on (1,1), now the monastery feature has 3 segments
        Tile W = tiles.get(66);
        assertTrue(gameBoard.placeTile(W, new Position(1, -1)));
        assertEquals(gameBoard.getFeatureList().size(), 4);

        // Put a D type on (0,1), rotate 270 degrees, now the monastery
        // feature has 4 segments
        Tile D = tiles.get(7);
        D.rotate();
        D.rotate();
        D.rotate();
        assertTrue(gameBoard.placeTile(D, new Position(0, -1)));
        assertEquals(gameBoard.getFeatureList().size(), 5);

        // Put a W type on (-1, 1), now the feature has 5 segments
        Tile W2 = tiles.get(67);
        assertTrue(gameBoard.placeTile(W2, new Position(-1, -1)));
        assertEquals(gameBoard.getFeatureList().size(), 7);

        // Put a J type, rotate 180 degree on (-1, 0), now feature has 6
        // segments
        Tile J = tiles.get(23);
        assertEquals(J.getTag(), 'J');
        J.rotate();
        J.rotate();
        assertTrue(gameBoard.placeTile(J, new Position(-1, 0)));
        assertEquals(gameBoard.getFeatureList().size(), 8);


        // Put a H type, rotate 90 on (-1, -1), feature has 7 segments
        Tile H = tiles.get(18);
        H.rotate();
        assertTrue(gameBoard.placeTile(H, new Position(-1, 1)));
        assertEquals(gameBoard.getFeatureList().size(), 9);

        // Put a V type, rotate 270 on (0, -1), feature has 8 segments
        Tile V = tiles.get(57);
        assertEquals(V.getTag(), 'V');
        V.rotate();
        V.rotate();
        V.rotate();
        assertTrue(gameBoard.placeTile(V, new Position(0, 1)));
        assertEquals(gameBoard.getFeatureList().size(), 10);

        // Finally put a K type at (1,-1), feature has 9 segments, feature is
        // complete!
        Tile K = tiles.get(26);
        assertEquals(K.getTag(), 'K');
        assertTrue(gameBoard.placeTile(K, new Position(1, 1)));
        assertEquals(gameBoard.getFeatureList().size(), 12);

        // And the player get 9 score in the end! And get his meeple back
        assertEquals(gameBoard.getPlayer(1).getScore(), 9);
        assertEquals(gameBoard.getPlayer(1).getLeftMeeple(), 7);
    }
}
