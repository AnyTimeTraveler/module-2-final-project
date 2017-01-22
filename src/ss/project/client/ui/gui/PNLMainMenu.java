package ss.project.client.ui.gui;

import ss.project.client.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by simon on 16.01.17.
 */
public class PNLMainMenu extends GUIPanel {

    private Controller controller;

    public PNLMainMenu(Controller controller) {
        super();
        this.controller = controller;
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

    @Override
    public void onEnter() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addComponent(new JLabel("Main Menu"));
        addComponent(new JButton("Single Player"), e -> controller.switchTo(Controller.Panel.SINGLE_PLAYER_SETTINGS));
        addComponent(new JButton("Multi Player"), e -> controller.switchTo(Controller.Panel.SERVER_BRWOSER));
        addComponent(new JButton("PNLOptions"), e -> controller.switchTo(Controller.Panel.OPTIONS));
        addComponent(new JButton("Exit"), e -> controller.shutdown());
    }

    @Override
    public void onLeave() {

    }
}
