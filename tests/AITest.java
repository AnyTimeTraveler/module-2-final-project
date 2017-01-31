import org.junit.Before;
import ss.project.shared.computerplayer.ComputerPlayer;
import ss.project.shared.computerplayer.MinMaxComputerPlayer;
import ss.project.shared.computerplayer.MinMaxComputerPlayer2;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector3;
import ss.project.shared.model.ClientConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A test unit that is used to improve the AI and make sure every AI places when calling doTurn.
 * Created by fw on 27/01/2017.
 */
public class AITest {
    Engine engine;
    List<Player> players;

    /**
     * Create the list of AI (ignore the HumanPlayer).
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        players = new ArrayList<>();
        int i = 0;
        for (Class value : ClientConfig.getInstance().PlayerTypes.values()) {
            if (!ComputerPlayer.class.isAssignableFrom(value)) {
                continue;
            }
            Player player = (Player) value.newInstance();
            player.setName(value.toString());
            player.setId(i);
            players.add(player);
            i++;
        }
    }

    /**
     * Let all AI play against each other.
     *
     * @throws Exception
     */
    // @Test
    public void testAllAI() throws Exception {
        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                testAI(players.get(i), players.get(j));
            }
        }
    }

    /**
     * Make two minmax play against each other to see which one wins.
     * Used to improve the AI.
     */
    // @Test
    public void minMaxChecker() {
        MinMaxComputerPlayer minMaxComputerPlayer = new MinMaxComputerPlayer("min max", 6);
        minMaxComputerPlayer.setId(1);
        MinMaxComputerPlayer2 minMaxComputerPlayer2 = new MinMaxComputerPlayer2("min max 2", 6);
        minMaxComputerPlayer2.setId(2);
        Player winner = testAI(minMaxComputerPlayer, minMaxComputerPlayer2);
        System.out.println(winner.getName());
    }

    /**
     * Let two AI play against each other on a default 4x4x4 world and return the winner.
     *
     * @param player1 The first AI.
     * @param player2 The second AI.
     * @return The player that won the game.
     */
    private Player testAI(Player player1, Player player2) {
        engine = new Engine(new Vector3(4, 4, 4), 4, Arrays.asList(player1, player2));
        engine.startGame();

        return engine.getPlayer(engine.getWinner());
    }
}