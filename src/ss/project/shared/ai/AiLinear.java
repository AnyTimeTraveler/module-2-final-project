package ss.project.shared.ai;

import ss.project.shared.game.Player;
import ss.project.shared.game.Vector2;
import ss.project.shared.game.World;

public class AiLinear implements AI {

	private Player player;

	@Override
	//TODO: call initialize before the game starts.
	public void initialize(Player player) {
		System.out.println("Initialize");
		this.player = player;
	}

	@Override
	public void doTurn(World world) {
		//System.out.println("Do a turn");

		setNewGameItem(world);
	}

	private void setNewGameItem(World world) {
		for (int x = 0; x < world.getSize().getX(); x++) {
			for (int y = 0; y < world.getSize().getY(); y++) {
				if (world.addGameItem(new Vector2(x, y), player)) {
					return;
				}
			}
		}
	}

	@Override
	public void end() {
		System.out.println("End");
	}

}
