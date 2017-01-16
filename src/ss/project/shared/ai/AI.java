/**
 * 
 */
package ss.project.shared.ai;

import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;

/**
 * @author fw
 *
 */
public interface AI {
	/**
	 * Called when the game starts.
	 */
	public void initialize(Player player);
	
	/**
	 * Called everytime the player has to place an item.
	 */
	public void doTurn(Engine engine);
	
	/**
	 * Called when the game ends.
	 */
	public void end();
}
