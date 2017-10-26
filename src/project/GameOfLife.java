package project;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Window;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
/**
 * Created by one on 9/21/17.
 */
public class GameOfLife extends Application {

    private static final double CELL_SIZE = 5;
    private static final int ROW = 100;
    private static final int COL = 80;
    private static int generation = 0;
    private static int liveCells = 0;
    private static Cell[][] cells;
    boolean playing = false;

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
                if(playing){
                    prepare();
                    update();
                }

            }
        }, 0, 200); // 0.2 sec
    }


    public void init(Stage primaryStage) {
        primaryStage.setResizable(false);
        GridPane pane = new GridPane();

        pane.setPadding(new Insets(0, 10, 0, 10));
        Scene scene = null;
        cells = new Cell[ROW][COL];

        AnchorPane anchorpane = new AnchorPane();

        HBox hbButtons = new HBox();
        hbButtons.setSpacing(10.0);
        hbButtons.setPadding(new Insets(10,10,0,10));

        Button btnStart = new Button();
        btnStart.setText("Start");

        Button btnStop = new Button();
        btnStop.setText("Stop");

        Button btnReset = new Button();
        btnReset.setText("Reset");

        Button btnStep = new Button();
        btnStep.setText("Step");

        hbButtons.getChildren().addAll(btnStart, btnStop, btnReset, btnStep);

        btnStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playing = true;
                Random random = new Random();
                for (int i = 0; i < ROW * COL / 2; i++) {

                    cells[random.nextInt(ROW)][random.nextInt(COL)].setAlive(true);

                    liveCells++;

                }


            }
        });

        btnStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(playing){
                    btnStop.setText("Continue");
                    playing = false;
                }


                else{
                    if(playing == false){
                        btnStop.setText("Stop");
                        playing = true;
                        prepare();
                        update();
                    }
                }
            }
        });

        btnReset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //start(primaryStage);
            }
        });

        btnStep.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                prepare();
                update();
            }
        });

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10,10));

        root.setTop(pane);
        root.setBottom(hbButtons);

        scene = new Scene(root);
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new Cell(false, false, CELL_SIZE);
                pane.add(cells[i][j], i, j);
            }
        }



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


    private static void step() {
        //goes to next generation - INCREASE GENERATION BY ONE.
    }

    private static void run() {

    }

    private static void pause() {

    }

    private static int getNumLiveNeighbors(Cell[][] cells, int i, int j) {
        int numLiveNeighbors = 0;

        // top-left
        if (i - 1 >= 0 && j - 1 >= 0) {
            if (cells[i - 1][j - 1].isAlive()) numLiveNeighbors++;
        }
        // top
        if (j - 1 >= 0) {
            if (cells[i][j - 1].isAlive()) numLiveNeighbors++;
        }

        // top-right
        if (i + 1 < cells.length && j - 1 >= 0) {
            if (cells[i + 1][j - 1].isAlive()) numLiveNeighbors++;
        }

        // left
        if (i - 1 >= 0) {
            if (cells[i - 1][j].isAlive()) numLiveNeighbors++;
        }

        // right
        if (i + 1 < cells.length) {
            if (cells[i + 1][j].isAlive()) numLiveNeighbors++;
        }

        // bottom-left
        if (i - 1 >= 0 && j + 1 < cells[i - 1].length) {
            if (cells[i - 1][j + 1].isAlive()) numLiveNeighbors++;
        }

        // bottom
        if (j + 1 < cells[i].length) {
            if (cells[i][j + 1].isAlive()) numLiveNeighbors++;
        }

        // bottom-right
        if (i + 1 < cells.length && j + 1 < cells[i + 1].length) {
            if (cells[i + 1][j + 1].isAlive()) numLiveNeighbors++;
        }


        return numLiveNeighbors;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
