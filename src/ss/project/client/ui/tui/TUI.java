package ss.project.client.ui.tui;


import ss.project.client.ui.UI;

import java.awt.*;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by simon on 23.12.16.
 */
public class TUI implements UI {
    private static TUI tui;
    private Screen current;
    private boolean isRunning;

    public TUI() {
        isRunning = true;
        switchTo(Panel.MAIN_MENU);
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

    private void switchTo(Screen screen) {
        current = screen;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (isRunning) {
            current.printScreen();
            current.handleInput(scanner.nextLine());
        }
    }

    @Override
    public void shutdown() {
        isRunning = false;
    }

    public enum Panel {
        MAIN_MENU(new MainMenu(tui)),
        SINGLE_PLAYER_SETTINGS(new SinglePlayerSettings(tui)),
        SERVER_BRWOSER(new ServerBrowser(tui)),
        OPTIONS(new Options(tui)),
        MULTI_PLAYER_LOBBY(new MultiPlayerLobby(tui)),
        MULTI_PLAYER_ROOM(new MultiPlayerRoom(tui)),
        MULTI_PLAYER_ROOM_CREATION(new MultiPlayerRoomCreation(tui));

        private final Screen screen;

        Panel(Screen screen) {
            this.screen = screen;
        }

        private Screen getScreen() {
            return screen;
        }
    }

}
