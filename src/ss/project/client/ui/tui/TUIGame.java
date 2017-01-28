package ss.project.client.ui.tui;

import ss.project.client.Controller;
import ss.project.client.HumanPlayer;
import ss.project.client.ui.GameDisplay;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector2;
import ss.project.shared.game.Vector3;
import ss.project.shared.game.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by simon on 16.01.17.
 */
public class TUIGame implements TUIPanel, GameDisplay {

    private Object waiter;
    private HumanPlayer humanPlayer;

    private Map<Player, Character> playerCharacterMap = new HashMap<>();

    @Override
    public void printScreen() {
        Controller.getController().getEngine().setUI(this);

        System.out.println(ASCIIArt.getHeadline("Game", 120));
        if (humanPlayer != null) {
            System.out.println(ASCIIArt.getHeadline("Current turn: " + humanPlayer.toString(), 120));
        }

        //Draw the game.
        World world = Controller.getController().getEngine().getWorld();
        System.out.println(printWorld(world));
    }

    /**
     * Print the world to string.
     *
     * @param world
     * @return
     */
    String printWorld(World world) {
        String result = "";

        String lineDash = "";
        String lineMark = "";
        for (int y = 0; y < world.getSize().getY(); y++) {
            lineDash = "";
            lineMark = "";
            for (int z = 0; z < world.getSize().getZ(); z++) {
                for (int x = 0; x < world.getSize().getX(); x++) {
                    lineDash += "----";
                    Player owner = world.getOwner(new Vector3(x, y, z));
                    lineMark += "| " + getPlayerCharacter(owner) + " ";
                }

                if (z != world.getSize().getZ() - 1) {
                    lineDash += "-   ";
                    lineMark += "|   ";
                }
            }
            lineDash += "-";
            lineMark += "|";
            result += lineDash + "\n";
            result += lineMark + "\n";
        }
        result += lineDash;
        return result;
    }

    /**
     * Get a character of a player.
     *
     * @param player
     * @return
     */
    Character getPlayerCharacter(Player player) {
        if (player == null) {
            return 'x';
        }
        if (!playerCharacterMap.containsKey(player)) {
            playerCharacterMap.put(player, (char) (playerCharacterMap.size() + 65));
        }
        return playerCharacterMap.get(player);
    }

    @Override
    public void handleInput(String input) {
        String[] coords = input.split(" ");
        if (humanPlayer != null && waiter != null) {
            if (coords.length > 1) {
                try {
                    humanPlayer.setSelectedCoordinates(new Vector2(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])));
                    synchronized (waiter) {
                        waiter.notify();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("<X> <Y>");
                }
            } else {
                System.out.println("<X> <Y>");
            }
        }
    }

    @Override
    public void startTurn(Object waiter, HumanPlayer humanPlayer) {
        this.waiter = waiter;
        this.humanPlayer = humanPlayer;
    }

    @Override
    public void update() {
        printScreen();
    }

    @Override
    public void showHint(int x, int y, int z) {
        System.out.println("Why don't you try: " + x + "," + y + "," + z);
    }

    @Override
    public void removeHint(int x, int y, int z) {

    }

    @Override
    public void setCurrentPlayer(Player player) {
        System.out.println("Current player: " + player.getName());
    }
}
