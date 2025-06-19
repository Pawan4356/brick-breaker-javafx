package com.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Paddle {
    private double x, y, width, height;
    private final double canvasWidth;  // Added to store the canvas width for boundary checking

    public Paddle(double x, double y, double width, double height, double canvasWidth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.canvasWidth = canvasWidth;  // Initialize with the canvas width
    }

    public void moveLeft() {
        if (x > 0) {  // Check if the paddle is within the left boundary
            x -= 7;
        }
    }

    public void moveRight() {
        if (x + width < canvasWidth) {  // Check if the paddle is within the right boundary
            x += 7;
        }
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);  // Set fill color to black
        gc.fillRect(x, y, width, height);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
