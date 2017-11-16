import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;

import javafx.scene.input.*;

import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.WindowEvent;

import java.io.File;
/**
 * Created by one on 9/21/17.
 */
public class GameOfLife extends Application {

    private static final double CELL_SIZE = 10;
    private static final int ROW = 50;
    private static final int COL = 50;
    private static int generation = 0;
    private static int liveCells = 0; // update livecells.
    //create function to display number of live cells.
    //create reference to the application on cell class.
    //then create function in main class incrementLiveCells.
    //whenever we detect the click we go to incrementLiveCells.
    //x button not working properly
    private static final int SPEED = 400; // 1 update per speed (in millisecond)
    private static Cell[][] cells;
    private static boolean isContinuous = true;
    private static boolean playing = false;
    private static boolean isReset = false;
    private static boolean isStopped = false;

//    private static String musicFile = "GOLmusic.mp3";
//    private static Media sound = new Media(new File(musicFile).toURI().toString());
//    private static MediaPlayer mediaPlayer = new MediaPlayer(sound);

    private static PatternCell selectedPatternCell = new PatternCell(null, CELL_SIZE);

    private static Label generationLabel;
    private static Label selectedPatternLabel;

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
                Platform.runLater(() -> {
                    if(playing){
                        prepare();
                        update();
                    }
                    updateGenerationLabel();
                    updateSelectedPatternLabel();
                });


            }
        }, 0, SPEED); // 0.2 sec
    }

    public void updateGenerationLabel() {
        generationLabel.setText(Integer.toString(generation));
    }

    public void updateSelectedPatternLabel() {
        if (selectedPatternCell.getPattern() == null) {
            selectedPatternLabel.setText("NULL");
            selectedPatternLabel.setGraphic(null);
        } else {
            selectedPatternLabel.setText(selectedPatternCell.getPattern().name());
            selectedPatternLabel.setGraphic(new PatternCell(selectedPatternCell));
        }
    }


    public void init(Stage primaryStage) {

        GridPane centerPane = getCenterPane();
        HBox bottomPane = getBottomPane();
        ScrollPane rightPane = getRightPane();
        GridPane leftPane = getLeftPane();
        BorderPane root = new BorderPane();

        root.setPadding(new Insets(10, 10, 10,10));
        root.setCenter(centerPane);
        root.setBottom(bottomPane);
        root.setRight(rightPane);
        root.setLeft(leftPane);

        Scene scene = new Scene(root);
        setupPrimaryStage(primaryStage, scene);

    }

    private static GridPane getCenterPane() {
        GridPane golPane = getGOLPane();
        cells = new Cell[ROW][COL];
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                Cell cell = new Cell(false, false, CELL_SIZE, r, c);
                cells[r][c] = cell;
                golPane.add(cell, c, r);
                setCellMouseClickAction(cell);
                setCellMouseEnteredAction(cell);
                setCellExitedAction(cell);
                setCellDragDetectedAction(cell);
                setCellDragOverAction(cell);
                setCellDragEnteredAction(cell);
                setCellDragDroppedAction(cell);
                setCellDragDoneAction(cell);

            }
        }

        return golPane;
    }

    private static GridPane getLeftPane() {
        GridPane grid = new GridPane();
        grid.add(new Label("Generation"), 0, 0);
        generationLabel = new Label(Integer.toString(generation));
        grid.add(generationLabel, 0, 1);
        grid.add(new Label("Selected Pattern"), 0, 2);
        selectedPatternLabel = new Label("NULL", selectedPatternCell);
        selectedPatternLabel.setContentDisplay(ContentDisplay.TOP);
        grid.add(selectedPatternLabel, 0, 3);
        grid.setPrefWidth(230);
        return grid;
    }

    private static ScrollPane getRightPane() {

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        int i = 0;
        for (Pattern pattern : Pattern.values()) {
            PatternCell patternCell = new PatternCell(pattern, CELL_SIZE);
            Label lbl = new Label(pattern.name(), patternCell);
            lbl.setContentDisplay(ContentDisplay.TOP);
            lbl.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Pattern selectedPattern = selectedPatternCell.getPattern();
                    if (selectedPattern != null && selectedPattern == pattern) {
                        selectedPatternCell.setPattern(null);
                    } else {
                        selectedPatternCell.setPattern(pattern);
                    }

                }
            });

            GridPane.setHalignment(lbl, HPos.CENTER);
            gridPane.add(lbl, 0, i++);

        }

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setPrefHeight(CELL_SIZE*ROW);
        scrollPane.setPrefWidth(230);
        scrollPane.setPadding(new Insets(10, 10, 10, 10));

        return scrollPane;
    }

    private static void setCellMouseClickAction(Cell cell) {
        cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (selectedPatternCell.getPattern() == null) {
                    cell.setAlive(true);
                    cell.setFill(Color.YELLOW); //.setAlive(true);
                } else {
                    int[][] data = selectedPatternCell.getData();
                    int r = cell.getRow();
                    int c = cell.getCol();
                    int centerR = (int) data.length / 2;
                    int centerC = (int) data[0].length / 2;
                    for (int i = 0; i < data.length; i++) {
                        for (int j = 0; j < data[i].length; j++) {
                            if (0 <= r+i-centerR && r+i-centerR < ROW && 0 <= c+j-centerC && c+j-centerC < COL) {
                                if (data[i][j] == 1) {
                                    cells[r+i-centerR][c+j-centerC].setFill(Cell.ALIVE_COLOR);
                                    cells[r+i-centerR][c+j-centerC].setAlive(true);
                                } else {
                                    cells[r+i-centerR][c+j-centerC].setFill(Cell.DEAD_COLOR);
                                    cells[r+i-centerR][c+j-centerC].setAlive(false);
                                }
                            }

                        }
                    }

                    selectedPatternCell.setPattern(null);
                }


            }
        });
    }

    private static void setCellMouseEnteredAction(Cell cell) {
        cell.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (selectedPatternCell.getPattern() == null) {
                    cell.setFill(Cell.HOVER_COLOR);
                } else {
                    int[][] data = selectedPatternCell.getData();
                    int r = cell.getRow();
                    int c = cell.getCol();
                    int centerR = (int) data.length / 2;
                    int centerC = (int) data[0].length / 2;
                    for (int i = 0; i < data.length; i++) {
                        for (int j = 0; j < data[i].length; j++) {

                            if (0 <= r+i-centerR && r+i-centerR < ROW && 0 <= c+j-centerC && c+j-centerC < COL) {
                                if (data[i][j] == 1) {
                                    cells[r+i-centerR][c+j-centerC].setFill(Cell.HOVER_COLOR);
                                } else {
                                    cells[r+i-centerR][c+j-centerC].setFill(Cell.DEAD_COLOR);
                                }
                            }

                        }
                    }
                }

            }
        });
    }

    private static void setCellExitedAction(Cell cell) {
        cell.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (selectedPatternCell.getPattern() == null) {
                    if (cell.isAlive()) {
                        cell.setFill(Cell.ALIVE_COLOR);
                    } else {
                        cell.setFill(Cell.DEAD_COLOR);
                    }
                } else {
                    int[][] data = selectedPatternCell.getData();
                    int r = cell.getRow();
                    int c = cell.getCol();

                    int centerR = (int) data.length / 2;
                    int centerC = (int) data[0].length / 2;
                    for (int i = 0; i < data.length; i++) {
                        for (int j = 0; j < data[i].length; j++) {
                            if (0 <= r+i-centerR && r+i-centerR < ROW && 0 <= c+j-centerC && c+j-centerC < COL) {
                                if (cells[r+i-centerR][c+j-centerC].isAlive()) {
                                    cells[r+i-centerR][c+j-centerC].setFill(Cell.ALIVE_COLOR);
                                } else {
                                    cells[r+i-centerR][c+j-centerC].setFill(Cell.DEAD_COLOR);
                                }
                            }

                        }
                    }
                }
            }
        });
    }

    private static void setCellDragDetectedAction(Cell cell) {
        cell.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                        /* allow any transfer mode */
                Dragboard db = cell.startDragAndDrop(TransferMode.ANY);

                //put a string on dragboard - can't make it work without this content block.
                ClipboardContent content = new ClipboardContent();
                content.putString("");
                db.setContent(content);

                cell.setAlive(true);
                cell.setFill(Color.YELLOW);
                liveCells++;
                event.consume();
            }
        });
    }

    private static void setCellDragOverAction(Cell cell) {
        cell.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                System.out.println("onDragOver");

                //if (event.getDragboard().hasString()) {

                //allow for both copying and moving, whatever user chooses
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                //}
                cell.setFill(Color.YELLOW);
                cell.setAlive(true);
                liveCells++;
                event.consume();
            }
        });
    }

    private static void setCellDragEnteredAction(Cell cell) {
        cell.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                System.out.println("onDragEntered");

                //if (event.getDragboard().hasString()) {

                cell.setFill(Color.YELLOW);
                //}
                cell.setAlive(true);
                liveCells++;

                event.consume();
            }
        });
    }

    private static void setCellDragDroppedAction(Cell cell) {
        cell.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                System.out.println("onDragDropped");

                       /* if there is a string data on dragboard, read it and use it */
                //Dragboard db = event.getDragboard();

                cell.setFill(Color.YELLOW);
                cell.setAlive(true);
                liveCells ++;
                event.setDropCompleted(true);

                event.consume();
            }
        });
    }

    private static void setCellDragDoneAction(Cell cell) {
        cell.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                       /* the drag-and-drop gesture ended */
                System.out.println("onDragDone");

                if (event.getTransferMode() == TransferMode.MOVE) {
                    cell.setFill(Color.YELLOW);
                    cell.setAlive(true);
                    liveCells++;
                }

                event.consume();
            }
        });
    }

    private static void setStepButtonAction(Button btn) {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //mediaPlayer.play();
               // mediaPlayer.getCycleDuration();



                prepare();
                update();

            }
        });
    }

    private static void setResetButtonAction(Button btn, Button btnStop) {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //start(primaryStage);
                int j=0;
                for (int i = 0; i < ROW ; i++) {
                    for(j=0; j< COL; j++) {
                        cells[i][j].setAlive(false);
                    }
                }
                isReset = true;
                isStopped = false;

                if(!playing){
                    btnStop.setText("Stop");
                    playing = false;

                }
//                mediaPlayer.stop();
            }

        });

    }

    private static void setStopButtonAction(Button btn) {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!isReset){
                    if(playing){
                        btn.setText("Continue");
                        playing = false;
                        isStopped = true;
                    }

                    else {
                        btn.setText("Stop");
                        playing = true;
                        isStopped = false;
                        prepare();
                        update();
                    }
//                    if(playing)
//                        mediaPlayer.play();
//                    else
//                        mediaPlayer.pause();
                }
            }
        });
    }

    private static void setStartButtonAction(Button btn) {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!isStopped){
                    playing = true;
                    initRandom();
                    isReset = false;
                    isStopped = false;
                    if(playing){
//                        mediaPlayer.play();
//                       //loop forever
//                        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                    }

                }
                else{
                    if(isReset){

                    }

                }

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
//        primaryStage.setMaximized(true);
        primaryStage.setTitle("GameOfLife"); // Set the stage title primaryStage.setScene(scene); // Place the scene in the stage primaryStage.show(); // Display the stage
        primaryStage.setScene(scene);
//        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
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

    private static HBox getBottomPane() {
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
        setResetButtonAction(btnReset, btnStop);
        setStepButtonAction(btnStep);

        return hbButtons;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
