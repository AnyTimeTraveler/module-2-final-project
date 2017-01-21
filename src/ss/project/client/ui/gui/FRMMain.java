package ss.project.client.ui.gui;

import ss.project.client.ClientConfig;
import ss.project.client.ui.MainWindow;
import ss.project.shared.game.Engine;

import javax.swing.*;
import java.awt.*;

/**
 * Created by simon on 16.01.17.
 */
public class FRMMain extends JFrame implements MainWindow {

    private static final boolean FULLSCREEN = false;
    private static FRMMain frame;
    private Engine engine;

    public FRMMain() {
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
            frame = new FRMMain();
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
        this.switchTo(Panel.MULTI_PLAYER_LOBBY);
        this.setVisible(true);
    }

    void switchTo(Panel p) {
        switchTo(p.getPanel());
    }

    private void switchTo(GUIPanel panel) {
        Container cp = getContentPane();
        if (cp.getComponentCount() > 0) {
            ((GUIPanel) cp.getComponent(0)).onLeave();
            cp.remove(0);
        }
        cp.add(panel);
        panel.onEnter();
        cp.revalidate();
        cp.repaint();
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
        MAIN_MENU(new PNLMainMenu(frame)),
        SINGLE_PLAYER_SETTINGS(new PNLSinglePlayerSettings(frame)),
        SERVER_BRWOSER(new PNLServerBrowser(frame)),
        OPTIONS(new PNLOptions(frame)),
        MULTI_PLAYER_LOBBY(new PNLMultiPlayerLobby(frame)),
        MULTI_PLAYER_ROOM(new PNLMultiPlayerRoom(frame)),
        MULTI_PLAYER_ROOM_CREATION(new PNLMultiPlayerRoomCreation(frame)),
        GAME(new PNLGame(frame));

        private final GUIPanel panel;

        Panel(GUIPanel panel) {
            this.panel = panel;
        }

        private GUIPanel getPanel() {
            return panel;
        }
    }

}