import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;

/**
 * Created by one on 9/21/17.
 */
public class GameOfLife extends Application {

    private static final double CELL_SIZE = 5;
    private static final int ROW = 100;
    private static final int COL = 80;
    private static int generation = 0;
    private static int liveCells = 0;
    private static final int SPEED = 400; // 1 update per speed (in millisecond)
    private static Cell[][] cells;
    private static boolean isContinuous = true;
    private static boolean playing = false;

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
        }, 0, SPEED); // 0.2 sec
    }


    public void init(Stage primaryStage) {


        GridPane pane = getGOLPane();

        cells = new Cell[ROW][COL];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new Cell(false, false, CELL_SIZE);
                pane.add(cells[i][j], i, j);
            }
        }

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

        setStartButtonAction(btnStart);
        setStopButtonAction(btnStop);
        setResetButtonAction(btnReset);
        setStepButtonAction(btnStep);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10,10));
        root.setTop(pane);
        root.setBottom(hbButtons);

        Scene scene = new Scene(root);

        setupPrimaryStage(primaryStage, scene);

    }

    private static void setStepButtonAction(Button btn) {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                prepare();
                update();
            }
        });
    }

    private static void setResetButtonAction(Button btn) {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //start(primaryStage);
            }
        });
    }

    private static void setStopButtonAction(Button btn) {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(playing){
                    btn.setText("Continue");
                    playing = false;
                }

                else {
                    btn.setText("Stop");
                    playing = true;
                    prepare();
                    update();

                }
            }
        });
    }

    private static void setStartButtonAction(Button btn) {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playing = true;
                initGlider();

            }
        });
    }

    private static GridPane getGOLPane() {
        GridPane pane = new GridPane();
        pane.setHgap(1);
        pane.setVgap(1);
        pane.setStyle("-fx-background-color: #000000;");
        return pane;
    }

    private static void setupPrimaryStage(Stage primaryStage, Scene scene) {
        // Create a scene and place it in the stage
        primaryStage.setResizable(false);
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

    private static void initRandom() {
        Random random = new Random();
        for (int i = 0; i < ROW * COL / 2; i++) {
            cells[random.nextInt(ROW)][random.nextInt(COL)].setAlive(true);
            liveCells++;
        }
    }

    private static void initGlider() {
        cells[50][50].setAlive(true);
        cells[50][51].setAlive(true);
        cells[50][52].setAlive(true);
        cells[49][52].setAlive(true);
        cells[48][51].setAlive(true);
        liveCells = 5;
    }

    private static int getNumLiveNeighbors(Cell[][] cells, int i, int j) {
        int numLiveNeighbors = 0;
        int r;
        int c;

        if (isContinuous) {
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
            c = (j + 1 < COL) ? j + 1 : 0;
            if (cells[r][c].isAlive()) numLiveNeighbors++;

        } else {

            // top
            if (i - 1 >= 0) {
                r = i - 1;
                c = j;
                if (cells[r][c].isAlive()) numLiveNeighbors++;
            }

            // top-left
            if (i - 1 >= 0 && j - 1 >= 0) {
                r = i - 1;
                c = j - 1;
                if (cells[r][c].isAlive()) numLiveNeighbors++;
            }

            // left
            if (j - 1 >= 0) {
                r = i;
                c = j - 1;
                if (cells[r][c].isAlive()) numLiveNeighbors++;
            }

            // bottom-left
            if (i + 1 < ROW && j - 1 >= 0) {
                r = i + 1;
                c = j - 1;
                if (cells[r][c].isAlive()) numLiveNeighbors++;
            }

            // bottom
            if (i + 1 < ROW) {
                r = i + 1;
                c = j;
                if (cells[r][c].isAlive()) numLiveNeighbors++;
            }

            // bottom-right
            if (i + 1 < ROW && j + 1 < COL) {
                r = i + 1;
                c = j + 1;
                if (cells[r][c].isAlive()) numLiveNeighbors++;
            }

            // right
            if (j + 1 < COL) {
                r = i;
                c = j + 1;
                if (cells[r][c].isAlive()) numLiveNeighbors++;
            }

            // top-right
            if (i - 1 >= 0 && j + 1 < COL) {
                r = i - 1;
                c = j + 1;
                if (cells[r][c].isAlive()) numLiveNeighbors++;
            }


        }


        return numLiveNeighbors;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
