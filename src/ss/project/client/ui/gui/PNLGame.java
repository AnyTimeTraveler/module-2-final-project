package ss.project.client.ui.gui;

import ss.project.client.Controller;
import ss.project.client.HumanPlayer;
import ss.project.client.ui.GameDisplay;
import ss.project.shared.game.Player;

import javax.swing.*;
import java.awt.*;
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
    private Controller controller;
    private Object waiter;
    private HumanPlayer currentPlayer;
    private GridBagConstraints worldPanelConstraints;
    private JLabel currentTurnLabel;
    private JPanel worldPanel;

    private Timer animationTimer;

    public PNLGame(Controller controller) {
        super(true);
        this.controller = controller;

        width = 350;
        height = 350;

        this.setLayout(new BorderLayout());

        //Show the title.
        currentTurnLabel = GUIUtils.createLabel("", GUIUtils.LabelType.TITLE);
        this.add(currentTurnLabel, BorderLayout.NORTH);

        //Create the world panel and add it.
        worldPanel = new JPanel(new GridBagLayout());
        worldPanelConstraints = new GridBagConstraints();
        this.add(worldPanel, BorderLayout.CENTER);

        animationTimer = new Timer(10, e -> doCanvasAnimations());
        animationTimer.stop();
    }

    @Override
    public void onEnter() {
        controller.getEngine().setUI(this);
        canvas2D = new GameCanvas2D[controller.getEngine().getWorld().getSize().getZ()];
        worldPanelConstraints.weightx = 0.5f;
        worldPanelConstraints.weighty = 0.5f;
        worldPanelConstraints.fill = GridBagConstraints.BOTH;
        int panelsOnOneRow = (int) Math.ceil(canvas2D.length / 2f);
        for (int z = 0; z < canvas2D.length; z++) {
            // Create a 2D graphics canvas.
            canvas2D[z] = new GameCanvas2D(this, controller.getEngine(), z, width, height);

            worldPanelConstraints.gridx = z % panelsOnOneRow;
            worldPanelConstraints.gridy = Math.floorDiv(z, panelsOnOneRow) + 1;
            worldPanel.add(canvas2D[z], worldPanelConstraints);
        }
        worldPanelConstraints.weightx = 0.5f;
        worldPanelConstraints.weighty = 0.5f;

        animationTimer.start();
    }

    public Object getWaiter() {
        return this.waiter;
    }

    public HumanPlayer getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void showHint(int x, int y, int z) {
        canvas2D[z].showHint(x, y, getCurrentPlayer());
    }

    public void removeHint(int x, int y, int z) {
        canvas2D[z].removeHint(x, y);
    }

    private void doCanvasAnimations() {
        for (int i = 0; i < canvas2D.length; i++) {
            canvas2D[i].increaseAnimationState();
        }
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

        animationTimer.stop();
    }
}
