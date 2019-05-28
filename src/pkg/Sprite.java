package pkg;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

class Sprite { //all characters inherit from these general traits of a sprite



    private boolean isVisible;
    private BufferedImage image; //image of sprites
    private int xPos;
    private int yPos;
    private boolean isDying;

    private final String explImagePath = "src/assets/alienExpl.png";

    boolean isVisible() {
        return isVisible;
    }

    void setVisible(boolean visible) {
        isVisible = visible;
    }


    void die() {
        try {
            image = ImageIO.read(new File(explImagePath));
        } catch (IOException ex) {
            System.err.println("No explosion image found. Make sure the file is in the assets folder");
            return;
        }
        setImage(image);
        Timer deathAnimationTimer = new Timer();
        deathAnimationTimer.schedule(alienDeathAnimation, 300);
        //isVisible = false;
    }


    private TimerTask alienDeathAnimation = new TimerTask() {
        @Override
        public void run() {
            setVisible(false);
        }
    };


    BufferedImage getImage() {
        return image;
    }

    void setImage(BufferedImage image) {
        this.image = image;
    }

    int getxPos() {
        return xPos;
    }

    void setxPos(int xPos) {
        this.xPos = xPos;
    }

    int getyPos() {
        return yPos;
    }

    void setyPos(int yPos) {
        this.yPos = yPos;
    }

    boolean isDying() {
        return isDying;
    }

    void setDying(boolean dying) {
        isDying = dying;
    }
}
