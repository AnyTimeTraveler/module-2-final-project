package ss.project.server;

import lombok.Data;
import ss.project.shared.Protocol;
import ss.project.shared.exceptions.IllegalParameterException;
import ss.project.shared.exceptions.ParameterLengthsMismatchException;
import ss.project.shared.exceptions.ProtocolException;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by simon on 25.01.17.
 */
@Data
public class LeaderboardEntry {
    private String playerName;
    private int wins;
    private int draws;
    private int losses;

    public LeaderboardEntry(String playerName, int wins, int draws, int losses) {
        this.playerName = playerName;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
    }

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
