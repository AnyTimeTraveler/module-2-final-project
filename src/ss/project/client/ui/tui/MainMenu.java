package ss.project.client.ui.tui;

/**
 * Created by simon on 16.01.17.
 */
public class MainMenu implements Screen {

    private final TUI tui;

    MainMenu(TUI tui) {
        this.tui = tui;
    }

    @Override
    public void printScreen() {
        System.out.println(ASCIIArt.getHeadline("Main Menu", 120));
        System.out.println(ASCIIArt.getChoiceItem(1, "Single Player", 120));
        System.out.println(ASCIIArt.getChoiceItem(2, "Multi Player", 120));
        System.out.println(ASCIIArt.getChoiceItem(3, "PNLOptions", 120));
        System.out.println(ASCIIArt.getChoiceItem(4, "Exit", 120));
    }

    @Override
    public void handleInput(String input) {
        switch (input) {
            case "1":
                tui.switchTo(TUI.Panel.SINGLE_PLAYER_SETTINGS);
                break;
            case "2":
                tui.switchTo(TUI.Panel.SERVER_BRWOSER);
                break;
            case "3":
                tui.switchTo(TUI.Panel.OPTIONS);
                break;
            case "4":
                tui.shutdown();
                break;
            default:
                System.out.println("Invalid input!");
                break;
        }
    }
}
