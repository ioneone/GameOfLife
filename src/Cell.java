/**
 * Program Name: Cell.java
 * Discussion:   Cell
 * Written By:   Junhong Wang
 * Date:         2017/11/30
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.EventListener;
import java.util.EventObject;

public class Cell extends Rectangle {

    public static final Color ALIVE_COLOR = Color.YELLOW;
    public static final Color DEAD_COLOR = Color.GRAY;
    public static final Color HOVER_COLOR = Color.RED;

    private boolean isAlive;
    private boolean isAliveNext;

    private int row = -1;
    private int col = -1;

    public Cell(boolean isAlive, boolean isAliveNext, double size) {
        super(size, size);
        if (isAlive) {
            setFill(ALIVE_COLOR);
        } else {
            setFill(DEAD_COLOR);
        }

        this.isAlive = isAlive;
        this.isAliveNext = isAliveNext;
    }

    public Cell(boolean isAlive, boolean isAliveNext, double size,
                int row, int col) {
        super(size, size);
        this.row = row;
        this.col = col;
        if (isAlive) {
            setFill(ALIVE_COLOR);
        } else {
            setFill(DEAD_COLOR);
        }

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
        if (numLiveNeighbours < 2 || numLiveNeighbours > 3) {
            // underpopulation and overpopulation
            isAliveNext = false;
        } else {
            isAliveNext = true;
        }
    }

    private void deadTransition(int numLiveNeighbours) {
        if (numLiveNeighbours == 3) {
            // reproduction
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

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

}