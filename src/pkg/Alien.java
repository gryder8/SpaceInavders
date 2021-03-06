package pkg;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Alien extends Sprite {

    private AlienBomb bomb;
    private boolean isDead = false;
    private int xDir;
    private int yDir;
    private int width;
    private int height;
    private int pointValue = 0;
    private static final String alienImagePath = "src/assets/alien.png";

    private BufferedImage alienImage = null;

    int getPointValue() {
        return pointValue;
    }

    void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    Alien (int x, int y){
        initializeAlien(x, y);
    }

    void setxDir(int xDir) {
        this.xDir = xDir;
    }

    void setyDir (int yDir) {
        this.yDir = yDir;
    }

    boolean isMovingRight() {
        return xDir > 0;
    }

    boolean isMovingDown() {
        return yDir > 0;
    }


    private void initializeAlien(int x, int y){
        yDir = +1;
        xDir = +1;
        this.setVisible(true);
        alienImage = null;
        this.setxPos(x);
        this.setyPos(y);

        bomb = new AlienBomb(x,y);
        try {
            alienImage = ImageIO.read(new File(alienImagePath));
        } catch (IOException ex){
            System.err.println("No alien image found. Make sure the file is in the assets folder");
        }
        super.setImage(alienImage);
    }

    int getWidth(){
       return alienImage.getWidth();
    }

    int getHeight(){
        return alienImage.getHeight();
    }

    AlienBomb getBomb(){
        return bomb;
    }

    void setBomb(AlienBomb bomb) {
        this.bomb = bomb;
    }


    void move (int xDirection, int yDirection){
        this.setxPos(getxPos()+(xDirection*xDir));
        this.setyPos(getyPos()+(yDirection*yDir));
    }




}
