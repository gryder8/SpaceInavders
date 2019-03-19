package pkg;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MysteryShip extends Sprite {


    private AlienBomb bomb;
    private boolean isDead = false;
    private int xDir;
    private int yDir;
    private int width;
    private int height;
    private int pointValue = 50;
    private static final String mysteryShipImagePath = "src/mysteryShip.png";

    public int getPointValue() {
        return pointValue;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    MysteryShip (int x, int y){
        initializeMysteryShip(x,y);
    }

    private BufferedImage mysteryShipImage = null;

    private void initializeMysteryShip (int x, int y){
        yDir = 0;
        xDir = +1;
        this.setVisible(true);
        this.setxPos(x);
        this.setyPos(y);

        bomb = new AlienBomb(x,y); //use same position as alien?
        try {
            mysteryShipImage = ImageIO.read(new File(mysteryShipImagePath));
        } catch (IOException ex){
            System.err.println("No alien image found. Make sure the file is in the src folder");
        }
        setImage(mysteryShipImage);
    }

    int getWidth(){
        return mysteryShipImage.getWidth();
    }

    int getHeight(){
        return mysteryShipImage.getHeight();
    }

    AlienBomb getBomb(){
        return bomb;
    }

    public void setBomb(AlienBomb bomb) {
        this.bomb = bomb;
    }

    void move (int xDirection){ //only moves in x
        setxPos(getxPos()+xDirection*xDir);
    }
}
