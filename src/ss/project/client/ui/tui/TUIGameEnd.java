package ss.project.client.ui.tui;

import ss.project.client.Controller;

/**
 * Created by fw on 26/01/2017.
 */
public class TUIGameEnd implements TUIPanel {
    @Override
    public void printScreen() {
        System.out.println(ASCIIArt.getHeadline("Game end!", 120));
        System.out.println(Controller.getController().getEngine().getWinReason().toString());
        System.out.println(ASCIIArt.getChoiceItem(1, "Main menu", 120));
        System.out.println(ASCIIArt.getChoiceItem(2, "Another game", 120));
    }

    @Override
    public void handleInput(String input) {
        switch (input) {
            case "1":
                Controller.getController().switchTo(Controller.Panel.MAIN_MENU);
                break;
            case "2":
                Controller.getController().switchTo(Controller.Panel.SINGLE_PLAYER_SETTINGS);
                break;
            default:
                System.out.println("Invalid input!");
                break;
        }
    }
}
