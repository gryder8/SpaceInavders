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
    int BASE_SHOT_MODIFIER = 10000;
    int BLOCKER_HEALTH = 5;

    String BEGIN_MSG = "                                             Welcome to Space Invaders! " + //centered
            "\n-Use the left and right arrow keys to move and the up arrow or spacebar to shoot." +
            "\n-The blockers will lose health and change color with each hit from a missile." +
            "\n-Avoid the missiles and remember you can always change the difficulty using the menu above." +
            "\n-You can change the color of your player or the stars by using the Options tab above." +
            "\n-Sound can be disabled or enabled using the Sound tab";
}
