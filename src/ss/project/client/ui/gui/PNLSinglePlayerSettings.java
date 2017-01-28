package ss.project.client.ui.gui;

import lombok.extern.java.Log;
import ss.project.client.Config;
import ss.project.client.Controller;
import ss.project.client.nameGenerator.NameGenerator;
import ss.project.shared.computerplayer.ComputerPlayer;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by simon on 16.01.17.
 */
@Log
public class PNLSinglePlayerSettings extends GUIPanel {
    NameGenerator nameGenerator = new NameGenerator();
    private JLabel headline;
    private JSpinner worldX;
    private JSpinner worldY;
    private JSpinner worldZ;
    private JSpinner playerAmount;
    private JSpinner winLength;
    private java.util.List<PlayerPanel> playerPanels = new ArrayList<>();
    private Controller controller;
    private GridBagConstraints gridBagConstraints;
    private int x = 0;
    private int y = 0;
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
        gridBagConstraints.gridwidth = gridBagConstraints.REMAINDER;
        gridBagConstraints.fill = gridBagConstraints.HORIZONTAL;

        add(GUIUtils.createLabel("Single Player", GUIUtils.LabelType.TITLE), gridBagConstraints);

        nextRow();
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;

        add(createLabel("World X:"), gridBagConstraints);
        nextColumn();
        add(worldX = createSpinner(4, 0, 100), gridBagConstraints);
        nextColumn();
        add(createLabel("World Y:"), gridBagConstraints);
        nextColumn();
        add(worldY = createSpinner(4, 0, 100), gridBagConstraints);
        nextColumn();
        add(createLabel("World Z:"), gridBagConstraints);
        nextColumn();
        add(worldZ = createSpinner(4, 0, 100), gridBagConstraints);

        nextRow();
        nextRow();
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;

        add(createLabel("Player Amount:"), gridBagConstraints);
        playerAmount = createSpinner(2, 2, 10);
        playerAmount.addChangeListener(e -> addPlayerPanes(((SpinnerNumberModel) playerAmount.getModel()).getNumber().intValue()));
        nextColumn();
        add(playerAmount, gridBagConstraints);
        nextColumn();
        add(createLabel("Win Length:"), gridBagConstraints);
        nextColumn();
        add(winLength = createSpinner(4, 1, 100), gridBagConstraints);

        nextRow();
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;

        /**
         * Create the panel with a list of players.
         */
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
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, min, max, 1));
        return spinner;
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
                pp.playerNameField.setText(nameGenerator.generateName().getFirstName());
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
            for (String key : Config.getInstance().PlayerTypes.keySet()) {
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
            Class smthing = Config.getInstance().PlayerTypes.get(getPlayerType());
            if (ComputerPlayer.class.isAssignableFrom(smthing)) {
                computerSmartness.setVisible(true);
            } else {
                computerSmartness.setVisible(false);
            }
        }

        /**
         * Get the name of the namefield.
         *
         * @return
         */
        public String getName() {
            return playerNameField.getText();
        }

        /**
         * Get the player type of this player. (Human, MinMax etc.)
         *
         * @return
         */
        public String getPlayerType() {
            return (String) playerType.getSelectedItem();
        }

        /**
         * @return True if the player is of type computerplayer. And thus has a smartness to set.
         */
        public boolean isComputerPlayer() {
            return computerSmartness.isVisible();
        }

        /**
         * Get the smartness of the computerplayer.
         *
         * @return
         */
        public int getComputerSmartness() {
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

                Player[] players = new Player[playerCount];
                for (int i = 0; i < playerCount; i++) {
                    Class c = Config.getInstance().PlayerTypes.get(playerPanels.get(i).getPlayerType());
                    players[i] = (Player) c.newInstance();
                    players[i].setName(playerPanels.get(i).getName());
                    players[i] = (Player) Config.getInstance().PlayerTypes.get(playerPanels.get(i).getPlayerType()).newInstance();
                    players[i].setName(playerPanels.get(i).getName());
                    players[i].setId(i);
                    if (playerPanels.get(i).isComputerPlayer()) {
                        ((ComputerPlayer) players[i]).setSmartness(playerPanels.get(i).getComputerSmartness());
                    }
                }

                Vector3 worldSize = new Vector3(worldSizeX, worldSizeY, worldSizeZ);
                Engine engine = new Engine(worldSize, winLen, players);

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
