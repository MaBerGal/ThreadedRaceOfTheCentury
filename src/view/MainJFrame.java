package view;

import controller.InputController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents the main JFrame for the racing game with animation.
class MainJFrame extends JFrame implements ActionListener {
    // Constructor to initialize MainJFrame.
    public MainJFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Threaded Race of the Millennium");

        // Set the icon for the JFrame.
        ImageIcon icon = new ImageIcon("src/assets/icon.png");
        setIconImage(icon.getImage());

        // Create the racing panel.
        RaceJPanel panel = new RaceJPanel();

        // Instantiate InputController and pass the racing panel to handle user input.
        InputController inputController = new InputController(panel);

        // Add the panel to the JFrame.
        add(panel);

    }

    // Unused actionPerformed method.
    @Override
    public void actionPerformed(ActionEvent e) {
    }

    // Main method to start the program.
    public static void main(String... args) {
        SwingUtilities.invokeLater(() -> {
            new MainJFrame().setVisible(true);
        });
    }
}
