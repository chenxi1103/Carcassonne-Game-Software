package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.Player;
import edu.cmu.cs.cs214.hw4.core.Tile;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * TileButton that represents the Tile on the GameBoard, which extends JButton
 */
class TileButton extends JButton {
    private BufferedImage tileImage;
    private Tile tile;
    private int rotateNum;
    private int xPos;
    private int yPos;
    private boolean placedMeeple;
    private int playerID;
    private int meepleOrientation;

    /**
     * Constructor of the TileButton
     *
     * @param tile
     * @throws IOException
     */
    TileButton(Tile tile) throws IOException {
        BufferedImage image = ImageIO.read(new File(tile.getFilePath()));
        tileImage = image.getSubimage(0, 0, 90, 90);
        ImageIcon icon = new ImageIcon(tileImage);
        this.setPreferredSize(new Dimension(90, 90));
        this.setIcon(icon);
        this.tile = tile;
        this.rotateNum = 0;
    }

    /**
     * Another Constructor of the TileButton that used to represent available
     * position
     *
     * @param str Place Tile
     */
    TileButton(String str) {
        this.setText(str);
    }

    /**
     * Accessor of the tileImage
     *
     * @return BufferedImage tileImage
     */
    BufferedImage getTileImage() {
        return this.tileImage;
    }

    /**
     * Set the real Tile that this TileButton is representing
     *
     * @param tile Tile
     */
    void setTile(Tile tile) {
        this.tile = tile;
    }

    /**
     * Set the rotateNum
     *
     * @param rotateNum rotateNum
     */
    void setRotateNum(int rotateNum) {
        this.rotateNum = rotateNum;
    }

    /**
     * Accessor of the tile
     * @return tile
     */
    Tile getTile() {
        return this.tile;
    }

    /**
     * Set the x position of the TileButton
     * @param x x
     */
    void setxPos(int x) {
        this.xPos = x;
    }

    /**
     * Set the y position of the TileButton
     * @param y y
     */
    void setyPos(int y) {
        this.yPos = y;
    }

    /**
     * Accessor of the x position of the TileButton
     * @return x
     */
    public int getxPos() {
        return this.xPos;
    }

    /**
     * Accessor of the y position of the TileButton
     * @return y
     */
    public int getyPos() {
        return this.yPos;
    }

    /**
     * This method return a new instance of BufferedImage representing the
     * source image after applying n 90-degree clockwise rotations
     */
    void rotateClockwise() {
        int weight = this.tileImage.getWidth();
        int height = this.tileImage.getHeight();

        AffineTransform at = AffineTransform.getQuadrantRotateInstance(1,
                weight / 2.0, height / 2.0);
        ++this.rotateNum;
        AffineTransformOp op = new AffineTransformOp(at,
                AffineTransformOp.TYPE_BILINEAR);

        BufferedImage dest = new BufferedImage(weight, height,
                this.tileImage.getType());
        op.filter(this.tileImage, dest);
        ImageIcon icon = new ImageIcon(dest);
        this.setIcon(icon);
        this.tileImage = dest;
    }

    /**
     * Rotate the image based on rotateNum
     */
    void rotateImage() {
        int weight = this.tileImage.getWidth();
        int height = this.tileImage.getHeight();

        AffineTransform at =
                AffineTransform.getQuadrantRotateInstance(this.rotateNum,
                weight / 2.0, height / 2.0);
        AffineTransformOp op = new AffineTransformOp(at,
                AffineTransformOp.TYPE_BILINEAR);

        BufferedImage dest = new BufferedImage(weight, height,
                this.tileImage.getType());
        op.filter(this.tileImage, dest);
        ImageIcon icon = new ImageIcon(dest);
        this.setIcon(icon);
        this.tileImage = dest;
        this.tile.rotate();
    }

    /**
     * Accessor of the rotateNum
     * @return rotateNum
     */
    int getRotateNum() {
        return this.rotateNum;
    }

    /**
     * Place a meeple dot on the TileButton
     * @param playerID playerID
     * @param orientation orientation
     */
    void placeMeeple(int playerID, int orientation, Color color) {
        this.placedMeeple = true;
        BufferedImage dest = new BufferedImage(this.tileImage.getWidth(),
                this.tileImage.getHeight(), this.tileImage.getType());
        Graphics2D g = (Graphics2D) dest.getGraphics();
        g.drawImage(this.tileImage, 0, 0, null);
        g.setColor(color);

        this.playerID = playerID;
        this.meepleOrientation = orientation;

        if (orientation == 4) {
            g.fillOval(36, 36, 20, 20);
        } else if (orientation == 0) {
            g.fillOval(36, 4, 20, 20);
        } else if (orientation == 1) {
            g.fillOval(72, 36, 20, 20);
        } else if (orientation == 2) {
            g.fillOval(36, 68, 20, 20);
        } else if (orientation == 3) {
            g.fillOval(3, 36, 20, 20);
        } else {
            this.placedMeeple = false;
            g.fillOval(0, 0, 0, 0);
        }
        g.dispose();
        ImageIcon icon = new ImageIcon(dest);
        this.setIcon(icon);
    }

    /**
     * Accessor of placedMeeple
     * @return true if there is a meeple on this TileButton
     */
    boolean isPlacedMeeple() {
        return placedMeeple;
    }

    /**
     * Accessor of playerID
     * @return playerID of the player who place the meeple on this TileButton
     */
    int getPlayerID() {
        return playerID;
    }

    /**
     * Accessor of the meepleOrientation
     * @return meepleOrientation
     */
    int getMeepleOrientation() {
        return meepleOrientation;
    }
}
