package com.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Home extends Application {

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(800, 600);  // Game window size

        StackPane root = new StackPane();
        Scene scene = new Scene(root);

        primaryStage.getIcons().add(new Image(getClass().getResource("/Icon.png").toExternalForm()));

        // Load and set the background image
        Image bg = new Image(getClass().getResource("/Background.jpg").toExternalForm());
        ImageView bgv = new ImageView(bg);
        bgv.setFitWidth(800);  // Match the canvas width
        bgv.setFitHeight(600); // Match the canvas height

        // Load the top image
        Image topImage = new Image(getClass().getResource("/Heading.png").toExternalForm());
        ImageView topImageView = new ImageView(topImage);
        topImageView.setFitWidth(200);
        topImageView.setPreserveRatio(true);
        topImageView.setTranslateY(-280);

        // Create a title text
        Text titleText = new Text("Select Difficulty");
        titleText.setFont(new Font("Impact", 36));
        titleText.setStyle("-fx-fill: black;");

        // Create buttons for difficulty levels
        Button easyButton = new Button("Easy");
        Button mediumButton = new Button("Medium");
        Button hardButton = new Button("Hard");

        // Style the buttons
        styleButton(easyButton);
        styleButton(mediumButton);
        styleButton(hardButton);

        // Add event handling for each button to start the game with the selected difficulty
        easyButton.setOnAction(event -> startGame(primaryStage, App.Difficulty.EASY));
        mediumButton.setOnAction(event -> startGame(primaryStage, App.Difficulty.MEDIUM));
        hardButton.setOnAction(event -> startGame(primaryStage, App.Difficulty.HARD));

        // VBox to align the buttons vertically
        VBox buttonBox = new VBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(titleText, easyButton, mediumButton, hardButton);

        // Add the background, top image, and button layout to the root
        root.getChildren().addAll(bgv, topImageView, buttonBox);

        // Key listener for Escape to exit
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                Platform.exit();
            }
        });

        // Set up the home stage
        primaryStage.setResizable(false);
        primaryStage.setTitle("Brick Breaker Home");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Helper method to start the game with the selected difficulty
    private void startGame(Stage primaryStage, App.Difficulty difficulty) {
        App gameApp = new App();  // Create a new instance of App
        gameApp.difficulty = difficulty;  // Set the difficulty
        gameApp.setupGame(primaryStage);  // Call the method to set up the game
    }
    

    // Helper method to style buttons
    private void styleButton(Button button) {
        button.setStyle("-fx-font-size: 18px; -fx-font-family: 'Impact'; -fx-background-color: transparent; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 18px; -fx-font-family: 'Impact'; -fx-background-color: rgba(0, 0, 0, 0.3); -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 18px; -fx-font-family: 'Impact'; -fx-background-color: transparent; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2px;"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
