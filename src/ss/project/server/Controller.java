package ss.project.server;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 23.12.16.
 */
public class Controller {

    private static List<ClientHandler> waitingPlayers;
    @Setter
    @Getter
    private static List<Room> rooms;

    static {
        waitingPlayers = new ArrayList<>();
    }

    public static void registerClient(ClientHandler clientHandler) {
        waitingPlayers.add(clientHandler);
        checkIfRoomCanBeCreated();
    }

    private static void checkIfRoomCanBeCreated() {

    }
}
