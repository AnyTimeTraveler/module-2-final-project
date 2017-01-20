package ss.project.client.ui.gui;

import javax.swing.*;

/**
 * Created by simon on 20.01.17.
 */
public abstract class GUIPanel extends JPanel implements Panel {
    public GUIPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public GUIPanel() {
    }

    public abstract void onEnter();

    public abstract void onLeave();
}

