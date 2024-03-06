
package view;

import model.BackgroundMusicPlayer;
import model.Runner;
import model.Goal;
import model.RunnerImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

// Represents the panel where the race and animation take place.
public class RaceJPanel extends JPanel {
    // Delay for the Timer.
    private static final int DELAY = 20;
    // Last time when Timer was updated.
    private long lastTime;
    // Frames per second counter.
    private int frames;
    // List to keep track of winning runners.
    private List<Runner> winners;
    // Array to store racing runners.
    public Runner[] corredores;
    // The finish line.
    private Goal goal;
    // Offscreen image for double buffering.
    private Image offscreenImage;
    // Array to store images for each runner.
    private BufferedImage[] runnerImages;
    // Current image for the animation.
    private BufferedImage currentRunnerImage;
    // Object responsible for managing runner animation images.
    private RunnerImage runnerImage;
    // Array to store background images.
    private BufferedImage[] backgrounds;
    // Current background image.
    private BufferedImage currentBackgroundImage;
    // Lap count to determine background change.
    private int lapCount;
    // Object responsible for playing background music.
    private BackgroundMusicPlayer musicPlayer;
    // Array to store podium portrait objects.
    private PodiumPortrait[] podiumPortraits;
    // Image displayed at the start of the race.
    private BufferedImage startImage;
    // Image displayed at the finish line.
    private BufferedImage goalImage;
    // Add a set of flags to track jump key presses for each runner.
    private boolean[] jumpKeyPressed = new boolean[5];
    // Add labels to display control instructions.
    private JLabel controlLabel1, controlLabel2, controlLabel3, upLabel, downLabel;

