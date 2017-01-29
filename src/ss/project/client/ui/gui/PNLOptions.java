package ss.project.client.ui.gui;

import ss.project.client.Controller;
import ss.project.shared.model.ClientConfig;

import javax.swing.*;
import java.awt.*;

/**
 * Created by simon on 16.01.17.
 */
public class PNLOptions extends GUIPanel {

    private final Controller controller;
    private JCheckBox fullScreenCheckbox;
    private JTextField playerTextField;
    private JCheckBox autoRefreshCheckbox;
    private JSpinner maxChatMessages;

    public PNLOptions(Controller controller) {
        super();
        this.controller = controller;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addComponent(GUIUtils.createLabel("Options", GUIUtils.LabelType.TITLE));

        JPanel playerTextPanel = new JPanel();
        playerTextPanel.add(GUIUtils.createLabel("Player name: "));
        playerTextField = GUIUtils.createTextField(ClientConfig.getInstance().PlayerName);
        playerTextPanel.add(playerTextField);
        playerTextPanel.setPreferredSize(new Dimension(1000, 50));
        playerTextPanel.setMaximumSize(new Dimension(1000, 50));
        add(playerTextPanel);

        autoRefreshCheckbox = GUIUtils.createCheckBox("Auto refresh");
        autoRefreshCheckbox.setSelected(ClientConfig.getInstance().AutoRefresh);
        addComponent(autoRefreshCheckbox);

        JPanel chatMessagePanel = new JPanel();
        JLabel label = GUIUtils.createLabel("Max chat messages: ");
        label.setPreferredSize(new Dimension(200, 50));
        label.setMaximumSize(new Dimension(200, 50));
        chatMessagePanel.add(label);
        maxChatMessages = GUIUtils.createSpinner(ClientConfig.getInstance().MaxChatMessages, 1, 200);
        maxChatMessages.setPreferredSize(new Dimension(200, 50));
        maxChatMessages.setMaximumSize(new Dimension(200, 50));
        chatMessagePanel.add(maxChatMessages);
        chatMessagePanel.setPreferredSize(new Dimension(1000, 50));
        chatMessagePanel.setMaximumSize(new Dimension(1000, 50));
        add(chatMessagePanel);

        fullScreenCheckbox = GUIUtils.createCheckBox("Fullscreen");
        fullScreenCheckbox.setSelected(ClientConfig.getInstance().Fullscreen);
        addComponent(fullScreenCheckbox);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton backbutton = GUIUtils.createButton("Back");
        backbutton.addActionListener(e -> {
            reInit();
            controller.switchTo(Controller.Panel.MAIN_MENU);
        });
        bottomPanel.add(backbutton);
        JButton applyButton = GUIUtils.createButton("Apply");
        applyButton.addActionListener(e -> apply());
        bottomPanel.add(applyButton);
        this.add(bottomPanel);
    }

    private void addComponent(JComponent comp) {
        comp.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(comp);
    }

    private void swichFullscreen(boolean isFullscreen) {
        ClientConfig.getInstance().Fullscreen = isFullscreen;
        controller.restartFrame();
    }

    /**
     * Apply the changed settings.
     */
    private void apply() {
        if (!playerTextField.getText().equals(ClientConfig.getInstance().PlayerName)) {
            ClientConfig.getInstance().PlayerName = playerTextField.getText();
        }

        if (autoRefreshCheckbox.isSelected() != ClientConfig.getInstance().AutoRefresh) {
            ClientConfig.getInstance().AutoRefresh = autoRefreshCheckbox.isSelected();
        }

        if ((int) maxChatMessages.getValue() != ClientConfig.getInstance().MaxChatMessages) {
            ClientConfig.getInstance().MaxChatMessages = (int) maxChatMessages.getValue();
        }

        if (fullScreenCheckbox.isSelected() != ClientConfig.getInstance().Fullscreen) {
            swichFullscreen(fullScreenCheckbox.isSelected());
        }

        controller.switchTo(Controller.Panel.MAIN_MENU);
    }

    /**
     * Go back to the values of the config.
     * Discard all changes the user has made.
     */
    private void reInit() {
        fullScreenCheckbox.setSelected(ClientConfig.getInstance().Fullscreen);
        playerTextField.setText(ClientConfig.getInstance().PlayerName);
        autoRefreshCheckbox.setSelected(ClientConfig.getInstance().AutoRefresh);
        maxChatMessages.setValue(ClientConfig.getInstance().MaxChatMessages);
    }

    @Override
    public void onEnter() {
        fullScreenCheckbox.setSelected(ClientConfig.getInstance().Fullscreen);
    }

    @Override
    public void onLeave() {

    }
}
