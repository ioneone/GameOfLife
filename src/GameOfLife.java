import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;

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
    //
    private static final int SPEED = 400; // 1 update per speed (in millisecond)
    private static Cell[][] cells;
    private static boolean isContinuous = true;
    private static boolean playing = false;
    private static boolean isReset = false;
    private static boolean isStopped = false;

    private static boolean isSelected = false;
    private static Pattern selectedPattern = null;
    private static PatternCell selectedPatternCell = new PatternCell();


    private static Label generationLabel;
    private static Label selectedPatternLabel;
//    int r=0, c=0;
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
                    generationLabel.setText(Integer.toString(generation));
                    System.out.println(selectedPattern);
                    if (selectedPattern == null) {
                        selectedPatternLabel.setText("NULL");
                        selectedPatternLabel.setGraphic(null);
                    } else {
                        selectedPatternLabel.setText(selectedPattern.name());
                        selectedPatternLabel.setGraphic(selectedPatternCell);
                    }
                });


            }
        }, 0, SPEED); // 0.2 sec
    }


    public void init(Stage primaryStage) {

        Rectangle2D rec = Screen.getPrimary().getVisualBounds();

        System.out.println(rec.toString());

        GridPane pane = getGOLPane();

        cells = new Cell[ROW][COL];
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                Cell cell = new Cell(false, false, CELL_SIZE, r, c);
                cells[r][c] = cell;
                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (selectedPattern == null) {
                            cell.setAlive(true);
                            cell.setFill(Color.YELLOW); //.setAlive(true);
                        } else {
                            int[][] data = selectedPatternCell.getData();
                            int r = cell.getRow();
                            int c = cell.getCol();
                            for (int i = 0; i < data.length; i++) {
                                for (int j = 0; j < data[i].length; j++) {
                                    if (r + i < ROW && c + j < COL) {
                                        if (data[i][j] == 1) {
                                            cells[r+i][c+j].setFill(Cell.ALIVE_COLOR);
                                            cells[r+i][c+j].setAlive(true);
                                            selectedPatternCell = null;
                                            selectedPattern = null;
                                        } else {
                                            cells[r+i][c+j].setFill(Cell.DEAD_COLOR);
                                            cells[r+i][c+j].setAlive(false);
                                            selectedPatternCell = null;
                                            selectedPattern = null;
                                        }
                                    }

                                }
                            }
                        }


                    }
                });
                cell.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (selectedPattern == null) {
                            cell.setFill(Cell.HOVER_COLOR);
                        } else {
                            int[][] data = selectedPatternCell.getData();
                            int r = cell.getRow();
                            int c = cell.getCol();
                            for (int i = 0; i < data.length; i++) {
                                for (int j = 0; j < data[i].length; j++) {
                                    if (r + i < ROW && c + j < COL) {
                                        if (data[i][j] == 1) {
                                            cells[r+i][c+j].setFill(Cell.HOVER_COLOR);
                                        } else {
                                            cells[r+i][c+j].setFill(Cell.DEAD_COLOR);
                                        }
                                    }

                                }
                            }
                        }

                    }
                });

                cell.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (selectedPattern == null) {
                            if (cell.isAlive()) {
                                cell.setFill(Cell.ALIVE_COLOR);
                            } else {
                                cell.setFill(Cell.DEAD_COLOR);
                            }
                        } else {
                            int[][] data = selectedPatternCell.getData();
                            int r = cell.getRow();
                            int c = cell.getCol();
                            for (int i = 0; i < data.length; i++) {
                                for (int j = 0; j < data[i].length; j++) {
                                    if (r + i < ROW && c + j < COL) {
                                        if (cells[r+i][c+j].isAlive()) {
                                            cells[r+i][c+j].setFill(Cell.ALIVE_COLOR);
                                        } else {
                                            cells[r+i][c+j].setFill(Cell.DEAD_COLOR);
                                        }
                                    }

                                }
                            }
                        }

                    }
                });
                /*
                cell.setOnDragEntered(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {

                    }
                });
                cell.setOnDragDropped(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        //cell.setFill(Color.YELLOW);
                    }
                });
                cell.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        //cell.setFill(Color.YELLOW);
                    }
                });
                cell.setOnDragDone(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        if(event.getTransferMode() == TransferMode.MOVE){
                            cell.setFill(Color.YELLOW);
                        }
                    }
                });

                        (new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        cell.setFill(Color.YELLOW); //.setAlive(true);
                    }
                });*/
                pane.add(cell, c, r);
                /*
                cells[r][c].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        cells[r][c].setFill(Color.YELLOW);
                    }
                });*/


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
        setResetButtonAction(btnReset, btnStop);
        setStepButtonAction(btnStep);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10,10));
        root.setCenter(pane);
        root.setBottom(hbButtons);

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
                    if (selectedPattern != null && selectedPattern == pattern) {
                        isSelected = false;
                        selectedPattern = null;
                        selectedPatternCell = null;
                    } else {
                        isSelected = true;
                        selectedPattern = pattern;
                        selectedPatternCell = new PatternCell(pattern, CELL_SIZE);
                    }
                    System.out.println(selectedPattern);
                }
            });

            GridPane.setHalignment(lbl, HPos.CENTER);
            gridPane.add(lbl, 0, i++);

        }

//        for (Node node : gridPane.getChildren()) {
//            node.setOnMouseEntered(e -> gridPane.getChildren().forEach(c -> {
//                Integer targetIndex = GridPane.getRowIndex(node);
//                if (GridPane.getRowIndex(c) == targetIndex) {
//                    c.setStyle("-fx-background-color:#f9f3c5;");
//                }
//            }));
//            node.setOnMouseExited(e -> gridPane.getChildren().forEach(c -> {
//                Integer targetIndex = GridPane.getRowIndex(node);
//                if (GridPane.getRowIndex(c) == targetIndex) {
//                    c.setStyle("-fx-background-color:#ffffff;");
//                }
//            }));
//        }

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setPrefHeight(CELL_SIZE*ROW);
        scrollPane.setPrefWidth(230);
        scrollPane.setPadding(new Insets(10, 10, 10, 10));
        root.setRight(scrollPane);

        GridPane grid = new GridPane();
        grid.add(new Label("Generation"), 0, 0);
        generationLabel = new Label(Integer.toString(generation));
        grid.add(generationLabel, 0, 1);
        grid.add(new Label("Selected Pattern"), 0, 2);
        selectedPatternLabel = new Label("NULL", selectedPatternCell);
        selectedPatternLabel.setContentDisplay(ContentDisplay.TOP);
        grid.add(selectedPatternLabel, 0, 3);
        grid.setPrefWidth(230);
        root.setLeft(grid);

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
        if(liveCells==0){
            cells[50][50].setAlive(true);
            cells[50][51].setAlive(true);
            cells[50][52].setAlive(true);
            cells[49][52].setAlive(true);
            cells[48][51].setAlive(true);
            liveCells = 5;
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

    public static void main(String[] args) {
        Application.launch(args);
    }

}
