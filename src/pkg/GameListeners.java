package pkg;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;

public class GameListeners extends KeyAdapter implements ActionListener, ChangeListener {

    GamePanel gamePanel;
    private Timer alienMoveTimer = new Timer(20, this);
    private Timer shotMoveTimer = new Timer(10, this);
    private Timer alienBombMoveTimer = new Timer(20, this);
    private Timer alienBombGenerationTimer = new Timer(1000, this);

    GameListeners(GamePanel panel) {
        this.gamePanel = panel;

        panel.addKeyListener(this);

        alienMoveTimer.start(); //start movement
        shotMoveTimer.start();
        alienBombMoveTimer.start();
        alienBombGenerationTimer.start();
    }

    @Override
    public void stateChanged(ChangeEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = " ";
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
        if (e.getSource() == alienBombMoveTimer){
            for (Alien a : gamePanel.getAliens()) {
                if (a.getBomb().isVisible()){
                    gamePanel.moveAlienBomb(a);
                    gamePanel.repaint();
                }
            }
        }
        if (e.getSource() == alienBombGenerationTimer){
            for (Alien a: gamePanel.getAliens()) {
                gamePanel.randomBombInit();
            }
        }

        //handle externals
        System.out.println(s.toLowerCase());
        switch (s.toLowerCase()){
            case "change player color": gamePanel.setPaused(true);
            gamePanel.getPlayer().setImage(gamePanel.changeColorOfImage(gamePanel.getPlayerImage(), gamePanel.customColor(gamePanel.getCurrentPlayerColor())));
            gamePanel.setPaused(false);
            break;
            case "change star color": gamePanel.setPaused(true);
                gamePanel.setStarColor(gamePanel.customColor(gamePanel.getStarColor()));
                gamePanel.setPaused(false);
                break;
            case "quit" :
                System.exit(0);
                break;
            case "1": gamePanel.setBombVeloDiffCompensation(0);
            gamePanel.setAlienDxDiffCompensation(0);
            gamePanel.setShotChanceModifier(0);
            gamePanel.setShotVeloDiffCompensation(0);
            break;
            case "2": gamePanel.setBombVeloDiffCompensation(1);
                gamePanel.setAlienDxDiffCompensation(3);
                gamePanel.setShotChanceModifier(500);
                gamePanel.setShotVeloDiffCompensation(3);
                break;
            case "3": gamePanel.setBombVeloDiffCompensation(4);
                gamePanel.setAlienDxDiffCompensation(5);
                gamePanel.setShotChanceModifier(800);
                gamePanel.setShotVeloDiffCompensation(5);
                break;
            case "4": gamePanel.setBombVeloDiffCompensation(5);
                gamePanel.setAlienDxDiffCompensation(7);
                gamePanel.setShotChanceModifier(2200);
                gamePanel.setShotVeloDiffCompensation(6);
                break;
            case "insane": gamePanel.setBombVeloDiffCompensation(6);
                gamePanel.setAlienDxDiffCompensation(10);
                gamePanel.setShotChanceModifier(3400);
                gamePanel.setShotVeloDiffCompensation(8);
                break;
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) {
            gamePanel.movePlayer(-12);
        }
        if (code == KeyEvent.VK_RIGHT) {
            gamePanel.movePlayer(+12);
        }
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_SPACE) {
            gamePanel.initPlayerShot();
        }
    }

}
