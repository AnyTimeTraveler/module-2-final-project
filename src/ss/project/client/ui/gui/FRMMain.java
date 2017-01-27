package ss.project.client.ui.gui;

import ss.project.client.Config;
import ss.project.client.ui.UIFrame;
import ss.project.client.ui.UIPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by simon on 16.01.17.
 */
public class FRMMain extends JFrame implements UIFrame {
    private JPanel mainPanel;
    private JPanel chatPanel;

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
            this.setSize(new Dimension(Config.getInstance().FullscreenWidth, Config.getInstance().FullscreenHeight));
            this.setUndecorated(true);
            this.setAlwaysOnTop(true);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            this.setSize(new Dimension(1200, 800));
        }
        this.requestFocus();
        this.setTitle(Config.getInstance().WindowTitle);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setMinimumSize(new Dimension(600, 0));
        chatPanel = new PNLChat();

        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainPanel, chatPanel);
        add(jSplitPane);
    }

    /**
     * Remove the current panel and show a specific panel.
     * Calls onLeave on the current panel and onEnter on the new panel.
     *
     * @param panel the panel that needs to be shown.
     */
    public void switchTo(UIPanel panel) {
        if (mainPanel.getComponentCount() > 0) {
            ((GUIPanel) mainPanel.getComponent(0)).onLeave();
            mainPanel.remove(0);
        }
        mainPanel.add((GUIPanel) panel, BorderLayout.CENTER);
        ((GUIPanel) panel).onEnter();
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
