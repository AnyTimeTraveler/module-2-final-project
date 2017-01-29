package ss.project.shared.model;

import lombok.Data;
import ss.project.shared.Protocol;
import ss.project.shared.Serializable;
import ss.project.shared.exceptions.IllegalParameterException;
import ss.project.shared.exceptions.ParameterLengthsMismatchException;
import ss.project.shared.exceptions.ProtocolException;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Contains Data about a leaderBoardEntry. A leaderboard would consist of multiple entries.
 * <p>
 * Created by simon on 25.01.17.
 */
@Data
public class LeaderboardEntry implements Serializable {
    private String playerName;
    private int wins;
    private int draws;
    private int losses;

    /**
     * Create a leaderboardEntry.
     *
     * @param playerName The name of the player.
     * @param wins       The amount of wins this player has.
     * @param draws      The amount of draws this player has.
     * @param losses     The amount of losses this player has.
     */
    public LeaderboardEntry(String playerName, int wins, int draws, int losses) {
        this.playerName = playerName;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
    }

    /**
     * Create a leaderboardentry from a string of the protocol.
     *
     * @param line
     * @return A new leaderBoardEntry
     * @throws ProtocolException The input is not a valid according to our protocol.
     */
    public static LeaderboardEntry fromString(String line) throws ProtocolException {
        // split room by it's parameters
        String[] params = line.split(Protocol.PIPE_SYMBOL);
        // check if the amount of parameters matches
        if (params.length != Protocol.ROOM_PARAMETERS) {
            throw new ParameterLengthsMismatchException(Protocol.ROOM_PARAMETERS, params.length);
        }
        try {
            // create an instance with the given parameters
            return new LeaderboardEntry(params[0], Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]));
        } catch (NumberFormatException e) {
            IllegalParameterException illegalParameterException = new IllegalParameterException("Could not convert arguments to numbers: " + Arrays.stream(params).collect(Collectors.joining(", ")));
            illegalParameterException.setStackTrace(e.getStackTrace());
            throw illegalParameterException;
        }
    }

    /**
     * Create a string representing a leaderboard entry in the protocol.
     *
     * @return
     */
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(playerName);
        sb.append(Protocol.PIPE_SYMBOL);
        sb.append(wins);
        sb.append(Protocol.PIPE_SYMBOL);
        sb.append(draws);
        sb.append(Protocol.PIPE_SYMBOL);
        sb.append(losses);
        return sb.toString();
    }
}
