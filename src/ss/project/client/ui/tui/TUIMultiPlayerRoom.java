package ss.project.client.ui.tui;

import ss.project.client.Controller;

/**
 * Created by simon on 16.01.17.
 */
public class TUIMultiPlayerRoom implements TUIPanel {
    public TUIMultiPlayerRoom() {

    }

    @Override
    public void printScreen() {
        System.out.println("Waiting... Type: 'Leave' to leave.");
    }

    @Override
    public void handleInput(String input) {
        if (input.equalsIgnoreCase("Leave")) {
            Controller.getController().leaveRoom();
        }
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onLeave() {

    }
}
