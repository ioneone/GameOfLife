/**
 * Program Name: GameOfLife.java
 * Discussion:   GameOfLife
 * Written By:   Junhong Wang, Winnie Su
 * Date:         2017/11/30
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.input.*;

import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
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

public class GameOfLife extends Application {

    private static final double CELL_SIZE = 10;
    private static final int ROW = 50;
    private static final int COL = 50;
    private static int generation = 0;
    private static int liveCells = 0;

    // 1 update per speed (in millisecond)
    private static final int SPEED = 400;

    private static Cell[][] cells;
    private static boolean playing = false;
    private static boolean isReset = false;
    private static boolean isPaused = true;
    private static boolean isRandom = false;

    private static MediaPlayer mediaPlayer;

    private static Label generationLabel;
    private static Label liveCellsLabel;

    private static PatternCell selectedPatternCell =
            new PatternCell(null, CELL_SIZE);
    private static Label selectedPatternLabel;

    // Author: J.W.
    @Override
    public void start(Stage primaryStage) throws Exception {
        loadMusicFile();
        init(primaryStage);
        gameLoop();
    }

    // Author: W.S.
    public void loadMusicFile() {
        String musicFile = "Debriefing.mp3";
        Media sound = new Media(getClass().getClassLoader().
                getResource(musicFile).toString());
        mediaPlayer = new MediaPlayer(sound);
    }

    // Author: J.W.
    public void gameLoop() {

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if(playing){

                        // Edit: W.S.
                        // Comment: added functions to
                        //          handle background music
                        if(liveCells > 0){
                            prepare();
                            update();
                            updateGenerationLabel();
                            updateLiveCellsLabel();
                            updateSelectedPatternLabel();
                            mediaPlayer.play();
                        } else {
                            mediaPlayer.stop();
                        }
                    }

                    if(!playing && isPaused){
                        updateGenerationLabel();
                        updateLiveCellsLabel();
                        updateSelectedPatternLabel();
                    }

                });
            }
        }, 0, SPEED);
    }

    // Author: J.W.
    public void updateLiveCellsLabel() {
        liveCellsLabel.setText(Integer.toString(liveCells));
    }

    // Author: J.W.
    public void updateGenerationLabel() {
        generationLabel.setText(Integer.toString(generation));
    }

    // Author: J.W.
    public void updateSelectedPatternLabel() {
        if (selectedPatternCell.getPattern() == null) {
            selectedPatternLabel.setText("NULL");
            selectedPatternLabel.setGraphic(null);
        } else {
            selectedPatternLabel.setText
                    (selectedPatternCell.getPattern().name());
            selectedPatternLabel.setGraphic
                    (new PatternCell(selectedPatternCell));
        }
    }

    // Author: J.W.
    public void init(Stage primaryStage) {
        GridPane centerPane = getCenterPane();
        HBox bottomPane = getBottomPane();
        ScrollPane rightPane = getRightPane();
        GridPane leftPane = getLeftPane();
        BorderPane root = new BorderPane();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("css/styles.css");

        root.setPadding(new Insets(10, 10, 10, 10));
        root.setCenter(centerPane);
        root.setBottom(bottomPane);
        root.setRight(rightPane);
        root.setLeft(leftPane);

        setupPrimaryStage(primaryStage, scene);

    }

    // Author: J.W.
    private static GridPane getCenterPane() {
        Cell cell;

        GridPane golPane = getGOLPane();
        cells = new Cell[ROW][COL];
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                cell = new Cell(false, false, CELL_SIZE, r, c);
                cells[r][c] = cell;
                golPane.add(cell, c, r);
                setCellMouseClickAction(cell);
                setCellMouseEnteredAction(cell);
                setCellExitedAction(cell);

                // Edit: W.S.
                // Comment: Added drag feature to cells
                setCellDragDetectedAction(cell);
                setCellDragOverAction(cell);
                setCellDragEnteredAction(cell);
                setCellDragDroppedAction(cell);
                setCellDragDoneAction(cell);

            }
        }

        return golPane;
    }

    // Author: J.W.
    private static GridPane getLeftPane() {
        GridPane grid = new GridPane();
        Label label;

        // generation label
        label = new Label("Generation");
        label.getStyleClass().add("underline");
        generationLabel = new Label(Integer.toString(generation));
        grid.add(label, 0, 0);
        grid.add(generationLabel, 0, 1);

        // liveCells label
        label = new Label("liveCells");
        label.getStyleClass().add("underline");
        liveCellsLabel = new Label(Integer.toString(liveCells));
        grid.add(label, 0, 2);
        grid.add(liveCellsLabel, 0, 3);

        // selected pattern label
        label = new Label("Selected Pattern");
        label.getStyleClass().add("underline");
        selectedPatternLabel = new Label("NULL", selectedPatternCell);
        selectedPatternLabel.setContentDisplay(ContentDisplay.TOP);
        grid.add(label, 0, 4);
        grid.add(selectedPatternLabel, 0, 5);
        
        grid.setPrefWidth(230);

        return grid;
    }

    // Author: J.W.
    private static ScrollPane getRightPane() {
        GridPane gridPane = new GridPane();
        ScrollPane scrollPane = new ScrollPane(gridPane);
        PatternCell patternCell;
        Label label;
        int i = 0;

        gridPane.setAlignment(Pos.CENTER);

        for (Pattern pattern : Pattern.values()) {
            patternCell = new PatternCell(pattern, CELL_SIZE);
            label = new Label(pattern.name(), patternCell);
            label.setContentDisplay(ContentDisplay.TOP);
            label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Pattern selectedPattern
                            = selectedPatternCell.getPattern();
                    if (selectedPattern != null &&
                            selectedPattern == pattern) {
                        selectedPatternCell.setPattern(null);
                    } else {
                        selectedPatternCell.setPattern(pattern);
                    }

                }
            });

            GridPane.setHalignment(label, HPos.CENTER);
            gridPane.add(label, 0, i++);

        }

        scrollPane.setPrefHeight(CELL_SIZE*ROW);
        scrollPane.setPrefWidth(230);
        scrollPane.setPadding(new Insets(10, 10, 10, 10));

        return scrollPane;
    }

    // Author: J.W.
    private static void setCellMouseClickAction(Cell cell) {
        cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int[][] data;
                int r;
                int c;
                int centerR;
                int centerC;

                if (selectedPatternCell.getPattern() == null) {
                    cell.setAlive(true);
                    liveCells++;
                } else {

                    data = selectedPatternCell.getData();
                    r = cell.getRow();
                    c = cell.getCol();
                    centerR = (int) data.length / 2;
                    centerC = (int) data[0].length / 2;

                    for (int i = 0; i < data.length; i++) {

                        for (int j = 0; j < data[i].length; j++) {

                            if (0 <= (r + i - centerR) &&
                                    (r + i - centerR) < ROW &&
                                    0 <= (c + j - centerC) &&
                                    (c + j - centerC) < COL) {

                                if (data[i][j] == 1) {
                                    cells[r + i - centerR]
                                            [c + j - centerC].
                                            setFill(Cell.ALIVE_COLOR);

                                    if (!cells[r + i - centerR]
                                            [c + j - centerC].
                                            isAlive()) {
                                        liveCells++;
                                    }

                                    cells[r + i - centerR]
                                            [c + j - centerC].
                                            setAlive(true);

                                } else {
                                    cells[r + i - centerR]
                                            [c + j - centerC].
                                            setFill(Cell.DEAD_COLOR);

                                    if (cells[r + i - centerR]
                                            [c + j - centerC].
                                            isAlive()) {
                                        liveCells--;
                                    }

                                    cells[r + i - centerR]
                                            [c + j - centerC].
                                            setAlive(false);
                                }
                            }

                        }
                    }

                    selectedPatternCell.setPattern(null);
                }


            }
        });
    }

    // Author: J.W.
    private static void setCellMouseEnteredAction(Cell cell) {
        cell.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int[][] data;
                int r;
                int c;
                int centerR;
                int centerC;

                if (selectedPatternCell.getPattern() == null) {
                    cell.setFill(Cell.HOVER_COLOR);
                } else {

                    data = selectedPatternCell.getData();
                    r = cell.getRow();
                    c = cell.getCol();
                    centerR = (int) data.length / 2;
                    centerC = (int) data[0].length / 2;

                    for (int i = 0; i < data.length; i++) {

                        for (int j = 0; j < data[i].length; j++) {

                            if (0 <= (r + i - centerR) &&
                                    (r + i - centerR) < ROW &&
                                    0 <= c + j - centerC &&
                                    c + j - centerC < COL) {

                                if (data[i][j] == 1) {
                                    cells[r + i - centerR]
                                            [c + j - centerC].
                                            setFill(Cell.HOVER_COLOR);
                                } else {
                                    cells[r + i - centerR]
                                            [c + j - centerC].
                                            setFill(Cell.DEAD_COLOR);
                                }
                            }

                        }
                    }
                }

            }
        });
    }

    // Author: J.W.
    private static void setCellExitedAction(Cell cell) {
        cell.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int[][] data;
                int r;
                int c;
                int centerR;
                int centerC;

                if (selectedPatternCell.getPattern() == null) {
                    if (cell.isAlive()) {
                        cell.setFill(Cell.ALIVE_COLOR);
                    } else {
                        cell.setFill(Cell.DEAD_COLOR);
                    }
                } else {
                    data = selectedPatternCell.getData();
                    r = cell.getRow();
                    c = cell.getCol();
                    centerR = (int) data.length / 2;
                    centerC = (int) data[0].length / 2;

                    for (int i = 0; i < data.length; i++) {

                        for (int j = 0; j < data[i].length; j++) {

                            if (0 <= (r + i - centerR) &&
                                    (r + i - centerR) < ROW &&
                                    0 <= (c + j - centerC) &&
                                    (c + j - centerC) < COL) {

                                if (cells[r + i - centerR]
                                        [c + j - centerC].
                                        isAlive()) {
                                    cells[r + i - centerR]
                                            [c + j - centerC].
                                            setFill(Cell.ALIVE_COLOR);
                                } else {
                                    cells[r + i - centerR]
                                            [c + j - centerC].
                                            setFill(Cell.DEAD_COLOR);
                                }
                            }

                        }
                    }
                }
            }
        });
    }

    // Author: W.S.
    private static void setCellDragDetectedAction(Cell cell) {
        cell.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                /* allow any transfer mode */
                Dragboard db = cell.startDragAndDrop(TransferMode.ANY);

                //put a string on dragboard
                // - can't make it work without this content block.
                ClipboardContent content = new ClipboardContent();

                content.putString("");
                db.setContent(content);

                cell.setAlive(true);
                liveCells++;
                event.consume();
            }
        });
    }

    // Author: W.S.
    private static void setCellDragOverAction(Cell cell) {
        cell.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                //allow for both copying and moving, whatever user chooses
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                cell.setAlive(true);
                event.consume();
            }
        });
    }

    // Author: W.S.
    private static void setCellDragEnteredAction(Cell cell) {
        cell.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                cell.setAlive(true);
                liveCells++;
                event.consume();
            }
        });
    }

    // Author: W.S.
    private static void setCellDragDroppedAction(Cell cell) {
        cell.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                cell.setAlive(true);
                liveCells++;
                event.setDropCompleted(true);
                event.consume();
            }
        });
    }

    // Author: W.S.
    private static void setCellDragDoneAction(Cell cell) {
        cell.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture ended */
                if (event.getTransferMode() == TransferMode.MOVE) {
                    cell.setAlive(true);
                }

                event.consume();
            }
        });
    }

    // Author: W.S.
    private static void setStepButtonAction(Button btn) {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(liveCells>0){
                    prepare();
                    update();
                }
            }
        });
    }

    // Author: W.S.
    private static void setResetButtonAction(Button btn,
                                             Button btnStop) {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (int i = 0; i < ROW ; i++) {
                    for(int j = 0; j < COL; j++) {
                        cells[i][j].setAlive(false);
                    }
                }
                isReset = true;
                isPaused = false;

                if(!playing){
                    playing = false;
                }

                mediaPlayer.stop();
            }

        });

    }

    // Author: W.S.
    private static void setStopButtonAction(Button btn) {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (int i = 0; i < ROW ; i++) {
                    for(int j = 0; j < COL; j++) {
                        cells[i][j].setAlive(false);
                    }
                }
                isPaused = true;
                generation = 0;
                liveCells = 0;
                playing = false;
                isRandom = false;
                mediaPlayer.stop();
            }
        });
    }

    // Author: W.S.
    private static void setRandomButtonAction(Button btn){
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mediaPlayer.play();
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                initRandom();
                isRandom = true;
                playing = true;
                btn.setDisable(true);
            }
        });
    }

    // Author: W.S.
    private static void setStartButtonAction(Button btn) {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(playing){
                    btn.setText("Play");
                    isPaused = true;
                    playing = false;
                    mediaPlayer.pause();
                }
                else{
                    if(liveCells > 0){
                        mediaPlayer.play();
                        mediaPlayer.setCycleCount
                                (MediaPlayer.INDEFINITE);
                        playing = true;
                        isPaused = false;
                        btn.setText("Pause");
                    }
                }
            }
        });
    }

    // Author: W.S.
    private static void setUpAllButtons(Button btnStart,
                                        Button btnStop,
                                        Button btnRandom){
        btnStart.addEventHandler(ActionEvent.ACTION, (e)->{
            if(liveCells>0)
                btnRandom.setDisable(true);
        });

        btnRandom.addEventHandler(ActionEvent.ACTION, (e)->{
            btnStart.setText("Pause");
            playing = true;
        });

        btnStop.addEventHandler(ActionEvent.ACTION, (e)->{
            btnStart.setText("Play");
            btnRandom.setDisable(false);
        });
    }

    // Author: J.W.
    private static GridPane getGOLPane() {
        GridPane pane = new GridPane();
        pane.setHgap(1);
        pane.setVgap(1);
        pane.getStyleClass().add("gol-pane");
        return pane;
    }

    // Author: J.W.
    private static void setupPrimaryStage(Stage primaryStage,
                                          Scene scene) {
        // Create a scene and place it in the stage
        primaryStage.setResizable(false);
        primaryStage.setTitle("GameOfLife");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        primaryStage.show();
    }

    // Author: J.W.
    private static void prepare() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j].updateNextState
                        (getNumLiveNeighbors(cells, i, j));
            }
        }
    }

    // Author: J.W.
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

    // Author: J.W.
    private static void initRandom() {
        Random random = new Random();
        for (int i = 0; i < ROW * COL / 2; i++) {
            cells[random.nextInt(ROW)][random.nextInt(COL)].
                    setAlive(true);
            liveCells++;
        }
    }

    // Author: J.W.
    private static int getNumLiveNeighbors(Cell[][] cells,
                                           int i, int j) {
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
        c = (j + 1 < COL) ? j + 1 : 0;
        if (cells[r][c].isAlive()) numLiveNeighbors++;

        return numLiveNeighbors;
    }

    // Author: W.S.
    private static HBox getBottomPane() {
        HBox hBox = new HBox();
        Button btnStart;
        Button btnStop;
        Button btnReset;
        Button btnStep;
        Button btnRandom;

        hBox.getStyleClass().add("hbox");
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 10, 0, 10));

        btnStart = new Button();
        btnStart.setText("Start");
        btnStart.getStyleClass().add("button");

        btnStop = new Button();
        btnStop.setText("Clear");

        btnReset = new Button();
        btnReset.setText("Reset");

        btnStep = new Button();
        btnStep.setText("Step");

        btnRandom = new Button();
        btnRandom.setText("Random");

        hBox.getChildren().addAll(btnStart, btnStop, btnStep, btnRandom);

        setStartButtonAction(btnStart);
        setStopButtonAction(btnStop);
        setResetButtonAction(btnReset, btnStop);
        setStepButtonAction(btnStep);
        setRandomButtonAction(btnRandom);
        setUpAllButtons(btnStart, btnStop, btnRandom);

        return hBox;
    }

    // Author: J.W.
    public static void main(String[] args) {
        Application.launch(args);
    }

}
