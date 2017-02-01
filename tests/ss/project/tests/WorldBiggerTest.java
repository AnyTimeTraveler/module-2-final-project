package ss.project.tests;

import org.junit.Before;
import org.junit.Test;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector3;
import ss.project.shared.game.World;

/**
 * Created by fw on 18/01/2017.
 */
public class WorldBiggerTest {
    World world;
    Player dummy;
    Player dummy2;

    @Before
    public void setUp() throws Exception {
        world = new World(new Vector3(7, 7, 7), 4);
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
    public void HasWon() {

    }
}
