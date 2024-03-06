package model;

// Represents a Goal object that moves horizontally and bounces back.
public class Goal extends Thread {

    // Coordinates of the Goal.
    private int x;
    private int y;

    // Initial X-coordinate for bouncing back.
    private int initialX;

    // Maximum X-coordinate for bouncing back.
    private int maxX;

    // Speed of the Goal's movement.
    private int speed;

    // Flag to indicate the direction of movement.
    private boolean movingRight;

    // Constructor for initializing Goal object.
    public Goal(int x, int y, int maxX, int speed) {
        this.x = x;
        this.y = y;
        this.initialX = x;
        this.maxX = maxX;
        this.speed = speed;
        // Start by moving to the right.
        this.movingRight = true;
    }

    // Getter method for retrieving the X-coordinate.
    public int getX() {
        return x;
    }

    // Getter method for retrieving the Y-coordinate.
    public int getY() {
        return y;
    }

    // Override the run method for thread execution.
    @Override
    public void run() {
        // Continuous loop for moving the Goal.
        while (true) {
            // Move the Goal.
            move();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Move the Goal towards the right and bounce back.
    private void move() {
        if (movingRight) {
            // Move right until reaching the maximum X-coordinate.
            if (x < maxX) {
                x += speed;
            } else {
                movingRight = false;  // Change direction when reaching maxX.
            }
        } else {
            // Move left until reaching the initial X-coordinate.
            if (x > initialX) {
                x -= speed;
            } else {
                // Change direction when reaching initialX.
                movingRight = true;
            }
        }
    }

    // Update the goalImage position based on Goal's x coordinate.
    public int getGoalX() {
        return x - 20;
    }
}
