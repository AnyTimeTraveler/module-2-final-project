package ss.project.shared.game;

import lombok.Getter;

public class World {

    private int remainingSpots;
    private Vector3 size;
    private Player[][][] worldPosition;
    @Getter
    private int winLength;

    /**
     * Create a new world object, set the size and initialize it.
     *
     * @param size A vector3 containing width,height and length.
     */
    //@ ensures this.getSize().equals(size);
    //@ ensures this.getWinLength().equals(winLength);
    public World(Vector3 size, int winLength) {
        this.size = size;
        this.winLength = winLength;
        initializeWorld(this.size);
    }

    /**
     * Initialize the world by creating worldPosition objects.
     */
    //@ ensures !this.isFull();
    private void initializeWorld(Vector3 worldSize) {
        worldPosition = new Player[worldSize.getX()][worldSize.getY()][worldSize.getZ()];
        remainingSpots = worldSize.getX() * worldSize.getY() * worldSize.getZ();
    }

    /**
     * Get the size of this world.
     *
     * @return the size of this world.
     */
    //@ pure;
    public Vector3 getSize() {
        return size;
    }

    /**
     * @param coordinates coordinates of the z axis we want to get.
     * @return WorldPosition at the first empty WorldPosition at x and y.
     * Returns null if coordinates are outside range or if no empty spot
     * has been found.
     */
    //@ pure;
    public Vector3 getWorldPosition(Vector2 coordinates) {
        if (!insideWorld(coordinates)) {
            return null;
        }

        //get the highest possible worldposition.
        for (int z = 0; z < size.getZ(); z++) {
            if (worldPosition[coordinates.getX()][coordinates.getY()][z] == null) {
                return new Vector3(coordinates.getX(), coordinates.getY(), z);
            }
        }
        //No position possible
        return null;
    }

    /**
     * @param coordinates
     * @param player
     * @return
     */
    public boolean isOwner(Vector3 coordinates, Player player) {
        return isOwner(coordinates.getX(), coordinates.getY(), coordinates.getZ(), player);
    }

    /**
     * Check if a player is the owner of the object at x,y,z.
     *
     * @param x
     * @param y
     * @param z
     * @param player
     * @return False if the coordinates are not inside the world.
     */
    public boolean isOwner(int x, int y, int z, Player player) {
        if (insideWorld(x, y, z)) {
            Player actualPlayer = worldPosition[x][y][z];
            if (actualPlayer != null) {
                return actualPlayer.equals(player);
            }
        }
        return false;
    }

    /**
     * Returns the owner of a coordinate. Null if no owner.
     *
     * @param coordinates
     * @return
     */
    //@ pure;
    //@ requires coordinates != null;
    public Player getOwner(Vector3 coordinates) {
        return worldPosition[coordinates.getX()][coordinates.getY()][coordinates.getZ()];
    }

    /**
     * @param coordinates
     * @return True if the coordinates are inside the world range.
     */
    public boolean insideWorld(Vector3 coordinates) {
        return insideWorld(coordinates.getX(), coordinates.getY(), coordinates.getZ());
    }

    /**
     * Check if x,y,z are coordinates inside this world.
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public boolean insideWorld(int x, int y, int z) {
        if (x >= 0 && y >= 0 && z >= 0 &&
                x < getSize().getX() && y < getSize().getY() &&
                z < getSize().getZ()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param coordinates
     * @return True if the coordinates are inside the world range.
     */
    public boolean insideWorld(Vector2 coordinates) {
        if (coordinates.getX() >= 0 && coordinates.getY() >= 0 && coordinates.getX() < getSize().getX() &&
                coordinates.getY() < getSize().getY()) {
            return true;
        }
        return false;
    }

