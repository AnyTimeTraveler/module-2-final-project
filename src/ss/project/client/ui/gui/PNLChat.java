package ss.project.client.ui.gui;

import ss.project.client.Controller;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by fw on 27/01/2017.
 */
public class PNLChat extends GUIPanel {
    java.util.List<JLabel> chatMessages;
    JTextField inputField;

    public PNLChat() {
        this.setLayout(new BorderLayout());
        this.add(GUIUtils.createLabel("Chat", GUIUtils.LabelType.TITLE), BorderLayout.NORTH);

        inputField = new HintTextField("Chat message...");
        inputField.addActionListener(e -> doInput(inputField.getText()));
        this.add(inputField, BorderLayout.SOUTH);

        chatMessages = new ArrayList<>();
    }

    private void doInput(String input) {
        System.out.println(input);
        if (input != null && !input.isEmpty()) {
            Controller.getController().sendChatMessage(input);
        }
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onLeave() {

    }


}
