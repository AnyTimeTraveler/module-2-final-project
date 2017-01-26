package ss.project.client.ui.gui;

import ss.project.client.Controller;

import javax.swing.*;
import java.awt.*;

/**
 * Created by simon on 16.01.17.
 */
public class PNLMultiPlayerRoomCreation extends GUIPanel {
    private JSpinner worldX;
    private JSpinner worldY;
    private JSpinner worldZ;
    private JSpinner amountOfPlayers;
    private JSpinner winLength;

    public PNLMultiPlayerRoomCreation(Controller controller) {
        this.setLayout(new BorderLayout());

        this.add(GUIUtils.createLabel("Create room", GUIUtils.LabelType.TITLE), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints centerPanelConstraints = new GridBagConstraints();
        populateCenterPanel(centerPanel, centerPanelConstraints);
        this.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> controller.switchTo(Controller.Panel.MULTI_PLAYER_LOBBY));
        bottomPanel.add(backButton);

        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> createRoom());
        bottomPanel.add(createButton);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    void populateCenterPanel(JPanel centerPanel, GridBagConstraints gridBagConstraints) {
        nextColumn(gridBagConstraints);
        centerPanel.add(GUIUtils.createLabel("World X:"), gridBagConstraints);
        nextColumn(gridBagConstraints);
        worldX = GUIUtils.createSpinner(4, 0, 100);
        centerPanel.add(worldX, gridBagConstraints);
        nextColumn(gridBagConstraints);
        centerPanel.add(GUIUtils.createLabel("World Y:"), gridBagConstraints);
        nextColumn(gridBagConstraints);
        worldY = GUIUtils.createSpinner(4, 0, 100);
        centerPanel.add(worldY, gridBagConstraints);
        nextColumn(gridBagConstraints);
        centerPanel.add(GUIUtils.createLabel("World Z:"), gridBagConstraints);
        nextColumn(gridBagConstraints);
        worldZ = GUIUtils.createSpinner(4, 0, 100);
        centerPanel.add(worldZ, gridBagConstraints);

        nextRow(gridBagConstraints);
        nextRow(gridBagConstraints);

        nextColumn(gridBagConstraints);
        centerPanel.add(GUIUtils.createLabel("Amount of players:"), gridBagConstraints);
        nextColumn(gridBagConstraints);
        amountOfPlayers = GUIUtils.createSpinner(2, 2, 100);
        centerPanel.add(amountOfPlayers, gridBagConstraints);
        nextColumn(gridBagConstraints);
        centerPanel.add(GUIUtils.createLabel("Win length:"), gridBagConstraints);
        nextColumn(gridBagConstraints);
        winLength = GUIUtils.createSpinner(4, 1, 100);
        centerPanel.add(winLength, gridBagConstraints);

        nextRow(gridBagConstraints);
    }

    void createRoom() {
        int playerCount = (int) amountOfPlayers.getValue();
        int worldSizeX = (int) worldX.getValue();
        int worldSizeY = (int) worldY.getValue();
        int worldSizeZ = (int) worldZ.getValue();
        int winLen = (int) winLength.getValue();
        Controller.controller.createRoom(playerCount, worldSizeX, worldSizeY, worldSizeZ, winLen);
    }

    void nextColumn(GridBagConstraints gridBagConstraints) {
        gridBagConstraints.gridx++;
    }

    void nextRow(GridBagConstraints gridBagConstraints) {
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy++;
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onLeave() {

    }
}
