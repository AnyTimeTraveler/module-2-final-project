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

    private JButton createButton(String text) {
        JButton result = new JButton(text);

        result.setPreferredSize(new Dimension(100, 50));
        result.setMaximumSize(new Dimension(150, 70));

        return result;
    }

    private JLabel createLabel(String text) {
        JLabel result = new JLabel(text, SwingConstants.CENTER);

        result.setAlignmentX(Component.CENTER_ALIGNMENT);
        result.setPreferredSize(new Dimension(100, 50));
        result.setMaximumSize(new Dimension(150, 70));

        return result;
    }

    @Override
    public void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        addComponent(createLabel("Main Menu"));
        addComponent(createButton("Single Player"), e -> controller.switchTo(Controller.Panel.SINGLE_PLAYER_SETTINGS));
        addComponent(createButton("Multi Player"), e -> controller.switchTo(Controller.Panel.SERVER_BRWOSER));
        addComponent(createButton("Options"), e -> controller.switchTo(Controller.Panel.OPTIONS));
        addComponent(createButton("Exit"), e -> controller.shutdown());
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onLeave() {

    }
}
