package ss.project.client.ui.gui;

import ss.project.client.ui.UIPanel;

import javax.swing.*;

/**
 * The base clss of all GUI panels. Contains Enter and Leave methods to switch the current menu.
 * <p>
 * Created by simon on 20.01.17.
 */
public abstract class GUIPanel extends JPanel implements UIPanel {
    public GUIPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public GUIPanel() {
        super();
    }

    /**
     * Called everytime when this GUIPanel is shown.
     */
    public abstract void onEnter();

    /**
     * Called everytime when this GUIPanel is hidden again.
     */
    public abstract void onLeave();
}

