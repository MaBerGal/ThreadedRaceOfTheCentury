
package model;


import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

// Represents the image handling for the runner in the game.
public class RunnerImage implements Runnable {
    // Static BufferedImage to hold the current runner image.
    private static BufferedImage currentRunnerImage;

    // Variables to track the current frame and scaled dimensions of the runner image.
    private int currentFrame;
    private int scaledWidth;
    private int scaledHeight;

    // Constructor to initialize the RunnerImage with scaled dimensions.
    public RunnerImage(int scaledWidth, int scaledHeight) {
        this.scaledWidth = scaledWidth;
        this.scaledHeight = scaledHeight;
    }

    // Static method to get the current runner image.
    public static BufferedImage getCurrentRunnerImage() {
        return currentRunnerImage;
    }

    // The main run method that continuously updates the runner image.
    @Override
    public void run() {
        while (true) {
            updateImage();
            try {
                // Sleep for 20 milliseconds to control the update rate.
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to update the runner image based on the current frame.
    private void updateImage() {
        // Cycle through 16 frames of the runner image.
        currentFrame = (currentFrame % 16) + 1;

        // Construct the image path based on the current frame.
        String imagePath = "../assets/furret (" + currentFrame + ").png";

        try {
            // Read the original image from the specified path.
            BufferedImage originalImage = ImageIO.read(getClass().getResource(imagePath));

            // Resize the original image to the specified dimensions.
            currentRunnerImage = resizeImage(originalImage, scaledWidth, scaledHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to resize an input BufferedImage to the specified width and height.
    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();

        // Draw the original image onto the resized image.
        g.drawImage(originalImage, 0, 0, width, height, null);

        // Dispose of the graphics context to free up resources.
        g.dispose();

        return resizedImage;
    }
}
