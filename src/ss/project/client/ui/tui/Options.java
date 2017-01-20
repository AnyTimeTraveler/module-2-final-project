package ss.project.client.ui.tui;

/**
 * Created by simon on 16.01.17.
 */
public class Options implements TUIPanel {

    private final TUI tui;

    public Options(TUI tui) {
        this.tui = tui;
    }

    @Override
    public void printScreen() {
        System.out.println(ASCIIArt.getHeadline("PNLOptions", 120));
        System.out.println(ASCIIArt.getChoiceItem(1, "Back", 120));
    }

    @Override
    public void handleInput(String input) {
        if (input.equalsIgnoreCase("1"))
            tui.switchTo(TUI.Panel.MAIN_MENU);
    }
}
