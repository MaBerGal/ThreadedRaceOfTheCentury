package model;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

// This class represents a simple background music player using Java Sound API.
public class BackgroundMusicPlayer extends Thread {

    // Represents the audio clip for background music.
    private Clip backgroundMusic;

    // Constructor for initializing the BackgroundMusicPlayer.
    // Takes the file path of the music file as a parameter.
    public BackgroundMusicPlayer(String musicFilePath) {
        try {
            // Obtain an AudioInputStream from the provided music file.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(musicFilePath));

            // Create a new Clip to hold the background music.
            backgroundMusic = AudioSystem.getClip();

            // Open the background music Clip with the obtained AudioInputStream.
            backgroundMusic.open(audioInputStream);

            // Set the background music to loop continuously.
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            // Print the stack trace if any exception occurs during initialization.
            e.printStackTrace();
        }
    }

    // Override the run method from Thread class.
    // This method is executed when the thread is started.
    @Override
    public void run() {
        // Start playing the background music only if it has been initialized.
        if (backgroundMusic != null) {
            backgroundMusic.start();
        }
    }

    // Override the interrupt method from Thread class.
    // This method is called when the thread is interrupted.
    @Override
    public void interrupt() {
        // Stop and close the background music if it has been initialized.
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }

        // Call the superclass's interrupt method to handle the thread interruption.
        super.interrupt();
    }
}
