package ss.project.client.ui.gui;

import ss.project.client.Controller;
import ss.project.server.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by simon on 16.01.17.
 */
public class PNLMultiPlayerRoom extends GUIPanel {
    Room room;
    Timer timer;
    JLabel waitingLabel;
    /**
     * Used for the waiting animation.
     */
    int waitingState;

    public PNLMultiPlayerRoom(Controller controller) {
        room = controller.getCurrentRoom();

        this.setLayout(new BorderLayout());
        this.add(GUIUtils.createLabel("Room " + room.getId(), GUIUtils.LabelType.TITLE), BorderLayout.NORTH);
        waitingLabel = GUIUtils.createLabel("Waiting", GUIUtils.LabelType.TITLE);
        this.add(waitingLabel, BorderLayout.CENTER);

        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String waitText = "Waiting";
                for (int i = 0; i < waitingState % 4; i++) {
                    waitText += ".";
                }
                waitingState++;
                waitingLabel.setText(waitText);
            }
        };


        timer = new Timer(200, taskPerformer);
        timer.start();

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> controller.leaveRoom());
        this.add(backButton, BorderLayout.SOUTH);
    }

    @Override
    public void onEnter() {
        timer.start();
    }

    @Override
    public void onLeave() {
        timer.stop();
    }
}
