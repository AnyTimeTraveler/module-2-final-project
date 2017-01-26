package ss.project.client.ui.gui;

import ss.project.client.Controller;

import javax.swing.*;
import java.awt.*;

/**
 * Created by fw on 26/01/2017.
 */
public class PNLGameEnd extends GUIPanel {
    JLabel informationLabel;

    public PNLGameEnd(Controller controller) {
        this.setLayout(new BorderLayout());

        this.add(GUIUtils.createLabel("End", GUIUtils.LabelType.TITLE), BorderLayout.NORTH);
        informationLabel = GUIUtils.createLabel("", GUIUtils.LabelType.TITLE);
        this.add(informationLabel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> Controller.getController().switchTo(Controller.Panel.MULTI_PLAYER_LOBBY));
        bottomPanel.add(backButton);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    @Override
    public void onEnter() {
        int winner = Controller.getController().getEngine().getWinner();
        switch (Controller.getController().getEngine().getWinReason()) {
            case WINLENGTHACHIEVED: {
                informationLabel.setText(Controller.getController().getEngine().getPlayer(winner).getName() + " won the game!");
                break;
            }
            case BOARDISFULL: {
                informationLabel.setText("Draw!");
                break;
            }
            case PLAYERDISCONNECTED: {
                informationLabel.setText(Controller.getController().getEngine().getPlayer(winner).getName() + " disconnected... :(");
                break;
            }
            case GAMETIMEOUT: {
                informationLabel.setText("Lost connection... :(");
                break;
            }
        }
    }

    @Override
    public void onLeave() {

    }
}
