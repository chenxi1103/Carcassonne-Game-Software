package edu.cmu.cs.cs214.hw4.core;

import java.util.List;
import java.util.Queue;
import java.util.HashSet;

/**
 * GameSystem class is designed to maintain the general gaming process of the
 * whole game. It is able to start the game, manage the procedure of each
 * ture, update score, finalize the score and end the game.
 */
public class GameSystem {
    private GameBoard gameBoard;
    private int currentID;
    private List<Tile> remainTiles;
    private int numPlayers;
    private Tile lastTile;
    private boolean gameOver;

    /**
     * Constructor for GameSystem
     *
     * @param numPlayers numPlayers
     */
    public GameSystem(int numPlayers) {
        this.gameBoard = new GameBoard(numPlayers);
        ParseTile parseTile = new ParseTile();
        this.remainTiles = parseTile.generateTiles();
        this.numPlayers = numPlayers;
        this.currentID = (int) (Math.random() * numPlayers);
        if (this.currentID == numPlayers) {
            this.currentID--;
        }
        this.lastTile = null;
        this.gameOver = false;
    }

    /**
     * Rotate the current tile for 90 degrees
     */
    public void rotateCurrTile() {
        this.lastTile.rotate();
    }

    /**
     * Accessor of the lastTile
     * @return last Tile
     */
    public Tile getLastTile() {
        return this.lastTile;
    }

    /**
     * Accessor of the currentID
     * @return current player's id
     */
    public int getCurrentID() {
        return this.currentID;
    }

    /**
     * Accessor of the number of the players
     * @return the number of the players
     */
    public int getNumPlayers() {
        return this.numPlayers;
    }

    /**
     * Get the next player's ID
     *
     * @return next player's ID
     */
    private int nextPlayer() {
        this.currentID = (this.currentID + 1) % this.numPlayers;
        return this.currentID;
    }

    /**
     * Accessor of gameOver
     * @return true if a system run out of tiles
     */
    public boolean isGameOver() {
        return this.gameOver;
    }

    /**
     * helper method that give players a tile
     */
    private void giveTile() {
        if (remainTiles.size() == 0) {
            gameOver = true;
        } else {
            // If current tile cannot be placed on the board, dump it
            while (!this.gameBoard.checkTileValid(new Tile(
                    remainTiles.get(0).getSegmentList(),
                    remainTiles.get(0).getHasMonastery(),
                    remainTiles.get(0).getTag()))) {
                remainTiles.remove(0);
            }
            Tile tile = remainTiles.get(0);
            this.lastTile = tile;
            remainTiles.remove(0);
        }
    }

    /**
     * Go to next turn of the game
     */
    public void nextTurn() {
        if (this.lastTile != null) {
            upldateScore(this.lastTile);
        }
        nextPlayer();
        giveTile();
    }

    /**
     * Method that allows player to place a tile on the game board
     *
     * @param position position
     */
    public void placeTile(Position position) {
        Tile tileCopy = new Tile(this.lastTile.getSegmentList(),
                    this.lastTile.getHasMonastery(), this.lastTile.getTag());
        this.gameBoard.placeTile(tileCopy, position);
    }

    /**
     * Only used for testing in unit test
     * @param tile
     * @param position
     */
    boolean placeTile(Tile tile, Position position) {
        Tile tileCopy = new Tile(tile.getSegmentList(),
                tile.getHasMonastery(), tile.getTag());
        return this.gameBoard.placeTile(tileCopy, position);
    }

    /**
     * Pre check if the tile placement is valid or not
     * @param position position
     * @return true if it is valid
     */
    public boolean placeTilePreCheck(Position position) {
        return this.gameBoard.placeTilePreCheck(this.lastTile, position);
    }

    /**
     * Method that used to update score at the end of each turn.
     */
    void upldateScore(Tile tile) {
        this.gameBoard.calculateRoadScore(tile);
        this.gameBoard.calculateCityScore(tile);
        this.gameBoard.calculateMonasteryScore(tile);
    }

    /**
     * Method that allows a player to place a meeple after placing a tile
     *
     * @param segment segment
     * @param playerID playerID
     * @return boolean -> true if successfully place.
     */
    public boolean placeMeeple(Segment segment, int playerID) {
        return this.gameBoard.placeMeeple(segment, playerID);
    }

    /**
     * Finalize the incompleted features and their scores
     */
    public void calculateFinalScore() {
        finalRoadScore();
        finalCityScore();
        finalMonasteryScore();
    }

    /**
     * Helper method that used to calculate incomplete monastery scores
     */
    private void finalMonasteryScore() {
        List<Feature> list = this.gameBoard.getFeatureList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType() == 1) {
                if (!list.get(i).isComplete()) {
                    int scores = list.get(i).getNumTiles();
                    this.gameBoard.getPlayer(list.get(i).returnMeeple().get(0)).addScore(scores);
                }
            }
        }
    }

    public int getPlayerScore(int PlayerID) {
        return this.gameBoard.getPlayer(PlayerID).getScore();
    }

    public int getLeftMeeple(int PlayerID) {
        return this.gameBoard.getPlayer(PlayerID).getLeftMeeple();
    }

    /**
     * Helper method that used to calculate incomplete road scores
     */
    private void finalRoadScore() {
        List<Feature> list = this.gameBoard.getFeatureList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType() == 2) {
                if (!list.get(i).isComplete()) {
                    int scores = list.get(i).getNumSegments();
                    List<Integer> players = list.get(i).returnMeeple();
                    HashSet<Integer> set = new HashSet<>();
                    for (int j = 0; j < players.size(); j++) {
                        if (!set.contains(players.get(j))) {
                            this.gameBoard.getPlayer(players.get(j)).addScore(scores);
                            set.add(players.get(j));
                        }
                    }
                }
            }
        }
    }

    /**
     * Get a specific tile based on position information
     *
     * @param position Position
     * @return Tile
     */
    public Tile getTileByPos(Position position) {
        return this.gameBoard.getTileByPos(position);
    }

    /**
     * Helper method that used to calculate incomplete city scores
     */
    private void finalCityScore() {
        List<Feature> list = this.gameBoard.getFeatureList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType() == 3) {
                if (!list.get(i).isComplete()) {
                    finalSingleCityScore(list.get(i));
                }

            }
        }
    }

    /**
     * Helper method to calculate single incomplete
     *
     * @param cityfeature
     */
    private void finalSingleCityScore(Feature cityfeature) {
        if (!cityfeature.isComplete()) {
            int scores =
                    cityfeature.getNumShield() + cityfeature.getNumSegments();
            List<Integer> players = cityfeature.returnMeeple();
            int[] times = new int[numPlayers];
            for (int j = 0; j < players.size(); j++) {
                times[players.get(j)]++;
            }
            int index = 0;
            for (int k = 1; k < times.length; k++) {
                if (times[index] < times[k]) {
                    index = k;
                }
            }
            if (index == 0 && times[0] == 0) return;
            this.gameBoard.getPlayer(index).addScore(scores);
        }
    }

    /**
     * Accessor of the returnedMeeplePos
     *
     * @return list of Position that has(had) a meeple
     */
    public Queue<Position> getReturnedMeeplePos() {
        return this.gameBoard.getReturnedMeeplePos();
    }

    /**
     * Accessor of availablePos -> used for GUI display
     *
     * @return available position that player can place the tile on
     */
    public HashSet<Position> getAvailablePos() {
        return this.gameBoard.getAvailablePos();
    }
}
