package ss.project.client.ui.gui;

import ss.project.client.Controller;
import ss.project.client.HumanPlayer;
import ss.project.client.ui.GameDisplay;
import ss.project.shared.computerplayer.MinMaxComputerPlayer;
import ss.project.shared.computerplayer.RandomComputerPlayer;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector3;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by simon on 16.01.17.
 */
public class PNLGame extends GUIPanel implements GameDisplay {
    Map<Player, Color> playerColorMap = new HashMap<>();
    Random random = new Random();
    /**
     * 2D rendering canvas.
     */
    private GameCanvas2D[] canvas2D;
    /**
     * Size of the graphics window in pixels.
     */
    private int width, height;
    /**
     * Backbuffer image used for 2D double buffering.
     */
    private Image[] backbuffer2D;
    private Engine engine;
    private Controller controller;
    private Object waiter;
    private HumanPlayer currentPlayer;

    public PNLGame(Controller controller) {
        super(true);
        this.controller = controller;
    }

    @Override
    public void onEnter() {
        width = 350;
        height = 350;

        controller.setFrameSize(width * 2 + 20, height * 2 + 40);

        RandomComputerPlayer test = new RandomComputerPlayer("computer random");

        //Create a new engine.
        MinMaxComputerPlayer minMaxComputerPlayer = new MinMaxComputerPlayer("min max", 6);
        MinMaxComputerPlayer minMaxComputerPlayer1 = new MinMaxComputerPlayer("min max2", 6);
        controller.setEngine(new Engine(new Vector3(4, 4, 4), new Player[]{minMaxComputerPlayer,
                minMaxComputerPlayer1}));

        engine = controller.getEngine();
        engine.setUI(this);

        this.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weightx = 0.5f;
        gridBagConstraints.weighty = 0.5f;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        setBackground(Color.white);

        canvas2D = new GameCanvas2D[engine.getWorld().getSize().getZ()];
        backbuffer2D = new Image[canvas2D.length];
        for (int z = 0; z < canvas2D.length; z++) {
            // Create a 2D graphics canvas.
            canvas2D[z] = new GameCanvas2D(this, engine, z, width, height);
            //canvas2D[i].setLocation(width + 10, 5);

            // Create the 2D backbuffer
            //backbuffer2D = createImage(width, height);
            backbuffer2D[z] = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            canvas2D[z].setBuffer(backbuffer2D[z]);

            gridBagConstraints.gridx = z % 2;
            gridBagConstraints.gridy = Math.floorDiv(z, 2);

            this.add(canvas2D[z], gridBagConstraints);
        }


        //EventQueue.invokeLater(engine::startGame);
        Thread thread = new Thread(() -> engine.startGame());
        thread.setDaemon(true);
        thread.start();
    }

    public Object getWaiter() {
        return this.waiter;
    }

    public HumanPlayer getCurrentPlayer() {
        return this.currentPlayer;
    }

    @Override
    public void update() {
        for (int z = 0; z < canvas2D.length; z++) {
            canvas2D[z].repaint();
        }
    }

    public Color getPlayerColor(Player player) {
        if (!playerColorMap.containsKey(player)) {
            float r = random.nextFloat() / 2 + 0.5f;
            float g = random.nextFloat() / 2 + 0.5f;
            float b = random.nextFloat() / 2 + 0.5f;
            playerColorMap.put(player, new Color(r, g, b));
        }
        return playerColorMap.get(player);
    }

    @Override
    public void startTurn(Object waiter, HumanPlayer humanPlayer) {
        System.out.println("Start the turn... Show whose turn it is: " + humanPlayer.getName());
        this.waiter = waiter;
        this.currentPlayer = humanPlayer;
        //canvas2D.setWaiter(waiter);
        //canvas2D.setCurrentPlayer(humanPlayer);
    }

    @Override
    public void onLeave() {

    }
}
