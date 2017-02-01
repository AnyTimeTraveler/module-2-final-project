package ss.project.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ss.project.server.ClientHandler;
import ss.project.server.Room;
import ss.project.server.Server;
import ss.project.shared.NetworkPlayer;
import ss.project.shared.Protocol;
import ss.project.shared.exceptions.AlreadyJoinedException;
import ss.project.shared.exceptions.NotInRoomException;
import ss.project.shared.exceptions.RoomFullException;

import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by simon on 24.01.17.
 */
public class RoomTest {
    private static ClientHandler clientHandler;

    static {
        try {
            Server server = new Server(1024 + new Random().nextInt(6000));
            Thread serverThread = new Thread(server::run);
            serverThread.setDaemon(true);
            serverThread.start();
            while (!server.isReady()) {
                Thread.sleep(10);
            }
            Socket socket = new Socket("127.0.0.1", server.getPort());
            clientHandler = new ClientHandler(server, socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Room room;
    private Room room2;
    private Room room3;
    private Room room4;
    private Room room5;
    private List<Room> roomList;

    @Before
    public void setup() throws Exception {
        room = new Room(1, 2, 4, 5, 6, 7);
        room2 = new Room(2, 3, 5, 6, 7, 6);
        room3 = new Room(3, 3, 6, 7, 8, 5);
        room4 = new Room(4, 3, 7, 8, 9, 4);
        room5 = new Room(5, 3, 8, 9, 10, 3);
        roomList = Arrays.asList(room, room2, room3, room4, room5);
    }

    @Test
    public void parseRoomString() throws Exception {
        List<Room> generatedRooms = Room.parseRoomListString(Protocol.createMessage(Protocol.Server.SENDLISTROOMS, room, room2, room3, room4, room5));
        Assert.assertEquals(roomList.size(), generatedRooms.size());
        for (int i = 0; i < roomList.size(); i++) {
            Assert.assertEquals(roomList.get(i), generatedRooms.get(i));
        }
    }

    @Test
    public void join() throws Exception {
        NetworkPlayer player = new NetworkPlayer(clientHandler);
        room.join(player);
        Assert.assertEquals(1, room.getCurrentPlayers());
        try {
            room.join(player);
            Assert.fail("Expected AlradyJoinedException");
        } catch (AlreadyJoinedException e) {
            // good.
        }
        NetworkPlayer player1 = new NetworkPlayer(clientHandler);
        room.join(player1);
        Assert.assertEquals(2, room.getCurrentPlayers());
        NetworkPlayer player2 = new NetworkPlayer(null);
        try {
            room.join(player2);
            Assert.fail("Expected RoomFullException");
        } catch (RoomFullException e) {
            // good.
        }
    }

    @Test(expected = NotInRoomException.class)
    public void leave() throws Exception {
        NetworkPlayer player = new NetworkPlayer(clientHandler);
        room.leave(player);
    }

    @Test
    public void otherFields() throws Exception {
        Room testRoom = new Room(15, 4, 5, 6, 7, 10);
        Assert.assertEquals(15, testRoom.getId());
        Assert.assertEquals(4, testRoom.getMaxPlayers());
        Assert.assertEquals(5, testRoom.getWorldSize().getX());
        Assert.assertEquals(6, testRoom.getWorldSize().getY());
        Assert.assertEquals(7, testRoom.getWorldSize().getZ());
        Assert.assertEquals(10, testRoom.getWinLength());
    }
}