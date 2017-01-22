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
    public void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        addComponent(GUIUtils.createLabel("Main Menu", GUIUtils.LabelType.TITLE));
        addComponent(GUIUtils.createButton("Single Player"), e -> controller.switchTo(Controller.Panel.SINGLE_PLAYER_SETTINGS));
        addComponent(GUIUtils.createButton("Multi Player"), e -> controller.switchTo(Controller.Panel.SERVER_BRWOSER));
        addComponent(GUIUtils.createButton("Options"), e -> controller.switchTo(Controller.Panel.OPTIONS));
        addComponent(GUIUtils.createButton("Exit"), e -> controller.shutdown());
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onLeave() {

    }
}
