package pkg;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Blocker extends Sprite implements Constants {

    private int health;
    private int maxHealth;
    private static final String greenBlockerImagePath = "src/assets/greenBlocker.png";
    private static final String yellowBlockerImagePath = "src/assets/yellowBlocker.png";
    private static final String redBlockerImagePath = "src/assets/redBlocker.png";

    Blocker (int x, int y, int health){
        this.maxHealth = health;
        initializeBlocker(x, y, health);
    }

    private void initializeBlocker(int xPos, int yPos, int health){
        super.setxPos(xPos);
        super.setyPos(yPos);
        this.health = health;
        changeImageToFileAtPath(greenBlockerImagePath);
        this.setVisible(true);
    }


    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health <= 0){
            this.setDying(true);
            return;
        }
        this.health = health;
        if ((double) health / (double) maxHealth >= 0.66) {
            changeImageToFileAtPath(greenBlockerImagePath);
        } else if ((double) health / (double) maxHealth >= 0.33){
            changeImageToFileAtPath(yellowBlockerImagePath);
        } else {
            changeImageToFileAtPath(redBlockerImagePath);
        }
    }

    private void changeImageToFileAtPath(String filePath){ //sets the image for this Sprite to the one found at the file path location
        BufferedImage image;
        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException ex){
            System.err.println("No image found. Make sure the file is in the assets folder");
            return;
        }
        super.setImage(image);
    }
}
