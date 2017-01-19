package ss.project.client.ui.gui;

import ss.project.shared.computerplayer.MinMaxComputerPlayer;
import ss.project.shared.computerplayer.RandomComputerPlayer;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector2;
import ss.project.shared.game.Vector3;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by simon on 16.01.17.
 */
public class Game extends JPanel {
    /**
     * 2D rendering canvas.
     */
    private Canvas2D canvas2D;
    /**
     * Size of the graphics window in pixels.
     */
    private int width, height;
    /**
     * Backbuffer image used for 2D double buffering.
     */
    private Image backbuffer2D;
    private Engine engine;

    public Game(MainFrame mainFrame) {
        super();

        width = 350;
        height = 350;

        RandomComputerPlayer test = new RandomComputerPlayer("computer random");

        //Create a new engine.
        mainFrame.setEngine(new Engine(new Vector3(4, 4, 4), new Player[]{test,
                new MinMaxComputerPlayer("computer minmax")}));

        engine = mainFrame.getEngine();

        engine.addGameItem(new Vector2(2, 3), test);
        engine.addGameItem(new Vector2(1, 3), test);

        setBackground(Color.white);

        // Create a 2D graphics canvas.
        canvas2D = new Canvas2D(engine);
        canvas2D.setSize(width, height);
        canvas2D.setLocation(width + 10, 5);
        canvas2D.addMouseListener(canvas2D);

        // Create the 2D backbuffer
        //backbuffer2D = createImage(width, height);
        backbuffer2D = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        canvas2D.setBuffer(backbuffer2D);

        this.add(canvas2D);
    }
}
