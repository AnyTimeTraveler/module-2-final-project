package ss.project.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ss.project.client.Network;
import ss.project.server.Server;
import ss.project.shared.model.Connection;
import ss.project.shared.model.ServerConfig;
import ss.project.shared.model.ServerInfo;

import java.util.Random;

/**
 * Created by simon on 01.02.17.
 */
public class ServerInfoTest {
    private ServerInfo info;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        Server server = new Server(1024 + new Random().nextInt(6000));
        Thread serverThread = new Thread(server::run);
        serverThread.start();
        while (!server.isReady()) {
            Thread.sleep(10);
        }
        connection = new Connection("Simon",
                                           "127.0.0.1",
                                           server.getPort());
        Network network = new Network(connection);
        info = network.ping();

    }

    @Test
    public void test() {
        ServerConfig c = ServerConfig.getInstance();
        Assert.assertEquals(c.maxPlayers, info.getMaxPlayers());
        Assert.assertEquals(c.maxDimensionX, info.getMaxDimensionX());
        Assert.assertEquals(c.maxDimensionY, info.getMaxDimensionY());
        Assert.assertEquals(c.maxDimensionZ, info.getMaxDimensionZ());
        Assert.assertEquals(c.maxWinLength, info.getMaxWinLength());
        Assert.assertEquals(c.roomSupport, info.isRoomSupport());
        Assert.assertEquals(c.chatSupport, info.isChatSupport());


        ServerInfo info2 = new ServerInfo(ServerInfo.Status.ONLINE,
                                                 connection,
                                                 2,
                                                 true,
                                                 4,
                                                 4,
                                                 4,
                                                 4,
                                                 true);
        Assert.assertEquals(connection, info2.getConnection());
        Assert.assertEquals(2, info2.getMaxPlayers());
        Assert.assertEquals(4, info2.getMaxDimensionX());
        Assert.assertEquals(4, info2.getMaxDimensionY());
        Assert.assertEquals(4, info2.getMaxDimensionZ());
        Assert.assertEquals(4, info2.getMaxWinLength());
        Assert.assertEquals(true, info2.isRoomSupport());
        Assert.assertEquals(true, info2.isChatSupport());
    }
}