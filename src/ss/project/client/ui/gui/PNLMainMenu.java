package ss.project.client.ui.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by simon on 16.01.17.
 */
public class PNLMainMenu extends JPanel {

    public PNLMainMenu(FRMMain FRMMain) {
        super(true);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addComponent(new JLabel("Main Menu"));
        addComponent(new JButton("Single Player"), e -> FRMMain.switchTo(FRMMain.Panel.SINGLE_PLAYER_SETTINGS));
        addComponent(new JButton("Multi Player"), e -> FRMMain.switchTo(FRMMain.Panel.SERVER_BRWOSER));
        addComponent(new JButton("PNLOptions"), e -> FRMMain.switchTo(FRMMain.Panel.OPTIONS));
        addComponent(new JButton("Exit"), e -> FRMMain.shutdown());
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
