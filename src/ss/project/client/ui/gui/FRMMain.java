package ss.project.client.ui.gui;

import ss.project.client.Config;
import ss.project.client.Controller;

import javax.swing.*;
import java.awt.*;

/**
 * Created by simon on 16.01.17.
 */
public class FRMMain extends JFrame {
    public FRMMain(Controller controller) {
        super();
    }

    /**
     * Fill the frame with content.
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
            this.setSize(new Dimension(300, 400));
        }
        this.requestFocus();
        this.setTitle(Config.getInstance().WindowTitle);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void switchTo(GUIPanel panel) {
        Container cp = getContentPane();
        if (cp.getComponentCount() > 0) {
            ((GUIPanel) cp.getComponent(0)).onLeave();
            cp.remove(0);
        }
        cp.add(panel);
        panel.onEnter();
        cp.revalidate();
        cp.repaint();
    }
}
