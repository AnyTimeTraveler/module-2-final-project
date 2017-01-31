package ss.project.client.ui.gui;

import ss.project.client.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by simon on 16.01.17.
 */
public class PNLMainMenu extends GUIPanel {

    public PNLMainMenu(Controller controller) {
        super();

        this.setLayout(new BorderLayout());
        this.add(GUIUtils.createLabel("Main Menu", GUIUtils.LabelType.TITLE), BorderLayout.NORTH);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
        addComponent(GUIUtils.createButton("Single Player"), e -> controller.switchTo(Controller.Panel.SINGLE_PLAYER_SETTINGS), centerPanel);
        addComponent(GUIUtils.createButton("Multi Player"), e -> controller.switchTo(Controller.Panel.SERVER_BROWSER), centerPanel);
        addComponent(GUIUtils.createButton("Options"), e -> controller.switchTo(Controller.Panel.OPTIONS), centerPanel);
        addComponent(GUIUtils.createButton("Exit"), e -> controller.shutdown(), centerPanel);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    private void addComponent(JButton comp, ActionListener actionListener, JPanel panel) {
        comp.setAlignmentX(Component.CENTER_ALIGNMENT);
        comp.addActionListener(actionListener);
        panel.add(comp);
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onLeave() {

    }
}
