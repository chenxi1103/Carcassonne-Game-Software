package edu.cmu.cs.cs214.hw4.gui;
import edu.cmu.cs.cs214.hw4.core.GameSystem;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import java.io.IOException;

/**
 * Main GUI for the whole Carcassone Game
 */
public class GUI extends JFrame{
    private int playerNum;
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 900;
    private static final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD,30);
    private static int xPOS;
    private static int yPOS;

    /**
     * Main Function that can start the GUI program
     * @param args no argument is needed
     */
    public static void main(String[] args){
        new GUI();
    }

    /**
     * Constructor of the GUI class
     */
    public GUI() {
        this.setSize(1600,900);
        this.setLocationRelativeTo(null);

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        xPOS = (dim.width / 2) - (this.getWidth() / 2);
        yPOS = (dim.height / 2) - (this.getHeight() / 2);
        this.setLocation(xPOS, yPOS);
        this.setResizable(false);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Carcassonne Game Start Panel");

        JLayeredPane layeredPane=new JLayeredPane();
        ImageIcon image=new ImageIcon("src/main/resources/background1.jpeg");
        JPanel jp=new JPanel();
        jp.setBounds(0,0,image.getIconWidth(),image.getIconHeight());

        JLabel jl=new JLabel(image);
        jp.add(jl);

        layeredPane.add(jp,JLayeredPane.DEFAULT_LAYER);

        // Button
        JButton jButton2 = new JButton("2 Players");
        jButton2.setBorderPainted(true);
        jButton2.setContentAreaFilled(true);
        jButton2.setBounds(580,550,100,50);
        jButton2.setFont(new Font("Dialog",1,13));

        JButton jButton3 = new JButton("3 Players");
        jButton3.setBorderPainted(true);
        jButton3.setContentAreaFilled(true);
        jButton3.setBounds(700,550,100,50);
        jButton3.setFont(new Font("Dialog",1,13));

        JButton jButton4 = new JButton("4 Players");
        jButton4.setBorderPainted(true);
        jButton4.setContentAreaFilled(true);
        jButton4.setBounds(820,550,100,50);
        jButton4.setFont(new Font("Dialog",1,13));

        JButton jButton5 = new JButton("5 Players");
        jButton5.setBorderPainted(true);
        jButton5.setContentAreaFilled(true);
        jButton5.setBounds(940,550,100,50);
        jButton5.setFont(new Font("Dialog",1,13));



        ActionListener playerNum = e -> {
            JButton sourceBtn = (JButton) e.getSource();
            this.playerNum = Integer.parseInt(sourceBtn.getText().split(" ")[0]);
            try {
                gameStart();
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }
        };

        jButton2.addActionListener(playerNum);
        jButton3.addActionListener(playerNum);
        jButton4.addActionListener(playerNum);
        jButton5.addActionListener(playerNum);

        layeredPane.add(jButton2,JLayeredPane.MODAL_LAYER);
        layeredPane.add(jButton3,JLayeredPane.MODAL_LAYER);
        layeredPane.add(jButton4,JLayeredPane.MODAL_LAYER);
        layeredPane.add(jButton5,JLayeredPane.MODAL_LAYER);

        this.setLayeredPane(layeredPane);
        this.setSize(image.getIconWidth(),image.getIconHeight());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(image.getIconWidth(),image.getIconHeight());
        this.setVisible(true);

    }

    /**
     * Start the GameBoardPanel when user click the certain button
     * @throws IOException
     */
    private void gameStart() throws IOException {
        this.dispose();
        JFrame gameFrame = new JFrame("Carcassonne GameBoard");

        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(new Dimension(WIDTH, HEIGHT));
        gameFrame.setLocation(xPOS, yPOS);

        GameSystem gameSystem = new GameSystem(this.playerNum);

        GameBoardPanel panel1 = new GameBoardPanel(gameSystem);
        panel1.setPreferredSize(new Dimension( 2000,2000));

        JScrollPane scrollFrame = new JScrollPane(panel1);
        panel1.setAutoscrolls(true);
        scrollFrame.setPreferredSize(new Dimension( 800,300));

        gameFrame.add(scrollFrame);
        gameFrame.setVisible(true);
    }
}
