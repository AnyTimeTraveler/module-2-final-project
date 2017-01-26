package ss.project.client.ui.gui;

import ss.project.client.Controller;
import ss.project.client.HumanPlayer;
import ss.project.client.ui.GameDisplay;
import ss.project.shared.game.Player;

import javax.swing.*;
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
    private GridBagConstraints worldPanelConstraints;
    private JLabel currentTurnLabel;
    private JPanel worldPanel;

    public PNLGame(Controller controller) {
        super(true);
        this.controller = controller;

        width = 350;
        height = 350;

        this.setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weightx = 0.5f;
        gridBagConstraints.weighty = 0.5f;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        setBackground(Color.white);

        //Show the title.
        currentTurnLabel = GUIUtils.createLabel("", GUIUtils.LabelType.TITLE);
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        this.add(currentTurnLabel, gridBagConstraints);

        //Create the world panel and add it.
        worldPanel = new JPanel(new GridBagLayout());
        worldPanelConstraints = new GridBagConstraints();
        this.add(worldPanel, gridBagConstraints);
    }

    @Override
    public void onEnter() {
        controller.getEngine().setUI(this);
        canvas2D = new GameCanvas2D[controller.getEngine().getWorld().getSize().getZ()];
        backbuffer2D = new Image[canvas2D.length];
        worldPanelConstraints.weightx = 0;
        worldPanelConstraints.weighty = 0;
        int panelsOnOneRow = (int) Math.ceil(canvas2D.length / 2f);
        for (int z = 0; z < canvas2D.length; z++) {
            // Create a 2D graphics canvas.
            canvas2D[z] = new GameCanvas2D(this, controller.getEngine(), z, width, height);

            // Create the 2D backbuffer.
            backbuffer2D[z] = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            canvas2D[z].setBuffer(backbuffer2D[z]);

            worldPanelConstraints.gridx = z % panelsOnOneRow;
            worldPanelConstraints.gridy = Math.floorDiv(z, panelsOnOneRow) + 1;
            worldPanel.add(canvas2D[z], worldPanelConstraints);
        }
        worldPanelConstraints.weightx = 0.5f;
        worldPanelConstraints.weighty = 0.5f;
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

    /**
     * Get the color of the player.
     *
     * @param player
     * @return
     */
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
        currentTurnLabel.setText(humanPlayer.getName() + "'s turn");
        this.waiter = waiter;
        this.currentPlayer = humanPlayer;
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
