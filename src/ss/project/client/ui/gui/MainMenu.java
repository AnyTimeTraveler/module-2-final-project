package ss.project.client.ui.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by simon on 16.01.17.
 */
public class MainMenu extends JPanel {

    public MainMenu(MainFrame mainFrame) {
        super(true);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addComponent(new JLabel("Main Menu"));
        addComponent(new JButton("Single Player"), e -> mainFrame.switchToSinglePlayerSettings());
        addComponent(new JButton("Multi Player"), e -> mainFrame.switchToServerBrowser());
        addComponent(new JButton("Options"), e -> mainFrame.switchToOptions());
        addComponent(new JButton("Exit"), e -> System.exit(0));
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
