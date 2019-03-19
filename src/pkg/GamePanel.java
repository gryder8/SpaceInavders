package pkg;


import com.sun.istack.internal.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GamePanel extends JPanel implements Constants { //TODO: Optimize, add sound

    private int alienDxDiffCompensation = 0;
    private int shotVeloDiffCompensation = 0;
    private int bombVeloDiffCompensation = 0;
    private int shotChanceModifier = 0;


    void setAlienDxDiffCompensation(int alienDxDiffCompensation) {
        this.alienDxDiffCompensation = alienDxDiffCompensation;
    }

    void setShotVeloDiffCompensation(int shotVeloDiffCompensation) {
        this.shotVeloDiffCompensation = shotVeloDiffCompensation;
    }

    void setBombVeloDiffCompensation(int bombVeloDiffCompensation) {
        this.bombVeloDiffCompensation = bombVeloDiffCompensation;
    }

    void setShotChanceModifier(int shotChanceModifier) {
        this.shotChanceModifier = shotChanceModifier;
    }

    private Random rand = new Random();
    private ArrayList<Alien> aliens = new ArrayList<>();
    private int[][] starPositions = new int[NUM_STARS][2]; //0 is x, 1 is y
    private Player player;
    private Shot playerShot = new Shot();
    private int playerWidth;
    private int playerHeight;
    private int bottomOffset;
    private MysteryShip mysteryShip = new MysteryShip(0 ,20);

    private Color currentPlayerColor = Color.GREEN;

    private Color starColor = Color.WHITE;
    private Font font = new Font("Lucida Sans", Font.PLAIN, 20);

    private boolean isPaused = false;

    private int playerLives = 0;

    private int playerScore = 0;

    MysteryShip getMysteryShip() {
        return mysteryShip;
    }

    Color getCurrentPlayerColor() {
        return currentPlayerColor;
    }

    Color getStarColor() {
        return starColor;
    }

    void setPaused(boolean paused) {
        isPaused = paused;
    }

    void setStarColor(Color starColor) {
        this.starColor = starColor;
    }

    ArrayList<Alien> getAliens() {
        return aliens;
    }

    Player getPlayer() {
        return player;
    }

    BufferedImage getPlayerImage() {
        return player.getImage();
    }

    GamePanel() { //constructor (does all necessary initialization)

        bottomOffset = 200;
        int numHorizontal = 10;
        int numVertical = 5;
        final int H_SPACING = 120;
        final int V_SPACING = 50;

        long beginTime = System.currentTimeMillis();

        for (int i = 0; i < numHorizontal; i++) {
            for (int j = 0; j < numVertical; j++) {
                aliens.add(j, new Alien((i * H_SPACING) + 300, (j * V_SPACING) + 50));
                alienColorInit(j, aliens.get(j));
            }
        }

        long endTime = System.currentTimeMillis();

        player = new Player(BOARD_WIDTH / 2, BOARD_HEIGHT - bottomOffset); //init
        playerWidth = player.getImage().getWidth(this);
        playerHeight = player.getImage().getHeight(this);
        generateStarPositions();
        playerShot.setVisible(false);
        mysteryShip.setVisible(false);
        repaint();
        System.out.println("Setup time: " + (endTime - beginTime));
    }

    private void randomMysteryShipInit (){
        if (aliens.size() > 0 && !isPaused) {
            int roll = ThreadLocalRandom.current().nextInt(0, 100); //use 100
            if (roll < 10) {
                initMysteryShip();
            }
        }
    }

    void initMysteryShip(){
        Color ORANGE = new Color(255, 105, 18);
        if (!mysteryShip.isVisible() && player.isVisible()){
            mysteryShip.setPointValue(100);
            mysteryShip.setImage(changeColorOfImage(mysteryShip.getImage(), ORANGE));
            mysteryShip.setVisible(true);
        }
    }

    void moveMysteryShip(){
        if (mysteryShip.isVisible() && mysteryShip.getxPos() < BOARD_WIDTH && mysteryShip.getxPos() >= 0){
            mysteryShip.move(3+alienDxDiffCompensation);
        } else{
            mysteryShip.setVisible(false);
        }
    }


    void randomBombInit() {
        if (aliens.size() > 0 && !isPaused) {
            for (Alien a : aliens) {
                int roll = ThreadLocalRandom.current().nextInt(0, 4000 - shotChanceModifier); //TODO: Tweak to change shooting amount
                if (roll == 1) {
                    initAlienBomb(a);
                }
            }
        }
    }

    private void initAlienBomb(Alien a) {
        if (!a.getBomb().isVisible() && a.isVisible() && player.isVisible()) {
            a.setBomb(new AlienBomb(a.getxPos(), a.getyPos()));
            a.getBomb().setVisible(true);
        }
    }

    private void alienColorInit(int gridPos, Alien a) { //color the rows of aliens
        final Color PURPLE = new Color(95, 20, 255); //use for purple
        final Color PINK = new Color(255, 102, 204); //more vibrant than Color.PINK
        switch (gridPos) { //cases = numVertical in constructor
            case 0:
                a.setImage(changeColorOfImage(a.getImage(), PINK));
                a.setPointValue(40);
                break;
            case 1:
                a.setImage(changeColorOfImage(a.getImage(), PURPLE));
                a.setPointValue(20);
                break;
            case 2:
                a.setImage(changeColorOfImage(a.getImage(), Color.CYAN));
                a.setPointValue(20);
                break;
            case 3:
                a.setImage(changeColorOfImage(a.getImage(), Color.GREEN));
                a.setPointValue(10);
                break;
            default:
                a.setImage(changeColorOfImage(a.getImage(), Color.RED));
                a.setPointValue(10);
                break;
        }

    }

    private void generateStarPositions() { //generates random x and y positions and stores them in a 2D array to be constantly redrawn when panel is repainted
        int rndX;
        int rndY;
        for (int i = 0; i < NUM_STARS; i++) { //draw stars
            rndX = rand.nextInt(BOARD_WIDTH);
            rndY = rand.nextInt(BOARD_HEIGHT);
            starPositions[i][0] = rndX;
            starPositions[i][1] = rndY;
        }
    }

    private void drawStars(Graphics g, Color starColor) { //positions stored in 2D array
        int rndSize;
        g.setColor(starColor); //allows the user to change the color of the stars
        for (int i = 0; i < NUM_STARS; i++) { //draw stars
            rndSize = ThreadLocalRandom.current().nextInt(1, 3); //randomizing this size causes them to "twinkle"
            g.fillRect(starPositions[i][0], starPositions[i][1], rndSize, rndSize); //draw them at (x,y) position with size
        }
    }

    @SuppressWarnings("Duplicates")
    private void drawAliens(Graphics g) { //iterate  through the array of aliens and draw them at their current positions with current images
        removeDeadAliens();
        for (Alien a : aliens) {
            if (a.isVisible()) {
                g.drawImage(a.getImage(), a.getxPos(), a.getyPos(), this);
            }
            if (a.isDying()) {
                a.die();
                repaint();
            }
            checkShotCollision(a, playerShot);
        }
    }

    @SuppressWarnings("Duplicates")
    private void drawPlayer(Graphics g) {
        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getxPos(), player.getyPos(), this);
        } else if (player.isDying()) {
            setPaused(true);
            JTextArea text = new JTextArea("Game over! You died! Score: " + playerScore);
            JOptionPane.showMessageDialog(this, text);
            System.exit(0);
        }
    }

    private void drawShot(Graphics g) {
        if (playerShot.isVisible() && playerShot != null) {
            g.drawImage(playerShot.getImage(), playerShot.getxPos(), playerShot.getyPos(), this);
        }
    }

    private void drawAlienBombs(Graphics g) {
        for (Alien a : aliens) {
            if (a.getBomb().isVisible()) {
                g.drawImage(a.getBomb().getImage(), a.getBomb().getxPos(), a.getBomb().getyPos(), this);
            } else if (a.getBomb().isDying()) {
                a.getBomb().die();
            }
        }
    }

    private void drawMysteryShip(Graphics g){
        if (mysteryShip.isVisible()){
            g.drawImage(mysteryShip.getImage(), mysteryShip.getxPos(), mysteryShip.getyPos(), this);
        }
        if (mysteryShip.isDying()){
            mysteryShip.setVisible(false);
            repaint();
        }
    }

    boolean isPlayerShotVisible() {
        return playerShot.isVisible();
    }

    void moveShot() {
        if (playerShot.getyPos() > 0) {
            playerShot.move(SHOT_VELOCITY + shotVeloDiffCompensation);
            repaint();
        } else {
            playerShot.setVisible(false);
        }
    }

    void initPlayerShot() {
        if (!playerShot.isVisible() && !isPaused) {
            playerShot = new Shot(player.getxPos(), player.getyPos());
            playerShot.setVisible(true);
            repaint();
            SoundManager.shootSound();
        }
    }

    void moveAlienBomb(Alien a) {
        if (!isPaused) {
            if (a.getBomb().getyPos() < BOARD_HEIGHT - 38) {
                a.getBomb().move(BOMB_VELOCITY + bombVeloDiffCompensation);
                repaint();
            } else {
                a.getBomb().setVisible(false);
            }
            checkAlienBombCollision(player, a.getBomb());
        }
    }

    private Alien topAlien() {
        Alien topAlien = aliens.get(0);
        for (Alien a : aliens) {
            if (a.getyPos() < topAlien.getyPos()) {
                topAlien = a;
            }
        }
        return topAlien;
    }

    private Alien bottomAlien() {
        Alien bottomAlien = aliens.get(0);
        for (Alien a : aliens) {
            if (a.getyPos() >= bottomAlien.getyPos()) {
                bottomAlien = a;
            }
        }
        return bottomAlien;
    }

    @SuppressWarnings("Duplicates")
    private Alien leftAlien() {
        Alien leftAlien = aliens.get(0);
        for (Alien a : aliens) {
            if (a.getxPos() < leftAlien.getxPos()) {
                leftAlien = a;
            }
        }
        return leftAlien;
    }

    @SuppressWarnings("Duplicates")
    private Alien rightAlien() {
        Alien rightAlien = aliens.get(0);
        for (Alien a : aliens) {
            if (a.getxPos() > rightAlien.getxPos()) {
                rightAlien = a;
            }
        }
        return rightAlien;
    }

    private void checkShotCollision(Alien a, Shot shot) {
        a.setDying(false);
        final int ALIEN_WIDTH = a.getWidth();
        final int ALIEN_HEIGHT = a.getHeight();
        final int MSHIP_WIDTH = mysteryShip.getWidth();
        final int MSHIP_HEIGHT = mysteryShip.getHeight();
        int shotX = shot.getxPos();
        int shotY = shot.getyPos();
        int alienX = a.getxPos();
        int alienY = a.getyPos();
        int mShipX = mysteryShip.getxPos();
        int mShipY = mysteryShip.getyPos();
        if (playerShot.isVisible() && a.isVisible()) {
            if (shotX >= (alienX) //collision check
                    && shotX <= (alienX + ALIEN_WIDTH)
                    && shotY >= (alienY)
                    && shotY <= (alienY + ALIEN_HEIGHT)) {
                a.setDying(true);
                playerShot.setVisible(false);
                SoundManager.invaderKilledSound();
                playerScore += a.getPointValue();
                randomMysteryShipInit();
            }
        }

        if (playerShot.isVisible() && mysteryShip.isVisible()){
            if (shotX >= (mShipX) //collision check
                    && shotX <= (mShipX + MSHIP_WIDTH)
                    && shotY >= (mShipY)
                    && shotY <= (mShipY + MSHIP_HEIGHT)) {
                mysteryShip.setDying(true);
                playerShot.setVisible(false);
                SoundManager.invaderKilledSound();
                playerScore += mysteryShip.getPointValue();
            }
        }
    }

    /**
     * @param p player object to check for collisions with
     * @param b bomb object to check for collisions with (iterate through on call)
     */
    private void checkAlienBombCollision(@NotNull Player p, @NotNull AlienBomb b) {

        final int PLAYER_HEIGHT = p.getHeight();
        final int PLAYER_WIDTH = p.getWidth();

        int bombX = b.getxPos();
        int bombY = b.getyPos();
        int playerX = p.getxPos();
        int playerY = p.getyPos();

        if (p.isVisible() && b.isVisible()) {
            if (bombX >= (playerX)
                    && bombX <= (playerX + PLAYER_WIDTH)
                    && bombY >= (playerY)
                    && bombY <= (playerY + PLAYER_HEIGHT)) {
                SoundManager.playerDeathSound();
                b.setVisible(false);
                playerShot.setVisible(false);
                playerLives--;
                player.setxPos(50);
                if (playerLives < 0) {
                    playerLives = 0;
                    delay(800);
                    player.setVisible(false);
                    player.setDying(true);
                }
            }
        }
    }




    void moveAliens() { //moves the aliens (called every 20 ms by timer in GameListeners)
        if (aliens.size() > 0 && player.isVisible() && !isPaused) {
            int dx = ((int) Math.ceil((double) 10 / aliens.size())) * 2 + alienDxDiffCompensation;

            if (aliens.size() == 1) {
                dx = 15 + alienDxDiffCompensation;
            }

            int dy = 0;


            boolean isMovingRight = aliens.get(0).isMovingRight();
            boolean isMovingDown = aliens.get(0).isMovingDown();

            Alien rightEdgeAlien = rightAlien();
            Alien leftEdgeAlien = leftAlien();
            Alien topAlien = topAlien();
            Alien bottomAlien = bottomAlien();
            int rightAlienWidth = rightEdgeAlien.getWidth();

            if (topAlien.getyPos() < topAlien.getHeight()) {
                isMovingDown = true;
            }

            if (bottomAlien.getyPos() > (player.getyPos() - bottomOffset)) {
                isMovingDown = false;
            }

            if (rightEdgeAlien.getxPos() > BOARD_WIDTH - rightAlienWidth) { //WIDTH = 1680
                isMovingRight = false;
                dy = rightEdgeAlien.getHeight() / 3;
            }


            if (leftEdgeAlien.getxPos() < 0) {
                isMovingRight = true;
                dy = leftEdgeAlien.getHeight() / 3;
            }


            for (Alien a : aliens) {

                if (isMovingRight) {
                    a.setxDir(+1);
                } else if (!isMovingRight) {
                    a.setxDir(-1);
                }

                if (isMovingDown) {
                    a.setyDir(+1);
                } else if (!isMovingDown) {
                    a.setyDir(-1);
                }
                a.move(dx, dy);
            }
            this.repaint();

        } else if (aliens.size() <= 0) {
            setPaused(true);
            JOptionPane.showMessageDialog(this, "You won! You scored " + playerScore + " points.");
            System.exit(0);
        }
    }

    private void removeDeadAliens() {
        Iterator<Alien> itr = aliens.iterator();
        while (itr.hasNext()) {
            if (!itr.next().isVisible()) {
                itr.remove();
            }
        }
    }

    void movePlayer(int dx) { //dx is passed in GameListeners
        player.move(dx);

        if (player.getxPos() > BOARD_WIDTH - playerWidth) { //make sure player stays on screen
            player.setxPos(BOARD_WIDTH - playerWidth);
        } else if (player.getxPos() < 0) {
            player.setxPos(0);
        }

        repaint();
    }

    private void delay(long ms) { //for testing use
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.err.println("Exception!");
        }
    }

    BufferedImage changeColorOfImage(BufferedImage img, Color color) {
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                if (img.getRGB(x, y) != 0) {
                    img.setRGB(x, y, color.getRGB());
                }
            }
        }
        return img;
    }

    Color customColor(Color input) {
        Color choice = JColorChooser.showDialog(this, "Choose Player Color", null);
        if (choice != null) {
            input = choice;
            return input;
        }

        return input;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        g.setFont(font);
        g.setColor(Color.RED);
        g.drawString("Lives Remaining: " + playerLives, 0, font.getSize()); //String, x, y

        g.setColor(Color.GREEN);

        String scoreDisplay = "Score: " + playerScore;
        g.drawString(scoreDisplay, BOARD_WIDTH - (scoreDisplay.length() * 11), font.getSize()); //String, x, y

        drawAliens(g);
        drawMysteryShip(g);
        drawStars(g, starColor);
        if (!isPaused) {
            drawPlayer(g);
        }
        drawShot(g);
        drawAlienBombs(g);

    }


}
