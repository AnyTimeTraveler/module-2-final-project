package ss.project.client.ui.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by simon on 16.01.17.
 */
public class PNLOptions extends JPanel {

    public PNLOptions(FRMMain mainFrame) {
        super(true);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addComponent(new JLabel("PNLOptions"));
//        addComponent(new JButton("Single Player"), e -> mainFrame.switchToSinglePlayerSettings());
//        addComponent(new JButton("Multi Player"), e -> mainFrame.switchToServerBrowser());
//        addComponent(new JButton("PNLOptions"), e -> mainFrame.switchToOptions());
        addComponent(new JButton("Back"), e -> mainFrame.switchTo(FRMMain.Panel.MAIN_MENU));
    }

    private void addComponent(JComponent comp) {
        comp.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(comp);
    }

    private void addComponent(JButton comp, ActionListener actionListener) {
        comp.setAlignmentX(Component.CENTER_ALIGNMENT);
        comp.addActionListener(actionListener);
        this.add(comp);
    }
}
