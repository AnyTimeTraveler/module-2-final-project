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
    Player dummy2;

    @Before
    public void setUp() throws Exception {
        world = new World(new Vector3(4, 4, 4));
        dummy = new Player("dummy") {
            @Override
            public void doTurn(Engine engine) {

            }
        };
        dummy2 = new Player("dummy opponent") {
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
        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                for (int z = 0; z < world.getSize().getZ(); z++) {
                    Assert.assertTrue(world.addGameItem(new Vector2(x, y), dummy));
                    Assert.assertNotNull(world.getWorldPosition(new Vector3(x, y, z)).getGameItem());
                    Assert.assertTrue(world.getWorldPosition(new Vector3(x, y, z)).hasGameItem());
                }
            }
        }
        Assert.assertFalse(world.addGameItem(new Vector2(0, 0), dummy));
    }

    @Test
    public void removeGameItem() throws Exception {
        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                for (int z = 0; z < world.getSize().getZ(); z++) {
                    Assert.assertTrue(world.addGameItem(new Vector2(x, y), dummy));
                    Assert.assertTrue(world.getWorldPosition(new Vector3(x, y, 0)).hasGameItem());
                    world.removeGameItem(new Vector3(x, y, 0));
                    Assert.assertFalse(world.getWorldPosition(new Vector3(x, y, 0)).hasGameItem());
                }
            }
        }
        world.removeGameItem(new Vector3(-1, -1, -1));
        world.removeGameItem(new Vector3(4, 4, 4));

        Assert.assertFalse(world.isFull());
    }

    @Test
    public void removeGameItem1() throws Exception {
        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                for (int z = 0; z < world.getSize().getZ(); z++) {
                    Assert.assertTrue(world.addGameItem(new Vector2(x, y), dummy));
                    Assert.assertTrue(world.getWorldPosition(new Vector3(x, y, 0)).hasGameItem());
                    world.removeGameItem(new Vector2(x, y));
                    Assert.assertFalse(world.getWorldPosition(new Vector3(x, y, 0)).hasGameItem());
                }
            }
        }
        world.removeGameItem(new Vector2(-1, -1));
        world.removeGameItem(new Vector2(4, 4));

        Assert.assertFalse(world.isFull());

        for (int z = 0; z < world.getSize().getZ(); z++) {
            Assert.assertTrue(world.addGameItem(new Vector2(0, 0), dummy));
            Assert.assertTrue(world.getWorldPosition(new Vector3(0, 0, z)).hasGameItem());
        }
        world.removeGameItem(new Vector2(0, 0));
        Assert.assertFalse(world.getWorldPosition(new Vector3(0, 0, world.getSize().getZ() - 1)).hasGameItem());
    }

    @Test
    public void hasWon() throws Exception {
        world.addGameItem(new Vector2(0, 0), dummy);
        Assert.assertFalse(world.hasWon(new Vector2(0, 0), dummy));
        world.addGameItem(new Vector2(1, 0), dummy);
        Assert.assertFalse(world.hasWon(new Vector2(1, 0), dummy));
        world.addGameItem(new Vector2(2, 0), dummy);
        Assert.assertFalse(world.hasWon(new Vector2(2, 0), dummy));
        world.addGameItem(new Vector2(3, 0), dummy);
        Assert.assertTrue(world.hasWon(new Vector2(3, 0), dummy));
    }

    //TODO: add direction (1,1,1)
    //TODO: add direction (1,-1,0) at z = 2
    @Test
    public void hasWon1() throws Exception {
        world.addGameItem(new Vector2(0, 0), dummy);
        Assert.assertFalse(world.hasWon(new Vector2(0, 0), dummy));
        world.addGameItem(new Vector2(1, 1), dummy);
        Assert.assertFalse(world.hasWon(new Vector2(1, 1), dummy));
        world.addGameItem(new Vector2(2, 2), dummy);
        Assert.assertFalse(world.hasWon(new Vector2(2, 2), dummy));
        world.addGameItem(new Vector2(3, 3), dummy);
        Assert.assertTrue(world.hasWon(new Vector2(3, 3), dummy));
    }

    @Test
    public void hasWonDiagonal2() throws Exception {
        world.addGameItem(new Vector2(3, 0), dummy);
        Assert.assertFalse(world.hasWon(new Vector2(3, 0), dummy));
        world.addGameItem(new Vector2(2, 1), dummy);
        Assert.assertFalse(world.hasWon(new Vector2(2, 1), dummy));
        world.addGameItem(new Vector2(1, 2), dummy);
        Assert.assertFalse(world.hasWon(new Vector2(1, 2), dummy));
        world.addGameItem(new Vector2(0, 3), dummy);
        Assert.assertTrue(world.hasWon(new Vector2(0, 3), dummy));
    }

    @Test
    public void hasWonDiagonal3() throws Exception {
        world.addGameItem(new Vector2(0, 0), dummy);
        Assert.assertFalse(world.hasWon(new Vector2(3, 0), dummy));
        world.addGameItem(new Vector2(1, 1), dummy2);
        world.addGameItem(new Vector2(2, 2), dummy2);
        world.addGameItem(new Vector2(2, 2), dummy2);
        world.addGameItem(new Vector2(3, 3), dummy2);
        world.addGameItem(new Vector2(3, 3), dummy2);
        world.addGameItem(new Vector2(3, 3), dummy2);
        world.addGameItem(new Vector2(1, 1), dummy);
        Assert.assertFalse(world.hasWon(new Vector2(1, 1), dummy));
        world.addGameItem(new Vector2(2, 2), dummy);
        Assert.assertFalse(world.hasWon(new Vector2(2, 2), dummy));
        world.addGameItem(new Vector2(3, 3), dummy);
        Assert.assertTrue(world.hasWon(new Vector2(3, 3), dummy));
    }

    @Test
    public void hasWonStraight() throws Exception {
        world.addGameItem(new Vector2(0, 0), dummy);
        //Assert.assertFalse(world.hasWon(new Vector2(0, 0), dummy));
        world.addGameItem(new Vector2(0, 0), dummy);
        //Assert.assertFalse(world.hasWon(new Vector2(0, 0), dummy));
        world.addGameItem(new Vector2(0, 0), dummy);
        //Assert.assertFalse(world.hasWon(new Vector2(0, 0), dummy));
        world.addGameItem(new Vector2(0, 0), dummy);
        Assert.assertTrue(world.hasWon(new Vector2(0, 0), dummy));
    }

    @Test
    public void writeTo() throws Exception {
        World copy = new World(world.getSize());
        world.addGameItem(new Vector2(1, 1), dummy);
        world.addGameItem(new Vector2(3, 3), dummy2);

        world.writeTo(copy);
        Assert.assertTrue(world.isOwner(new Vector3(1, 1, 0), dummy));
        Assert.assertTrue(world.isOwner(new Vector3(3, 3, 0), dummy2));

        world.removeGameItem(new Vector2(1, 1));

        world.writeTo(copy);
        Assert.assertFalse(world.getWorldPosition(new Vector3(1, 1, 0)).hasGameItem());
    }

    @Test
    public void isFull() throws Exception {

    }

    @Test
    public void getHighestPosition() throws Exception {

    }

    @Test
    public void deepCopy() throws Exception {
        world.addGameItem(new Vector2(1, 1), dummy);
        world.addGameItem(new Vector2(3, 3), dummy2);
        World newWorld = world.deepCopy();
        Assert.assertTrue(world.isOwner(new Vector3(1, 1, 0), dummy));
        Assert.assertTrue(world.isOwner(new Vector3(3, 3, 0), dummy2));
    }
}