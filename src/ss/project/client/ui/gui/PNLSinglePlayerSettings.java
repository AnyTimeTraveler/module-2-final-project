package ss.project.client.ui.gui;

import lombok.extern.java.Log;
import ss.project.client.ClientConfig;
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
public class PNLSinglePlayerSettings extends JPanel {
    private JLabel headline;
    private JSpinner worldX;
    private JSpinner worldY;
    private JSpinner worldZ;
    private JSpinner playerAmount;
    private JSpinner winLength;
    private PlayerPanel[] playerPanels;
    private FRMMain FRMMain;

    // Worls Size
    // Player count
    // Win length

    public PNLSinglePlayerSettings(FRMMain FRMMain) {
        super();
        this.FRMMain = FRMMain;
        GridBagLayout mgr = new GridBagLayout();
        this.setLayout(mgr);
        GridBagConstraints c = new GridBagConstraints();

        drawHeadline(c);
        addSpinner(worldX, c, 4, 0, 100, 0, 1, 1, 1);
        addSpinner(worldY, c, 4, 0, 100, 1, 1, 1, 1);
        addSpinner(worldZ, c, 4, 0, 100, 2, 1, 1, 1);
        addSpinner(playerAmount, c, 4, 0, 100, 3, 1, 1, 1);
        addSpinner(winLength, c, 4, 0, 100, 4, 1, 1, 1);

        c.gridx = 0;
        c.gridy = 2;
        c.gridheight = 3;
        c.gridwidth = 4;
        this.add(addPlayerPanes(3), c);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new MyActionListener());
        c.gridx = 4;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(startButton);
        playerPanels = new PlayerPanel[0];
    }

    private void addSpinner(JSpinner spinner, GridBagConstraints c, int value, int min, int max, int gridX, int gridY, int width, int height) {
        spinner = new JSpinner(new SpinnerNumberModel(value, min, max, 1));
        c.gridx = gridX;
        c.gridy = gridY;
        c.gridwidth = width;
        c.gridheight = height;
        this.add(spinner, c);
    }

    private void drawHeadline(GridBagConstraints c) {
        headline = new JLabel("Single Player");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        this.add(headline, c);
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
            for (String key : ClientConfig.getInstance().playerTypes.keySet()) {
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
                    players[i] = (Player) ClientConfig.getInstance().playerTypes.get(playerPanels[i].getPlayerType()).newInstance();
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
