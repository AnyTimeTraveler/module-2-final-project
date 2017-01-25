package ss.project.client.ui.gui;

import ss.project.client.Controller;
import ss.project.server.Room;
import ss.project.shared.game.Vector3;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by simon on 16.01.17.
 */
public class PNLMultiPlayerLobby extends GUIPanel {

    private Controller controller;
    private JPanel roomsPanelOwner;
    private java.util.List<RoomPanel> roomPanels;

    private int roomPanelHeight = 75;
    private int spaceBetweenText = 10;

    public PNLMultiPlayerLobby(Controller controller) {
        super(true);

        this.controller = controller;

        roomPanels = new ArrayList<RoomPanel>();

        this.setLayout(new BorderLayout());

        this.add(GUIUtils.createLabel("Lobby", GUIUtils.LabelType.TITLE), BorderLayout.NORTH);

        roomsPanelOwner = new JPanel();
        roomsPanelOwner.setLayout(new BoxLayout(roomsPanelOwner, BoxLayout.Y_AXIS));
        this.add(roomsPanelOwner, BorderLayout.CENTER);

        JPanel jPanel = new JPanel();
        jPanel.add(new JButton("Back"));
        jPanel.add(new JButton("Create"));
        this.add(jPanel, BorderLayout.SOUTH);
    }

    @Override
    public void onEnter() {
        //TODO: Create a connection and retrieve all rooms.
        Room[] rooms = new Room[3];
        for (int i = 0; i < rooms.length; i++) {
            rooms[i] = new Room(5 - i, new Vector3(i, i + 1, i + 2));
        }
        addRoomPanels(rooms);
    }

    @Override
    public void onLeave() {

    }

    private void addRoomPanels(Room[] rooms) {
        roomsPanelOwner.removeAll();
        for (int i = 0; i < rooms.length; i++) {
            RoomPanel roomPanel;
            if (roomPanels.size() > i) {
                roomPanel = roomPanels.get(i);
                roomPanel.refreshValues();
            } else {
                roomPanel = new RoomPanel(rooms[i]);
            }
            roomPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            if (!roomPanels.contains(roomPanel)) {
                roomPanels.add(roomPanel);
            }
            roomsPanelOwner.add(roomPanel);
        }
        this.repaint();
        roomsPanelOwner.revalidate();
    }

    /**
     * A panel showing information about a lobby.
     */
    private class RoomPanel extends JPanel {
        private Room room;
        private JLabel roomID;
        private JLabel winLength;
        private JLabel playerAmount;
        private JLabel worldSize;

        /**
         * Create a new roomPanel and set the correct data.
         *
         * @param room data that should be used for the texts.
         */
        private RoomPanel(Room room) {
            super();
            this.room = room;

            this.setLayout(new BorderLayout());
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.setMaximumSize(new Dimension(10000, roomPanelHeight));
            roomID = new JLabel();
            this.add(roomID, BorderLayout.WEST);

            JPanel jPanel = new JPanel(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = new Insets(0, 0, 0, spaceBetweenText);
            winLength = new JLabel("", SwingConstants.CENTER);
            jPanel.add(winLength, constraints);
            playerAmount = new JLabel();
            jPanel.add(playerAmount, constraints);
            worldSize = new JLabel();
            jPanel.add(worldSize, constraints);
            this.add(jPanel, BorderLayout.CENTER);

            JButton joinButton = new JButton("Join");
            joinButton.addActionListener(e -> joinRoom());
            this.add(joinButton, BorderLayout.EAST);

            refreshValues();
        }

        /**
         * Refresh all values shown on the screen from this room.
         */
        public void refreshValues() {
            roomID.setText("|" + room.getId() + "|");
            winLength.setText("Win length: " + room.getWinLength());
            playerAmount.setText("Players: " + room.getMaxPlayers());
            worldSize.setText("World:" + room.getWorldSize().getX() + "," + room.getWorldSize().getY() + "," + room.getWorldSize().getZ());
        }

        /**
         * Get the room corresponding to this RoomPanel.
         *
         * @return
         */
        public Room getRoom() {
            return room;
        }

        public void joinRoom() {
            controller.joinRoom(getRoom());
        }
    }
}
