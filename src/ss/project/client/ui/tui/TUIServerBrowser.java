package ss.project.client.ui.tui;

/**
 * Created by simon on 16.01.17.
 */
public class TUIServerBrowser implements TUIPanel {
    public TUIServerBrowser() {

    }

    @Override
    public void printScreen() {
        System.out.println(ASCIIArt.getHeadline("Game", 120));
        for (int i = 0; i < 5; i++) {
            //Create server options.
        }
    }

    @Override
    public void handleInput(String input) {

    }
}
