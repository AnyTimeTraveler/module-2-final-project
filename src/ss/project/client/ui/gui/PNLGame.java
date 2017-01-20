package ss.project.client.ui.gui;

import ss.project.client.HumanPlayer;
import ss.project.client.ui.GameDisplay;
import ss.project.shared.computerplayer.MinMaxComputerPlayer;
import ss.project.shared.computerplayer.RandomComputerPlayer;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector3;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by simon on 16.01.17.
 */
public class PNLGame extends GUIPanel implements GameDisplay {
    /**
     * 2D rendering canvas.
     */
    private GameCanvas2D canvas2D;
    /**
     * Size of the graphics window in pixels.
     */
    private int width, height;
    /**
     * Backbuffer image used for 2D double buffering.
     */
    private Image backbuffer2D;
    private Engine engine;
    private FRMMain mainFrame;

    public PNLGame(FRMMain mainFrame) {
        super();
        this.mainFrame = mainFrame;
    }

    @Override
    public void onEnter() {
        width = 350;
        height = 350;

        RandomComputerPlayer test = new RandomComputerPlayer("computer random");

        //Create a new engine.
        mainFrame.setEngine(new Engine(new Vector3(4, 4, 4), new Player[]{new HumanPlayer("0"),
                new MinMaxComputerPlayer("min max")}));

        engine = mainFrame.getEngine();
        engine.setUI(this);

        setBackground(Color.white);

        // Create a 2D graphics canvas.
        canvas2D = new GameCanvas2D(engine);
        canvas2D.setSize(width, height);
        canvas2D.setLocation(width + 10, 5);

        // Create the 2D backbuffer
        //backbuffer2D = createImage(width, height);
        backbuffer2D = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        canvas2D.setBuffer(backbuffer2D);

        this.add(canvas2D);

        //EventQueue.invokeLater(engine::startGame);
        Thread thread = new Thread(() -> engine.startGame());
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void startTurn(Object waiter, HumanPlayer humanPlayer) {
        System.out.println("Start the turn... Show whose turn it is: " + humanPlayer.getName());
        canvas2D.setWaiter(waiter);
        canvas2D.setCurrentPlayer(humanPlayer);
    }

    @Override
    public void update() {

    }


    @Override
    public void onLeave() {

    }
}
