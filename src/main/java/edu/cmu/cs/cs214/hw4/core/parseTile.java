package edu.cmu.cs.cs214.hw4.core;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A specific class that used for automatically parse the JSON file to
 * generate 72 different tiles at the beginning of the game.
 */
class ParseTile {
    private static final int STARTTILE = 7;
    /**
     * Class that represents a list of tile
     */
    private class Tiles {
        private List<Tile> tiles;
    }

    /**
     * private method to parse JSON file
     * @param configFile
     * @return list of Tiles
     */
    private static Tiles parse(String configFile) {
        Gson gson = new Gson();
        try {
            Reader reader = new FileReader(new File(configFile));
            Tiles result = gson.fromJson(reader, Tiles.class);
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException("Error when reading file: " + configFile, e);
        }
    }

    /**
     * generate tiles with random order and a start tile with D type
     * @return list of tiles
     */
    List<Tile> generateTiles() {
        Tiles result = parse("src/main/resources/config.json");
        Tile tile = result.tiles.get(STARTTILE);
        Tile startTile = new Tile(tile.getSegmentList(),
                tile.getHasMonastery(), tile.getTag());
        Collections.shuffle(result.tiles);
        List<Tile> tileList = new ArrayList<>(result.tiles);
        tileList.add(0, startTile);
        for (int i = 0; i < tileList.size(); i++) {
            setImgFilePath(tileList.get(i));
        }
        return tileList;
    }

    /**
     * Set the image path of each tile
     * @param tile
     */
    void setImgFilePath(Tile tile) {
        String tag = Character.toString(tile.getTag());
        String filePath = "src/main/resources/tileImg/" + tag + ".png";
        tile.setFilePath(filePath);
    }

    /**
     * generate tiles with sorted order, mainly for test use
     * @return list of tiles
     */
    List<Tile> generateSortedTiles() {
        Tiles result = parse("src/main/resources/config.json");
        List<Tile> list = new ArrayList<>(result.tiles);
        return list;
    }
}
