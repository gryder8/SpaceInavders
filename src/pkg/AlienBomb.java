package pkg;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class AlienBomb extends Sprite {

    private static final String bombImagePath = "src/bomb.png";

    AlienBomb(int x, int y){
        bombInit(x,y);
    }

    private void bombInit(int x, int y){
        BufferedImage bombImage; //init
        this.setVisible(false);

        try {
            bombImage = ImageIO.read(new File(bombImagePath));
        } catch (IOException ex){
            System.err.println("No bomb image found");
            return;
        }
        setImage(bombImage); //set the sprite image as well

        final int HORIZ_SPACE = 13;
        final int VERT_SPACE  = 8;
        this.setxPos(x+HORIZ_SPACE);
        this.setyPos(y+VERT_SPACE);
    }




    void move (int dy) {
        this.setyPos(getyPos()+dy);
    }


}
