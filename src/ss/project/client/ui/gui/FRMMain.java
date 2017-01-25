package ss.project.client.ui.gui;

import ss.project.client.Config;
import ss.project.client.Controller;
import ss.project.client.ui.UIFrame;
import ss.project.client.ui.UIPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by simon on 16.01.17.
 */
public class FRMMain extends JFrame implements UIFrame {
    public FRMMain(Controller controller) {
public class FRMMain extends JFrame {
    public FRMMain() {
        super();
    }

    /**
     * Fill the frame with content.
     * Sets the title and default close operations.
     */
    public void init() {
        this.setName("Main Frame");
        if (Config.getInstance().Fullscreen) {
            this.setLocation(0, 0);
            this.setSize(new Dimension(1920, 1080));
            this.setUndecorated(true);
            this.setAlwaysOnTop(true);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            this.setSize(new Dimension(800, 800));
        }
        this.requestFocus();
        this.setTitle(Config.getInstance().WindowTitle);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Remove the current panel and show a specific panel.
     * Calls onLeave on the current panel and onEnter on the new panel.
     *
     * @param panel the panel that needs to be shown.
     */
    public void switchTo(UIPanel panel) {
        Container cp = getContentPane();
        if (cp.getComponentCount() > 0) {
            ((GUIPanel) cp.getComponent(0)).onLeave();
            cp.remove(0);
        }
        cp.add((GUIPanel) panel);
        ((GUIPanel) panel).onEnter();
        cp.revalidate();
        cp.repaint();
    }
}
