package ss.project.client.ui.gui;

import ss.project.client.Controller;

import javax.swing.*;
import java.awt.*;

/**
 * Created by simon on 16.01.17.
 */
public class PNLMultiPlayerRoom extends GUIPanel {
    private final JLabel headline;
    private final Controller controller;
    Timer timer;
    JLabel waitingLabel;
    /**
     * Used for the waiting animation.
     */
    int waitingState;

    public PNLMultiPlayerRoom(Controller controller) {
        this.controller = controller;
        this.setLayout(new BorderLayout());
        headline = GUIUtils.createLabel("Room null", GUIUtils.LabelType.TITLE);
        this.add(headline, BorderLayout.NORTH);
        waitingLabel = GUIUtils.createLabel("Waiting", GUIUtils.LabelType.TITLE);
        this.add(waitingLabel, BorderLayout.CENTER);

        timer = new Timer(200, evt -> {
            String waitText = "Waiting";
            for (int i = 0; i < waitingState % 4; i++) {
                waitText += ".";
            }
            waitingState++;
            waitingLabel.setText(waitText);
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> controller.leaveRoom());
        this.add(backButton, BorderLayout.SOUTH);
    }

    @Override
    public void onEnter() {
        headline.setText("Room " + controller.getCurrentRoom().getId());
        timer.start();
    }

    @Override
    public void onLeave() {
        timer.stop();
    }
}
