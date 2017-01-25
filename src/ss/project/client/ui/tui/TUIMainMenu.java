package ss.project.client.ui.tui;

import ss.project.client.Controller;

/**
 * Created by simon on 16.01.17.
 */
public class TUIMainMenu implements TUIPanel {

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
                Controller.controller.switchTo(Controller.Panel.SINGLE_PLAYER_SETTINGS);
                break;
            case "2":
                Controller.controller.switchTo(Controller.Panel.SERVER_BRWOSER);
                break;
            case "3":
                Controller.controller.switchTo(Controller.Panel.OPTIONS);
                break;
            case "4":
                Controller.controller.shutdown();
                break;
            default:
                System.out.println("Invalid input!");
                break;
        }
    }
}
