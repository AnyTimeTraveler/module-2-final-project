package ss.project.shared.game;

//Should be renamed...
public class GameItem {

	private Player owner;

	/**
	 * Create a new gameItem, a block that is placed by a player.
	 * 
	 * @param owner
	 *            Owner of the item.
	 */
	public GameItem(Player owner) {
		this.owner = owner;
	}

	/**
	 * 
	 * @return the owner of this GameItem.
	 */
	public Player getOwner() {
		return owner;
	}
}
