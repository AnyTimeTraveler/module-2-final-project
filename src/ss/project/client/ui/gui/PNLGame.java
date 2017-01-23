package ss.project.client.ui.gui;

import ss.project.client.Controller;
import ss.project.client.HumanPlayer;
import ss.project.client.ui.GameDisplay;
import ss.project.shared.game.Player;

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
    private Controller controller;
    private Object waiter;
    private HumanPlayer currentPlayer;
    private GridBagConstraints gridBagConstraints;

    public PNLGame(Controller controller) {
        super(true);
        this.controller = controller;

        width = 350;
        height = 350;

        controller.setFrameSize(width * 2 + 20, height * 2 + 40);

        this.setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weightx = 0.5f;
        gridBagConstraints.weighty = 0.5f;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        setBackground(Color.white);
    }

    @Override
    public void onEnter() {
        controller.getEngine().setUI(this);
        canvas2D = new GameCanvas2D[controller.getEngine().getWorld().getSize().getZ()];
        backbuffer2D = new Image[canvas2D.length];
        for (int z = 0; z < canvas2D.length; z++) {
            // Create a 2D graphics canvas.
            canvas2D[z] = new GameCanvas2D(this, controller.getEngine(), z, width, height);
            //canvas2D[i].setLocation(width + 10, 5);

            // Create the 2D backbuffer
            //backbuffer2D = createImage(width, height);
            backbuffer2D[z] = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            canvas2D[z].setBuffer(backbuffer2D[z]);

            gridBagConstraints.gridx = z % 2;
            gridBagConstraints.gridy = Math.floorDiv(z, 2);

            this.add(canvas2D[z], gridBagConstraints);
        }
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
        /**
         * Remove all canvasses we've made.
         */
        for (int i = 0; i < canvas2D.length; i++) {
            this.remove(canvas2D[i]);
        }
    }
}
