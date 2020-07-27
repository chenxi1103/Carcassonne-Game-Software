package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.GameSystem;
import edu.cmu.cs.cs214.hw4.core.Position;
import edu.cmu.cs.cs214.hw4.core.Tile;

import javax.swing.JPanel;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.JRootPane;
import javax.swing.JFrame;

import java.awt.Font;
import java.awt.Color;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

/**
 * GameBoardPanel that represent the Carcassonne GameBoard that extends JPanel
 */
class GameBoardPanel extends JPanel {
    private GameSystem gameSystem;
    private TileButton currTile;
    private HashSet<Position> availablePos;
    private JLabel warningLabel;
    private JButton confirmButton;
    private JButton confirmTilePlacement;
    private ActionListener rotate;
    private ActionListener confirm;
    private ActionListener confirmPlacement;
    private JLabel currPlayerLebal;
    private JLabel placeMeepleLabel;
    private HashSet<TileButton> buttonList;
    private ArrayList<TileButton> tiles;
    private ArrayList<JLabel> playerResult;
    private HashMap<Position, TileButton> meepleButton;

    /**
     * Constructor of GameBoardPanel
     *
     * @param gameSystem gameSystem
     * @throws IOException
     */
    GameBoardPanel(GameSystem gameSystem) throws IOException {
        this.gameSystem = gameSystem;
        this.setLayout(null);
        this.currPlayerLebal = new JLabel("");
        buttonList = new HashSet<>();
        this.tiles = new ArrayList<>();
        this.playerResult = new ArrayList<>();
        this.meepleButton = new HashMap<>();

        /**
         * Game Instruction that would never change
         */
        JLabel instructionTitle = new JLabel("Game Instruction");
        instructionTitle.setBounds(90, 570, 400, 30);
        instructionTitle.setFont(new Font("Dialog", Font.BOLD, 18));
        this.add(instructionTitle);

        JLabel instructionLabel = new JLabel();
        instructionLabel.setBounds(15, 600, 600, 240);
        instructionLabel.setText("<html><p>* Please first place the tile on " +
                "the" +
                " available position<br>on the GameBoard first.<br>" +
                "* Click Confirm Tile Placement button to confirm<br>your " +
                "tile placement." +
                "<br> * If your placement is invalid, please click <br>Reset " +
                "button to change your decision.<br>" +
                "* Then you can choose to place meeple on the <br>current " +
                "tile.<br>" +
                "* If the meeple placement is invalid, please <br>click " +
                "Remove Meeple button to change your decision.<br>" +
                "* After place the meeple, click Confirm to <br>move to next " +
                "turn.<br>" +
                "* Unless this is a Monastery Segment, please do <br>" +
                "not to put your meeple on the center. Thanks!</p></html>");
        this.add(instructionLabel);

        /**
         * Let the game to move to the first turn
         */
        next();
        this.add(this.currPlayerLebal);

        // Set the currTile TileButton
        currTile = new TileButton(this.gameSystem.getLastTile());
        currTile.setBounds(48, 60, 90, 90);
        this.add(currTile);

        // Define the rotate ActionListener and add it on the currTile
        rotate = e -> {
            currTile.rotateClockwise();
            this.gameSystem.rotateCurrTile();
        };
        currTile.addActionListener(rotate);

        // Show the info for all the players
        showPlayers();

        // Set up the warning label that would show warning message when
        // problems occur
        this.warningLabel = new JLabel("");
        warningLabel.setBounds(72, 530, 600, 30);
        warningLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        warningLabel.setForeground(Color.RED);
        this.add(warningLabel);

        // placeMeepleLabel
        this.placeMeepleLabel = new JLabel("");
        placeMeepleLabel.setBounds(205, 20, 200, 20);
        placeMeepleLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        placeMeepleLabel.setForeground(Color.RED);
        this.add(placeMeepleLabel);

        // Place Meeple Button
        JButton removeMeeple = new JButton("Remove Meeple");
        removeMeeple.setBounds(200, 40, 130, 30);
        JButton placeCenter = new JButton("On the Center");
        placeCenter.setBounds(200, 70, 130, 30);
        JButton placeTop = new JButton("On the Top");
        placeTop.setBounds(200, 100, 130, 30);
        JButton placeRight = new JButton("On the Right");
        placeRight.setBounds(200, 130, 130, 30);
        JButton placeDown = new JButton("On the Down");
        placeDown.setBounds(200, 160, 130, 30);
        JButton placeLeft = new JButton("On the Left");
        placeLeft.setBounds(200, 190, 130, 30);

        ActionListener removePlacedMeeple = e -> {
            tiles.get(tiles.size() - 1).placeMeeple(this.gameSystem.getCurrentID(), 5, this.pickColor(this.gameSystem.getCurrentID()));
            this.warningLabel.setText("");
        };
        ActionListener placeMeepleCenter = e -> {
            removeMeeple.setEnabled(true);
            tiles.get(tiles.size() - 1).placeMeeple(this.gameSystem.getCurrentID(), 4, this.pickColor(this.gameSystem.getCurrentID()));
        };

        ActionListener placeMeepleTop = e -> {
            removeMeeple.setEnabled(true);
            tiles.get(tiles.size() - 1).placeMeeple(this.gameSystem.getCurrentID(), 0, this.pickColor(this.gameSystem.getCurrentID()));
        };

        ActionListener placeMeepleRight = e -> {
            removeMeeple.setEnabled(true);
            tiles.get(tiles.size() - 1).placeMeeple(this.gameSystem.getCurrentID(), 1, this.pickColor(this.gameSystem.getCurrentID()));
        };
        ActionListener placeMeepleDown = e -> {
            removeMeeple.setEnabled(true);
            tiles.get(tiles.size() - 1).placeMeeple(this.gameSystem.getCurrentID(), 2, this.pickColor(this.gameSystem.getCurrentID()));
        };
        ActionListener placeMeepleLeft = e -> {
            removeMeeple.setEnabled(true);
            tiles.get(tiles.size() - 1).placeMeeple(this.gameSystem.getCurrentID(), 3, this.pickColor(this.gameSystem.getCurrentID()));
        };

        removeMeeple.addActionListener(removePlacedMeeple);
        placeCenter.addActionListener(placeMeepleCenter);
        placeTop.addActionListener(placeMeepleTop);
        placeLeft.addActionListener(placeMeepleLeft);
        placeDown.addActionListener(placeMeepleDown);
        placeRight.addActionListener(placeMeepleRight);

        this.add(removeMeeple);
        this.add(placeCenter);
        this.add(placeTop);
        this.add(placeRight);
        this.add(placeDown);
        this.add(placeLeft);

        removeMeeple.setEnabled(false);
        placeCenter.setEnabled(false);
        placeTop.setEnabled(false);
        placeLeft.setEnabled(false);
        placeDown.setEnabled(false);
        placeRight.setEnabled(false);


        // putTile ActionListener set up
        ActionListener putTile = e -> {
            confirmTilePlacement.addActionListener(confirmPlacement);
            TileButton thisButton = (TileButton) e.getSource();
            int xPos = thisButton.getxPos();
            int yPos = thisButton.getyPos();
            currTile.setxPos(xPos);
            currTile.setyPos(yPos);
            currTile.setBounds(xPos * 90 + 900, yPos * 90 + 450, 90, 90);
            this.remove(thisButton);
            buttonList.remove(thisButton);
        };

        // Set up reset Buttion and its ActionListener
        JButton resetButton = new JButton("Reset");
        resetButton.setBounds(100, 190, 70, 30);
        ActionListener reset = e -> {
            currTile.setBounds(48, 60, 90, 90);
            confirmButton.removeActionListener(confirm);
            this.warningLabel.setText("");
            try {
                TileButton button = new TileButton("Place Tile");
                button.setBounds(currTile.getxPos() * 90 + 900,
                        currTile.getyPos() * 90 + 450, 90, 90);
                button.addActionListener(putTile);
                removePreviousButton();
                buttonList.add(button);
                showAvailablePos();
                currTileDisplay();
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }
        };
        resetButton.addActionListener(reset);
        this.add(resetButton);

        // Set up confirmTilePlacement Button and its ActionListener
        confirmTilePlacement = new JButton("Confirm Tile Placement");
        confirmTilePlacement.setBounds(0, 160, 180, 30);
        this.confirmPlacement = e -> {
            gameSystem.getLastTile().initialOrientation();
            if (this.gameSystem.placeTilePreCheck(new Position(currTile.getxPos(), currTile.getyPos()))) {
                this.gameSystem.placeTile(new Position(currTile.getxPos(), currTile.getyPos()));
                resetButton.setEnabled(false);
                try {
                    TileButton tileCopy = copyTile();
                    this.tiles.add(tileCopy);
                    tileCopy.setBounds(tileCopy.getxPos() * 90 + 900,
                            tileCopy.getyPos() * 90 + 450, 90, 90);
                    this.meepleButton.put(new edu.cmu.cs.cs214.hw4.core.Position(tileCopy.getxPos(), tileCopy.getyPos()), tileCopy);
                    this.remove(currTile);
                    this.add(tileCopy);
                    confirmTilePlacement.removeActionListener(confirmPlacement);
                    confirmButton.addActionListener(confirm);
                    this.placeMeepleLabel.setText("PLACE MEEPLE");
                    this.warningLabel.setText("");
                    placeCenter.setEnabled(true);
                    placeTop.setEnabled(true);
                    placeLeft.setEnabled(true);
                    placeDown.setEnabled(true);
                    placeRight.setEnabled(true);
                    this.updateUI();
                } catch (IOException e1) {
                    System.out.println(e1.getMessage());
                }
            } else {
                if (!this.placeMeepleLabel.getText().equals("PLACE MEEPLE")) {
                    this.warningLabel.setText("Placement Invalid!");
                }
            }
        };
        this.add(confirmTilePlacement);

        // Set up the confirmButton and its ActionListener
        confirmButton = new JButton("Confirm");
        confirmButton.setBounds(10, 190, 80, 30);
        this.confirm = e -> {
            try {
                TileButton thisTileButton = tiles.get(tiles.size() - 1);
                this.gameSystem.getTileByPos(new Position(thisTileButton.getxPos(), thisTileButton.getyPos())).initialOrientation();
                Tile thisTile =
                        this.gameSystem.getTileByPos(new Position(thisTileButton.getxPos(), thisTileButton.getyPos()));
                if (thisTileButton.isPlacedMeeple() && !this.gameSystem.placeMeeple(thisTile
                        .getEdgeSegment(thisTileButton.getMeepleOrientation()), thisTileButton.getPlayerID())) {
                    this.warningLabel.setText("Meeple Invalid!");
                } else {
                    next();
                    this.add(currTile);
                    resetButton.setEnabled(true);
                    confirmTilePlacement.removeActionListener(confirmPlacement);
                    confirmButton.removeActionListener(confirm);
                    placeMeepleLabel.setText("");
                    warningLabel.setText("");
                    removeMeeple.setEnabled(false);
                    placeCenter.setEnabled(false);
                    placeTop.setEnabled(false);
                    placeLeft.setEnabled(false);
                    placeDown.setEnabled(false);
                    placeRight.setEnabled(false);
                    this.updateUI();
                }
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }
        };
        this.add(confirmButton);
    }

