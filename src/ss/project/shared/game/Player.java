package ss.project.shared.game;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public abstract class Player {
    @Getter
    @Setter
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


    /**
     * Called everytime a new turn should be done.
     */
    public abstract void doTurn(Engine engine);
}
