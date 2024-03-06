
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

// Represents the podium portrait display in the game view.
class PodiumPortrait extends JPanel {
    // BufferedImage to hold the portrait image.
    private BufferedImage portraitImage;

    // System time when the display started.
    private long displayStartTime;

    // Display duration in milliseconds.
    private static final long DISPLAY_DURATION = 2000;

    // Constructor to initialize the PodiumPortrait with a portrait image.
    public PodiumPortrait(BufferedImage portraitImage) {
        this.portraitImage = portraitImage;
        this.displayStartTime = System.currentTimeMillis();
    }

    // Check if the display time has expired.
    public boolean isDisplayTimeExpired() {
        return System.currentTimeMillis() - displayStartTime > DISPLAY_DURATION;
    }

    // Override the paintComponent method to paint the portrait image.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the portrait image if the display time has not expired.
        if (!isDisplayTimeExpired()) {
            g.drawImage(portraitImage, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
