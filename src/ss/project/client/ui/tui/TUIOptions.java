package ss.project.client.ui.tui;

import ss.project.client.Controller;

/**
 * Created by simon on 16.01.17.
 */
public class TUIOptions implements TUIPanel {


    @Override
    public void printScreen() {
        System.out.println(ASCIIArt.getHeadline("Options", 120));
        System.out.println(ASCIIArt.getChoiceItem(1, "Back", 120));
    }

    @Override
    public void handleInput(String input) {
        if (input.equalsIgnoreCase("1")) {
            Controller.getController().switchTo(Controller.Panel.MAIN_MENU);
        }
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onLeave() {

    }
}
