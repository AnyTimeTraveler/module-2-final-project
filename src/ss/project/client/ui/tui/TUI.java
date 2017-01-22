package ss.project.client.ui.tui;


import java.util.Scanner;

/**
 * Created by simon on 23.12.16.
 */
public class TUI {
    private static TUI tui;
    private TUIPanel current;
    private boolean isRunning;

    public TUI() {
        isRunning = true;
        switchTo(TUI.Panel.MAIN_MENU);
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
            current.printScreen();
            current.handleInput(scanner.nextLine());
        }
    }

    public void shutdown() {
        System.exit(0);
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
