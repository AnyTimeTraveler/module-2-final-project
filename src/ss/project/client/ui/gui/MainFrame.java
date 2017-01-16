package ss.project.client.ui.gui;

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

    private JPanel[] panels;

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

        // Group them into an array, so they can be quickly accessed by batch operations
        panels = new JPanel[]{mainMenu, multiPlayerLobby, multiPlayerRoom, multiPlayerRoomCreation, serverBrowser, singlePlayerSettings, options, game};
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
        this.setTitle("Connect Four 3D");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        this.setContentPane(new MainMenu(this));
        this.setVisible(true);
        switchToMainMenu();
    }

    public void switchToMainMenu() {
        getContentPane().removeAll();
        getContentPane().add(mainMenu);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void switchToMultiPlayerLobby() {
        getContentPane().removeAll();
        getContentPane().add(multiPlayerLobby);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void switchToMultiPlayerRoom() {
        getContentPane().removeAll();
        getContentPane().add(multiPlayerRoom);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void switchToMultiPlayerRoomCreation() {
        getContentPane().removeAll();
        getContentPane().add(multiPlayerRoomCreation);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void switchToServerBrowser() {
        getContentPane().removeAll();
        getContentPane().add(serverBrowser);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void switchToSinglePlayerSettings() {
        getContentPane().removeAll();
        getContentPane().add(singlePlayerSettings);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void switchToOptions() {
        getContentPane().removeAll();
        getContentPane().add(options);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void switchToGame() {
        getContentPane().removeAll();
        getContentPane().add(game);
        getContentPane().revalidate();
        getContentPane().repaint();
    }
}
