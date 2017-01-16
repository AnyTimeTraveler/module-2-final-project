package ss.project.shared.game;

public abstract class Player {

    private String name;

    /**
     * Create a new player object with specified name.
     *
     * @param name name of the new player.
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Called everytime a new turn should be done. TODO: add the possibility for
     * the user to do a turn, now it's just ai.
     */
    public abstract void doTurn(Engine engine);

    /**
     * get the name of this player. Set when initializing.
     *
     * @return a string of the name.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Player [name=" + name + "]";
    }
}
