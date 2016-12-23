package ss.project.test;

import org.junit.Before;
import org.junit.Test;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Vector3;

import static org.junit.Assert.*;

public class EngineTest {

	private Engine game;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void test4x4x4() {
		//Create a new game with size 4, 0 players of which 0 AI.
		game = new Engine(new Vector3(4, 4, 4), 0, 0);

		assertNotNull("World should not be created.", game.getWorld());

		//Check inside range.
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				for (int z = 0; z < 4; z++) {
					assertTrue(String.format("Vector({0},{1},{2}) should be inside range.", x, y, z),
						game.getWorld().insideWorld(new Vector3(x, y, z)));

					assertNotNull(String.format("Vector({0},{1},{2})", x, y, z),
						game.getWorld().getWorldPosition(new Vector3(x, y, z)));
				}
			}
		}

		//Check outside range.
		assertFalse("Vector(-1,0,0) should be outside range", game.getWorld().insideWorld(new Vector3(-1, 0, 0)));
		assertFalse("Vector(0,-1,0) should be outside range", game.getWorld().insideWorld(new Vector3(0, -1, 0)));
		assertFalse("Vector(0,0,-1) should be outside range", game.getWorld().insideWorld(new Vector3(0, 0, -1)));

		assertFalse("Vector(4,0,0) should be outside range", game.getWorld().insideWorld(new Vector3(4, 0, 0)));
		assertFalse("Vector(0,4,0) should be outside range", game.getWorld().insideWorld(new Vector3(0, 4, 0)));
		assertFalse("Vector(0,0,4) should be outside range", game.getWorld().insideWorld(new Vector3(0, 0, 4)));
	}

	@Test
	public void testCoordinates() {
        //Create 2 players of which 0 ai.
        game = new Engine(new Vector3(4, 4, 4), 2, 0);

		assertTrue("The WorldPosition at coordinates (0,0,0) should have coordinates (0,0,0).",
			game.getWorld().getWorldPosition(Vector3.ZERO).getCoordinates().equals(Vector3.ZERO));
		assertTrue("The WorldPosition at coordinates (3,3,3) should have coordinates (3,3,3).",
			game.getWorld().getWorldPosition(new Vector3(3, 3, 3)).getCoordinates().equals(new Vector3(3, 3, 3)));
	}

	@Test
	public void testPlayersReal() {
        //Create 2 players of which 0 ai.
        game = new Engine(new Vector3(4, 4, 4), 2, 0);

		assertNotNull("Player 0 should be created. ", game.getPlayer(0));
		assertNotNull("Player 1 should be created. ", game.getPlayer(1));

		assertNull("Player 2 should not be created. ", game.getPlayer(2));
		assertNull("Player -1 should not be created. ", game.getPlayer(-1));

		assertFalse("Player 0 should be a real player.", game.getPlayer(0).isHasAI());
		assertFalse("Player 1 should be a real player.", game.getPlayer(1).isHasAI());
	}

	@Test
	public void testPlayersAI() {
		//Create 2 players of which 2 AI.
		game = new Engine(new Vector3(4, 4, 4), 2, 2);

		assertNotNull("Player 0 should be created. ", game.getPlayer(0));
		assertNotNull("Player 1 should be created. ", game.getPlayer(1));

		assertNull("Player 2 should not be created. ", game.getPlayer(2));
		assertNull("Player -1 should not be created. ", game.getPlayer(-1));

        assertTrue("Player 0 should be an ai player.", game.getPlayer(0).isHasAI());
        assertTrue("Player 1 should be an ai player.", game.getPlayer(1).isHasAI());
    }

	@Test
	public void testPlayersRealAI() {
        //Create 2 players of which 1 ai.
        game = new Engine(new Vector3(4, 4, 4), 2, 1);

		assertNotNull("Player 0 should be created. ", game.getPlayer(0));
		assertNotNull("Player 1 should be created. ", game.getPlayer(1));

		assertNull("Player 2 should not be created. ", game.getPlayer(2));
		assertNull("Player -1 should not be created. ", game.getPlayer(-1));

        assertTrue("Player 0 should be an ai player.", game.getPlayer(0).isHasAI());
        assertFalse("Player 1 should be a real player.", game.getPlayer(1).isHasAI());
	}

	@Test
	public void testPlayersInvalid() {
        //Create 0 players of which 2 ai.
        game = new Engine(new Vector3(4, 4, 4), 0, 2);

		assertNull("Player 0 should not be created. ", game.getPlayer(0));
		assertNull("Player 1 should not be created. ", game.getPlayer(0));
		assertNull("Player 2 should not be created. ", game.getPlayer(0));
		assertNull("Player -1 should not be created. ", game.getPlayer(-1));
	}

	@Test
	public void testAddItems() {
		game = new Engine(new Vector3(4, 4, 4), 2, 0);

        assertTrue("Player 0 should be able to place his GameItem at (0,0,0).",
                game.getWorld().addGameItem(new Vector3(0, 0, 0), game.getPlayer(0)));

		assertEquals("Owner should be player 0.",
			game.getWorld().getWorldPosition(new Vector3(0, 0, 0)).getGameItem().getOwner(),
			game.getPlayer(0));

		assertFalse("Player 0 should not be able to place his GameItem at (0,0,0), it's already in use.",
			game.getWorld().addGameItem(new Vector3(0, 0, 0), game.getPlayer(0)));

		assertFalse("Player 0 should not be able to place his GameItem at (-1,0,0).",
			game.getWorld().addGameItem(new Vector3(-1, 0, 0), game.getPlayer(0)));

		assertTrue("Player 0 should be able to place his GameItem at (3,3,3).",
			game.getWorld().addGameItem(new Vector3(3, 3, 3), game.getPlayer(0)));

		assertEquals("Owner should be player 0.",
			game.getWorld().getWorldPosition(new Vector3(3, 3, 3)).getGameItem().getOwner(),
			game.getPlayer(0));

		assertFalse("Player 0 should be able to place his GameItem at (4,4,4).",
			game.getWorld().addGameItem(new Vector3(4, 4, 4), game.getPlayer(0)));
	}
}