    /**
     * Create and set a new GameItem in this world with specified owner.
     *
     * @param coordinates Coordinates where the GameItem should be placed.
     * @param owner       The owner of the GameItem.
     * @return False if this move is not possible, true if possible.
     */
    public boolean addGameItem(Vector2 coordinates, Player owner) {
        Vector3 coords3d = getWorldPosition(coordinates);
        if (coords3d != null) {
            //Set the item to this owner.
            Player actualPlayer = worldPosition[coords3d.getX()][coords3d.getY()][coords3d.getZ()];
            if (actualPlayer == null) {
                worldPosition[coords3d.getX()][coords3d.getY()][coords3d.getZ()] = owner;
                remainingSpots--;
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a move would be valid, but do not place anything.
     *
     * @param coordinates The coordinates the move should be placed.
     * @return True if it's valid, false if not.
     */
    public boolean isValidMove(Vector2 coordinates) {
        Vector3 coords3d = getWorldPosition(coordinates);
        if (coords3d != null) {
            if (worldPosition[coords3d.getX()][coords3d.getY()][coords3d.getZ()] == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Remove a gameItem from this board.
     *
     * @param coordinates Coordinates of the gameitem.
     */
    public void removeGameItem(Vector3 coordinates) {
        if (!insideWorld(coordinates)) {
            return;
        }
        Player actualPlayer = worldPosition[coordinates.getX()][coordinates.getY()][coordinates.getZ()];
        if (actualPlayer != null) {
            worldPosition[coordinates.getX()][coordinates.getY()][coordinates.getZ()] = null;
            remainingSpots++;
        }
    }

    /**
     * Remove the highest gameitem from this board.
     *
     * @param coordinates Coordinates of which it should check the highest gameitem.
     */
    public void removeGameItem(Vector2 coordinates) {
        Vector3 coords3d = getHighestPosition(coordinates);
        if (coords3d != null) {
            removeGameItem(coords3d);
        }
    }

    /**
     * Checks if someone has won.
     *
     * @param newCoordinates
     * @param player
     * @return
     */
    //@ requires newCoordinates != null;
    //@ requires player != null;
    //@ pure;
    public boolean hasWon(Vector2 newCoordinates, Player player) {
        Vector3 coordinates = getHighestPosition(newCoordinates);
        if (coordinates != null) {
            return hasWon(coordinates, player);
        }
        return false;
    }

    /**
     * Check whether the newCoordinates make the player win the game.
     *
     * @param newCoordinates The coordinates where the player has put a new object.
     * @param player         The player has placed a new object.
     */
    //@ pure;
    //@ requires newCoordinates != null;
    //@ requires player != null;
    public boolean hasWon(Vector3 newCoordinates, Player player) {
        int newX = newCoordinates.getX();
        int newY = newCoordinates.getY();
        int newZ = newCoordinates.getZ();
        if (!isOwner(newCoordinates, player)) {
            return false;
        }
        for (int x = newCoordinates.getX() - 1; x < newCoordinates.getX() + 2; x++) {
            for (int y = newCoordinates.getY() - 1; y < newCoordinates.getY() + 2; y++) {
                for (int z = newCoordinates.getZ() - 1; z < newCoordinates.getZ() + 2; z++) {
                    Vector3 vector = new Vector3(x, y, z);
                    //Don't check zero, because that's ourself.
                    if (!vector.equals(newCoordinates)) {
                        //We found a neighbor that is owned by us as well! Continue this path.
                        int dirX = newX - x;
                        int dirY = newY - y;
                        int dirZ = newZ - z;
                        int count1 = hasWon(newX, newY, newZ, player, dirX, dirY, dirZ, 1);
                        int count2 = hasWon(newX, newY, newZ, player, -dirX, -dirY, -dirZ, 0);
                        if (count1 + count2 >= winLength) {
                            //we won!
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Keep checking in a certain direction if we have 4 on a row.
     *
     * @param player The player who might win.
     * @param count  The amount of objects that are ours, if this equals 4 player
     *               has won!
     * @return The amount on a row.
     */
    //@ pure;
    //@ requires coordinates != null;
    //@ requires player != null;
    //@ requires direction != null;
    //@ requires count >=0 ;
    private int hasWon(int x, int y, int z, Player player, int dirX, int dirY, int dirZ, int count) {
        int newX = x + dirX;
        int newY = y + dirY;
        int newZ = z + dirZ;
        if (isOwner(newX, newY, newZ, player)) {
            //again we're the owner!

            //check the next coordinates!
            return hasWon(newX, newY, newZ, player, dirX, dirY, dirZ, count + 1);
        }
        return count;
    }

    /**
     * returns the highest coordinates of a stack of gameitems that is still used.
     *
     * @param coordinates
     * @return null if coordinates are out of bounds.
     */
    //@ pure;
    //@ requires coordinates != null;
    public Vector3 getHighestPosition(Vector2 coordinates) {
        //get the highest possible worldposition.
        if (!insideWorld(coordinates)) {
            return null;
        }
        int highestZ = 0;
        for (int z = 0; z < size.getZ(); z++) {
            Player actualPlayer = worldPosition[coordinates.getX()][coordinates.getY()][z];
            if (actualPlayer != null) {
                highestZ = z;
            } else {
                break;
            }
        }
        return new Vector3(coordinates.getX(), coordinates.getY(), highestZ);
    }

    /**
     * Create a deepcopy of this world.
     * Creates new GameItems and adds them to the world.
     *
     * @return A deepcopy of this world.
     */
    //@ ensures \result != null;
    public World deepCopy() {
        World newWorld = new World(this.getSize(), winLength);
        writeTo(newWorld);
        return newWorld;
    }

    /**
     * Write all changes of this world to a destination world.
     *
     * @param destination The world that should be edited and be synchronized with this world.
     */
    //@ reuires destination != null;
    //@ ensures \result != null;
    public void writeTo(World destination) {
        for (int x = 0; x < this.getSize().getX(); x++) {
            for (int y = 0; y < this.getSize().getY(); y++) {
                for (int z = 0; z < this.getSize().getZ(); z++) {
                    Vector3 coordinates = new Vector3(x, y, z);
                    Player owner = this.getOwner(coordinates);
                    Player owner2 = destination.getOwner(coordinates);
                    if (owner == null && owner2 != null) {
                        destination.removeGameItem(coordinates);
                        continue;
                    }
                    if (owner != null && !owner.equals(owner2)) {
                        destination.addGameItem(new Vector2(x, y), owner);
                    }
                }
            }
        }
    }

    /**
     * Check if the board is full.
     *
     * @return True if it's full, false if not.
     */
    //@ pure;
    public boolean isFull() {
        return remainingSpots <= 0;
    }

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
                Player owner = worldPosition[x][0][z];
                if (owner == null) {
                    result += "x";
                }

                for (int y = 1; y < size.getY(); y++) {
                    owner = worldPosition[x][y][z];
                    if (owner == null) {
                        result += " x";
                    }
                }
            }
        }

        return result;
    }
}
