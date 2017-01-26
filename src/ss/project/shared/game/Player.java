package ss.project.shared.game;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public abstract class Player {
    @Getter
    @Setter
    private String name;
    /**
     * Used in multiplayer.
     */
    @Getter
    private int id;

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

    /**
     * Set the id of this player. Used in multiplayer.
     *
     * @param value
     */
    protected void setId(int value) {
        id = value;
    }

    /**
     * Called everytime a new turn should be done.
     */
    public abstract void doTurn(Engine engine);
}
