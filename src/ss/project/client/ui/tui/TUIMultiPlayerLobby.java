package ss.project.client.ui.tui;

import ss.project.client.Controller;
import ss.project.server.Room;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by simon on 16.01.17.
 */
public class TUIMultiPlayerLobby implements TUIPanel, Observer {
    private List<Room> rooms;
    private int disconnectID;

    public TUIMultiPlayerLobby() {

    }

    @Override
    public void printScreen() {
        rooms = Controller.getController().getRooms();

        System.out.println(ASCIIArt.getHeadline("Lobby", 120));
        for (int i = 0; i < rooms.size(); i++) {
            System.out.println(ASCIIArt.getChoiceItem(i, getRoomString(rooms.get(i)), 120));
        }
        disconnectID = rooms.size();
        System.out.println(ASCIIArt.getChoiceItem(disconnectID, "Disconnect", 120));
    }

    private String getRoomString(Room room) {
        StringBuilder result = new StringBuilder();
        result.append(room.getId());
        result.append("| ");
        result.append("X: " + room.getWorldSize().getX());
        result.append("Y: " + room.getWorldSize().getY());
        result.append("Z: " + room.getWorldSize().getZ());
        result.append("Max players: " + room.getMaxPlayers());
        result.append("Win length: " + room.getWinLength());

        return result.toString();
    }

    @Override
    public void handleInput(String input) {
        try {
            int choice = Integer.parseInt(input);
            if (choice < 0 || choice > disconnectID) {
                System.out.println("Please type a valid number.");
                return;
            }
            if (choice == disconnectID) {
                //Disconnect and go back.
                Controller.getController().getNetwork().shutdown();
                Controller.getController().switchTo(Controller.Panel.SERVER_BROWSER);
                return;
            }

            Controller.getController().joinRoom(rooms.get(choice));
        } catch (NumberFormatException e) {
            System.out.println("Please type a number...");
        }
    }

    @Override
    public void onEnter() {
        Controller.getController().addObserver(this);
    }

    @Override
    public void onLeave() {
        Controller.getController().deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Controller) {
            if (arg.equals("UpdateRoom")) {
                printScreen();
            }
        }
    }
}
