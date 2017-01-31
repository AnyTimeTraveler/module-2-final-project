package ss.project.client.ui.gui;

import lombok.Getter;
import ss.project.client.Controller;
import ss.project.client.ui.UIFrame;
import ss.project.client.ui.UIPanel;
import ss.project.shared.model.ClientConfig;

import javax.swing.*;
import java.awt.*;

/**
 * Created by simon on 16.01.17.
 */
public class FRMMain extends JFrame implements UIFrame {
    private JPanel mainPanel;
    @Getter
    private PNLChat chatPanel;

    private JSplitPane splitPane;

    public FRMMain() {
        super();
    }

    /**
     * Fill the frame with content.
     * Sets the title and default close operations.
     */
    public void init() {
        this.setName("Main Frame");
        if (ClientConfig.getInstance().Fullscreen) {
            this.setLocation(0, 0);
            this.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());
            this.setUndecorated(true);
            this.setAlwaysOnTop(true);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            this.setSize(new Dimension(1200, 800));
        }
        this.requestFocus();
        this.setTitle(ClientConfig.getInstance().WindowTitle);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setMinimumSize(new Dimension(600, 0));
        chatPanel = new PNLChat(Controller.getController());

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainPanel, chatPanel);
        splitPane.setContinuousLayout(true);
        splitPane.setResizeWeight(1);
        add(splitPane);
        setConnected(false);
    }

    @Override
    public void setConnected(boolean enabled) {
        chatPanel.setVisible(enabled);
        if (enabled) {
            chatPanel.onEnter();
        } else {
            chatPanel.onLeave();
        }
        splitPane.revalidate();
        splitPane.resetToPreferredSizes();
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
