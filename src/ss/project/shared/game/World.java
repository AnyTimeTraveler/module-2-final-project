package ss.project.shared.game;

public class World {

	int counter = 0;
	private int remainingSpots;
	private Vector3				size;
	private WorldPosition[][][]	worldPosition;

	/**
	 * Create a new world object, set the size and initialize it.
	 *
	 * @param size
	 *            A vector3 containing width,height and length.
	 */
	public World(Vector3 size) {
		this.size = size;
		initializeWorld(this.size);
	}

	/**
	 * Initialize the world by creating worldPosition objects.
	 */
	private void initializeWorld(Vector3 worldSize) {
		worldPosition = new WorldPosition[worldSize.getX()][worldSize.getY()][worldSize.getZ()];

		for (int x = 0; x < worldSize.getX(); x++) {
			for (int y = 0; y < worldSize.getY(); y++) {
				for (int z = 0; z < worldSize.getZ(); z++) {
					worldPosition[x][y][z] = new WorldPosition(new Vector3(x, y, z));
				}
			}
		}
		remainingSpots = worldSize.getX() * worldSize.getY() * worldSize.getZ();
	}

	/**
	 * Get the size of this world.
	 *
	 * @return the size of this world.
	 */
	public Vector3 getSize() {
		return size;
	}

	/**
	 *
	 * @param coordinates
	 *            coordinates of the WorldPosition we want to know.
	 * @return WorldPosition at specified coordinates. Returns null if
	 *         coordinates are outside range.
	 */
	public WorldPosition getWorldPosition(Vector3 coordinates) {
		if (!insideWorld(coordinates)) {
			return null;
		}
		return worldPosition[coordinates.getX()][coordinates.getY()][coordinates.getZ()];
	}

	/**
	 *
	 * @param coordinates
	 *            coordinates of the z axis we want to get.
	 * @return WorldPosition at the first empty WorldPosition at x and y.
	 *         Returns null if coordinates are outside range or if no empty spot
	 *         has been found.
	 */
	public WorldPosition getWorldPosition(Vector2 coordinates) {
		if (!insideWorld(new Vector3(coordinates))) {
			return null;
		}

		//get the highest possible worldposition.
		for (int z = 0; z < size.getZ(); z++) {
			WorldPosition wp = worldPosition[coordinates.getX()][coordinates.getY()][z];
			if (wp != null && !wp.hasGameItem()) {
				return wp;
			}
		}
		//No position possible
		return null;
	}

	/**
	 *
	 * @param coordinates
	 * @param player
	 * @return
	 */
	public boolean isOwner(Vector3 coordinates, Player player) {
		WorldPosition worldPos = getWorldPosition(coordinates);
		if (worldPos != null) {
			return worldPos.isOwner(player);
		}
		return false;
	}

	/**
	 *
	 * @param coordinates
	 * @return True if the coordinates are inside the world range.
	 */
	public boolean insideWorld(Vector3 coordinates) {
		if (coordinates.getX() >= 0 && coordinates.getY() >= 0 && coordinates.getZ() >= 0 &&
				coordinates.getX() < getSize().getX() && coordinates.getY() < getSize().getY() &&
				coordinates.getZ() < getSize().getZ()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Create and set a new GameItem in this world with specified owner.
	 *
	 * @param coordinates
	 *            Coordinates where the GameItem should be placed.
	 * @param owner
	 *            The owner of the GameItem.
	 * @return False if this move is not possible, true if possible. TODO: Sync
	 *         this with the addGameItem Vector2 version.
	 */
	public boolean addGameItem(Vector3 coordinates, Player owner) {
		WorldPosition wp = getWorldPosition(coordinates);
		if (wp != null) {
			if (wp.hasGameItem()) {
				return false;
			} else {
				wp.setGameItem(owner);
				remainingSpots--;

				//There's no space left!
				if (remainingSpots <= 0) {
					Engine.getEngine().finishGame();
				}
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * Create and set a new GameItem in this world with specified owner.
	 *
	 * @param coordinates
	 *            Coordinates where the GameItem should be placed.
	 * @param owner
	 *            The owner of the GameItem.
	 * @return False if this move is not possible, true if possible.
	 */
	public boolean addGameItem(Vector2 coordinates, Player owner) {
		WorldPosition wp = getWorldPosition(coordinates);
		if (wp != null) {
			if (wp.hasGameItem()) {
				return false;
			} else {
				//Set the item to this owner.
				wp.setGameItem(owner);
				remainingSpots--;

				//Check whether we have 4 on a row.
				checkWin(wp.getCoordinates(), owner);

				//There's no space left!
				if (remainingSpots <= 0) {
					Engine.getEngine().finishGame();
				}
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * Check whether the newCoordinates make the player win the game.
	 *
	 * @param newCoordinates
	 *            The coordinates where the player has put a new object.
	 * @param player
	 *            The player has placed a new object.
	 */
	private void checkWin(Vector3 newCoordinates, Player player) {
		for (int x = newCoordinates.getX() - 1; x < newCoordinates.getX() + 1; x++) {
			for (int y = newCoordinates.getY() - 1; y < newCoordinates.getY() + 1; y++) {
				for (int z = newCoordinates.getZ() - 1; z < newCoordinates.getZ() + 1; z++) {
					Vector3 vector = new Vector3(x, y, z);
					//Don't check zero, because that's ourself.
					if (!vector.equals(newCoordinates)) {
						//We found a neighbor that is owner by us as well! Continue this path.
						Vector3 direction = newCoordinates.subtract(x, y, z);
						if (checkWin(newCoordinates, player, direction, 1) + checkWin(newCoordinates, player, direction.inverse(), 0) >= 4) {
							//we won!
							System.out.println(player.getName() + " won!");
							//TODO: show something on the screen, clean stuff up, stop the game.
							Engine.getEngine().finishGame();
							return;
						}
					}
				}
			}
		}
	}

	/**
	 * Keep checking in a certain direction if we have 4 on a row.
	 *
	 * @param coordinates
	 *            Coordinates of the base, we add direction to this.
	 * @param player
	 *            The player who might win.
	 * @param direction
	 *            The direction we're going.
	 * @param count
	 *            The amount of objects that are ours, if this equals 4 player
	 *            has won!
	 * @return The amount on a row.
	 */
	private int checkWin(Vector3 coordinates, Player player, Vector3 direction, int count) {
		counter++;
		Vector3 newCoordinates = coordinates.add(direction);
		if (isOwner(newCoordinates, player)) {
			//again we're the owner!

			//we have four on a row!
			if (count >= 4) {
				System.out.println("four on a row");
				return count;
			}

			//check the next coordinates!
			return checkWin(newCoordinates, player, direction, count + 1);
		}
		return count;
	}

	@Deprecated
	@Override
	public String toString() {
		String result = "";
		for (int z = 0; z < size.getZ(); z++) {
			result += "\n\n" + "z: " + z + "\n";

			result += "   ";
			for (int header = 0; header < size.getY(); header++) {
				result += "Y ";
			}

			for (int x = 0; x < size.getX(); x++) {
				result += "\n";

				result += "X: ";
				Player owner = worldPosition[x][0][z].getOwner();
				if (owner != null) {
					//result += owner.getID();
				} else {
					result += "x";
				}

				for (int y = 1; y < size.getY(); y++) {
					owner = worldPosition[x][y][z].getOwner();
					if (owner != null) {
						//result += " " + owner.getID();
					} else {
						result += " x";
					}
				}
			}
		}

		return result;
	}
}