    /**
     * Move to the next turn of the game
     *
     * @throws IOException
     */
    private void next() throws IOException {
        this.gameSystem.nextTurn();
        returnMeeple();
        updateAvailablePlacement();
        showPlayers();
    }

    /**
     * Function that would return the meeple on the GameBoardPanel
     *
     * @throws IOException
     */
    private void returnMeeple() throws IOException {
        Queue<Position> queue =
                this.gameSystem.getReturnedMeeplePos();
        while (!queue.isEmpty()) {
            Position targetPos = queue.poll();
            TileButton origin = meepleButton.get(targetPos);
            TileButton returnedTile = new TileButton(origin.getTile());
            returnedTile.setxPos(origin.getxPos());
            returnedTile.setyPos(origin.getyPos());
            returnedTile.setRotateNum(origin.getRotateNum());
            returnedTile.rotateImage();
            this.remove(meepleButton.get(targetPos));
            returnedTile.setBounds(returnedTile.getxPos() * 90 + 900,
                    returnedTile.getyPos() * 90 + 450, 90, 90);
            this.add(returnedTile);
            this.updateUI();
        }
    }

    /**
     * Function that would make a copy of the currTile TileButton
     *
     * @return Copy of the currTile
     * @throws IOException
     */
    private TileButton copyTile() throws IOException {
        TileButton tileCopy = new TileButton(currTile.getTile());
        tileCopy.setxPos(currTile.getxPos());
        tileCopy.setyPos(currTile.getyPos());
        tileCopy.setRotateNum(currTile.getRotateNum());
        tileCopy.rotateImage();
        if (currTile.isPlacedMeeple()) {
            tileCopy.placeMeeple(currTile.getPlayerID(),
                    currTile.getMeepleOrientation(),this.pickColor(currTile.getPlayerID()));
        }
        return tileCopy;
    }

