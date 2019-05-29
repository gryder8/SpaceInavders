package pkg;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GamePanel extends JPanel implements Constants {

    private int alienDxDiffCompensation;
    private int shotVeloDiffCompensation = 0;
    private int bombVeloDiffCompensation = 0;
    private int shotChanceModifier = 0;
    private Random rand = new Random();
    private ArrayList<Alien> aliens = new ArrayList<>();
    private ArrayList<Blocker> blockers = new ArrayList<>();
    private int[][] starPositions = new int[NUM_STARS][2]; //0 is x, 1 is y
    private Player player;
    private Shot playerShot = new Shot();
    private int playerWidth;
    private int playerHeight;
    private int bottomOffset;
    private MysteryShip mysteryShip = new MysteryShip(0, 20);
    private Color currentPlayerColor = Color.GREEN;
    private Color starColor = Color.WHITE;
    private int aliensKilled = 0;

    private int scoreModifier = 1;
    private int diffRangeIncrease = 0;

    private Font font;
    /**
     * Game States
     */
    private boolean isPaused = false;
    private boolean isMuted = false;
    private int playerLives = 2;
    private int playerScore = 0;

    GamePanel() { //constructor (does all necessary initialization)
        long beginTime = System.currentTimeMillis();
        File fontFile = new File("src/assets/invaders.otf"); //file for font

        bottomOffset = 200;
        int numHorizontal = 10;
        int numVertical = 5;
        final int H_SPACING = 85;
        final int V_SPACING = 50;

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            font = font.deriveFont(30F);
        } catch (FontFormatException e) {
            System.err.println("Font format error!");
        } catch (IOException e) {
            System.err.println("Exception occurred while setting font!");
        }

        for (int j = 0; j < NUM_BLOCKERS; j++) {
            blockers.add(new Blocker(((BOARD_WIDTH / NUM_BLOCKERS) * j) + 100, BOARD_HEIGHT - 300, BLOCKER_HEALTH));
        }

        for (int i = 0; i < numHorizontal; i++) {
            for (int j = 0; j < numVertical; j++) {
                aliens.add(j, new Alien((i * H_SPACING) + 300, (j * V_SPACING) + 50));
                alienColorAndPointInit(j, aliens.get(j));
            }
        }


        player = new Player(BOARD_WIDTH / 2, BOARD_HEIGHT - bottomOffset); //init
        playerWidth = player.getImage().getWidth(this);
        playerHeight = player.getImage().getHeight(this);
        generateStarPositions();
        playerShot.setVisible(false);
        mysteryShip.setVisible(false);
        repaint();
        long endTime = System.currentTimeMillis();
        System.out.println("Setup time: " + (endTime - beginTime) + " ms");
    }

    static void delay(long ms) { //for testing use
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.err.println("Exception!");
        }
    }

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

    boolean isMuted() {
        return isMuted;
    }

    void setMuted(boolean muted) {
        isMuted = muted;
    }

    MysteryShip getMysteryShip() {
        return mysteryShip;
    }

    Color getCurrentPlayerColor() {
        return currentPlayerColor;
    }

    Color getStarColor() {
        return starColor;
    }

    void setStarColor(Color starColor) {
        this.starColor = starColor;
    }

    void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public int getDiffRangeIncrease() {
        return diffRangeIncrease;
    }

    public void setDiffRangeIncrease(int diffRangeIncrease) {
        this.diffRangeIncrease = diffRangeIncrease;
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

    public int getScoreModifier() {
        return scoreModifier;
    }

    public void setScoreModifier(int scoreModifier) {
        this.scoreModifier = scoreModifier;
    }

    private void randomMysteryShipInit() { //use a random number to possibly init the ship
        if (aliens.size() > 0 && !isPaused && !mysteryShip.isVisible()) {
            int roll = ThreadLocalRandom.current().nextInt(0, 100); //use 100
            if (roll < 10) {
                initMysteryShip();
            }
        }
    }

    private void initMysteryShip() { //create the mystery ship with a random color and make it worth 100 points
        int R = ThreadLocalRandom.current().nextInt(10, 255);
        int G = ThreadLocalRandom.current().nextInt(10, 255);
        int B = ThreadLocalRandom.current().nextInt(10, 255);
        Color RANDOM = new Color(R, G, B);
        if (!mysteryShip.isVisible() && player.isVisible()) {
            mysteryShip.setPointValue(100);
            mysteryShip.setxPos(1);
            mysteryShip.setImage(changeColorOfImage(mysteryShip.getImage(), RANDOM));
            mysteryShip.setVisible(true);
            if (!isMuted) {
                SoundManager.mysteryShipIntroSound();
            }
        }
    }

    void moveMysteryShip() {
        if (mysteryShip.isVisible() && mysteryShip.getxPos() < BOARD_WIDTH && mysteryShip.getxPos() >= 0) { //is between the bounds and is visible
            mysteryShip.move(4);

        } else {
            mysteryShip.setVisible(false);
        }
    }

    void randomBombInit() { //use a random number to possibly init the bomb
        if (aliens.size() > 0 && !isPaused) {
            for (Alien a : aliens) {
                int roll = ThreadLocalRandom.current().nextInt(0, BASE_SHOT_MODIFIER - shotChanceModifier); //Tweak these to change shooting amount
                if (roll <= (aliensKilled / 2) + diffRangeIncrease ) { //aliens will fire more shots as you kill more of them
                    initAlienBomb(a);
                }
            }
        }
    }

    private void initAlienBomb(Alien a) { //init a bomb "belonging" to a certain alien object a
        if (!a.getBomb().isVisible() && a.isVisible() && player.isVisible()) {
            a.setBomb(new AlienBomb(a.getxPos(), a.getyPos()));
            a.getBomb().setVisible(true);
        }
    }

    private void alienColorAndPointInit(int gridPos, Alien a) { //color the rows of aliens and set their point values accordingly
        final Color PURPLE = new Color(95, 20, 255); //use for purple
        final Color PINK = new Color(255, 102, 204); //more vibrant than Color.PINK

        switch (gridPos) { //cases = numVertical in constructor
            case 0:
                a.setImage(changeColorOfImage(a.getImage(), PINK));
                a.setPointValue(50);
                break;
            case 1:
                a.setImage(changeColorOfImage(a.getImage(), PURPLE));
                a.setPointValue(40);
                break;
            case 2:
                a.setImage(changeColorOfImage(a.getImage(), Color.CYAN));
                a.setPointValue(30);
                break;
            case 3:
                a.setImage(changeColorOfImage(a.getImage(), Color.GREEN));
                a.setPointValue(20);
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

    private void drawAliens(Graphics g) { //iterate  through the array of aliens and draw them at their current positions with current images
        removeDeadObjects();
        for (Alien a : aliens) {
            if (a.isVisible()) {
                g.drawImage(a.getImage(), a.getxPos(), a.getyPos(), this);
            }
            if (a.isDying()) {
                a.die();
            }
            checkShotCollision(a, playerShot);
        }
    }

    private void drawPlayer(Graphics g) { //draw the player at it's position if it's visible
        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getxPos(), player.getyPos(), this);
        } else if (player.isDying()) {
            setPaused(true);
            String msg = "Game over! You died! Score: " + playerScore;
            JOptionPane.showMessageDialog(this, msg);
            System.exit(0);
        }
    }

    private void drawShot(Graphics g) { //draw the shot under certain conditions if it's visible
        if (playerShot.isVisible() && playerShot != null) {
            g.drawImage(playerShot.getImage(), playerShot.getxPos(), playerShot.getyPos(), this);
        }
    }

    private void drawAlienBombs(Graphics g) { //draw all the bombs on the screen iteratively
        for (Alien a : aliens) {
            if (a.getBomb().isVisible()) {
                g.drawImage(a.getBomb().getImage(), a.getBomb().getxPos(), a.getBomb().getyPos(), this);
            } else if (a.getBomb().isDying()) {
                a.getBomb().die();
            }
        }
    }

    private void drawMysteryShip(Graphics g) { //draw the ship at its postion if its visible and initialized
        if (mysteryShip.isVisible()) {
            g.drawImage(mysteryShip.getImage(), mysteryShip.getxPos(), mysteryShip.getyPos(), this);
        }
        if (mysteryShip.isDying()) {
            mysteryShip.setVisible(false);
        }
    }

    private void drawBlockers(Graphics g) {
        for (Blocker b : blockers) {
            if (b.isDying()) {
                b.setVisible(false);
            } else if (b.isVisible()) {
                g.drawImage(b.getImage(), b.getxPos(), b.getyPos(), this);
            }
        }
    }

    boolean isPlayerShotVisible() { //getter
        return playerShot.isVisible();
    }

    void moveShot() { //move the shot if it's visible
        if (playerShot.getyPos() > 0) {
            playerShot.move(SHOT_VELOCITY + shotVeloDiffCompensation);
            if (playerShot.getyPos() > (BOARD_HEIGHT / 2) - 200)
                for (Blocker b : blockers) {
                    checkShotCollisionWithBlocker(playerShot, b);
                }
        } else {
            playerShot.setVisible(false);
        }
    }

    void initPlayerShot() { //initialize the player shot object if certain conditions are met
        if (!playerShot.isVisible() && !isPaused) {
            playerShot = new Shot(player.getxPos(), player.getyPos());
            playerShot.setVisible(true);
            if (!isMuted) {
                SoundManager.shootSound();
            }
        }
    }

    void moveAlienBomb(Alien a) {
        if (!isPaused) {
            if (a.getBomb().getyPos() < BOARD_HEIGHT - 38) {
                a.getBomb().move(BOMB_VELOCITY + bombVeloDiffCompensation);
            } else {
                a.getBomb().setVisible(false);
            }
            checkAlienBombCollision(player, a.getBomb());
            if (a.getBomb().getyPos() > BOARD_HEIGHT / 2 + 100) {
                for (Blocker b : blockers) {
                    checkBlockerCollisionWithBomb(b, a.getBomb());
                }
            }
        }
    }

    private Alien topAlien() { //find the top-most alien in the grid of aliens
        Alien topAlien = aliens.get(0);
        for (Alien a : aliens) {
            if (a.getyPos() < topAlien.getyPos()) {
                topAlien = a;
            }
        }
        return topAlien;
    }

    private Alien bottomAlien() { //find the bottom alien in the grid of aliens
        Alien bottomAlien = aliens.get(0);
        for (Alien a : aliens) {
            if (a.getyPos() >= bottomAlien.getyPos()) {
                bottomAlien = a;
            }
        }
        return bottomAlien;
    }

    private Alien leftAlien() { //finds the left-most alien in the grid of aliens
        Alien leftAlien = aliens.get(0);
        for (Alien a : aliens) {
            if (a.getxPos() < leftAlien.getxPos()) {
                leftAlien = a;
            }
        }
        return leftAlien;
    }

    private Alien rightAlien() { //finds the right-most alien in the grid of aliens
        Alien rightAlien = aliens.get(0);
        for (Alien a : aliens) {
            if (a.getxPos() > rightAlien.getxPos()) {
                rightAlien = a;
            }
        }
        return rightAlien;
    }

    /**
     * @param a:    alien object to check for collisions with (iterate)
     * @param shot: shot object to check for collisions with
     */
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
                if (!isMuted) {
                    SoundManager.invaderKilledSound();
                }
                playerScore += a.getPointValue() * scoreModifier;
                aliensKilled++;
                randomMysteryShipInit();
            }
        }

        if (playerShot.isVisible() && mysteryShip.isVisible()) {
            if (shotX >= (mShipX) //collision check
                    && shotX <= (mShipX + MSHIP_WIDTH)
                    && shotY >= (mShipY)
                    && shotY <= (mShipY + MSHIP_HEIGHT)) {
                mysteryShip.setDying(true);
                playerShot.setVisible(false);
                if (!isMuted) {
                    SoundManager.invaderKilledSound();
                }
                playerScore += mysteryShip.getPointValue();
            }
        }
    }

    /**
     * @param b:  blocker object to check for collisions with
     * @param ab: bomb object to check for collisions with (iterate through on call)
     */
    private void checkBlockerCollisionWithBomb(Blocker b, AlienBomb ab) {
        final int BLOCKER_HEIGHT = b.getImage().getHeight();
        final int BLOCKER_WIDTH = b.getImage().getWidth();

        int bombX = ab.getxPos();
        int bombY = ab.getyPos();
        int blockerX = b.getxPos();
        int blockerY = b.getyPos();

        if (b.isVisible() && ab.isVisible()) {
            if (bombX >= (blockerX)
                    && bombX <= (blockerX + BLOCKER_WIDTH)
                    && bombY >= (blockerY)
                    && bombY <= (blockerY + BLOCKER_HEIGHT)) {
                ab.setVisible(false);
                b.setHealth(b.getHealth() - 1);
            }
        }
    }

    private void checkShotCollisionWithBlocker(Shot shot, Blocker b) {
        final int BLOCKER_HEIGHT = b.getImage().getHeight();
        final int BLOCKER_WIDTH = b.getImage().getWidth();
        int blockerX = b.getxPos();
        int blockerY = b.getyPos();

        int shotX = shot.getxPos();
        int shotY = shot.getyPos();

        if (b.isVisible() && shot.isVisible()) {
            if (shotX >= (blockerX)
                    && shotX <= (blockerX + BLOCKER_WIDTH)
                    && shotY >= (blockerY)
                    && shotY <= (blockerY + BLOCKER_HEIGHT)) {
                shot.setVisible(false);
            }
        }
    }

    /**
     * @param p: player object to check for collisions with
     * @param b: bomb object to check for collisions with (iterate through on call)
     */
    private void checkAlienBombCollision(Player p, AlienBomb b) {

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
                if (!isMuted) {
                    SoundManager.playerDeathSound();
                }
                b.setVisible(false);
                player.setVisible(false);
                playerShot.setVisible(false);
                playerLives--;
                player.setxPos(50);
                player.setVisible(true);
                if (playerLives < 0) {
                    playerLives = 0;
                    delay(800);
                    player.setVisible(false);
                    player.setDying(true);
                }
            }
        }
    }

    void moveAliens() { //moves the aliens (called by timer in GameListeners)
        if (aliens.size() > 0 && player.isVisible() && !isPaused) {
            int dx = (((int) Math.ceil((double) 10 / aliens.size())) * 2) + alienDxDiffCompensation;
            //System.out.println(dx);
            //System.out.println(aliens.size());

            if (aliens.size() == 1) { //make last alien super fast
                dx = 18 + alienDxDiffCompensation;
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
                dy = rightEdgeAlien.getHeight() / 3; //change divisor to make it move down less
            }


            if (leftEdgeAlien.getxPos() < 0) {
                isMovingRight = true;
                dy = leftEdgeAlien.getHeight() / 3; //change divisor to make it move down less
            }


            for (Alien a : aliens) { //iterate through
                if (isMovingRight) {
                    a.setxDir(+1);
                } else if (!isMovingRight) { //condition is not always true (ignore)
                    a.setxDir(-1);
                }

                if (isMovingDown) {
                    a.setyDir(+1);
                } else if (!isMovingDown) { //condition is not always true (ignore)
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

    private void removeDeadObjects() { //use an iterator to avoid a concurrent modification exception
        //iterator is an Alien object moving through aliens array
        //removes it from the array
        aliens.removeIf(alien -> !alien.isVisible());
        blockers.removeIf(blocker -> blocker.getHealth() <= 0);
    }

    void movePlayer(int dx) { //dx is passed in GameListeners
        player.move(dx);

        if (player.getxPos() > BOARD_WIDTH - playerWidth) { //make sure player stays on screen
            player.setxPos(BOARD_WIDTH - playerWidth);
        } else if (player.getxPos() < 0) {
            player.setxPos(0);
        }

        //repaint();
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
        g.drawString("Lives Remaining: " + playerLives, 5, font.getSize()); //String, x, y

        g.setColor(Color.GREEN);

        String scoreDisplay = "Score: " + playerScore;
        g.drawString(scoreDisplay, BOARD_WIDTH - (scoreDisplay.length() * 10), font.getSize()); //String, x, y
        drawBlockers(g);
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
