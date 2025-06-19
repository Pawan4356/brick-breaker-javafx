package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    // Enum to define game difficulty settings
    public enum Difficulty {
        EASY(150, 3, 10),
        MEDIUM(125, 5, 15),
        HARD(100, 7, 20);

        public final int paddleWidth;
        public final int minUnbreakable;
        public final int minStrong;

        Difficulty(int paddleWidth, int minUnbreakable, int minStrong) {
            this.paddleWidth = paddleWidth;
            this.minUnbreakable = minUnbreakable;
            this.minStrong = minStrong;
        }
    }

    Difficulty difficulty;
    private Ball ball;
    private Paddle paddle;
    private List<Brick> bricks;

    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean gameOver = false;
    private boolean gameStarted = false;
    private boolean ballLaunched = false;

    private AnimationTimer gameLoop;

    @Override
    public void start(Stage primaryStage) {
        // Prompt for difficulty
        difficulty = selectDifficulty();

        if (difficulty == null) {
            Platform.exit();
            return;
        }

        // Call a separate method to set up the game
        setupGame(primaryStage);
    }

    public void setupGame(Stage primaryStage) {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane();
        Scene scene = new Scene(root);

        // Set up background and top image
        Image bg = new Image("file:D:\\Pawan\\Brick Breaker\\demomavinfx\\src\\main\\resources\\Background.jpg");
        ImageView bgv = new ImageView(bg);
        bgv.setFitWidth(800);
        bgv.setFitHeight(600);

        Image topImage = new Image("file:D:\\Pawan\\Brick Breaker\\demomavinfx\\src\\main\\resources\\Heading.png");
        ImageView topImageView = new ImageView(topImage);
        topImageView.setFitWidth(200);
        topImageView.setPreserveRatio(true);
        topImageView.setTranslateY(-280);

        initializeGame();

        canvas.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    moveLeft = true;
                    break;
                case RIGHT:
                    moveRight = true;
                    break;
                case SPACE:
                    if (!gameStarted) {
                        gameStarted = true;
                        ballLaunched = true;  // Ensure the ball is marked as launched
                        ball.launch();        // Launch the ball
                    } else if (gameOver) {
                        restartGame(gc);
                    }
                    break;
                case ESCAPE:
                    Platform.exit();
                    break;
                default:
                    break;
            }
        });

        canvas.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT:
                    moveLeft = false;
                    break;
                case RIGHT:
                    moveRight = false;
                    break;
                default:
                    break;
            }
        });

        // "Home" button to return to home screen
        Button homeButton = new Button("Home");
        homeButton.setStyle("-fx-font-family: 'Impact'; -fx-font-size: 24px; -fx-text-fill: black; -fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 2px; -fx-padding: 3px 10px;");
        homeButton.setOnMouseEntered(e -> homeButton.setStyle("-fx-font-family: 'Impact'; -fx-font-size: 24px; -fx-background-color: rgba(0, 0, 0, 0.1); -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2px; -fx-padding: 5px 10px;"));
        homeButton.setOnMouseExited(e -> homeButton.setStyle("-fx-font-family: 'Impact'; -fx-font-size: 24px; -fx-background-color: transparent; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2px; -fx-padding: 5px 10px;"));
        homeButton.setOnAction(e -> {
            Home homeScreen = new Home();
            try {
                homeScreen.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        homeButton.setTranslateX(-350);
        homeButton.setTranslateY(-277);
        homeButton.setFocusTraversable(false);

        root.getChildren().addAll(bgv, topImageView, canvas, homeButton);

        startGameLoop(gc);

        primaryStage.setResizable(false);
        primaryStage.setTitle("Brick Breaker Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Request focus on the canvas to capture key events
        canvas.requestFocus();
    }

    private Difficulty selectDifficulty() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Select Difficulty");
        alert.setHeaderText("Choose a difficulty level:");
        alert.getButtonTypes().setAll(ButtonType.CANCEL, new ButtonType("Easy"), new ButtonType("Medium"), new ButtonType("Hard"));

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
        if (result == ButtonType.CANCEL) return null;

        switch (result.getText()) {
            case "Easy":
                return Difficulty.EASY;
            case "Medium":
                return Difficulty.MEDIUM;
            case "Hard":
                return Difficulty.HARD;
            default:
                return null;        
        }
    }

    private void initializeGame() {
        paddle = new Paddle(350, 550, difficulty.paddleWidth, 5, 800);
        ball = new Ball(paddle.getX() + paddle.getWidth() / 2 - 10, paddle.getY() - 20, 10);
    
        bricks = new ArrayList<>();
        List<int[]> unbreakablePositions = new ArrayList<>();
    
        // Define unbreakable brick positions based on difficulty
        switch (difficulty) {
            case EASY:
                unbreakablePositions.add(new int[]{2, 2});
                unbreakablePositions.add(new int[]{2, 7});
                break;
            case MEDIUM:
                unbreakablePositions.add(new int[]{1, 1});
                unbreakablePositions.add(new int[]{3, 3});
                unbreakablePositions.add(new int[]{1, 8});
                unbreakablePositions.add(new int[]{3, 6});
                break;
            case HARD:
                unbreakablePositions.add(new int[]{2, 1});
                unbreakablePositions.add(new int[]{2, 8});
                unbreakablePositions.add(new int[]{1, 3});
                unbreakablePositions.add(new int[]{1, 6});
                unbreakablePositions.add(new int[]{2, 3});
                unbreakablePositions.add(new int[]{2, 6});
                unbreakablePositions.add(new int[]{3, 3});
                unbreakablePositions.add(new int[]{3, 4});
                unbreakablePositions.add(new int[]{3, 5});
                unbreakablePositions.add(new int[]{3, 6});
                break;
        }
    
        // Add bricks to the grid, with unbreakable bricks in specified positions
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                Brick.BrickType type;
    
                // Check if the current position is an unbreakable brick position
                if (isUnbreakablePosition(i, j, unbreakablePositions)) {
                    type = Brick.BrickType.UNBREAKABLE;
                } else {
                    // Randomly assign other types of bricks (e.g., strong or regular)
                    type = determineBrickType();
                }
    
                bricks.add(new Brick(80 * j + 5, 30 * i + 50, 70, 20, type));
            }
        }
    
        gameOver = false;
        ballLaunched = false;
    }

    private boolean isUnbreakablePosition(int row, int col, List<int[]> positions) {
        for (int[] pos : positions) {
            if (pos[0] == row && pos[1] == col) {
                return true;
            }
        }
        return false;
    }
    
    // Helper method to assign random brick types for other bricks
    private Brick.BrickType determineBrickType() {
        Random random = new Random();
        int randomType = random.nextInt(3);
    
        switch (randomType) {
            case 1:
                return Brick.BrickType.STRONG;
            default:
                return Brick.BrickType.REGULAR;
        }
    }

    private void startGameLoop(GraphicsContext gc) {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameOver) {
                    gc.clearRect(0, 0, 800, 600);
    
                    if (!ballLaunched) {
                        // Ensure the ball is positioned correctly above the paddle
                        ball.setPosition(paddle.getX() + paddle.getWidth() / 2 - ball.getRadius(), paddle.getY() - ball.getRadius() * 2);
                    } else {
                        ball.update(); // Update the ball position if launched
                    }
    
                    ball.draw(gc);
    
                    if (moveLeft) {
                        paddle.moveLeft();
                    }
                    if (moveRight) {
                        paddle.moveRight();
                    }
                    paddle.draw(gc);
    
                    // Check for collisions with bricks
                    for (Brick brick : bricks) {
                        if (!brick.isBroken() && brick.checkCollision(ball)) {
                            ball.setDy(ball.getDy() * -1);
                            break; // Exit the loop after collision
                        }
                        brick.draw(gc);
                    }
    
                    // Paddle collision
                    if (ball.getY() + ball.getRadius() * 2 >= paddle.getY() &&
                        ball.getX() + ball.getRadius() >= paddle.getX() &&
                        ball.getX() <= paddle.getX() + paddle.getWidth()) {
                        ball.setDy(ball.getDy() * -1);
                    }
    
                    // Game over conditions
                    if (ball.getY() >= 600) {
                        gameOver = true;
                        gameLoop.stop();
                        showGameOverAlert();
                    }
                }
            }
        };
        gameLoop.start();
    }
    

    private void restartGame(GraphicsContext gc) {
        gameOver = false;
        gameStarted = false; // Reset gameStarted flag
        ballLaunched = false; // Reset ballLaunched flag
        initializeGame();
        startGameLoop(gc); // Restart the game loop
    }
    

    public void showGameOverAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("You lost!");
        alert.show();  // Non-blocking dialog
    }

    public static void main(String[] args) {
        launch(args);
    }
}
