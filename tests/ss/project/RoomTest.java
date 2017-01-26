package ss.project;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ss.project.server.ClientHandler;
import ss.project.server.NetworkPlayer;
import ss.project.server.Room;
import ss.project.shared.Protocol;
import ss.project.shared.exceptions.AlreadyJoinedException;
import ss.project.shared.exceptions.NotInRoomException;
import ss.project.shared.exceptions.RoomFullException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by simon on 24.01.17.
 */
public class RoomTest {
    private Room room;
    private Room room2;
    private Room room3;
    private Room room4;
    private Room room5;
    private List<Room> roomList;
    private ClientHandler clientHandler;


    @Before
    public void setup() throws IOException {
        room = new Room(1, 2, 4, 5, 6, 7);
        room2 = new Room(2, 3, 5, 6, 7, 6);
        room3 = new Room(3, 3, 6, 7, 8, 5);
        room4 = new Room(4, 3, 7, 8, 9, 4);
        room5 = new Room(5, 3, 8, 9, 10, 3);
        roomList = Arrays.asList(room, room2, room3, room4, room5);
    }

    @Test
    public void parseRoomString() throws Exception {
        List<Room> generatedRooms = Room.parseRoomString(Protocol.createMessage(Protocol.Server.SENDLISTROOMS, room, room2, room3, room4, room5));
        Assert.assertEquals(roomList.size(), generatedRooms.size());
        for (int i = 0; i < roomList.size(); i++) {
            Assert.assertEquals(roomList.get(i), generatedRooms.get(i));
        }
    }

    @Test
    public void join() throws Exception {
        NetworkPlayer player = new NetworkPlayer(null);
        room.join(player);
        Assert.assertEquals(1, room.getCurrentPlayers());
        try {
            room.join(player);
            Assert.fail("Expected AlradyJoinedException");
        } catch (AlreadyJoinedException e) {
            // good.
        }
        NetworkPlayer player1 = new NetworkPlayer(null);
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
        NetworkPlayer player = new NetworkPlayer(null);
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