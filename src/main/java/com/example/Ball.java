package com.example;

import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball {
    private double x, y, radius, dx, dy;

    public Ball(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.dx = 0;  // Ball won't move until launched
        this.dy = 0;
    }

    // Method to launch the ball with a random angle
    public void launch() {
        Random random = new Random();
        double angle;
        
        // Randomize between 45 to 60 degrees
        angle = Math.toRadians(random.nextInt(16) + 45);

        double speed = 7;  // Adjust the speed as needed
        dx = speed * Math.cos(angle);
        dy = -speed * Math.sin(angle);  // Move upwards
    }

    public void update() {
        x += dx;
        y += dy;

        // Ball bouncing off walls
        if (x <= 0 || x >= 800 - radius * 2) {
            dx *= -1;
        }
        if (y <= 0) {
            dy *= -1;
        }
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillOval(x, y, radius * 2, radius * 2);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}