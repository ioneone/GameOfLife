package project;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by one on 9/21/17.
 */
public class GameOfLife extends Application {

    private static final double CELL_SIZE = 3;
    private static final int ROW = 100;
    private static final int COL = 100;
    private static final int SPEED = 200; // 1 update per speed (in millisecond)
    private static int generation = 0;
    private static int liveCells = 0;
    private static Cell[][] cells;

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        gameLoop();
    }

    public void gameLoop() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                prepare();
                update();
            }
        }, 0, SPEED);
    }

    public void init(Stage primaryStage) {
        GridPane pane = new GridPane();
        pane.setHgap(1);
        pane.setVgap(1);
        pane.setStyle("-fx-background-color: #000000;");
        Scene scene = new Scene(pane);
        cells = new Cell[ROW][COL];

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new Cell(false, false, CELL_SIZE);
                pane.add(cells[i][j], i, j);
            }
        }

        

//        Random random = new Random();
//        for (int i = 0; i < ROW * COL / 2; i++) {
//            cells[random.nextInt(ROW)][random.nextInt(COL)].setAlive(true);
//            liveCells++;
//        }

        cells[50][50].setAlive(true);
        cells[50][51].setAlive(true);
        cells[50][52].setAlive(true);
        cells[49][52].setAlive(true);
        cells[48][51].setAlive(true);

        // Create a scene and place it in the stage
        primaryStage.setTitle("GameOfLife"); // Set the stage title primaryStage.setScene(scene); // Place the scene in the stage primaryStage.show(); // Display the stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void prepare() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j].updateNextState(getNumLiveNeighbors(cells, i, j));
            }
        }
    }

    private static void update() {
        liveCells = 0;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j].updateCurrentState();
                if (cells[i][j].isAlive()) liveCells++;
            }
        }
        generation++;
    }

    private static void clear() {

    }

    private static void step() {

    }

    private static void run() {

    }

    private static void pause() {

    }

    private static int getNumLiveNeighbors(Cell[][] cells, int i, int j) {
        int numLiveNeighbors = 0;
        int r;
        int c;

        // top
        r = (i - 1 < 0) ? ROW - 1 : i - 1;
        c = j;
        if (cells[r][c].isAlive()) numLiveNeighbors++;

        // top-left
        r = (i - 1 < 0) ? ROW - 1 : i - 1;
        c = (j - 1 < 0) ? COL - 1 : j - 1;
        if (cells[r][c].isAlive()) numLiveNeighbors++;

        // left
        r = i;
        c = (j - 1 < 0) ? COL - 1 : j - 1;
        if (cells[r][c].isAlive()) numLiveNeighbors++;

        // bottom-left
        r = (i + 1 < ROW) ? i + 1 : 0;
        c = (j - 1 < 0) ? COL - 1 : j - 1;
        if (cells[r][c].isAlive()) numLiveNeighbors++;

        // bottom
        r = (i + 1 < ROW) ? i + 1 : 0;
        c = j;
        if (cells[r][c].isAlive()) numLiveNeighbors++;

        // bottom-right
        r = (i + 1 < ROW) ? i + 1 : 0;
        c = (j + 1 < COL) ? j + 1 : 0;
        if (cells[r][c].isAlive()) numLiveNeighbors++;

        // right
        r = i;
        c = (j + 1 < COL) ? j + 1 : 0;
        if (cells[r][c].isAlive()) numLiveNeighbors++;

        // top-right
        r = (i - 1 < 0) ? ROW - 1 : i - 1;
        c = c = (j + 1 < COL) ? j + 1 : 0;
        if (cells[r][c].isAlive()) numLiveNeighbors++;

        return numLiveNeighbors;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
