package ss.project.client.ui.gui;

import ss.project.client.Controller;
import ss.project.server.Room;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by simon on 16.01.17.
 */
public class PNLMultiPlayerLobby extends GUIPanel implements Observer {

    private Controller controller;
    private JPanel roomsPanelOwner;
    private List<RoomPanel> roomPanels;

    private int roomPanelHeight = 75;
    private int spaceBetweenText = 10;

    public PNLMultiPlayerLobby(Controller controller) {
        super(true);

        this.controller = controller;


        roomPanels = new ArrayList<>();

        this.setLayout(new BorderLayout());

        this.add(GUIUtils.createLabel("Lobby", GUIUtils.LabelType.TITLE), BorderLayout.NORTH);

        roomsPanelOwner = new JPanel();
        roomsPanelOwner.setLayout(new BoxLayout(roomsPanelOwner, BoxLayout.Y_AXIS));
        this.add(roomsPanelOwner, BorderLayout.CENTER);

        JPanel jPanel = new JPanel();
        JButton backButton = new JButton("Disconnect");
        backButton.addActionListener(e -> controller.getNetwork().shutdown());
        jPanel.add(backButton);

        JButton leaderBoardButton = new JButton("Leaderboard");
        leaderBoardButton.addActionListener(e -> controller.switchTo(Controller.Panel.LEADERBOARD));
        jPanel.add(leaderBoardButton);

        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> controller.switchTo(Controller.Panel.MULTI_PLAYER_ROOM_CREATION));
        jPanel.add(createButton);
        this.add(jPanel, BorderLayout.SOUTH);
    }

    @Override
    public void onEnter() {
        controller.addObserver(this);
        Controller.getController().refreshRoomList();
        refreshRooms();
    }

    @Override
    public void onLeave() {
        controller.deleteObserver(this);
    }

    private void refreshRooms() {
        addRoomPanels(Controller.getController().getRooms());
    }

    /**
     * Add the room panels to the screen and update all values.
     * Has ot be synchronized, because new roomvalues in the Controller triggers an update.
     *
     * @param rooms
     */
    private synchronized void addRoomPanels(List<Room> rooms) {
        roomsPanelOwner.removeAll();
        if (rooms != null) {
            for (int i = 0; i < rooms.size(); i++) {
                RoomPanel roomPanel;
                if (roomPanels.size() > i) {
                    roomPanel = roomPanels.get(i);
                    roomPanel.refreshValues(rooms.get(i));
                } else {
                    roomPanel = new RoomPanel(rooms.get(i));
                }
                roomPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                if (!roomPanels.contains(roomPanel)) {
                    roomPanels.add(roomPanel);
                }
                roomsPanelOwner.add(roomPanel);
            }
        }
        this.repaint();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Controller) {
            if (arg.equals("UpdateRoom")) {
                refreshRooms();
            }
        }
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

            refreshValues(room);
        }

        /**
         * Refresh all values shown on the screen from this room.
         */
        public void refreshValues(Room room) {
            this.room = room;
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

        /**
         * Join this room.
         */
        public void joinRoom() {
            controller.joinRoom(getRoom());
        }
    }
}
