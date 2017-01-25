package ss.project.client.ui.tui;

import ss.project.client.ui.UIPanel;

/**
 * Created by simon on 20.01.17.
 */
public interface TUIPanel extends UIPanel {
    void printScreen();

    void handleInput(String input);
}
