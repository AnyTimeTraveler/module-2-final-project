package ss.project.client.ui.gui;

import lombok.Getter;
import ss.project.client.Controller;
import ss.project.shared.model.ServerInfo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by simon on 16.01.17.
 */
public class PNLServerBrowser extends GUIPanel {

    private Controller controller;
    private JPanel serversListPanel;

    public PNLServerBrowser(Controller controller) {
        super(true);
        this.controller = controller;

        this.setLayout(new BorderLayout());
        this.add(GUIUtils.createLabel("Servers", GUIUtils.LabelType.TITLE), BorderLayout.NORTH);

        serversListPanel = new JPanel();
        serversListPanel.setLayout(new BoxLayout(serversListPanel, BoxLayout.Y_AXIS));
        this.add(serversListPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back");
        buttonPanel.add(backButton);
        backButton.addActionListener(e -> controller.switchTo(Controller.Panel.MAIN_MENU));
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            onLeave();
            controller.addServer(JOptionPane.showInputDialog(this, "IP:PORT"));
            onEnter();
        });
        buttonPanel.add(addButton);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshServerList());
        buttonPanel.add(refreshButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void onEnter() {
        addServerPanels(controller.pingServers());
    }

    private void refreshServerList() {
        onLeave();
        onEnter();
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
            int roomPanelHeight = 75;
            this.setMaximumSize(new Dimension(10000, roomPanelHeight));

            JPanel westPanel = new JPanel(new GridBagLayout());
            JButton deleteButton = new JButton("X");
            deleteButton.addActionListener(e -> {
                Controller.getController().removeServer(serverInfo);
                refreshServerList();
            });
            westPanel.add(deleteButton);
            JLabel serverStatus = new JLabel(serverInfo.getStatus().toString());
            westPanel.add(serverStatus);
            add(westPanel, BorderLayout.WEST);

            JPanel jPanel = new JPanel(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            int spaceBetweenText = 10;
            constraints.insets = new Insets(0, 0, 0, spaceBetweenText);
            JLabel winLength = new JLabel("Win length: " + serverInfo.getMaxWinLength(), SwingConstants.CENTER);
            jPanel.add(winLength, constraints);
            JLabel playerAmount = new JLabel("Players: " + serverInfo.getMaxPlayers());
            jPanel.add(playerAmount, constraints);
            JLabel worldSize = new JLabel("World:" + serverInfo.getMaxDimensionX() +
                    "," + serverInfo.getMaxDimensionY() + "," + serverInfo.getMaxDimensionZ());
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

            this.setToolTipText(serverInfo.getConnection().getAddress() + ":" + serverInfo.getConnection().getPort());
        }
    }
}
