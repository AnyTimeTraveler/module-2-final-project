/**
 * 
 */
package ss.project.shared.ai;

import ss.project.shared.game.Player;
import ss.project.shared.game.World;

/**
 * @author fw
 *
 */
public interface IArtificialIntelligence {
	/**
	 * Called when the game starts.
	 */
	public void initialize(Player player);
	
	/**
	 * Called everytime the player has to place an item.
	 */
	public void doTurn(World world);
	
	/**
	 * Called when the game ends.
	 */
	public void end();
}
