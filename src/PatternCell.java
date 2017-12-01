/**
 * Program Name: PatternCell.java
 * Discussion:   PatternCell
 * Written By:   Junhong Wang
 * Date:         2017/11/30
 */

import javafx.scene.layout.GridPane;

public class PatternCell extends GridPane {

    private int[][] data = null;
    private Pattern pattern = null;
    private double cellSize;

    public PatternCell(Pattern pattern, double cellSize) {
        setHgap(1);
        setVgap(1);
        setStyle("-fx-background-color: #000000;");
        this.pattern = pattern;
        this.cellSize = cellSize;
        setData(pattern);
    }

    public PatternCell(PatternCell patternCell) {
        setHgap(1);
        setVgap(1);
        setStyle("-fx-background-color: #000000;");
        this.pattern = patternCell.pattern;
        this.cellSize = patternCell.cellSize;
        setData(patternCell.pattern);
    }

    private void init(double cellSize) {
        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    Cell cell = new Cell(false, false, cellSize);
                    if (data[i][j] == 1) {
                        cell.setAlive(true);
                    }
                    add(cell, j, i);
                }
            }
        }

    }

    public int[][] getData() {
        return data;
    }

    public void setData(Pattern pattern) {
        if (pattern != null) {
            switch (pattern) {
                case BLOCK:
                    data = new int[][] {
                            {0, 0, 0, 0},
                            {0, 1, 1, 0},
                            {0, 1, 1, 0},
                            {0, 0, 0, 0}
                    };
                    break;
                case BEEHIVE:
                    data = new int[][] {
                            {0, 0, 0, 0, 0, 0},
                            {0, 0, 1, 1, 0, 0},
                            {0, 1, 0, 0, 1, 0},
                            {0, 0, 1, 1, 0, 0},
                            {0, 0, 0, 0, 0, 0}
                    };
                    break;
                case LOAF:
                    data = new int[][] {
                            {0, 0, 0, 0, 0, 0},
                            {0, 0, 1, 1, 0, 0},
                            {0, 1, 0, 0, 1, 0},
                            {0, 0, 1, 0, 1, 0},
                            {0, 0, 0, 1, 0, 0},
                            {0, 0, 0, 0, 0, 0}
                    };
                    break;
                case BOAT:
                    data = new int[][] {
                            {0, 0, 0, 0, 0},
                            {0, 1, 1, 0, 0},
                            {0, 1, 0, 1, 0},
                            {0, 0, 1, 0, 0},
                            {0, 0, 0, 0, 0}
                    };
                    break;
                case TUB:
                    data = new int[][] {
                            {0, 0, 0, 0, 0},
                            {0, 0, 1, 0, 0},
                            {0, 1, 0, 1, 0},
                            {0, 0, 1, 0, 0},
                            {0, 0, 0, 0, 0}
                    };
                    break;
                case BLINKER:
                    data = new int[][] {
                            {0, 0, 0, 0, 0},
                            {0, 0, 1, 0, 0},
                            {0, 0, 1, 0, 0},
                            {0, 0, 1, 0, 0},
                            {0, 0, 0, 0, 0}
                    };
                    break;
                case TOAD:
                    data = new int[][] {
                            {0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0},
                            {0, 0, 1, 1, 1, 0},
                            {0, 1, 1, 1, 0, 0},
                            {0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0}
                    };
                    break;
                case BEACON:
                    data = new int[][] {
                            {0, 0, 0, 0, 0, 0},
                            {0, 1, 1, 0, 0, 0},
                            {0, 1, 0, 0, 0, 0},
                            {0, 0, 0, 0, 1, 0},
                            {0, 0, 0, 1, 1, 0},
                            {0, 0, 0, 0, 0, 0}
                    };
                    break;
                case PULSAR:
                    data = new int[][] {
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    0, 0, 0},
                            {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0,
                                    0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    0, 0, 0},
                            {0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0,
                                    1, 0, 0},
                            {0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0,
                                    1, 0, 0},
                            {0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0,
                                    1, 0, 0},
                            {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0,
                                    0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    0, 0, 0},
                            {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0,
                                    0, 0, 0},
                            {0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0,
                                    1, 0, 0},
                            {0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0,
                                    1, 0, 0},
                            {0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0,
                                    1, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    0, 0, 0},
                            {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0,
                                    0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    0, 0, 0}
                    };
                    break;
                case PENTADECATHLON:
                    data = new int[][] {
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
                            {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                            {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                            {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
                            {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                            {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                            {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
                    };
                    break;
                case GLIDER:
                    data = new int[][] {
                            {0, 0, 0, 0, 0},
                            {0, 0, 1, 0, 0},
                            {0, 0, 0, 1, 0},
                            {0, 1, 1, 1, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;
                case LIGHTWEIGHT_SPACESHIP:
                    data = new int[][] {
                            {0, 0, 0, 0, 0, 0, 0},
                            {0, 1, 0, 0, 1, 0, 0},
                            {0, 0, 0, 0, 0, 1, 0},
                            {0, 1, 0, 0, 0, 1, 0},
                            {0, 0, 1, 1, 1, 1, 0},
                            {0, 0, 0, 0, 0, 0, 0},
                    };
                    break;
                default:
                    data = new int[][] {};

            }
        } else {
            data = null;
        }

        init(cellSize);

    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
        setData(pattern);
    }

    public Pattern getPattern() {
        return pattern;
    }

}
