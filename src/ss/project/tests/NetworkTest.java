package ss.project.tests;

import org.junit.Assert;
import org.junit.Test;
import ss.project.client.Network;
import ss.project.server.Server;
import ss.project.shared.NetworkPlayer;
import ss.project.shared.model.ClientConfig;
import ss.project.shared.model.Connection;
import ss.project.shared.model.ServerInfo;

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
        Network client = new Network(new Connection("Simon", "127.0.0.1", server.getPort()));
        Assert.assertEquals(ServerInfo.Status.ONLINE, client.ping().getStatus());
    }

    public void testClientCapabilities() throws InterruptedException, IOException {
        Server server = new Server("127.0.0.1", 1024 + new Random().nextInt(6000));
        Thread serverThread = new Thread(server::run);
        serverThread.start();
        while (!server.isReady()) {
            Thread.sleep(10);
        }
        Network client = new Network(new Connection("Simon", "127.0.0.1", server.getPort()));
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
        Assert.assertEquals(ClientConfig.getInstance().RoomSupport, np.isRoomSupport());
        Assert.assertEquals(ClientConfig.getInstance().MaxDimensionX, np.getMaxDimensionX());
        Assert.assertEquals(ClientConfig.getInstance().MaxDimensionY, np.getMaxDimensionY());
        Assert.assertEquals(ClientConfig.getInstance().MaxDimensionZ, np.getMaxDimensionZ());
        Assert.assertEquals(ClientConfig.getInstance().MaxWinLength, np.getMaxWinLength());
        Assert.assertEquals(ClientConfig.getInstance().ChatSupport, np.isChatSupport());
        Assert.assertEquals(ClientConfig.getInstance().AutoRefresh, np.isAutoRefresh());
    }
}
