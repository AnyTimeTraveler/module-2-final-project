package ss.project.tests;

import org.junit.Assert;
import org.junit.Test;
import ss.project.client.Controller;
import ss.project.client.Controller.Panel;
import ss.project.server.Room;
import ss.project.shared.computerplayer.LinearComputerPlayer;
import ss.project.shared.game.ClientEngine;
import ss.project.shared.game.Player;
import ss.project.shared.model.ChatMessage;
import ss.project.shared.model.GameParameters;

import java.util.ArrayList;

public class ControllerTest {

    @Test
    public void start() {
        Controller c = Controller.getController();
        c.start(false);
        c.addMessage(new ChatMessage("Simon", "Test!"));
        c.switchTo(Panel.SERVER_BROWSER);
        c.addServer("localhost", 1234);
        c.switchTo(Panel.SINGLE_PLAYER_SETTINGS);
        ArrayList<Player> players = new ArrayList<>();
        players.add(new LinearComputerPlayer("Test1"));
        players.add(new LinearComputerPlayer("Test2"));
        c.setEngine(new ClientEngine(new GameParameters(4, 4, 4, 4), players, Controller.getController().getNetwork(), 12));
        Room room = new Room(2, 4, 4, 4, 4);
        c.setCurrentRoom(room);
        Assert.assertEquals(room, c.getCurrentRoom());
        c.addServer("localhost");
        c.addServer("localhost:1234");
        c.setFrameSize(100, 200);
    }
}
