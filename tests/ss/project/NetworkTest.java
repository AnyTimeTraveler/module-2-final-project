package ss.project;

import org.junit.Assert;
import org.junit.Test;
import ss.project.client.Controller;
import ss.project.client.networking.Connection;
import ss.project.client.networking.Network;
import ss.project.client.networking.ServerInfo;
import ss.project.server.Server;

import java.io.IOException;

/**
 * Created by simon on 23.01.17.
 */
public class NetworkTest {

    @Test
    public void testPing() throws IOException, InterruptedException {
        Server server = new Server("127.0.0.1", 1234);
        Thread serverThread = new Thread(server::run);
        serverThread.start();
        while (!server.isReady()) {
            Thread.sleep(10);
        }
        Network client = new Network(Controller.controller, new Connection("Simon", "127.0.0.1", 1234));
        Assert.assertEquals(ServerInfo.Status.ONLINE, client.ping().getStatus());
    }

    @Test
    public void testClientCapabilities() throws InterruptedException, IOException {
        Server server = new Server("127.0.0.1", 2345);
        Thread serverThread = new Thread(server::run);
        serverThread.start();
        while (!server.isReady()) {
            Thread.sleep(10);
        }
        Network client = new Network(Controller.controller, new Connection("Simon", "127.0.0.1", 2345));
        client.start();
        Thread.sleep(100);
        Assert.assertEquals(1, server.getClientHandlers().size());

    }
}
