package pkg;

import java.awt.*;


public interface Constants {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int NUM_ALIENS = 50;
    int NUM_STARS = 200;
    int BOARD_WIDTH = screenSize.width; //600
    int BOARD_HEIGHT = screenSize.height; //600
    int SHOT_VELOCITY = -10;
    int BOMB_VELOCITY = +6;

}
