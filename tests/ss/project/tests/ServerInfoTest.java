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
    private Server server;
    private Network client;
    private ServerInfo info;
    private ServerInfo info2;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        server = new Server("127.0.0.1", 1024 + new Random().nextInt(6000));
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
        Assert.assertEquals(c.MaxPlayers, info.getMaxPlayers());
        Assert.assertEquals(c.MaxDimensionX, info.getMaxDimensionX());
        Assert.assertEquals(c.MaxDimensionY, info.getMaxDimensionY());
        Assert.assertEquals(c.MaxDimensionZ, info.getMaxDimensionZ());
        Assert.assertEquals(c.MaxWinLength, info.getMaxWinLength());
        Assert.assertEquals(c.RoomSupport, info.isRoomSupport());
        Assert.assertEquals(c.ChatSupport, info.isChatSupport());


        info2 = new ServerInfo(ServerInfo.Status.ONLINE,
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