package controller;

import model.Runner;
import view.RaceJPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputController implements KeyListener {

    private RaceJPanel racePanel;

    public InputController(RaceJPanel racePanel) {
        this.racePanel = racePanel;
        racePanel.addKeyListener(this);
        racePanel.setFocusable(true);
        racePanel.requestFocusInWindow();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        handleKeyPress(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        handleKeyRelease(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    private void handleKeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Check for key presses related to jumping for each runner.
        for (Runner runner : racePanel.corredores) {
            int jumpKeyCode = getJumpKeyCode(runner.getNumber());

            // Set the jumping state for the corresponding runner.
            if (keyCode == jumpKeyCode) {
                runner.setJumping(true);
            }
        }

        // Control the overall speed of all runners based on arrow keys.
        if (keyCode == KeyEvent.VK_UP) {
            for (Runner c : racePanel.corredores) {
                c.setSpeed(c.getSpeed() + 1);
            }
        } else if (keyCode == KeyEvent.VK_DOWN) {
            for (Runner c : racePanel.corredores) {
                c.setSpeed(Math.max(c.getSpeed() - 1, 0));
            }
        }

        // Adjust the speed of each runner individually based on numeric keys.
        for (int i = 0; i < racePanel.corredores.length; i++) {
            Runner c = racePanel.corredores[i];
            int runnerNumber = c.getNumber();

            if (keyCode == KeyEvent.VK_1 && runnerNumber == 1) {
                c.setSpeed(c.getSpeed() + 1);
            } else if (keyCode == KeyEvent.VK_2 && runnerNumber == 2) {
                c.setSpeed(c.getSpeed() + 1);
            } else if (keyCode == KeyEvent.VK_3 && runnerNumber == 3) {
                c.setSpeed(c.getSpeed() + 1);
            } else if (keyCode == KeyEvent.VK_4 && runnerNumber == 4) {
                c.setSpeed(c.getSpeed() + 1);
            } else if (keyCode == KeyEvent.VK_5 && runnerNumber == 5) {
                c.setSpeed(c.getSpeed() + 1);
            }

            // Adjust the speed of each runner individually based on QWERT keys.
            if (keyCode == KeyEvent.VK_Q && runnerNumber == 1) {
                c.setSpeed(Math.max(c.getSpeed() - 1, 0));
            } else if (keyCode == KeyEvent.VK_W && runnerNumber == 2) {
                c.setSpeed(Math.max(c.getSpeed() - 1, 0));
            } else if (keyCode == KeyEvent.VK_E && runnerNumber == 3) {
                c.setSpeed(Math.max(c.getSpeed() - 1, 0));
            } else if (keyCode == KeyEvent.VK_R && runnerNumber == 4) {
                c.setSpeed(Math.max(c.getSpeed() - 1, 0));
            } else if (keyCode == KeyEvent.VK_T && runnerNumber == 5) {
                c.setSpeed(Math.max(c.getSpeed() - 1, 0));
            }
        }
    }

    private void handleKeyRelease(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Handle key releases related to jumping for each runner.
        for (Runner runner : racePanel.corredores) {
            int jumpKeyCode = getJumpKeyCode(runner.getNumber());

            if (keyCode == jumpKeyCode) {
                runner.setJumping(false);
            }
        }
    }

    // Get the pressed key related to jumping.
    private int getJumpKeyCode(int runnerNumber) {
        switch (runnerNumber) {
            case 1:
                return KeyEvent.VK_Z;
            case 2:
                return KeyEvent.VK_X;
            case 3:
                return KeyEvent.VK_C;
            case 4:
                return KeyEvent.VK_V;
            case 5:
                return KeyEvent.VK_B;
            default:
                return 0;
        }
    }
}
