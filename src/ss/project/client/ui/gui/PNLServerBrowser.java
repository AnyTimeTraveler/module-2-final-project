package ss.project.client.ui.gui;

import ss.project.client.Config;
import ss.project.client.Controller;
import ss.project.client.networking.Connection;
import ss.project.client.networking.ServerInfo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 16.01.17.
 */
public class PNLServerBrowser extends GUIPanel {

    private Controller controller;
    private JPanel roomsPanelOwner;
    private java.util.List<ServerPanel> serverPanels;

    private int roomPanelHeight = 75;
    private int spaceBetweenText = 10;

    public PNLServerBrowser(Controller controller) {
        super(true);

        this.controller = controller;

        serverPanels = new ArrayList<ServerPanel>();

        this.setLayout(new BorderLayout());

        this.add(GUIUtils.createLabel("Servers", GUIUtils.LabelType.TITLE), BorderLayout.NORTH);

        roomsPanelOwner = new JPanel();
        roomsPanelOwner.setLayout(new BoxLayout(roomsPanelOwner, BoxLayout.Y_AXIS));
        this.add(roomsPanelOwner, BorderLayout.CENTER);

        JPanel jPanel = new JPanel();
        JButton backButton = new JButton("Back");
        jPanel.add(backButton);
        backButton.addActionListener(e -> controller.switchTo(Controller.Panel.MAIN_MENU));
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            controller.addServer(JOptionPane.showInputDialog(this, "IP:PORT"));
            onEnter();
        });
        jPanel.add(addButton);
        this.add(jPanel, BorderLayout.SOUTH);
    }

    @Override
    public void onEnter() {
        //TODO: Create a connection and retrieve all rooms.
        java.util.List<Connection> connections = Config.getInstance().KnownServers;
        List<ServerInfo> serverInfos = new ArrayList<>();
        for (int i = 0; i < connections.size(); i++) {
            serverInfos.add(new ServerInfo(ServerInfo.Status.ONLINE, 1, true, 4, 4, 4, 4, true));
        }
        addServerPanels(serverInfos);
    }

    @Override
    public void onLeave() {

    }

    private void addServerPanels(List<ServerInfo> serverInfos) {
        roomsPanelOwner.removeAll();
        for (int i = 0; i < serverInfos.size(); i++) {
            ServerPanel serverPanel;
            if (serverPanels.size() > i) {
                serverPanel = serverPanels.get(i);
                serverPanel.refreshValues();
            } else {
                serverPanel = new ServerPanel(serverInfos.get(i));
            }
            serverPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            if (!serverPanels.contains(serverPanel)) {
                serverPanels.add(serverPanel);
            }
            roomsPanelOwner.add(serverPanel);
        }
        this.repaint();
        roomsPanelOwner.revalidate();
    }

    /**
     * A panel showing information about a lobby.
     */
    private class ServerPanel extends JPanel {
        private ServerInfo serverInfo;
        private JLabel serverStatus;
        private JCheckBox chatSupport;
        private JLabel winLength;
        private JLabel playerAmount;
        private JLabel worldSize;
        private JCheckBox lobbySupport;

        /**
         * Create a new roomPanel and set the correct data.
         *
         * @param serverInfo data that should be used for the texts.
         */
        private ServerPanel(ServerInfo serverInfo) {
            super();
            this.serverInfo = serverInfo;

            this.setLayout(new BorderLayout());
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.setMaximumSize(new Dimension(10000, roomPanelHeight));

            serverStatus = new JLabel("");
            this.add(serverStatus, BorderLayout.WEST);

            JPanel jPanel = new JPanel(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = new Insets(0, 0, 0, spaceBetweenText);
            winLength = new JLabel("", SwingConstants.CENTER);
            jPanel.add(winLength, constraints);
            playerAmount = new JLabel();
            jPanel.add(playerAmount, constraints);
            worldSize = new JLabel();
            jPanel.add(worldSize, constraints);
            chatSupport = new JCheckBox("Chat", false);
            chatSupport.setEnabled(false);
            jPanel.add(chatSupport, constraints);
            lobbySupport = new JCheckBox("Lobby", false);
            lobbySupport.setEnabled(false);
            jPanel.add(lobbySupport, constraints);
            this.add(jPanel, BorderLayout.CENTER);

            JButton joinButton = new JButton("Join");
            joinButton.addActionListener(e -> {
                joinServer();
            });
            this.add(joinButton, BorderLayout.EAST);

            refreshValues();
        }

        /**
         * Refresh all values shown on the screen from this room.
         */
        public void refreshValues() {
            serverStatus.setText(serverInfo.getStatus().toString());
            winLength.setText("Win length: " + serverInfo.getMaxWinLength());
            playerAmount.setText("Players: " + serverInfo.getMaxPlayers());
            worldSize.setText("World:" + serverInfo.getMaxDimensionX() + "," + serverInfo.getMaxDimensionY() + "," + serverInfo.getMaxDimensionZ());
            chatSupport.setSelected(serverInfo.isChatSupport());
            lobbySupport.setSelected(serverInfo.isRoomSupport());
        }

        /**
         * Get the room corresponding to this ServerPanel.
         *
         * @return
         */
        public ServerInfo getServerInfo() {
            return serverInfo;
        }

        public void joinServer() {
            controller.joinServer(getServerInfo());
        }
    }
}
