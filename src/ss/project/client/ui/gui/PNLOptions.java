package ss.project.client.ui.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by simon on 16.01.17.
 */
public class PNLOptions extends JPanel {

    public PNLOptions(FRMMain FRMMain) {
        super(true);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addComponent(new JLabel("PNLOptions"));
//        addComponent(new JButton("Single Player"), e -> FRMMain.switchToSinglePlayerSettings());
//        addComponent(new JButton("Multi Player"), e -> FRMMain.switchToServerBrowser());
//        addComponent(new JButton("PNLOptions"), e -> FRMMain.switchToOptions());
        addComponent(new JButton("Back"), e -> FRMMain.switchTo(FRMMain.Panel.MAIN_MENU));
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