    /**
     * Show the score and the number of left meeples of each player
     */
    private void showPlayers() {
        int numOfPlayers = this.gameSystem.getNumPlayers();
        if (this.playerResult.size() != 0) {
            for (int i = 0; i < playerResult.size(); i++) {
                this.remove(playerResult.get(i));
            }
        }
        playerResult = new ArrayList<>();
        int maxScore = 0;
        for (int i = 0; i < numOfPlayers; i++) {
            if (this.gameSystem.getPlayerScore(i) > maxScore) {
                maxScore = this.gameSystem.getPlayerScore(i);
            }
            JLabel currPlayer = new JLabel("Player " + i + "\'s Score: "
                    + this.gameSystem.getPlayerScore(i) + " | Left " +
                    "Meeples: " + this.gameSystem.getLeftMeeple(i));
            currPlayer.setBounds(15, i * 30 + 360, 400, 25);
            currPlayer.setFont(new Font("Dialog", Font.BOLD, 16));
            currPlayer.setForeground(this.pickColor(i));
            playerResult.add(currPlayer);
            this.add(currPlayer);
        }
        // Show the final result
        if (this.gameSystem.isGameOver()) {
            StringBuilder sb = new StringBuilder();
            sb.append("The winner is");
            for (int i = 0; i < numOfPlayers; i++) {
                if (this.gameSystem.getPlayerScore(i) == maxScore) {
                    sb.append(" Player " + i);
                }
            }
            JLabel finalResult = new JLabel(sb.toString());
            finalResult.setBounds(15, numOfPlayers * 30 + 360, 800, 25);
            finalResult.setFont(new Font("Dialog", Font.BOLD, 16));
            finalResult.setForeground(Color.RED);
            this.add(finalResult);
        }
    }

