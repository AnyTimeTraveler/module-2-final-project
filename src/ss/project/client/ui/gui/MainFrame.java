package ss.project.client.ui.gui;

import ss.project.client.ui.UI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by simon on 16.01.17.
 */
public class MainFrame extends JFrame implements UI, Runnable {

    private static final boolean FULLSCREEN = false;

    public MainFrame() {
        this.setName("Main Frame");
        if (FULLSCREEN) {
            this.setLocation(0, 0);
            this.setSize(new Dimension(1920, 1080));
            this.setUndecorated(true);
            this.setAlwaysOnTop(true);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            this.setSize(new Dimension(300, 400));
        }
        this.requestFocus();
        this.setTitle("Connect Four 3D");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setContentPane(new MainMenu(this));
        this.setVisible(true);
    }

    public static void main(String[] args) {
        Thread ui = new Thread(new MainFrame());
        ui.start();
    }

    @Override
    public void run() {

    }

    public void switchToMainMenu() {
        this.setContentPane(new MainMenu(this));
    }

    public void switchToMultiPlayerLobby() {
        this.setContentPane(new MultiPlayerLobby());
    }

    public void switchToMultiPlayerRoom() {
        this.setContentPane(new MultiPlayerRoom());
    }

    public void switchToMultiPlayerRoomCreation() {
        this.setContentPane(new MultiPlayerRoomCreation());
    }

    public void switchToServerBrowser() {
        this.setContentPane(new ServerBrowser());
    }

    public void switchToSinglePlayerSettings() {
        this.setContentPane(new SinglePlayerSettings());
    }

    public void switchToOptions() {
        this.setContentPane(new Options());
    }

    public void switchToGame() {
        this.setContentPane(new Game());
    }
}
