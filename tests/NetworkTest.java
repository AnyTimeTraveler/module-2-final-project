import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ss.project.client.networking.Connection;
import ss.project.client.networking.Network;
import ss.project.client.networking.ServerInfo;
import ss.project.server.Server;

import java.io.IOException;

/**
 * Created by simon on 23.01.17.
 */
public class NetworkTest {

    private Server server;
    private Network client;

    @Before
    public void setup() throws IOException, InterruptedException {
        server = new Server("127.0.0.1", 1234);
        Thread serverThread = new Thread(server::run);
        serverThread.start();
        while (!server.isReady()) {
            Thread.sleep(10);
        }
        client = new Network(new Connection("Simon", "127.0.0.1", 1234));
    }

    @Test
    public void testPing() throws IOException {
        Assert.assertEquals(ServerInfo.Status.ONLINE, client.ping().getStatus());
    }

    @Test
    public void testClientCapabilities() {
        client.run();
        // TODO: Check for capabilities having arrived
    }
}
