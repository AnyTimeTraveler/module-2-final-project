package ss.project.test;

import org.junit.Before;
import org.junit.Test;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Vector3;

import static org.junit.Assert.fail;

public class WorldTest {

	Engine game;

	@Before
	public void setUp() throws Exception {
		//Create a new game with size 4, 0 players of which 0 AI.
		game = new Engine(new Vector3(4, 4, 4), 0, 0);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
