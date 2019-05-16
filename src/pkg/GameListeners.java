package pkg;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameListeners extends KeyAdapter implements ActionListener {

    private GamePanel gamePanel;
    /**
     * Timers to run movement (send action events at specified interval)
     * May push CPU quite hard
     */
    private Timer alienMoveTimer = new Timer(12, this);
    private Timer mysteryShipMoveTimer = new Timer(25, this);
    private Timer shotMoveTimer = new Timer(20, this);
    private Timer alienBombMoveTimer = new Timer(20, this);
    private Timer alienBombGenerationTimer = new Timer(1000, this);

    GameListeners(GamePanel panel) {
        this.gamePanel = panel;

        panel.addKeyListener(this);

        alienMoveTimer.start(); //start movement
        shotMoveTimer.start();
        alienBombMoveTimer.start();
        alienBombGenerationTimer.start();
        mysteryShipMoveTimer.start();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String s = "";
        if (e.getActionCommand() != null) {
            s = e.getActionCommand();
        }

        if (e.getSource() == alienMoveTimer) {
            gamePanel.moveAliens();
        }
        if (e.getSource() == shotMoveTimer) {
            if (gamePanel.isPlayerShotVisible()) {
                gamePanel.moveShot();
            }
        }
        if (e.getSource() == alienBombMoveTimer) {
            for (Alien a : gamePanel.getAliens()) {
                if (a.getBomb().isVisible()) {
                    gamePanel.moveAlienBomb(a);
                    gamePanel.repaint();
                }
            }
        }
        if (e.getSource() == alienBombGenerationTimer) {
            for (Alien a : gamePanel.getAliens()) {
                gamePanel.randomBombInit();
            }
        }
        if (e.getSource() == mysteryShipMoveTimer) {
            if (gamePanel.getMysteryShip().isVisible()) {
                gamePanel.moveMysteryShip();
            }
        }

        //handle externals
        switch (s.toLowerCase()) {
            case "change player color":
                gamePanel.setPaused(true);
                gamePanel.getPlayer().setImage(gamePanel.changeColorOfImage(gamePanel.getPlayerImage(), gamePanel.customColor(gamePanel.getCurrentPlayerColor())));
                gamePanel.setPaused(false);
                break;
            case "change star color":
                gamePanel.setPaused(true);
                gamePanel.setStarColor(gamePanel.customColor(gamePanel.getStarColor()));
                gamePanel.setPaused(false);
                break;
            case "toggle sound":
                gamePanel.setMuted(!gamePanel.isMuted());
                break;
            case "quit":
                System.exit(0);
                break;
            case "1":
                gamePanel.setBombVeloDiffCompensation(0);
                gamePanel.setAlienDxDiffCompensation(0);
                gamePanel.setShotChanceModifier(0);
                gamePanel.setShotVeloDiffCompensation(0);
                break;
            case "2":
                gamePanel.setBombVeloDiffCompensation(1);
                gamePanel.setAlienDxDiffCompensation(3);
                gamePanel.setShotChanceModifier(500);
                gamePanel.setShotVeloDiffCompensation(3);
                break;
            case "3":
                gamePanel.setBombVeloDiffCompensation(4);
                gamePanel.setAlienDxDiffCompensation(5);
                gamePanel.setShotChanceModifier(800);
                gamePanel.setShotVeloDiffCompensation(5);
                break;
            case "4":
                gamePanel.setBombVeloDiffCompensation(5);
                gamePanel.setAlienDxDiffCompensation(7);
                gamePanel.setShotChanceModifier(2200);
                gamePanel.setShotVeloDiffCompensation(6);
                gamePanel.setScoreModifier(2);
                break;
            case "insane":
                gamePanel.setBombVeloDiffCompensation(6);
                gamePanel.setAlienDxDiffCompensation(10);
                gamePanel.setShotChanceModifier(4000);
                gamePanel.setShotVeloDiffCompensation(8);
                gamePanel.setScoreModifier(3);
                break;
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
            gamePanel.movePlayer(-12);
        }
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
            gamePanel.movePlayer(+12);
        }
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_SPACE) {
            gamePanel.initPlayerShot();
        }
    }

}
