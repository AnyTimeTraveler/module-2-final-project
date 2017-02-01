package ss.project.client.ui.gui;

import lombok.extern.java.Log;
import ss.project.client.Controller;
import ss.project.shared.computerplayer.ComputerPlayer;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.model.ClientConfig;
import ss.project.shared.model.GameParameters;
import ss.project.shared.model.NameList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

/**
 * Created by simon on 16.01.17.
 */
@Log
public class PNLSinglePlayerSettings extends GUIPanel {
    private JSpinner worldX;
    private JSpinner worldY;
    private JSpinner worldZ;
    private JSpinner playerAmount;
    private JSpinner winLength;
    private java.util.List<PlayerPanel> playerPanels = new ArrayList<>();
    private Controller controller;
    private GridBagConstraints gridBagConstraints;
    /**
     * Panel that is responsible for the player list.
     */
    private JPanel playerPanel;

    // Worls Size
    // Player count
    // Win length

    public PNLSinglePlayerSettings(Controller controller) {
        super();
        this.controller = controller;

        GridBagLayout mgr = new GridBagLayout();
        this.setLayout(mgr);
        gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

        add(GUIUtils.createLabel("Single Player", GUIUtils.LabelType.TITLE), gridBagConstraints);

        nextRow();
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;

        add(createLabel("World X:"), gridBagConstraints);
        nextColumn();
        worldX = createSpinner(4, 0, 100);
        add(worldX, gridBagConstraints);
        nextColumn();
        add(createLabel("World Y:"), gridBagConstraints);
        nextColumn();
        worldY = createSpinner(4, 0, 100);
        add(worldY, gridBagConstraints);
        nextColumn();
        add(createLabel("World Z:"), gridBagConstraints);
        nextColumn();
        worldZ = createSpinner(4, 0, 100);
        add(worldZ, gridBagConstraints);

        nextRow();
        nextRow();
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;

        add(createLabel("Player Amount:"), gridBagConstraints);
        playerAmount = createSpinner(2, 2, 10);
        playerAmount.addChangeListener(e ->
                addPlayerPanes(((SpinnerNumberModel) playerAmount.getModel()).getNumber().intValue()));
        nextColumn();
        add(playerAmount, gridBagConstraints);
        nextColumn();
        add(createLabel("Win Length:"), gridBagConstraints);
        nextColumn();
        winLength = createSpinner(4, 1, 100);
        add(winLength, gridBagConstraints);

        nextRow();
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;

        //Create the panel with a list of players.
        playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        this.add(addPlayerPanes(2), gridBagConstraints);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> controller.switchTo(Controller.Panel.MAIN_MENU));
        this.add(backButton);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new MyActionListener());
        this.add(startButton);

    }

    private void nextRow() {
        gridBagConstraints.gridy++;
        gridBagConstraints.gridx = 0;
    }

    private void nextColumn() {
        gridBagConstraints.gridx += 2;
    }

    private JSpinner createSpinner(int value, int min, int max) {
        return new JSpinner(new SpinnerNumberModel(value, min, max, 1));
    }

    private JLabel createLabel(String text) {
        return new JLabel(text);
    }

    private JPanel addPlayerPanes(int players) {
        playerPanel.removeAll();

        for (int i = 0; i < players; i++) {
            PlayerPanel pp;
            if (playerPanels.size() > i) {
                pp = playerPanels.get(i);
            } else {
                pp = new PlayerPanel();
                pp.playerNameField.setText(NameList.NAMES[new Random().nextInt(NameList.NAMES.length)]);
            }
            pp.setAlignmentX(Component.CENTER_ALIGNMENT);
            if (!playerPanels.contains(pp)) {
                playerPanels.add(pp);
            }
            playerPanel.add(pp);
        }
        this.repaint();
        playerPanel.revalidate();
        return playerPanel;
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onLeave() {

    }

    private class PlayerPanel extends JPanel {

        private final JTextField playerNameField;
        private final JComboBox<String> playerType;
        private final JSpinner computerSmartness;

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
            playerType.addActionListener(e -> updateSmartness());
            this.add(playerType);
            computerSmartness = GUIUtils.createSpinner(6, 1, 100);
            computerSmartness.setVisible(false);
            this.add(computerSmartness);
        }

        /**
         * The playertype has changed, check if this is a computer. If so, show the smartness spinner.
         */
        private void updateSmartness() {
            Class smthing = ClientConfig.getInstance().playerTypes.get(getPlayerType());
            if (ComputerPlayer.class.isAssignableFrom(smthing)) {
                computerSmartness.setVisible(true);
            } else {
                computerSmartness.setVisible(false);
            }
        }

        /**
         * Get the name of the namefield.
         *
         * @return Name of the player
         */
        public String getName() {
            return playerNameField.getText();
        }

        /**
         * Get the player type of this player. (Human, MinMax etc.)
         *
         * @return a String representing the chosen playerType
         */
        String getPlayerType() {
            return (String) playerType.getSelectedItem();
        }

        /**
         * @return True if the player is of type computerplayer. And thus has a smartness to set.
         */
        boolean isComputerPlayer() {
            return computerSmartness.isVisible();
        }

        /**
         * Get the smartness of the computerplayer.
         *
         * @return the smartness-value of the computerplayer.
         */
        int getComputerSmartness() {
            return (int) computerSmartness.getValue();
        }
    }

    private class MyActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                //Create a new Engine with spefied settings.
                int playerCount = (int) playerAmount.getValue();
                int worldSizeX = (int) worldX.getValue();
                int worldSizeY = (int) worldY.getValue();
                int worldSizeZ = (int) worldZ.getValue();
                int winLen = (int) winLength.getValue();

                List<Player> players = new ArrayList<>();
                for (int i = 0; i < playerCount; i++) {
                    Class<? extends Player> playerType = ClientConfig.getInstance()
                            .playerTypes.get(playerPanels.get(i).getPlayerType());
                    Player player = playerType.newInstance();
                    player.setName(playerPanels.get(i).getName());
                    player = ClientConfig.getInstance().
                            playerTypes.get(playerPanels.get(i).getPlayerType()).newInstance();
                    player.setName(playerPanels.get(i).getName());
                    player.setId(i);
                    if (playerPanels.get(i).isComputerPlayer()) {
                        ((ComputerPlayer) player).setSmartness(playerPanels.get(i).getComputerSmartness());
                    }
                    players.add(player);
                }
                Engine engine = new Engine(new GameParameters(
                        worldSizeX, worldSizeY, worldSizeZ, winLen), players, false);

                controller.setEngine(engine);
                controller.switchTo(Controller.Panel.GAME);
                controller.startGame();

            } catch (NumberFormatException ex) {
                log.log(Level.WARNING, "Invalid Input!", ex);
            } catch (IllegalAccessException | InstantiationException ex) {
                log.log(Level.SEVERE, "This should never happen", ex);
                log.log(Level.INFO, "Does the requested player type have a constructor without arguments?");
            }
        }
    }
}
