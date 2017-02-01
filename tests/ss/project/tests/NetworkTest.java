package ss.project.tests;

import org.junit.Assert;
import org.junit.Test;
import ss.project.client.Controller;
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

    private static Server server;
    private static Network client;

    static {
        try {
            Controller.getController().start(false);
            server = new Server("127.0.0.1", 1024 + new Random().nextInt(6000));
            Thread serverThread = new Thread(server::run);
            serverThread.start();
            while (!server.isReady()) {
                Thread.sleep(10);
            }

            Controller.getController()
                    .joinServer(new Network(new Connection("Simon", "127.0.0.1", server.getPort())).ping());
            client = Controller.getController().getNetwork();
            while (!client.isReady()) {
                Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testPing() throws IOException, InterruptedException {
        Assert.assertEquals(ServerInfo.Status.ONLINE, Controller.getController().getCurrentServer().getStatus());
    }

    @Test
    public void testClientCapabilities() throws InterruptedException, IOException {
        Assert.assertEquals(1, server.getClientHandlers().size());
        Assert.assertNotNull(server.getClientHandlers().get(0));
        Assert.assertNotNull(server.getClientHandlers().get(0).getPlayer());
        NetworkPlayer np = server.getClientHandlers().get(0).getPlayer();
        Assert.assertNull(np.getCurrentRoom());
        while (np.getMaxPlayers() == 0) {
            Thread.sleep(10);
        }
        Assert.assertEquals(ClientConfig.getInstance().roomSupport, np.isRoomSupport());
        Assert.assertEquals(ClientConfig.getInstance().maxDimensionX, np.getMaxDimensionX());
        Assert.assertEquals(ClientConfig.getInstance().maxDimensionY, np.getMaxDimensionY());
        Assert.assertEquals(ClientConfig.getInstance().maxDimensionZ, np.getMaxDimensionZ());
        Assert.assertEquals(ClientConfig.getInstance().maxWinLength, np.getMaxWinLength());
        Assert.assertEquals(Controller.getController().isDoGui(), np.isChatSupport());
        Assert.assertEquals(ClientConfig.getInstance().autoRefresh, np.isAutoRefresh());
    }
}
