package ss.project.client.ui.gui;

import lombok.Getter;
import ss.project.client.Config;
import ss.project.client.Controller;
import ss.project.client.networking.Connection;
import ss.project.client.networking.Network;
import ss.project.client.networking.ServerInfo;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 16.01.17.
 */
public class PNLServerBrowser extends GUIPanel {

    private Controller controller;
    private JPanel serversListPanel;
    private List<ServerPanel> serverPanels;

    private int roomPanelHeight = 75;
    private int spaceBetweenText = 10;

    public PNLServerBrowser(Controller controller) {
        super(true);

        this.controller = controller;

        serverPanels = new ArrayList<>();

        this.setLayout(new BorderLayout());

        this.add(GUIUtils.createLabel("Servers", GUIUtils.LabelType.TITLE), BorderLayout.NORTH);

        serversListPanel = new JPanel();
        serversListPanel.setLayout(new BoxLayout(serversListPanel, BoxLayout.Y_AXIS));
        this.add(serversListPanel, BorderLayout.CENTER);

        JPanel jPanel = new JPanel();
        JButton backButton = new JButton("Back");
        jPanel.add(backButton);
        backButton.addActionListener(e -> controller.switchTo(Controller.Panel.MAIN_MENU));
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            onLeave();
            controller.addServer(JOptionPane.showInputDialog(this, "IP:PORT"));
            onEnter();
        });
        jPanel.add(addButton);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            onLeave();
            onEnter();
        });
        jPanel.add(refreshButton);
        this.add(jPanel, BorderLayout.SOUTH);
    }

    @Override
    public void onEnter() {
        List<Connection> connections = Config.getInstance().KnownServers;
        List<ServerInfo> serverInfos = new ArrayList<>();
        for (Connection connection : connections) {
            try {
                serverInfos.add(new Network(Controller.getController(), connection).ping());
            } catch (IOException e) {
                serverInfos.add(new ServerInfo(ServerInfo.Status.OFFLINE, 0, false, 0, 0, 0, 0, false));
            }
        }
        addServerPanels(serverInfos);
    }

    @Override
    public void onLeave() {
        serversListPanel.removeAll();
    }


    private void addServerPanels(List<ServerInfo> serverInfos) {
        for (ServerInfo serverInfo : serverInfos) {
            ServerPanel serverPanel = new ServerPanel(serverInfo);
            serverPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            serversListPanel.add(serverPanel);
        }
        serversListPanel.revalidate();
        serversListPanel.repaint();
    }

    /**
     * A panel showing information about a lobby.
     */
    private class ServerPanel extends JPanel {
        @Getter
        private ServerInfo serverInfo;

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

            JLabel serverStatus = new JLabel(serverInfo.getStatus().toString());
            this.add(serverStatus, BorderLayout.WEST);

            JPanel jPanel = new JPanel(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = new Insets(0, 0, 0, spaceBetweenText);
            JLabel winLength = new JLabel("Win length: " + serverInfo.getMaxWinLength(), SwingConstants.CENTER);
            jPanel.add(winLength, constraints);
            JLabel playerAmount = new JLabel("Players: " + serverInfo.getMaxPlayers());
            jPanel.add(playerAmount, constraints);
            JLabel worldSize = new JLabel("World:" + serverInfo.getMaxDimensionX() + "," + serverInfo.getMaxDimensionY() + "," + serverInfo.getMaxDimensionZ());
            jPanel.add(worldSize, constraints);
            JCheckBox chatSupport = new JCheckBox("Chat", serverInfo.isChatSupport());
            chatSupport.setEnabled(false);
            jPanel.add(chatSupport, constraints);
            JCheckBox lobbySupport = new JCheckBox("Lobby", serverInfo.isRoomSupport());
            lobbySupport.setEnabled(false);
            jPanel.add(lobbySupport, constraints);
            this.add(jPanel, BorderLayout.CENTER);

            JButton joinButton = new JButton("Join");
            joinButton.addActionListener(e -> controller.joinServer(serverInfo));
            if (serverInfo.getStatus().equals(ServerInfo.Status.ONLINE)) {
                joinButton.setEnabled(true);
            } else {
                joinButton.setEnabled(false);
            }
            this.add(joinButton, BorderLayout.EAST);
        }
    }
}
