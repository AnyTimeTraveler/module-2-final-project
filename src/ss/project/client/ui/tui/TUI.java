package ss.project.client.ui.tui;


import ss.project.client.ui.UIFrame;
import ss.project.client.ui.UIPanel;

import java.util.Scanner;

/**
 * Created by simon on 23.12.16.
 */
public class TUI implements UIFrame {
    private static TUI tui;
    private TUIPanel current;
    private boolean isRunning;

    public TUI() {
        isRunning = true;
    }

    public void switchTo(UIPanel panel) {
        current = (TUIPanel) panel;
    }

    @Override
    public void init() {
        run();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void setSize(int width, int height) {

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

}
