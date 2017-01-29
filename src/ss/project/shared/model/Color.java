package ss.project.shared.model;

import lombok.Data;
import ss.project.shared.Serializable;

import java.util.Random;

/**
 * Created by simon on 28.01.17.
 */
@Data
public class Color implements Serializable {
    private int r, g, b;

    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static Color fromString(String line) {
        int start = line.indexOf(' ') + 1;
        int r = hexToInt(line, start);
        int g = hexToInt(line, start + 2);
        int b = hexToInt(line, start + 4);
        return new Color(r, g, b);
    }

    private static int hexToInt(String line, int pos) {
        return Integer.parseInt(line.substring(pos, pos + 2), 16);
    }

    public static Color getRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    @Override
    public String serialize() {
        return String.format("%02X%02X%02X", r, g, b);
    }
}
