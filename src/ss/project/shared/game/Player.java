package ss.project.shared.game;


public abstract class Player {

    private String name;

    public Player() {

    }

    /**
     * Create a new player object with specified name.
     *
     * @param name name of the new player.
     */
    public Player(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Called everytime a new turn should be done.
     */
    public abstract void doTurn(Engine engine);

    @Override
    public String toString() {
        return "Player [name=" + name + "]";
    }
}
