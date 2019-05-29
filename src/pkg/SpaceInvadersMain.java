package pkg;

import javax.swing.*;

public class SpaceInvadersMain extends JFrame implements Constants {


    public static void main(String[] args) {
        JFrame window = new JFrame("Space Invaders"); //initial setup stuff
        GamePanel gamePanel = new GamePanel(); //creates the gamepanel
        GameListeners listener = new GameListeners(gamePanel); //adds listeners to the gamePanel
        gamePanel.addKeyListener(listener); //adds keyListener for player movement
        JMenuBar menuBar = new JMenuBar(); //adds the menubar

        //*************************************************************************
        //*************************************************************************

        JMenu file = new JMenu("File");

        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(listener);
        file.add(quit);

        //*************************************************************************
        //*************************************************************************

        JMenu options = new JMenu("Options");

        JMenuItem pause = new JMenuItem("Pause (P)");
        pause.addActionListener(listener);

        JMenuItem chooseColor = new JMenuItem("Change Player Color");
        chooseColor.addActionListener(listener);

        JMenuItem chooseStarColor = new JMenuItem("Change Star Color");
        chooseStarColor.addActionListener(listener);

        options.add(pause);
        options.add(chooseColor);
        options.add(chooseStarColor);

        //*************************************************************************
        //*************************************************************************

        JMenuItem difficulty = new JMenu("Difficulty");

        JMenuItem diff1 = new JMenuItem("1");
        diff1.addActionListener(listener);
        difficulty.add(diff1);

        JMenuItem diff2 = new JMenuItem("2");
        diff2.addActionListener(listener);
        difficulty.add(diff2);

        JMenuItem diff3 = new JMenuItem("3");
        diff3.addActionListener(listener);
        difficulty.add(diff3);

        JMenuItem diff4 = new JMenuItem("4");
        diff4.addActionListener(listener);
        difficulty.add(diff4);

        JMenuItem diffInsane = new JMenuItem("INSANE");
        diffInsane.addActionListener(listener);
        difficulty.add(diffInsane);

        //*************************************************************************
        //*************************************************************************

        JMenuItem soundOptions = new JMenu("Sound");
        JMenuItem toggleSound = new JMenuItem("Toggle Sound");
        toggleSound.addActionListener(listener);
        soundOptions.add(toggleSound);


        //*************************************************************************
        //*************************************************************************

        menuBar.add(file);
        menuBar.add(options);
        menuBar.add(difficulty);
        menuBar.add(soundOptions);

        window.setJMenuBar(menuBar);

        window.setResizable(false);
        window.setContentPane(gamePanel);
        window.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        window.setLocation(100, 100);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        //*************************************************************************
        //*************************************************************************

        gamePanel.requestFocusInWindow();
    }
}
