package ss.project.client.ui.gui;

import ss.project.client.ui.UIPanel;

import javax.swing.*;

/**
 * Created by simon on 20.01.17.
 */
public abstract class GUIPanel extends JPanel implements UIPanel {
    public GUIPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public GUIPanel() {
        super();
    }

    public abstract void onEnter();

    public abstract void onLeave();
}

