package pkg;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Player extends Sprite {

    private static final String playerImagePath = "src/player.png";
    private static final String explosionImagePath = "src/alienExpl.png";


    private Shot playerBomb;

    private int height = 0;
    private int width = 0;
    private BufferedImage playerImage;

    public Shot getShot (){
        return playerBomb;
    }

    Player(int x, int y){
        initPlayer(x,y);
    }

    private void initPlayer(int x, int y){
        this.setVisible(true);
         playerImage = null;
        this.setxPos(x);
        this.setyPos(y);

        playerBomb = new Shot(x,y);
        try {
            playerImage = ImageIO.read(new File(playerImagePath));
        } catch (IOException ex){
            System.err.println("No player image found");
            return;
        }

        setImage(playerImage);

        this.height = playerImage.getHeight();
        this.width = playerImage.getWidth();
    }

    int getHeight() {
        return height;
    }

    int getWidth() {
        return width;
    }

    @Override //from Sprite class
    void die(){
        try {
            playerImage = ImageIO.read(new File(explosionImagePath));
        } catch (IOException ex){
            System.err.println("No player image found");
            return;
        }
        this.setImage(playerImage);
    }


    void move (int dx){
        this.setxPos(getxPos() + dx);
    }

}
