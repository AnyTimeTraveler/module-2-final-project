package ss.project.client.ui.gui;

import javax.swing.*;

/**
 * Created by simon on 20.01.17.
 */
public abstract class GUIPanel extends JPanel {
    protected boolean initialized;

    public GUIPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public GUIPanel() {
        super();
    }

    /**
     * Called the very first time this panel is shown.
     */
    public abstract void initialize();

    public abstract void onEnter();

    public abstract void onLeave();
}

