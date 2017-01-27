package ss.project.client.ui.gui;

import ss.project.client.Config;
import ss.project.client.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by simon on 16.01.17.
 */
public class PNLOptions extends GUIPanel {

    private final Controller controller;
    private JCheckBox fullScreenCheckbox;

    public PNLOptions(Controller controller) {
        super();
        this.controller = controller;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addComponent(GUIUtils.createLabel("Options", GUIUtils.LabelType.TITLE));
        fullScreenCheckbox = GUIUtils.createCheckBox("Fullscreen");
        //TODO:Add Resolution Spinners
        addComponent(fullScreenCheckbox, e -> swichFullscreen(((JCheckBox) e.getSource()).isSelected()));
        addComponent(GUIUtils.createButton("Back"), e -> controller.switchTo(Controller.Panel.MAIN_MENU));
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

    private void addComponent(JCheckBox comp, ActionListener actionListener) {
        comp.setAlignmentX(Component.CENTER_ALIGNMENT);
        comp.addActionListener(actionListener);
        this.add(comp);
    }

    private void swichFullscreen(boolean isFullscreen) {
        Config.getInstance().Fullscreen = isFullscreen;
        controller.restartFrame();
    }

    @Override
    public void onEnter() {
        fullScreenCheckbox.setSelected(Config.getInstance().Fullscreen);
    }

    @Override
    public void onLeave() {

    }
}
