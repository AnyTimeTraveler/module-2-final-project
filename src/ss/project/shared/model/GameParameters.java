package ss.project.shared.model;

import lombok.Data;
import ss.project.shared.Protocol;
import ss.project.shared.Serializable;
import ss.project.shared.game.Vector3;


/**
 * Created by simon on 28.01.17.
 */
@Data
public class GameParameters implements Serializable {
    private int sizeX;
    private int sizeY;
    private int sizeZ;
    private int winLength;

    public GameParameters(int sizeX, int sizeY, int sizeZ, int winLength) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.winLength = winLength;
    }

    public static GameParameters fromString(String line) {
        String[] params = line.split(Protocol.PIPE_SYMBOL);

        return new GameParameters(Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]));
    }

    public String serialize() {
        return String.join("|", String.valueOf(sizeX), String.valueOf(sizeY), String.valueOf(sizeZ), String.valueOf(winLength));
    }

    public Vector3 getWorldSize() {
        return new Vector3(sizeX, sizeY, sizeZ);
    }
}
