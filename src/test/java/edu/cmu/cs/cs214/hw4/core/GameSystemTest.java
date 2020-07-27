package edu.cmu.cs.cs214.hw4.core;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Test class for testing GameSystem class and its methods
 */
public class GameSystemTest {
    private ParseTile parseTile;
    private List<Tile> tiles;
    private GameSystem gameSystem;
    private final static int NUMPLAYERS = 5;

    @Before
    public void setUp() {
        gameSystem = new GameSystem(NUMPLAYERS);
        // Tiles for testing
        parseTile = new ParseTile();
        // For better testing, this tile list is not randomly shuffle, so
        // that a specific type of tile can be found easily
        tiles = parseTile.generateSortedTiles();
    }

    @Test
    public void testCalculateRoadScore() {
        Tile P1 = tiles.get(39);
        assertTrue(gameSystem.placeTile(P1, new Position(0, 0)));
        gameSystem.placeMeeple(P1.getEdgeSegment(1), 0);
        gameSystem.upldateScore(P1);

        Tile V1 = tiles.get(57);
        assertTrue(gameSystem.placeTile(V1, new Position(1, 0)));
        gameSystem.upldateScore(V1);

        Tile K = tiles.get(26);
        assertTrue(gameSystem.placeTile(K, new Position(1, 1)));
        gameSystem.upldateScore(K);

        Tile V2 = tiles.get(58);
        V2.rotate();
        V2.rotate();
        assertTrue(gameSystem.placeTile(V2, new Position(0, 1)));
        gameSystem.upldateScore(V2);
        assertEquals(gameSystem.getPlayerScore(0), 4);

    }

    @Test
    public void testCalculateCityScore() {
        Tile P = tiles.get(39);
        assertTrue(gameSystem.placeTile(P, new Position(0, 0)));
        gameSystem.placeMeeple(P.getEdgeSegment(1), 0);
        gameSystem.upldateScore(P);

        Tile I = tiles.get(21);
        I.rotate();
        assertTrue(gameSystem.placeTile(I, new Position(0, -1)));
        gameSystem.upldateScore(I);

        Tile E = tiles.get(10);
        E.rotate();
        assertTrue(gameSystem.placeTile(E, new Position(-1, 0)));
        gameSystem.upldateScore(E);
        assertEquals(gameSystem.getPlayerScore(0), 6);
    }

    /**
     * This function is especially test calculate road / city score.
     * Monastery score is test in "testPlaceTile_Monastery"
     */
    @Test
    public void testCalculateScore() {
        //At the beginning of the game, the tile can only be placed on (0,0)
        Tile A = tiles.get(0);
        assertTrue(gameSystem.placeTile(A, new Position(0, 0)));
        //Assume player 0 put a meeple on the road
        gameSystem.placeMeeple(A.getEdgeSegment(2), 0);
        gameSystem.upldateScore(A);

        //put type D tile on (0,-1) point
        Tile D = tiles.get(7);
        assertEquals(D.getTag(), 'D');
        assertTrue(gameSystem.placeTile(D, new Position(0, 1)));
        //Assume player 1 put a meeple on the city
        gameSystem.placeMeeple(D.getEdgeSegment(1), 1);
        gameSystem.upldateScore(D);

        //put type F tile on (1,-1) point
        Tile F = tiles.get(15);
        assertTrue(gameSystem.placeTile(F, new Position(1, 1)));
        //Assume player 2 try to put a meeple on the city, but cannot since
        // city is occupied by 1
        assertFalse(gameSystem.placeMeeple(F.getEdgeSegment(1), 2));
        gameSystem.upldateScore(F);

        // put type J tile on (0,-2) rotate 270 degrees
        Tile J = tiles.get(23);
        // rotate 90 degrees
        J.rotate();
        // rotate 180 degrees
        J.rotate();
        // rotate 270 degrees
        J.rotate();
        assertTrue(gameSystem.placeTile(J, new Position(0, 2)));
        // Assume player 3 decide to put his meeple on city feature since
        // road has been occupied by player0
        assertTrue(gameSystem.placeMeeple(J.getEdgeSegment(3), 3));
        gameSystem.upldateScore(J);

        // Put type D tile on (2, -1) and now a city feature is complete,
        // player1 should get 6 points for three city feature. Since the feature
        // also has a shield, another 2 points for player1. Player1 should get
        // 8 points in total!
        Tile D1 = tiles.get(7);
        assertEquals(D1.getTag(), 'D');
        D1.rotate();
        D1.rotate();
        assertTrue(gameSystem.placeTile(D1, new Position(2, 1)));
        // Player 4 can only put his meeple on road feature
        assertTrue(gameSystem.placeMeeple(D1.getEdgeSegment(0), 4));
        gameSystem.upldateScore(D1);
        assertEquals(gameSystem.getPlayerScore(1), 8);

        // put O type on (2, -2), rotate 180 degree
        Tile O = tiles.get(37);
        assertEquals(O.getTag(), 'O');
        O.rotate();
        O.rotate();
        assertTrue(gameSystem.placeTile(O, new Position(2, 2)));
        // Player 0 can only put his meeple on city feature
        assertTrue(gameSystem.placeMeeple(O.getEdgeSegment(1), 0));
        gameSystem.upldateScore(O);

        //Now put a U type tile to connect these two road feature! (rotate 90
        // degree)
        //Now the combined feature would have 2 meeples on it, one belongs to
        // player 0 and the other belongs to player 4
        Tile U = tiles.get(49);
        assertEquals(U.getTag(), 'U');
        U.rotate();
        assertTrue(gameSystem.placeTile(U, new Position(1, 2)));
        gameSystem.upldateScore(U);

        // Finally we put a W type tile on (2, 0) to complete the this
        // feature while
        // introduce another two new road feature
        Tile W = tiles.get(66);
        assertEquals(W.getTag(), 'W');
        assertTrue(gameSystem.placeTile(W, new Position(2, 0)));
        // There are three road segments on the tile. Player 1 can place his
        // meeple on other two road segments instead of the completed one
        assertFalse(gameSystem.placeMeeple(W.getEdgeSegment(2), 1));
        assertTrue(gameSystem.placeMeeple(W.getEdgeSegment(1), 1));
        gameSystem.upldateScore(W);
        // Now as the completed road feature has 7 segments with two meeples
        // on it. Both player0 and player4 should earn 7 points!
        assertEquals(gameSystem.getPlayerScore(0), 7);
        assertEquals(gameSystem.getPlayerScore(4), 7);
    }

