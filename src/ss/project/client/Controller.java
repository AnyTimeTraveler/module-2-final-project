package ss.project.client;

import lombok.Getter;
import lombok.Setter;
import ss.project.client.ui.gui.*;
import ss.project.server.Room;
import ss.project.shared.game.Engine;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by simon on 21.01.17.
 */
public class Controller {
    public static Controller controller;
    @Setter
    @Getter
    private Engine engine;
    @Setter
    @Getter
    private List<Room> rooms;
    private FRMMain frame;

    private Controller() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        controller = new Controller();
        controller.start();
    }

    private void start() {
        // Start GUI
        EventQueue.invokeLater(() -> {
            controller.frame = new FRMMain(controller);
            Thread.currentThread().setName("GUI");
            controller.frame.init();
            controller.switchTo(Panel.MAIN_MENU);
        });
    }

    public void restartFrame() {
        frame.dispose();
        start();
    }

    public void startGame() {
        Thread thread = new Thread(() -> engine.startGame());
        thread.setDaemon(true);
        thread.start();
    }


    public void shutdown() {
        System.exit(0);
    }

    public void switchTo(Panel p) {
        frame.switchTo(p.getPanel());
    }

    public void setFrameSize(int width, int height) {
        frame.setSize(width, height);
    }

    public enum Panel {
        MAIN_MENU(new PNLMainMenu(controller)),
        SINGLE_PLAYER_SETTINGS(new PNLSinglePlayerSettings(controller)),
        SERVER_BRWOSER(new PNLServerBrowser(controller)),
        OPTIONS(new PNLOptions(controller)),
        MULTI_PLAYER_LOBBY(new PNLMultiPlayerLobby(controller)),
        MULTI_PLAYER_ROOM(new PNLMultiPlayerRoom(controller)),
        MULTI_PLAYER_ROOM_CREATION(new PNLMultiPlayerRoomCreation(controller)),
        GAME(new PNLGame(controller));

        private final GUIPanel panel;

        Panel(GUIPanel panel) {
            this.panel = panel;
        }

        private GUIPanel getPanel() {
            return panel;
        }
    }

}
