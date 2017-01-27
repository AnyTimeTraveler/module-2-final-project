import org.junit.Assert;
import org.junit.Test;
import ss.project.client.Config;
import ss.project.client.Controller;
import ss.project.client.networking.Connection;
import ss.project.client.networking.Network;
import ss.project.client.networking.ServerInfo;
import ss.project.server.NetworkPlayer;
import ss.project.server.Server;

import java.io.IOException;
import java.util.Random;

/**
 * Created by simon on 23.01.17.
 */
public class NetworkTest {

    @Test
    public void testPing() throws IOException, InterruptedException {
        Server server = new Server("127.0.0.1", 1024 + new Random().nextInt(6000));
        Thread serverThread = new Thread(server::run);
        serverThread.start();
        while (!server.isReady()) {
            Thread.sleep(10);
        }
        Network client = new Network(Controller.getController(), new Connection("Simon", "127.0.0.1", server.getPort()));
        Assert.assertEquals(ServerInfo.Status.ONLINE, client.ping().getStatus());
    }

    @Test
    public void testClientCapabilities() throws InterruptedException, IOException {
        Server server = new Server("127.0.0.1", 1024 + new Random().nextInt(6000));
        Thread serverThread = new Thread(server::run);
        serverThread.start();
        while (!server.isReady()) {
            Thread.sleep(10);
        }
        Network client = new Network(Controller.getController(), new Connection("Simon", "127.0.0.1", server.getPort()));
        client.start();
        while (!client.isReady()) {
            Thread.sleep(10);
        }
        Assert.assertEquals(1, server.getClientHandlers().size());
        Assert.assertNotNull(server.getClientHandlers().get(0));
        Assert.assertNotNull(server.getClientHandlers().get(0).getPlayer());
        NetworkPlayer np = server.getClientHandlers().get(0).getPlayer();
        Assert.assertNull(np.getCurrentRoom());
        while (np.getMaxPlayers() == 0) {
            Thread.sleep(10);
        }
        Assert.assertEquals(Config.getInstance().RoomSupport, np.isRoomSupport());
        Assert.assertEquals(Config.getInstance().MaxDimensionX, np.getMaxDimensionX());
        Assert.assertEquals(Config.getInstance().MaxDimensionY, np.getMaxDimensionY());
        Assert.assertEquals(Config.getInstance().MaxDimensionZ, np.getMaxDimensionZ());
        Assert.assertEquals(Config.getInstance().MaxWinLength, np.getMaxWinLength());
        Assert.assertEquals(Config.getInstance().ChatSupport, np.isChatSupport());
        Assert.assertEquals(Config.getInstance().AutoRefresh, np.isAutoRefresh());
    }
}