    /**
     * This function test if the game run out of tiles, if the system can
     * correctly finalize the score
     * <p>
     * This test case is simulated as "rules" provided final scoring example
     */
    @Test
    public void testFinalizeScore() {
        // Player 0 put start tile type D, on (0,0), put his meeple on
        // road segment
        Tile D = tiles.get(7);
        assertTrue(gameSystem.placeTile(D, new Position(0, 0)));
        assertTrue(gameSystem.placeMeeple(D.getEdgeSegment(4), 0));
        gameSystem.upldateScore(D);

        // Player 1 put F type on (1,0), put his meeple on city segment;
        Tile F = tiles.get(15);
        assertTrue(gameSystem.placeTile(F, new Position(1, 0)));
        assertTrue(gameSystem.placeMeeple(F.getEdgeSegment(4), 1));
        gameSystem.upldateScore(F);

        // Player 2 put tile O on (0,1), put his meeple on city segment
        Tile O = tiles.get(37);
        assertTrue(gameSystem.placeTile(O, new Position(0, -1)));
        assertTrue(gameSystem.placeMeeple(O.getEdgeSegment(3), 2));
        gameSystem.upldateScore(O);

        // Player 3 put tile A, rotate 90 degrees, on (1,1), put his meeple
        // on monastery segment
        Tile A = tiles.get(0);
        A.rotate();
        assertTrue(gameSystem.placeTile(A, new Position(1, -1)));
        assertTrue(gameSystem.placeMeeple(A.getEdgeSegment(4), 3));
        gameSystem.upldateScore(A);

        // Player 4 put tile Q, rotate 270 degrees, on (-1,0), put his meeple
        // on city segments
        Tile Q = tiles.get(42);
        Q.rotate();
        Q.rotate();
        Q.rotate();
        assertTrue(gameSystem.placeTile(Q, new Position(-1, 0)));
        assertTrue(gameSystem.placeMeeple(Q.getEdgeSegment(3), 4));
        gameSystem.upldateScore(Q);

        // Player 1 put tile N, rotate 180 degrees, on (-1,1)
        Tile N = tiles.get(34);
        N.rotate();
        N.rotate();
        assertTrue(gameSystem.placeTile(N, new Position(-1, -1)));
        gameSystem.upldateScore(N);

        // Player 2 put tile H, rotate 90 degrees, on (-2,1), put his meeple
        // on city segments
        Tile H = tiles.get(18);
        H.rotate();
        assertTrue(gameSystem.placeTile(H, new Position(-2,-1)));
        assertTrue(gameSystem.placeMeeple(H.getEdgeSegment(2), 2));
        gameSystem.upldateScore(H);


        // Player 3 put tile C on (-2,0)
        Tile C = tiles.get(6);
        assertTrue(gameSystem.placeTile(C, new Position(-2,0)));
        gameSystem.upldateScore(C);

        // Finalize score!
        gameSystem.calculateFinalScore();

        // Based on the rule
        // Player0 : 3 points
        // Player1 : 3 points
        // player2 : 8 points
        // Player3 : 4 points
        // player4 : 0 points
        assertEquals(gameSystem.getPlayerScore(0),3);
        assertEquals(gameSystem.getPlayerScore(1),3);
        assertEquals(gameSystem.getPlayerScore(2),8);
        assertEquals(gameSystem.getPlayerScore(3),4);
        assertEquals(gameSystem.getPlayerScore(4),0);
    }
}
