package ss.project.client.ui.gui;

import lombok.extern.java.Log;
import ss.project.client.Config;
import ss.project.client.Controller;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

/**
 * Created by simon on 16.01.17.
 */
@Log
public class PNLSinglePlayerSettings extends GUIPanel {
    private JLabel headline;
    private JSpinner worldX;
    private JSpinner worldY;
    private JSpinner worldZ;
    private JSpinner playerAmount;
    private JSpinner winLength;
    private PlayerPanel[] playerPanels;
    private Controller controller;
    private GridBagConstraints c;

    private int x = 0;
    private int y = 0;

    // Worls Size
    // Player count
    // Win length

    public PNLSinglePlayerSettings(Controller controller) {
        super();
        this.controller = controller;
    }

    private void nextRow() {
        y++;
    }

    private JSpinner createSpinner(int value, int min, int max, int width, int height) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, min, max, 1));
        c.gridx = x++;
        c.gridy = y;
        c.gridwidth = width;
        c.gridheight = height;
        c.weightx = 0.5f;
        c.fill = GridBagConstraints.HORIZONTAL;
        return spinner;
    }

    private JLabel createLabel(String text, int width, int height) {
        JLabel label = new JLabel(text);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = width;
        c.gridheight = height;
        return label;
    }

    private JPanel addPlayerPanes(int players) {
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < players; i++) {
            PlayerPanel pp = new PlayerPanel();
            pp.setAlignmentX(Component.CENTER_ALIGNMENT);
            playerPanel.add(pp);
        }
        return playerPanel;
    }

    @Override
    public void onEnter() {
        GridBagLayout mgr = new GridBagLayout();
        this.setLayout(mgr);
        c = new GridBagConstraints();

        add(createLabel("Single Player", 4, 1));
        nextRow();
        add(createLabel("World X:", 4, 1));
        add(worldX = createSpinner(4, 0, 100, 1, 1));
        add(createLabel("World Y:", 4, 1));
        add(worldY = createSpinner(4, 0, 100, 1, 1));
        add(createLabel("World Z:", 4, 1));
        add(worldZ = createSpinner(4, 0, 100, 1, 1));
        add(createLabel("Player Amount:", 4, 1));
        playerAmount = createSpinner(4, 0, 100, 1, 1);
        playerAmount.addChangeListener(e -> addPlayerPanes(((SpinnerNumberModel) playerAmount.getModel()).getNumber().intValue()));
        add(playerAmount);
        add(createLabel("Win Length:", 4, 1));
        add(winLength = createSpinner(4, 0, 100, 1, 1));

        nextRow();
        c.gridx = 0;
        c.gridy = 2;
        c.gridheight = 3;
        c.gridwidth = 4;
        this.add(addPlayerPanes(3), c);

        nextRow();
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> controller.switchTo(Controller.Panel.GAME));
//        startButton.addActionListener(new MyActionListener());
        c.gridx = 4;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(startButton);
        playerPanels = new PlayerPanel[0];
    }

    @Override
    public void onLeave() {

    }

    private class PlayerPanel extends JPanel {

        private final JTextField playerNameField;
        private final JComboBox<String> playerType;

        private PlayerPanel() {
            super();
            this.setLayout(new FlowLayout());
            this.add(new JLabel("Name:"));
            playerNameField = new JTextField();
            playerNameField.setColumns(15);
            this.add(playerNameField);
            this.add(new JLabel("Type:"));
            playerType = new JComboBox<>();
            for (String key : Config.getInstance().playerTypes.keySet()) {
                playerType.addItem(key);
            }
            this.add(playerType);
        }

        public String getName() {
            return playerNameField.getText();
        }

        public String getPlayerType() {
            return (String) playerType.getSelectedItem();
        }
    }

    private class MyActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int playerCount = (int) playerAmount.getValue();
                int worldSizeX = (int) worldX.getValue();
                int worldSizeY = (int) worldY.getValue();
                int worldSizeZ = (int) worldZ.getValue();

                Player[] players = new Player[playerCount];
                for (int i = 0; i < playerCount; i++) {
                    players[i] = (Player) Config.getInstance().playerTypes.get(playerPanels[i].getPlayerType()).newInstance();
                    players[i].setName(playerPanels[i].getName());
                }

                Vector3 worldSize = new Vector3(worldSizeX, worldSizeY, worldSizeZ);
                Engine engine = new Engine(worldSize, players);

            } catch (NumberFormatException ex) {
                log.log(Level.WARNING, "Invalid Input!", ex);
            } catch (IllegalAccessException | InstantiationException ex) {
                log.log(Level.SEVERE, "This should never happen", ex);
            }
        }
    }
}
