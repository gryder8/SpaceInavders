package pkg;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Shot extends Sprite {
    private final String shotImagePath = "src/assets/shot.png";

//    private final int HORIZ_SPACE = 12;
//    private final int VERT_SPACE = 10;

    private BufferedImage shotImage = null; //init


    public Shot(){
        this.setVisible(false);
    }

    public Shot (int x, int y){
        shotInit(x,y);
    }

    private void shotInit(int x, int y){

        try {
            shotImage = ImageIO.read(new File(shotImagePath));
        } catch (IOException ex){
            System.err.println("No shot image found");
            return;
        }
        this.setImage(shotImage); //set the sprite image as well
        final int HORIZ_SPACE = (shotImage.getWidth()/2) + 4;
        final int VERT_SPACE = shotImage.getHeight()/2;
        setyPos(y - VERT_SPACE);
        setxPos(x + HORIZ_SPACE);
    }

    void move (int dy) {
        this.setyPos(getyPos()+dy);
    }
}
