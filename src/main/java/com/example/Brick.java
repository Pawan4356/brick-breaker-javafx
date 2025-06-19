package com.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Brick {
    private double x, y, width, height;
    private boolean isBroken;
    private int health;  // Health defines how many hits it can take
    private Color color;  // Color of the brick

    public enum BrickType {
        REGULAR, STRONG, UNBREAKABLE
    }

    public Brick(double x, double y, double width, double height, BrickType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isBroken = false; // Initially, the brick is not broken
        setBrickType(type);  // Initialize brick with type
    }

    public void draw(GraphicsContext gc) {
        if (!isBroken) { // Draw only if the brick is not broken
            gc.setFill(color);
            gc.fillRect(x, y, width, height);
            // Add border
            gc.setStroke(Color.BLACK);  // Black border for all bricks
            gc.strokeRect(x, y, width, height);
        }
    }

    public boolean checkCollision(Ball ball) {
        // Check for collision with the ball only if the brick is not broken
        if (!isBroken && ball.getX() + ball.getRadius() >= x && ball.getX() <= x + width &&
            ball.getY() + ball.getRadius() >= y && ball.getY() <= y + height) {
            
            // Reduce health when a collision is detected
            health--;
            
            // If the brick is strong and has 1 health left, change its color to red (indicating it has become a regular brick)
            if (health == 1 && color == Color.BLUE) {
                color = Color.RED;  // Change to regular brick color
            }
            
            // If the health reaches 0, the brick is broken
            if (health <= 0) {
                isBroken = true;
            }
            
            return true;  // Collision detected
        }
        return false;  // No collision
    }
    
    // Set the brick type and initialize its attributes based on type
    private void setBrickType(BrickType type) {
        switch (type) {
            case REGULAR:
                this.color = Color.RED;
                this.health = 1; // Regular brick can take 1 hit
                break;
            case STRONG:
                this.color = Color.BLUE;
                this.health = 2; // Strong brick can take 2 hits
                break;
            case UNBREAKABLE:
                this.color = Color.GRAY;
                this.health = Integer.MAX_VALUE;  // Unbreakable brick cannot be destroyed
                break;
        }
    }

    public boolean isBroken() {
        return isBroken; // Return whether the brick is broken
    }
}
