package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

	public class Game extends Application {
	    private Board game = new Board();
	    private static final int TILE_SIZE = 110;
	    private static final int BOARD_SIZE = 4;
	    private static final int BOARD_OFFSET_X = 200;
	    private static final int BOARD_OFFSET_Y = 130;
	    private static final int BOARD_WIDTH = BOARD_SIZE * TILE_SIZE;
	    private static final int BOARD_HEIGHT = BOARD_SIZE * TILE_SIZE;
	    private static final int SCORE_OFFSET_X = 130;
	    private static final int SCORE_OFFSET_Y = 90;
	    private int bestScore = 0;
	    
	    StackPane root = new StackPane();
        Canvas canvas = new Canvas(1000, 800);

	    @Override
	    public void start(Stage primaryStage) {
	        
	        root.getChildren().add(canvas);
	        Scene scene = new Scene(root);

	        scene.setOnKeyPressed(event -> {
	            KeyCode code = event.getCode();
	            if (code == KeyCode.W || code == KeyCode.UP) {
	                game.up();
	                game.spawn();
	            } else if (code == KeyCode.S || code == KeyCode.DOWN) {
	                game.down();
	                game.spawn();
	            } else if (code == KeyCode.A || code == KeyCode.LEFT) {
	                game.left();
	                game.spawn();
	            } else if (code == KeyCode.D || code == KeyCode.RIGHT) {
	                game.right();
	                game.spawn();
	            } else if (code == KeyCode.ENTER) {
	            	resetGame();
	            }

	            drawGame(canvas.getGraphicsContext2D());
	            if (game.gameOver() || game.blackOut()) {
	                drawGameOver(canvas.getGraphicsContext2D());
	            }
	        });

	        primaryStage.setTitle("2048Game");
	        primaryStage.setScene(scene);
	        primaryStage.setResizable(true);
	        primaryStage.setFullScreen(true);
	        primaryStage.setFullScreenExitHint("Press q to exit full screen");
	        primaryStage.setFullScreenExitKeyCombination(KeyCombination.valueOf("q"));
	        primaryStage.show();

	        game.spawn();
	        game.spawn();
	        drawGame(canvas.getGraphicsContext2D());
	    }

	    private void drawGame(GraphicsContext gc) {
	        gc.clearRect(0, 0, 600, 1000);
	        gc.setFill(Color.BLACK);
	        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
	        gc.fillText("Welcome to 2048", 300, 40);
	        gc.fillText("Score: " + game.getScore(), SCORE_OFFSET_X, SCORE_OFFSET_Y);
	        gc.fillText("Highest Tile: " + game.getHighTile(), 380, SCORE_OFFSET_Y);
	        gc.fillText("Press 'Enter' to Start", 260, 650);
	        gc.fillText("Use 'wasd' or Arrow Keys to move", 140, 700);

	        for (int row = 0; row < BOARD_SIZE; row++) {
	            for (int col = 0; col < BOARD_SIZE; col++) {
	                drawTile(gc, game.board[row][col], BOARD_OFFSET_X + col * TILE_SIZE, BOARD_OFFSET_Y + row * TILE_SIZE);
	            }
	        }
	        gc.fillText("Best Score: " + bestScore, 700, SCORE_OFFSET_Y);
	    }
	    

	    private void drawTile(GraphicsContext gc, Tile tile, int x, int y) {
	        int tileValue = tile.getValue();
	        int length = String.valueOf(tileValue).length();
	        gc.setFill(Color.LIGHTGRAY);
	        gc.fillRect(x, y, TILE_SIZE, TILE_SIZE);
	        gc.setFill(Color.BLACK);

	        if (tileValue > 0) {
	            gc.setFill(tile.getColor());
	            gc.fillRect(x, y, TILE_SIZE, TILE_SIZE);
	            gc.setFill(Color.BLACK);
	            gc.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
	            gc.fillText("" + tileValue, x + TILE_SIZE / 2 - 9 * length, y + TILE_SIZE / 2 + 8);
	        }
	    }

	    private void drawGameOver(GraphicsContext gc) {
	        gc.setFill(Color.GRAY);
	        gc.fillRect(BOARD_OFFSET_X, BOARD_OFFSET_Y, BOARD_WIDTH, BOARD_HEIGHT);

	        for (int row = 0; row < BOARD_SIZE; row++) {
	            for (int col = 0; col < BOARD_SIZE; col++) {
	                gc.setFill(Color.RED);
	                gc.fillRoundRect(BOARD_OFFSET_X + col * TILE_SIZE, BOARD_OFFSET_Y + row * TILE_SIZE, TILE_SIZE, TILE_SIZE, 5, 5);
	                gc.setFill(Color.BLACK);
	                gc.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	                
	                
	            }
	        }
	        gc.fillText("GAME", BOARD_OFFSET_X + 1 * TILE_SIZE + 10, BOARD_OFFSET_Y + 1 * TILE_SIZE + 25);
            gc.fillText("OVER", BOARD_OFFSET_X + 2 * TILE_SIZE + 10, BOARD_OFFSET_Y + 2 * TILE_SIZE + 45);
	    
            if (game.gameOver()) {
                if (game.getScore() > bestScore) {
                    bestScore = game.getScore();
                }
            }
	    Button resetButton = new Button("Reset");
        resetButton.setOnAction(event -> resetGame());
        resetButton.setTextFill(Color.BLACK);
        resetButton.setFont(Font.font("Helvetica"));
        resetButton.applyCss();
        resetButton.setId("button");
        StackPane.setAlignment(resetButton,Pos.BOTTOM_CENTER);
      
        StackPane.setMargin(resetButton, new Insets(0, 0, 20, 0));
        root.getChildren().add(resetButton);
    }
	    
//	    Button Undo = new Button("Undo");
//	    undo.setOnAction(event ->undoGame());

    // Method to reset the game
    private void resetGame() {
        game = new Board();
        game.spawn();
        game.spawn();
        drawGame(canvas.getGraphicsContext2D());
    }
    

	    public static void main(String[] args) {
	        launch(args);
	    }
	
}
