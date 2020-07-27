package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

/**
 * GameBoard is the main logic processing class in the Carcassonne game.
 * It is designed to represent the relationship among tiles, segments,
 * positions, Meeples, and features.
 * The GameBoard is able to check the tile placement, update the features and
 * feature completeness, and calculate the scores.
 */
class GameBoard {
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int NINE = 9;

    private static final int[] X = new int[]{0, 0, 1, 1, 1, -1, -1, -1};
    private static final int[] Y = new int[]{1, -1, 0, -1, 1, 0, -1, 1};

    private static final int[] XDIR = new int[]{0, 1, 0, -1};
    private static final int[] YDIR = new int[]{-1, 0, 1, 0};

    // Map that used to store the position and tile information
    private HashMap<Position, Tile> mapPosition;
    private List<Feature> featureList;
    private HashSet<Position> availablePos;
    private List<Player> players;
    private Queue<Position> returnedMeeplePos;

    /**
     * Constructor of the game board
     *
     * @param numPlayers numPlayers
     */
    GameBoard(int numPlayers) {
        mapPosition = new HashMap<>();
        this.featureList = new ArrayList<>();
        availablePos = new HashSet<>();
        availablePos.add(new Position(0, 0));
        players = new ArrayList<>();
        this.returnedMeeplePos = new LinkedList<>();
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(i));
        }
    }

    Player getPlayer(int playerId) {
        return players.get(playerId);
    }

    boolean checkTileValid(Tile tile) {
        for (Position position : availablePos) {
            for (int i = 0; i < THREE; i++) {
                if (isMatch(tile, position)) return true;
                tile.rotate();
            }
            tile.rotate();
        }
        return false;
    }

    /**
     * Check how many a specific segment are there in the tile
     *
     * @param tile
     * @param type
     * @return int - > the number of segments
     */
    private int numOfSegment(Tile tile, int type) {
        int num = 0;
        for (int i = 0; i < FIVE; i++) {
            if (tile.getEdgeSegment(i).getType() == type) {
                num++;
            }
        }
        return num;
    }

    /**
     * Private helper function for check if current tile match the surroundings
     *
     * @param tile
     * @param position
     * @return boolean -> true if matches
     */
    private boolean isMatch(Tile tile, Position position) {
        // If this is the first tile
        if (position.getX() == 0 && position.getY() == 0) {
            return true;
        }
        int x = position.getX();
        int y = position.getY();
        if (mapPosition.get(new Position(x, y - 1)) != null) {
            if (mapPosition.get(new Position(x, y - 1)).getEdgeSegment(TWO).getType() != tile.getEdgeSegment(0).getType()) {
                return false;
            }
        }
        if (mapPosition.get(new Position(x + 1, y)) != null) {
            if (mapPosition.get(new Position(x + 1, y)).getEdgeSegment(THREE).getType() != tile.getEdgeSegment(1).getType()) {
                return false;
            }
        }
        if (mapPosition.get(new Position(x, y + 1)) != null) {
            if (mapPosition.get(new Position(x, y + 1)).getEdgeSegment(0).getType() != tile.getEdgeSegment(TWO).getType()) {
                return false;
            }
        }
        if (mapPosition.get(new Position(x - 1, y)) != null) {
            if (mapPosition.get(new Position(x - 1, y)).getEdgeSegment(1).getType() != tile.getEdgeSegment(THREE).getType()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Do pre check if the tile placement is valid or not without actually
     * place the tile on the board.
     *
     * @param tile     Tile
     * @param position Position
     * @return true if the placement is valid
     */
    boolean placeTilePreCheck(Tile tile, Position position) {
        if (!availablePos.contains(position) || mapPosition.get(position) != null
                || !isMatch(tile, position)) {
            return false;
        }
        return true;
    }

    /**
     * Get a specific tile based on position information
     *
     * @param position Position
     * @return Tile
     */
    Tile getTileByPos(Position position) {
        if (mapPosition.containsKey(position)) {
            return mapPosition.get(position);
        }
        return null;
    }

    /**
     * place a tile on the game board and return if it can be placed
     * successfully
     *
     * @param tile
     * @param position
     * @return boolean -> True if successfully placed
     */
    boolean placeTile(Tile tile, Position position) {
        // if available position set does not contain this position or if this
        // position has already been occupied.
        if (!availablePos.contains(position) || mapPosition.get(position) != null
                || !isMatch(tile, position)) {
            return false;
        }
        int x = position.getX();
        int y = position.getY();

        if (mapPosition.get(new Position(x, y - 1)) == null) {
            Position possiblePosition = new Position(x, y - 1);
            if (!availablePos.contains(possiblePosition)) {
                availablePos.add(possiblePosition);
            }
        }

        if (mapPosition.get(new Position(x + 1, y)) == null) {
            Position possiblePosition = new Position(x + 1, y);
            if (!availablePos.contains(possiblePosition)) {
                availablePos.add(possiblePosition);
            }
        }

        if (mapPosition.get(new Position(x, y + 1)) == null) {
            Position possiblePosition = new Position(x, y + 1);
            if (!availablePos.contains(possiblePosition)) {
                availablePos.add(possiblePosition);
            }
        }

        if (mapPosition.get(new Position(x - 1, y)) == null) {
            Position possiblePosition = new Position(x - 1, y);
            if (!availablePos.contains(possiblePosition)) {
                availablePos.add(possiblePosition);
            }
        }

        availablePos.remove(position);

        for (int i = 0; i < FIVE; i++) {
            tile.getEdgeSegment(i).setPosition(position);
        }

        // Put this tile into map
        mapPosition.put(position, tile);

        //Update the features
        updateRoad(tile, position);
        updateCity(tile, position);
        updateMonastery(tile, position);

        checkSurrounding(position);

        return true;
    }

    /**
     * Check if the tile has a monastery, if it has, a new MonasteryFeature
     * needs
     * to be created.
     *
     * @param tile
     * @param position
     */
    private void updateMonastery(Tile tile, Position position) {
        int num = numOfSegment(tile, 1);
        if (num != 0) {
            int x = position.getX();
            int y = position.getY();
            Feature newFeature = new MonasteryFeature(featureList.size());
            tile.getEdgeSegment(FOUR).setFeature(featureList.size());
            newFeature.addSegment(tile.getEdgeSegment(FOUR));
            for (int i = 0; i < X.length; i++) {
                int currX = x + X[i];
                int currY = y + Y[i];
                Position currPos = new Position(currX, currY);
                if (mapPosition.containsKey(currPos)) {
                    newFeature.addSegment(new Segment(1, false, 0));
                }
            }
            featureList.add(newFeature);
        }
    }

    /**
     * A function that used to update the city feature
     *
     * @param tile
     * @param position
     */
    private void updateCity(Tile tile, Position position) {
        int num = numOfSegment(tile, THREE);
        // if there is only 1 or 2 city segments, then check if it has
        // neighbor or not
        if (num == 1 || num == TWO) {
            int orientation = 0;
            for (int i = 0; i < FOUR; i++) {
                // If we find a city segment
                if (tile.getEdgeSegment(i).getType() == THREE) {
                    orientation = tile.getEdgeSegment(i).getOrientation();
                    int x = position.getX();
                    int y = position.getY();

                    Position checkPos = new Position(x + XDIR[orientation],
                            y + YDIR[orientation]);
                    //If it has a neighbor, then add to the neighbor's feature
                    if (mapPosition.containsKey(checkPos)) {
                        int featureID =
                                mapPosition.get(checkPos).getEdgeSegment((orientation + TWO) % FOUR).getFeature();
                        if (tile.getEdgeSegment(i).getHasShield()) {
                            featureList.get(featureID).addShield();
                        }
                        tile.getEdgeSegment(i).setFeature(featureID);
                        featureList.get(featureID).addSegment(tile.getEdgeSegment(i));
                        featureList.get(featureID).addRealSegment(tile.getEdgeSegment(i));
                    }
                    // If it does not have a neighbor, then create a new
                    // cityfeature.
                    else {
                        Feature newFeature = new CityFeature(featureList.size());
                        if (tile.getEdgeSegment(i).getHasShield()) {
                            newFeature.addShield();
                        }
                        tile.getEdgeSegment(i).setFeature(featureList.size());
                        newFeature.addSegment(tile.getEdgeSegment(i));
                        newFeature.addRealSegment(tile.getEdgeSegment(i));
                        tile.getEdgeSegment(i).setFeature(featureList.size());
                        this.featureList.add(newFeature);
                    }
                }
            }
        }

        // If there are more than 2 city segment, a combination is definitely
        // needed
        else if (num > TWO) {
            // We are pretty sure after the combination, there would be only
            // one city segment
            int featureID = Integer.MAX_VALUE;
            boolean shieldFlag = false;
            int x = position.getX();
            int y = position.getY();

            for (int i = 0; i < FOUR; i++) {
                // If we find a city segment
                if (tile.getEdgeSegment(i).getType() == THREE) {
                    // This segment has a shield.
                    if (tile.getEdgeSegment(i).getHasShield()) {
                        shieldFlag = true;
                    }
                    int orientation = tile.getEdgeSegment(i).getOrientation();
                    Position checkPos = new Position(x + XDIR[orientation],
                            y + YDIR[orientation]);
                    if (mapPosition.containsKey(checkPos)) {
                        int currID =
                                mapPosition.get(checkPos).getEdgeSegment((orientation + TWO) % FOUR).getFeature();

                        tile.getEdgeSegment(i).setFeature(currID);
                        featureList.get(currID).addRealSegment(tile.getEdgeSegment(i));

                        // if this is the first one that needs to check
                        if (featureID == Integer.MAX_VALUE) {
                            featureID = currID;
                        }
                        // If They are from different feature, they will be
                        // connect together to be the new feature.
                        else if (featureID != currID) {
                            Feature newFeature = new CityFeature(featureList.size());
                            combineTwoFeature(featureList.get(featureID),
                                    featureList.get(currID), newFeature);
                            int shieldNum = featureList.get(featureID).getNumShield() + featureList.get(currID).getNumShield();
                            while (shieldNum > 0) {
                                newFeature.addShield();
                                shieldNum--;
                            }
                            featureList.add(newFeature);
                            featureID = newFeature.getFeatureID();
                        }
                        // If the ids are the same, nothing needs to do
                    }
                }
            }

            // Now combine the citys together
            Segment segment = new Segment(THREE, shieldFlag, 0);
            // This means no city segments has a neighbor, a new cityfeature
            // is needed
            if (featureID == Integer.MAX_VALUE) {
                Feature cityFeature = new CityFeature(featureList.size());
                if (shieldFlag) {
                    cityFeature.addShield();
                }
                segment.setFeature(featureList.size());
//                segment.setFeature(cityList.size());
                cityFeature.addSegment(segment);
                for (int i = 0; i < FOUR; i++) {
                    if (tile.getEdgeSegment(i).getType() == THREE) {
                        tile.getEdgeSegment(i).setFeature(featureList.size());
                        cityFeature.addRealSegment(tile.getEdgeSegment(i));
                    }
                }
                featureList.add(cityFeature);

            } else {
                segment.setFeature(featureID);
                featureList.get(featureID).addSegment(segment);
                for (int i = 0; i < FIVE; i++) {
                    if (tile.getEdgeSegment(i).getType() == THREE) {
                        tile.getEdgeSegment(i).setFeature(featureID);
                        featureList.get(featureID).addRealSegment(tile.getEdgeSegment(i));
                    }
                }
                if (shieldFlag) {
                    featureList.get(featureID).addShield();
                }
            }
        }
    }

    /**
     * A function that used to update the road feature
     *
     * @param tile
     * @param position
     */
    private void updateRoad(Tile tile, Position position) {
        int num = numOfSegment(tile, TWO);
        if (num == 1 || (num == THREE && tile.getEdgeSegment(FOUR).getType() == 0) || num == FOUR) {
            int orientation = 0;
            int index = 0;
            for (int i = 0; i < FOUR; i++) {
                if (tile.getEdgeSegment(i).getType() == TWO) {
                    index = i;
                    orientation = tile.getEdgeSegment(i).getOrientation();
                    int x = position.getX();
                    int y = position.getY();

                    Position checkPos = new Position(x + XDIR[orientation],
                            y + YDIR[orientation]);
                    //If it has a neighbor, then add to the neighbor's feature
                    if (mapPosition.containsKey(checkPos)) {
                        int featureID =
                                mapPosition.get(checkPos).getEdgeSegment((orientation + TWO) % FOUR).getFeature();
                        tile.getEdgeSegment(index).setFeature(featureID);
                        featureList.get(featureID).addSegment(tile.getEdgeSegment(index));
                        featureList.get(featureID).addRealSegment(tile.getEdgeSegment(index));
                    }
                    // If it does not have a neighbor, then create a new
                    // roadfeature.
                    else {
                        Feature newFeature = new RoadFeature(featureList.size());
                        tile.getEdgeSegment(index).setFeature(featureList.size());
                        newFeature.addSegment(tile.getEdgeSegment(index));
                        newFeature.addRealSegment(tile.getEdgeSegment(index));
                        featureList.add(newFeature);
                    }
                }
            }
        }

        if (num == 2 || (num == THREE && tile.getEdgeSegment(FOUR).getType() == TWO)) {
            // We are pretty sure after the combination, there would be only
            // one road segment
            int featureID = Integer.MAX_VALUE;
            boolean shieldFlag = false;

            for (int i = 0; i < FOUR; i++) {
                // If we find a road segment
                if (tile.getEdgeSegment(i).getType() == TWO) {
                    int orientation = tile.getEdgeSegment(i).getOrientation();
                    int x = position.getX();
                    int y = position.getY();
                    Position checkPos = new Position(x + XDIR[orientation],
                            y + YDIR[orientation]);
                    if (mapPosition.containsKey(checkPos)) {
                        int currID =
                                mapPosition.get(checkPos).getEdgeSegment((orientation + TWO) % FOUR).getFeature();
                        tile.getEdgeSegment(i).setFeature(currID);

                        featureList.get(currID).addRealSegment(tile.getEdgeSegment(i));

                        // if this is the first one that needs to check
                        if (featureID == Integer.MAX_VALUE) {
                            featureID = currID;
                        }
                        // If They are from different feature, they will be
                        // connect together to be the new feature.
                        else if (featureID != currID) {

                            RoadFeature newFeature =
                                    new RoadFeature(featureList.size());
                            combineTwoFeature(featureList.get(featureID),
                                    featureList.get(currID), newFeature);
                            featureList.add(newFeature);

                            featureID = newFeature.getFeatureID();
                        }
                        // If the ids are the same, nothing needs to do
                    }
                }
            }

            // Two road segments have to combine together
            Segment segment = new Segment(TWO, false, 0);
            // This means no road segments has a neighbor, a new roadfeature
            // is needed
            if (featureID == Integer.MAX_VALUE) {
                Feature roadFeature = new RoadFeature(featureList.size());
                roadFeature.addSegment(segment);
                for (int i = 0; i < FOUR; i++) {
                    if (tile.getEdgeSegment(i).getType() == TWO) {
                        tile.getEdgeSegment(i).setFeature(featureList.size());
                        roadFeature.addRealSegment(tile.getEdgeSegment(i));
                    }
                }
                featureList.add(roadFeature);
            } else {
                featureList.get(featureID).addSegment(segment);
                for (int i = 0; i < FOUR; i++) {
                    if (tile.getEdgeSegment(i).getType() == TWO) {
                        tile.getEdgeSegment(i).setFeature(featureID);
                        featureList.get(featureID).addRealSegment(tile.getEdgeSegment(i));
                    }
                }
            }
        }
    }

    /**
     * Helper function that merge to connected feature
     *
     * @param feature1
     * @param feature2
     * @param feature
     */
    private void combineTwoFeature(Feature feature1, Feature feature2,
                                   Feature feature) {
        List<Integer> meeples1 = feature1.returnMeeple();
        List<Position> positions1 = feature1.getMeeplePos();
        for (int i = 0; i < meeples1.size(); i++) {
            feature.addMeeple(meeples1.get(i), positions1.get(i));
        }
        List<Integer> meeples2 = feature2.returnMeeple();
        List<Position> positions2 = feature2.getMeeplePos();
        for (int i = 0; i < meeples2.size(); i++) {
            feature.addMeeple(meeples2.get(i), positions2.get(i));
        }

        List<Segment> segments1 = feature1.getSegments();
        for (int i = 0; i < segments1.size(); i++) {
            segments1.get(i).setFeature(feature.getFeatureID());
            feature.addSegment(segments1.get(i));
        }

        List<Segment> segments2 = feature2.getSegments();
        for (int i = 0; i < segments2.size(); i++) {
            segments2.get(i).setFeature(feature.getFeatureID());
            feature.addSegment(segments2.get(i));
        }

        List<Segment> realSegments1 = feature1.getRealSegments();
        for (int i = 0; i < realSegments1.size(); i++) {
            realSegments1.get(i).setFeature(feature.getFeatureID());
            feature.addRealSegment(realSegments1.get(i));
        }

        List<Segment> realSegments2 = feature2.getRealSegments();
        for (int i = 0; i < realSegments2.size(); i++) {
            realSegments2.get(i).setFeature(feature.getFeatureID());
            feature.addRealSegment(realSegments2.get(i));
        }
        feature1.setComplete();
        feature2.setComplete();
    }

    /**
     * Helper function that used to calculate the monastery score during the
     * game
     *
     * @param tile
     */
    void calculateMonasteryScore(Tile tile) {
        // Check if this has been calcuated already
        if (tile.getEdgeSegment(FOUR).getType() != 1) return;
        if (!featureList.get(tile.getEdgeSegment(FOUR).getFeature()).isComplete()) {
            if (featureList.get(tile.getEdgeSegment(FOUR).getFeature()).checkIsComplete()) {
                int playerId =
                        featureList.get(tile.getEdgeSegment(FOUR).getFeature()).returnMeeple().get(0);
                players.get(playerId).addScore(NINE);
                players.get(playerId).returnMeeple();
            }
        }
    }

    /**
     * Helper function that used to calculate the road score during the game
     *
     * @param tile
     */
    void calculateRoadScore(Tile tile) {
        for (int i = 0; i < FOUR; i++) {
            if (tile.getEdgeSegment(i).getType() == TWO) {
                int featureId = tile.getEdgeSegment(i).getFeature();
                if (!featureList.get(featureId).isComplete()) {
                    boolean complete =
                            checkFeatureComplete(featureList.get(featureId));
                    if (complete) {
                        featureList.get(featureId).setComplete();
                        List<Integer> player =
                                featureList.get(featureId).returnMeeple();
                        int scores = featureList.get(featureId).getNumSegments();
                        HashSet<Integer> set = new HashSet<>();
                        for (int j = 0; j < player.size(); j++) {
                            int playerID = player.get(j);
                            if (!set.contains(playerID)) {
                                this.players.get(playerID).addScore(scores);
                                this.players.get(playerID).returnMeeple();
                                set.add(playerID);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Helper function that used to calculate the city score during the game
     *
     * @param tile
     */
    void calculateCityScore(Tile tile) {
        for (int i = 0; i < FOUR; i++) {
            if (tile.getEdgeSegment(i).getType() == THREE) {
                int featureId = tile.getEdgeSegment(i).getFeature();
                if (!featureList.get(featureId).isComplete()) {
                    boolean complete =
                            checkFeatureComplete(featureList.get(featureId));
                    if (complete) {
                        featureList.get(featureId).setComplete();
                        List<Integer> player =
                                featureList.get(featureId).returnMeeple();
                        int scores =
                                (featureList.get(featureId).getNumSegments() + featureList.get(featureId).getNumShield()) * TWO;
                        int[] times = new int[this.players.size()];
                        for (int j = 0; j < player.size(); j++) {
                            times[player.get(j)]++;
                            players.get(player.get(j)).returnMeeple();
                        }
                        // Get the max value of number of meeple
                        int maxValue = 0;
                        for (int m = 0; m < times.length; m++) {
                            if (maxValue < times[m]) {
                                maxValue = times[m];
                            }
                        }
                        for (int k = 0; k < times.length; k++) {
                            if (times[k] == maxValue) {
                                this.players.get(k).addScore(scores);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * A function that validate if a player attempts to place a meeple on a
     * segment
     *
     * @param segment
     * @param playerID
     * @return boolean -> true if placed successfully
     */
    boolean placeMeeple(Segment segment, int playerID) {
        int featureID = segment.getFeature();
        if (segment.getType() == 0) return false;
        if (featureList.get(featureID).getMeepleNum() != 0) {
            return false;
        } else {
            if (this.players.get(playerID).placeMeeple()) {
                featureList.get(featureID).addMeeple(playerID,
                        segment.getPosition());
                return true;
            }
        }
        return false;
    }

    /**
     * Used to check if the surrounding has a monastery tile
     *
     * @param position
     * @return List of tiles that has monastery on it
     */
    private void checkSurrounding(Position position) {
        int x = position.getX();
        int y = position.getY();
        for (int i = 0; i < X.length; i++) {
            int newX = x + X[i];
            int newY = y + Y[i];
            if (mapPosition.containsKey(new Position(newX, newY))) {
                Tile currTile = mapPosition.get(new Position(newX, newY));
                if (currTile.getEdgeSegment(FOUR).getType() == 1) {
                    int featureID = currTile.getEdgeSegment(FOUR).getFeature();
                    featureList.get(featureID).addSegment(new Segment(1,
                            false, 0));
                    if (featureList.get(featureID).getNumTiles() == NINE) {
                        featureList.get(featureID).setComplete();
                        int playerId =
                                featureList.get(featureID).returnMeeple().get(0);
                        players.get(playerId).addScore(NINE);
                        players.get(playerId).returnMeeple();
                    }
                }
            }
        }
    }

    /**
     * General function that used to check the feature is complete or not
     *
     * @param feature
     * @return
     */
    private boolean checkFeatureComplete(Feature feature) {
        List<Segment> realSegments = feature.getRealSegments();
        for (int i = 0; i < realSegments.size(); i++) {
            if (realSegments.get(i).getOrientation() == FOUR) {
                continue;
            }
            Segment currSegment = realSegments.get(i);
            Position currPos = currSegment.getPosition();
            int x = currPos.getX();
            int y = currPos.getY();
            int orientation = currSegment.getOrientation();
            Position checkPos = new Position(x + XDIR[orientation],
                    y + YDIR[orientation]);
            if (!mapPosition.containsKey(checkPos)) {
                return false;
            }
        }
        // This feature is complete!
        for (int k = 0; k < feature.getMeeplePos().size(); k++) {
            this.returnedMeeplePos.add(feature.getMeeplePos().get(k));
        }
        return true;
    }


    List<Feature> getFeatureList() {
        return new ArrayList<>(featureList);
    }

    /**
     * Accessor of availablePos -> used for GUI display
     *
     * @return available position that player can place the tile on
     */
    HashSet<Position> getAvailablePos() {
        HashSet<Position> hashSet = new HashSet<>();
        for (Position position : this.availablePos) {
            hashSet.add(position);
        }
        return hashSet;
    }

    /**
     * Accessor of the returnedMeeplePos
     *
     * @return list of Position that has(had) a meeple
     */
    Queue<Position> getReturnedMeeplePos() {
        int size = this.returnedMeeplePos.size();
        Queue<Position> queue = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            queue.add(this.returnedMeeplePos.poll());
        }
        return queue;
    }
}
