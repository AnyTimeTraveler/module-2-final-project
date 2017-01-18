import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ss.project.shared.game.*;

/**
 * Created by fw on 18/01/2017.
 */
public class WorldTest {
    World world;
    Player dummy;

    @Before
    public void setUp() throws Exception {
        world = new World(new Vector3(4, 4, 4));
        dummy = new Player("dummy") {
            @Override
            public void doTurn(Engine engine) {

            }
        };
    }

    @Test
    public void getSize() throws Exception {
        Assert.assertEquals(world.getSize(), (new Vector3(4, 4, 4)));
    }

    @Test
    public void getWorldPosition() throws Exception {
        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                for (int z = 0; z < world.getSize().getZ(); z++) {
                    Assert.assertNotNull(world.getWorldPosition(new Vector3(x, y, z)));
                    Assert.assertEquals(world.getWorldPosition(new Vector3(x, y, z)).getCoordinates(), new Vector3(x, y, z));
                }
            }
        }
    }

    @Test
    public void getWorldPosition1() throws Exception {
        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                Assert.assertNotNull(world.getWorldPosition(new Vector2(x, y)));
                Assert.assertEquals(world.getWorldPosition(new Vector2(x, y)).getCoordinates(), new Vector3(x, y, 0));
            }
        }
    }

    @Test
    public void isOwner() throws Exception {
        world.addGameItem(new Vector2(3, 3), dummy);
        Assert.assertTrue(world.isOwner(world.getHighestPosition(new Vector2(3, 3)), dummy));
    }

    @Test
    public void getOwner() throws Exception {
        world.addGameItem(new Vector2(3, 3), dummy);
        Assert.assertEquals(world.getOwner(world.getHighestPosition(new Vector2(3, 3))), dummy);
    }

    @Test
    public void insideWorld() throws Exception {
        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                for (int z = 0; z < world.getSize().getZ(); z++) {
                    Assert.assertTrue(world.insideWorld(new Vector3(x, y, z)));
                }
            }
        }
        Assert.assertFalse(world.insideWorld(new Vector3(-1, 0, 0)));
        Assert.assertFalse(world.insideWorld(new Vector3(0, -1, 0)));
        Assert.assertFalse(world.insideWorld(new Vector3(0, 0, -1)));
        Assert.assertFalse(world.insideWorld(new Vector3(-1, -1, -1)));

        Assert.assertFalse(world.insideWorld(new Vector3(4, 0, 0)));
        Assert.assertFalse(world.insideWorld(new Vector3(0, 4, 0)));
        Assert.assertFalse(world.insideWorld(new Vector3(0, 0, 4)));
        Assert.assertFalse(world.insideWorld(new Vector3(4, 4, 4)));
    }

    @Test
    public void addGameItem() throws Exception {

    }

    @Test
    public void removeGameItem() throws Exception {

    }

    @Test
    public void removeGameItem1() throws Exception {

    }

    @Test
    public void hasWon() throws Exception {

    }

    @Test
    public void hasWon1() throws Exception {

    }

    @Test
    public void writeTo() throws Exception {

    }

    @Test
    public void isFull() throws Exception {

    }

    @Test
    public void getHighestPosition() throws Exception {

    }

    @Test
    public void deepCopy() throws Exception {

    }
}