    /**
     * Remove the previous available position button
     */
    private void removePreviousButton() {
        for (TileButton button : this.buttonList) {
            this.remove(button);
        }
        this.buttonList = new HashSet<>();
    }

    /**
     * Show the available position as TileButton on the GameBoardPanel
     */
    private void showAvailablePos() {
        ActionListener putTile = e -> {
            confirmTilePlacement.addActionListener(confirmPlacement);
            TileButton thisButton = (TileButton) e.getSource();
            int xPos = thisButton.getxPos();
            int yPos = thisButton.getyPos();
            currTile.setxPos(xPos);
            currTile.setyPos(yPos);
            currTile.setBounds(xPos * 90 + 900, yPos * 90 + 450, 90, 90);
            this.remove(thisButton);
            buttonList.remove(thisButton);
        };

        for (Position position : availablePos) {
            TileButton availableButton = new TileButton("Place Tile");
            availableButton.setBounds(position.getX() * 90 + 900,
                    position.getY() * 90 + 450, 90, 90);
            availableButton.setxPos(position.getX());
            availableButton.setyPos(position.getY());
            availableButton.addActionListener(putTile);
            this.buttonList.add(availableButton);
            this.add(availableButton);
        }
    }

    /**
     * update the necessary components as the game goes to the next turn.
     *
     * @throws IOException
     */
    private void updateAvailablePlacement() throws IOException {
        if (gameSystem.isGameOver()) {
            gameSystem.calculateFinalScore();
            showPlayers();
            this.updateUI();
            String[] options = {"Yes! Start New Game", "No! Quit the Game"};
            int result = JOptionPane.showOptionDialog(
                    this,
                    "Dear Players, the Game is Over! Start Another Game?",
                    "Game is Over",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (result == JOptionPane.YES_OPTION) {
                JViewport jViewport = (JViewport)this.getParent();
                JScrollPane jScrollPane = (JScrollPane)jViewport.getParent();
                JPanel jPanel = (JPanel) jScrollPane.getParent();
                JLayeredPane jLayeredPane = (JLayeredPane) jPanel.getParent();
                JRootPane jRootPane = (JRootPane)  jLayeredPane.getParent();
                JFrame jFrame = (JFrame) jRootPane.getParent();
                jFrame.dispose();
                GUI.main(new String[0]);
            } else {
                System.exit(0);
            }
        }
        currTile = new TileButton(gameSystem.getLastTile());
        currTile.setBounds(48, 60, 90, 90);
        currTile.addActionListener(rotate);

        availablePos = this.gameSystem.getAvailablePos();
        removePreviousButton();
        showAvailablePos();

        currPlayerDisplay(this.gameSystem.getCurrentID());
        currTileDisplay();
    }

    /**
     * Show whose turn currently is going on.
     *
     * @param playerID playerID
     */
    private void currPlayerDisplay(int playerID) {
        this.currPlayerLebal.setText("PLAYER " + playerID + "\'s TURN");
        this.currPlayerLebal.setBounds(16, 300, 200, 22);
        this.currPlayerLebal.setFont(new Font("Dialog", Font.BOLD, 20));
        this.currPlayerLebal.setForeground(this.pickColor(playerID));
    }


    /**
     * Display the currTile TileButton on the top-left of the GameBoardPanel
     *
     * @throws IOException
     */
    private void currTileDisplay() throws IOException {
        JLabel currTileLabel = new JLabel("CURRENT TILE");
        currTileLabel.setBounds(30, 30, 140, 20);
        currTileLabel.setFont(new Font("Dialog", Font.BOLD, 17));
        currTileLabel.setForeground(Color.BLUE);
        this.add(currTileLabel);
        currTile.setBounds(48, 60, 90, 90);
    }

    /**
     * Color that represents a player's meeple -> used for GUI display
     * @param playerID playerID
     * @return Color
     */
    private Color pickColor(int playerID) {
        switch (playerID) {
            case 0:
                return Color.PINK;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.ORANGE;
            case 4:
                return Color.BLACK;
            default:
                return Color.RED;
        }
    }
}