    // Constructor to initialize RaceJPanel.
    public RaceJPanel() {
        corredores = new Runner[5];
        this.runnerImage = new RunnerImage(80, 50);

        podiumPortraits = new PodiumPortrait[3];

        // Initialize jump key flags.
        Arrays.fill(jumpKeyPressed, false);

        // Create and start the RunnerImage thread.
        for (int i = 0; i < corredores.length; i++) {
            corredores[i] = new Runner(10, 450 + i * 10);
            corredores[i].setNumber(i + 1);
            corredores[i].start();
        }

        // Create and start the RunnerImage thread.
        Thread runnerImageThread = new Thread(runnerImage);
        runnerImageThread.start();

        // Initialize the background music player.
        musicPlayer = new BackgroundMusicPlayer("src/assets/background_music.wav");
        musicPlayer.start();

        try {
            // Load the static images.
            startImage = ImageIO.read(getClass().getResource("../assets/blueportal.png"));
            goalImage = ImageIO.read(getClass().getResource("../assets/orangeportal.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Empty winners list to start with.
        winners = new ArrayList<>();
        // Create an instance of Goal with initial position and parameters.
        goal = new Goal(600, 200, 750, 2);
        goal.start();
        lapCount = 0;

        // Load background images for each lap.
        backgrounds = new BufferedImage[9];
        for (int i = 0; i < 8; i++) {
            try {
                String imagePath = "../assets/day" + (i + 1) + ".png";
                backgrounds[i] = ImageIO.read(getClass().getResource(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Set the initial background image.
        currentBackgroundImage = backgrounds[0];

        // Add labels for control instructions.
        controlLabel1 = new JLabel("1 2 3 4 5 - Increase Runner Speed");
        controlLabel2 = new JLabel("Q W E R T - Decrease Runner Speed");
        upLabel = new JLabel("UP - Increase Everyone's Speed");
        downLabel = new JLabel("DOWN - Decrease Everyone's Speed");
        controlLabel3 = new JLabel("Z X C V B - Hold to make a Runner jump");

        // Set font and color for control labels.
        Font controlFont = new Font("Arial", Font.BOLD, 13);
        controlLabel1.setFont(controlFont);
        controlLabel2.setFont(controlFont);
        upLabel.setFont(controlFont);
        downLabel.setFont(controlFont);
        controlLabel3.setFont(controlFont);

        controlLabel1.setForeground(Color.WHITE);
        controlLabel2.setForeground(Color.WHITE);
        upLabel.setForeground(Color.WHITE);
        downLabel.setForeground(Color.WHITE);
        controlLabel3.setForeground(Color.WHITE);

        // Add labels to the panel.
        add(controlLabel1);
        add(controlLabel2);
        add(upLabel);
        add(downLabel);
        add(controlLabel3);

        // Set up a Timer for continuous updates and rendering.
        Timer timer = new Timer(DELAY, e -> {
            updatePositions();
            updateRunnerImage();
            repaint();
        });

        // Record the starting time for frame rate calculations.
        lastTime = System.currentTimeMillis();
        // Start the Timer.
        timer.start();
    }

    // Method to update the positions of the runners and handle lap completion.
    private void updatePositions() {
        for (Runner runner : corredores) {
            if (runner.getX() >= goal.getX() && !winners.contains(runner)) {
                // Reset runner position after reaching the goal.
                runner.setX(10);
                // Increment the laps completed by the runner.
                runner.incrementLaps();
                // Update the lap count to determine background change.
                if (runner.getCompletedLaps() > lapCount) {
                    lapCount = runner.getCompletedLaps();
                    updateBackgroundImage();
                }

                // Check if the runner completed all laps and add to winners list.
                if (runner.getCompletedLaps() == 8) {
                    winners.add(runner);
                    // Display the podium when three winners are found.
                    if (winners.size() == 3) {
                        showPodium();
                    }
                }
            }
        }
    }

    // Method to update the current runner image based on animation.
    private void updateRunnerImage() {
        currentRunnerImage = RunnerImage.getCurrentRunnerImage();
    }

    // Method to update the background image based on the leading runner's lap count.
    private void updateBackgroundImage() {
        int maxLaps = 0;
        Runner leadingRunner = null;

        // Find the maximum number of laps completed by any runner.
        for (Runner runner : corredores) {
            if (runner.getCompletedLaps() > maxLaps) {
                maxLaps = runner.getCompletedLaps();
                leadingRunner = runner;
            }
        }

        // Change the background only when the leading runner completes a lap.
        if (maxLaps < 8 && leadingRunner != null) {
            currentBackgroundImage = backgrounds[leadingRunner.getCompletedLaps()];
        }
    }

    // Method to update the frames per second counter.
    private void updateFPS(long elapsedTime) {
        if (elapsedTime > 0) {
            frames = (int) (1000 / elapsedTime);
        }
    }

    // Displays the podium with winners and provides options to play again or exit the game.
    private void showPodium() {
        // Create a panel to hold the podium portraits and labels.
        JPanel podiumPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;

        // Create PodiumPortrait instances for the winners and add them to the panel.
        for (int i = 0; i < winners.size(); i++) {
            int place = i + 1;
            BufferedImage portraitImage = loadPodiumPortrait(winners.get(i), getPlaceString(place));
            PodiumPortrait podiumPortrait = new PodiumPortrait(portraitImage);

            // Adjust the size of the PodiumPortrait component.
            Dimension preferredSize = new Dimension(200, 200);
            podiumPortrait.setPreferredSize(preferredSize);

            // Create a label to display the place on top of the portrait.
            JLabel placeLabel = new JLabel("Runner " + winners.get(i).getNumber(), SwingConstants.CENTER);
            placeLabel.setForeground(getPlaceColor(place));

            // Add the portrait and label to the panel using GridBagLayout constraints.
            constraints.gridx = i;
            podiumPanel.add(podiumPortrait, constraints);

            // Move to the next row for labels.
            constraints.gridy = 1;
            podiumPanel.add(placeLabel, constraints);

            // Reset the row for the next iteration.
            constraints.gridy = 0;
        }

        // Show the podium panel in the dialog with "Again" and "Exit" options.
        int option = JOptionPane.showOptionDialog(
                null,
                podiumPanel,
                "Podium",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{"Again", "Exit"},  // Options: "Again" and "Exit"
                "Again"  // Default selection.
        );

        // Process user's choice.
        if (option == 0) {
            // "Again" was chosen, reset the game.
            resetGame();
        } else {
            // "Exit" was chosen, close the application.
            System.exit(0);
        }
    }

    // Resets the game by clearing winners, lap count, and setting the background to the initial image.
    // Stops existing threads, resets runner positions and speeds, and recreates new runner threads.
    private void resetGame() {
        winners.clear();
        lapCount = 0;
        currentBackgroundImage = backgrounds[0];

        // Reset the runner states and stop their existing threads.
        for (Runner runner : corredores) {
            runner.setX(10);
            runner.setSpeed(new Random().nextInt(5) + 1);
            runner.setLapsCompleted(0);
            runner.setHasFinished(false);
            runner.interrupt();
        }

        // Recreate and start new runner threads.
        corredores = new Runner[5];
        for (int i = 0; i < corredores.length; i++) {
            corredores[i] = new Runner(10, 450 + i * 10);
            corredores[i].setNumber(i + 1);
            corredores[i].start();
        }
    }

    // Gets the corresponding place string (e.g., "first", "second", "third") based on the place.
    private String getPlaceString(int place) {
        switch (place) {
            case 1:
                return "first";
            case 2:
                return "second";
            case 3:
                return "third";
            default:
                return "";
        }
    }

    // Gets the color based on the place for displaying on the podium.
    private Color getPlaceColor(int place) {
        switch (place) {
            case 1:
                return new Color(187, 150, 0); // Dark yellow.
            case 2:
                return new Color(128, 128, 128); // Dark gray.
            case 3:
                return new Color(139, 69, 19); // Brown.
            default:
                return Color.BLACK; // Black as default.
        }
    }

    // Loads the respective podium portrait image for the given runner and place.
    private BufferedImage loadPodiumPortrait(Runner runner, String place) {
        // Use the runner information to load the respective podium portrait.
        String imagePath = "../assets/" + place + ".png";
        try {
            BufferedImage image = ImageIO.read(getClass().getResource(imagePath));
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Overrides the paintComponent method to draw the race components and animations.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        long currentTime = System.currentTimeMillis();

        // Create offscreen image for double buffering.
        if (offscreenImage == null) {
            offscreenImage = createImage(getWidth(), getHeight());
        }

        Graphics offscreenGraphics = offscreenImage.getGraphics();
        offscreenGraphics.setColor(getBackground());
        offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());

        // Draw the background image at the centered position.
        offscreenGraphics.drawImage(currentBackgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Draw the static images at the same x position where runners spawn and at the goal.
        offscreenGraphics.drawImage(startImage, 10, 450, this);
        offscreenGraphics.drawImage(goalImage, goal.getGoalX(), 450, this);

        // Draw the current runner images at their respective positions.
        for (int i = 0; i < corredores.length; i++) {
            offscreenGraphics.drawImage(currentRunnerImage, corredores[i].getX(), corredores[i].getY(), this);
        }

        // Draw the podium portraits.
        for (PodiumPortrait podiumPortrait : podiumPortraits) {
            if (podiumPortrait != null) {
                podiumPortrait.paintComponent(offscreenGraphics);
            }
        }

        // Uncomment the following code to draw a red line representing the goal.
        /*
        offscreenGraphics.setColor(Color.RED);
        offscreenGraphics.fillRect(goal.getX(), 0, 20, getHeight()); */

        // Draw the frames per second counter.
        drawFPS(offscreenGraphics);

        // Remove expired podium portraits.
        for (int i = 0; i < podiumPortraits.length; i++) {
            if (podiumPortraits[i] != null && podiumPortraits[i].isDisplayTimeExpired()) {
                podiumPortraits[i] = null;
            }
        }

        // Draw the offscreen image onto the visible panel.
        g.drawImage(offscreenImage, 0, 0, this);

        // Calculate the elapsed time for the frame.
        long elapsedTime = currentTime - lastTime;

        // Update the lastTime to the current time for the next frame calculation.
        lastTime = currentTime;

        // Update the frames per second counter based on the elapsed time.
        updateFPS(elapsedTime);

    }

    // Draw the frames per second counter on the screen.
    private void drawFPS(Graphics g) {
        g.setColor(Color.GREEN);
        g.drawString("FPS: " + frames, 10, 40);
    }


}
