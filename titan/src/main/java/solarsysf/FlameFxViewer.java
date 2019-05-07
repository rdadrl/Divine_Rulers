package solarsysf;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FlameFxViewer extends ImageView {
    private long lastAnimationTS;
    private int speed = 50;//ms per frame (13 frames in total)
    private Image[] frames = new Image[13];
    private int currImgIndex = 0;

    public FlameFxViewer() {
        frames[0] = new Image("flames/fire1_01.png");
        frames[1] = new Image("flames/fire1_02.png");
        frames[2] = new Image("flames/fire1_03.png");
        frames[3] = new Image("flames/fire1_04.png");
        frames[4] = new Image("flames/fire1_05.png");
        frames[5] = new Image("flames/fire1_06.png");
        frames[6] = new Image("flames/fire1_07.png");
        frames[7] = new Image("flames/fire1_08.png");
        frames[8] = new Image("flames/fire1_09.png");
        frames[9] = new Image("flames/fire1_10.png");
        frames[10] = new Image("flames/fire1_11.png");
        frames[11] = new Image("flames/fire1_12.png");
        frames[12] = new Image("flames/fire1_13.png");
        this.setImage(frames[0]);
        lastAnimationTS = System.currentTimeMillis();
    }

    public void animate () {
        if (System.currentTimeMillis() - lastAnimationTS >= speed) {
            this.setImage(frames[currImgIndex % 12]);
            lastAnimationTS = System.currentTimeMillis(); //doesn't have to be very exact...
            currImgIndex++;
        }
    }

    public void setAnimationSpeed(int urms) { //update rate in ms
        this.speed = urms;
    }
    public int getAnimationSpeed() { //update rate in ms
        return speed;
    }
}
