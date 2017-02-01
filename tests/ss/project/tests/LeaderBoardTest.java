package ss.project.tests;

import org.junit.Assert;
import org.junit.Test;
import ss.project.client.Controller;
import ss.project.client.Network;
import ss.project.server.Server;
import ss.project.shared.Protocol;
import ss.project.shared.model.Connection;
import ss.project.shared.model.LeaderboardEntry;
import ss.project.shared.model.ServerConfig;

import java.util.ArrayList;

/**
 * Created by simon on 31.01.17.
 */
public class LeaderBoardTest {

    @Test
    public void emptyAndFull() throws Exception {
        Controller.getController().start(false);
        Server server = new Server(4657);
        Thread serverThread = new Thread(server::run);
        serverThread.setDaemon(true);
        serverThread.start();
        while (!server.isReady()) {
            Thread.sleep(100);
        }
        Controller.getController().joinServer(new Network(new Connection("", "localhost", 4657)).ping());
        Network client = Controller.getController().getNetwork();
        while (!client.isReady()) {
            Thread.sleep(100);
        }
        ServerConfig.getInstance().leaderboard = new ArrayList<>();
        client.sendMessage(Protocol.createMessage(Protocol.Client.REQUESTLEADERBOARD));
        Thread.sleep(100);
        Assert.assertEquals(0, Controller.getController().getLeaderBoard().size());

        ServerConfig.getInstance().leaderboard = new ArrayList<>();
        ServerConfig.getInstance().leaderboard.add(new LeaderboardEntry("Simon", 0, 0, 1));
        client.sendMessage(Protocol.createMessage(Protocol.Client.REQUESTLEADERBOARD));
        Thread.sleep(100);
        Assert.assertEquals(1, Controller.getController().getLeaderBoard().size());
    }
}
