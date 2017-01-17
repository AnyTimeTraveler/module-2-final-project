package ss.project.client.ui.gui;

import ss.project.client.Config;
import ss.project.client.ui.UI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by simon on 16.01.17.
 */
public class MainFrame extends JFrame implements UI {

    private static final boolean FULLSCREEN = false;
    private MainMenu mainMenu;
    private MultiPlayerLobby multiPlayerLobby;
    private MultiPlayerRoom multiPlayerRoom;
    private MultiPlayerRoomCreation multiPlayerRoomCreation;
    private ServerBrowser serverBrowser;
    private SinglePlayerSettings singlePlayerSettings;
    private Options options;
    private Game game;

    public MainFrame() {
        // Run the parent constructor
        super();

        // Create and load all the panels that are needed later
        mainMenu = new MainMenu(this);
        multiPlayerLobby = new MultiPlayerLobby(this);
        multiPlayerRoom = new MultiPlayerRoom(this);
        multiPlayerRoomCreation = new MultiPlayerRoomCreation(this);
        serverBrowser = new ServerBrowser(this);
        singlePlayerSettings = new SinglePlayerSettings(this);
        options = new Options(this);
        game = new Game(this);
    }

    public static void main(String[] args) {
        // Create a standalone thread for the GUI
        EventQueue.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            Thread.currentThread().setName("GUI");
            frame.init();
        });
    }

    /**
     * Fill the frame with content.
     */
    public void init() {
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
        this.setTitle(Config.getInstance().WindowTitle);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        switchToMainMenu();
    }

    public void switchToMainMenu() {
        switchTo(mainMenu);
    }

    public void switchToMultiPlayerLobby() {
        switchTo(multiPlayerLobby);
    }

    public void switchToMultiPlayerRoom() {
        switchTo(multiPlayerRoom);
    }

    public void switchToMultiPlayerRoomCreation() {
        switchTo(multiPlayerRoomCreation);
    }

    public void switchToServerBrowser() {
        switchTo(serverBrowser);
    }

    public void switchToSinglePlayerSettings() {
        switchTo(singlePlayerSettings);
    }

    public void switchToOptions() {
        switchTo(options);
    }

    public void switchToGame() {
        switchTo(game);
    }

    private void switchTo(JPanel panel) {
        getContentPane().removeAll();
        getContentPane().add(panel);
        getContentPane().revalidate();
        getContentPane().repaint();
    }
}
