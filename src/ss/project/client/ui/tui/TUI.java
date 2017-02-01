package ss.project.client.ui.tui;


import ss.project.client.Controller;
import ss.project.client.ui.UIFrame;
import ss.project.client.ui.UIPanel;

import java.util.Scanner;

/**
 * Created by simon on 23.12.16.
 */
public class TUI implements UIFrame {
    private TUIPanel current;
    private boolean isRunning;

    public TUI() {
        isRunning = true;
    }

    public void switchTo(UIPanel panel) {
        if (current != null) {
            current.onLeave();
        }
        current = (TUIPanel) panel;
        current.onEnter();
    }

    @Override
    public void init() {
        Controller.getController().switchTo(Controller.Panel.MAIN_MENU);
        run();
    }

    @Override
    public void dispose() {
        System.exit(0);
    }

    @Override
    public void setSize(int width, int height) {

    }

    @Override
    public void setConnected(boolean enabled) {

    }

    private void run() {
        Scanner sc = new Scanner(System.in);
        while (isRunning) {
            current.printScreen();
            current.handleInput(sc.nextLine());
        }
        sc.close();
    }
}
