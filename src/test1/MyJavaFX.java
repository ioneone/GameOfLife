package test1;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Created by one on 9/15/17.
 */
public class MyJavaFX extends Application {

    private static final double TILE_SIZE = 5;
    private static Rectangle[][] grid = new Rectangle[100][100];

    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane pane = new GridPane();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Rectangle(TILE_SIZE, TILE_SIZE);
                pane.add(grid[i][j], i, j);
            }
        }

        grid[3][3].setFill(Color.RED);

        // Place nodes in the pane


        // Create a scene and place it in the stage
        Scene scene = new Scene(pane);
        primaryStage.setTitle("ShowGridPane"); // Set the stage title primaryStage.setScene(scene); // Place the scene in the stage primaryStage.show(); // Display the stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
