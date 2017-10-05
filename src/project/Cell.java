package project;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by one on 9/21/17.
 */
public class Cell extends Rectangle {

    private static final Color ALIVE_COLOR = Color.YELLOW;
    private static final Color DEAD_COLOR = Color.GRAY;

    private boolean isAlive;
    private boolean isAliveNext;

    public Cell(boolean isAlive, boolean isAliveNext, double size) {
        super(size, size);
        setFill(DEAD_COLOR);
        this.isAlive = isAlive;
        this.isAliveNext = isAliveNext;

    }

    public void updateNextState(int numLiveNeighbours) {
        if (isAlive) {
            liveTransition(numLiveNeighbours);
        } else {
            deadTransition(numLiveNeighbours);
        }
    }

    public void updateCurrentState() {
        isAlive = isAliveNext;
        updateColor();
    }

    private void updateColor() {
        if (isAlive) {
            setFill(ALIVE_COLOR);
        } else {
            setFill(DEAD_COLOR);
        }
    }

    private void liveTransition(int numLiveNeighbours) {
        if (numLiveNeighbours < 2 || numLiveNeighbours > 3) { // underpopulation and overpopulation
            isAliveNext = false;
        } else {
            isAliveNext = true;
        }
    }

    private void deadTransition(int numLiveNeighbours) {
        if (numLiveNeighbours == 3) { // reproduction
            isAliveNext = true;
        } else {
            isAliveNext = false;
        }
    }


    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
        updateColor();
    }

}
