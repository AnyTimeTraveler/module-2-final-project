package ss.project.client.ui.tui;


import ss.project.client.ui.MainWindow;

import java.awt.*;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by simon on 23.12.16.
 */
public class TUI implements MainWindow {
    private static TUI tui;
    private TUIPanel current;
    private boolean isRunning;

    public TUI() {
        isRunning = true;
        switchTo(TUI.Panel.MAIN_MENU);
    }

    public static void main(String[] args) throws IOException {
        EventQueue.invokeLater(() -> {
            tui = new TUI();
            Thread.currentThread().setName("TUI");
            tui.run();
        });
    }

    void switchTo(Panel p) {
        switchTo(p.getScreen());
    }

    private void switchTo(TUIPanel screen) {
        current = screen;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (isRunning) {
            current.show();
            current.handleInput(scanner.nextLine());
        }
    }

    @Override
    public void shutdown() {
        isRunning = false;
    }

    public enum Panel {
        MAIN_MENU(new MainMenu(tui)),
        SINGLE_PLAYER_SETTINGS(new SinglePlayerSettings()),
        SERVER_BRWOSER(new ServerBrowser()),
        OPTIONS(new Options(tui)),
        MULTI_PLAYER_LOBBY(new MultiPlayerLobby()),
        MULTI_PLAYER_ROOM(new MultiPlayerRoom()),
        MULTI_PLAYER_ROOM_CREATION(new MultiPlayerRoomCreation());

        private final TUIPanel screen;

        Panel(TUIPanel screen) {
            this.screen = screen;
        }

        private TUIPanel getScreen() {
            return screen;
        }
    }

}
