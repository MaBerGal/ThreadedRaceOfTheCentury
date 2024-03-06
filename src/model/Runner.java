package model;

import java.util.Random;

// Represents a racing runner in the game.
public class Runner extends Thread {
    // Current x-coordinate of the runner on the track.
    private int x;

    // Current y-coordinate of the runner on the track.
    private int y;

    // Speed of the runner on the track.
    private int speed;

    // Number of laps completed by the runner.
    private int lapsCompleted;

    // Flag indicating whether the runner has finished the race.
    private boolean hasFinished;

    // Flag indicating whether the runner is currently jumping.
    private boolean isJumping;

    // Height of the jump when the runner is jumping.
    private int jumpHeight;

    // Original y-coordinate before jumping, used to reset after landing.
    private int originalY;

    // Duration of the jump animation in milliseconds.
    private int jumpDuration;

    // Runner's identification number.
    private int number;

    // Constructor to initialize Runner with starting position (x, y).
    public Runner(int x, int y) {
        this.x = x;
        this.y = y;

        // Initialize speed with a random value between 1 and 5.
        Random random = new Random();
        this.speed = random.nextInt(5) + 1;

        // Initialize jumping-related variables.
        this.isJumping = false;
        this.jumpHeight = 50;
        this.originalY = y;
        this.jumpDuration = 500;
    }

    // Getter methods for x, y, and speed.
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    // Getter method for the runner's number.
    public int getNumber() {
        return number;
    }

    // Setter method to set the runner's number.
    public void setNumber(int number) {
        this.number = number;
    }

    // Getter method for lapsCompleted
    public int getCompletedLaps() {
        return lapsCompleted;
    }

    // Setter method to set the number of laps completed by the runner.
    public void setLapsCompleted(int lapsCompleted) {
        this.lapsCompleted = lapsCompleted;
    }

    // Increment the number of laps completed by the runner.
    public void incrementLaps() {
        lapsCompleted++;
    }

    // Setter methods for x, y, and speed.
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // Set the speed of the runner, ensuring it's non-negative.
    public void setSpeed(int speed) {
        if (speed >= 0) {
            this.speed = speed;
        }
    }

    // Setter method to set the jumping state of the runner.
    public void setJumping(boolean jumping) {
        this.isJumping = jumping;
    }

    // Method to move the runner on the track based on laps completed and jumping state.
    public void mover() {
        if (lapsCompleted < 8) {
            if (isJumping) {
                // Implement jump motion
                y = originalY - (int) (jumpHeight * Math.sin(Math.PI * (System.currentTimeMillis() % jumpDuration) / jumpDuration));
                x += speed;
            } else {
                // Normal horizontal movement.
                x += speed;
                y = originalY;  // Reset y-coordinate after landing.
            }
        } else if (!hasFinished) {
            // Move the runner to the finish line and mark it as finished.
            setX(610);
            hasFinished = true;
        }
    }

    // The main run method that continuously moves the runner and sleeps for 20 milliseconds.
    @Override
    public void run() {
        while (true) {
            mover();
            try {
                // Sleep for 20 milliseconds to control the update rate.
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Setter method to set the finish state of the runner.
    public void setHasFinished(boolean b) {
        this.hasFinished = b;
    }
}
