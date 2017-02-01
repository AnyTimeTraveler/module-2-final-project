package ss.project.client.ui.gui;

import ss.project.client.Controller;
import ss.project.shared.model.ChatMessage;
import ss.project.shared.model.ClientConfig;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by fw on 27/01/2017.
 */
public class PNLChat extends GUIPanel {
    private final JTextArea chatArea;
    private HintTextField inputField;
    private Controller controller;

    PNLChat(Controller controller) {
        this.controller = controller;
        this.setLayout(new BorderLayout());
        this.add(GUIUtils.createLabel("Chat", GUIUtils.LabelType.TITLE), BorderLayout.NORTH);
        chatArea = new JTextArea();
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scrollPane, BorderLayout.CENTER);
        inputField = new HintTextField("Chat message...");
        inputField.addActionListener(e -> onSend(inputField.getText()));
        this.add(inputField, BorderLayout.SOUTH);
    }

    private void onSend(String message) {
        controller.sendChatMessage(message);
        inputField.setText("");
    }

    @Override
    public void onEnter() {
        this.setPreferredSize(new Dimension(150, 0));
        this.setMinimumSize(new Dimension(150, 0));
    }

    @Override
    public void onLeave() {

    }

    public void update() {
        List<ChatMessage> messages = controller.getRecentChatMessages(ClientConfig.getInstance().MaxChatMessages);
        StringBuilder sb = new StringBuilder();
        for (ChatMessage message : messages) {
            sb.append(formatMessage(message));
            sb.append("\n");
        }
        chatArea.setText(sb.toString());
    }

    private String formatMessage(ChatMessage message) {
        return message.getName() + " : " + message.getMessage();
    }
}
