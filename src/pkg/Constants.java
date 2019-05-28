package pkg;

import java.awt.*;


public interface Constants {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int NUM_ALIENS = 50; //reference
    int NUM_STARS = 200;
    int BOARD_WIDTH = screenSize.width;
    int BOARD_HEIGHT = screenSize.height;
    int SHOT_VELOCITY = -16;
    int BOMB_VELOCITY = +7;
    int NUM_BLOCKERS = 6;
    int BASE_SHOT_MODIFIER = 8600;
    int BLOCKER_HEALTH = 5;

}
