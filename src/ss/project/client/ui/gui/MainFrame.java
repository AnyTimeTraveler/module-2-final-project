package ss.project.client.ui.gui;

import ss.project.client.ClientConfig;
import ss.project.client.ui.UI;
import ss.project.shared.game.Engine;

import javax.swing.*;
import java.awt.*;

/**
 * Created by simon on 16.01.17.
 */
public class MainFrame extends JFrame implements UI {

    private static final boolean FULLSCREEN = false;
    private static MainFrame frame;
    private Engine engine;

    public MainFrame() {
        // Run the parent constructor
        super();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Create a standalone thread for the GUI
        EventQueue.invokeLater(() -> {
            frame = new MainFrame();
            Thread.currentThread().setName("GUI");
            frame.init();
        });
    }

    /**
     * Fill the frame with content.
     */
    private void init() {
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
        this.setTitle(ClientConfig.getInstance().WindowTitle);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.switchTo(Panel.GAME);
        this.setVisible(true);
    }

    void switchTo(Panel p) {
        switchTo(p.getPanel());
    }

    private void switchTo(JPanel panel) {
        getContentPane().removeAll();
        getContentPane().add(panel);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    @Override
    public void shutdown() {
        System.exit(0);
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public enum Panel {
        MAIN_MENU(new MainMenu(frame)),
        SINGLE_PLAYER_SETTINGS(new SinglePlayerSettings(frame)),
        SERVER_BRWOSER(new ServerBrowser(frame)),
        OPTIONS(new Options(frame)),
        MULTI_PLAYER_LOBBY(new MultiPlayerLobby(frame)),
        MULTI_PLAYER_ROOM(new MultiPlayerRoom(frame)),
        MULTI_PLAYER_ROOM_CREATION(new MultiPlayerRoomCreation(frame)),
        GAME(new Game(frame));

        private final JPanel panel;

        Panel(JPanel panel) {
            this.panel = panel;
        }

        private JPanel getPanel() {
            return panel;
        }
    }

}
