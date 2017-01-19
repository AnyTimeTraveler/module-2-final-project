/*
 * $RCSfile: Canvas2D.java,v $
 *
 * Copyright (c) 2007 Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 *
 * $Revision: 1.2 $
 * $Date: 2007/02/09 17:21:37 $
 * $State: Exp $
 */

package ss.project.client.ui.gui;

import ss.project.shared.game.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Class: Canvas2D
 * <p>
 * Description: Used to respond to mouse events in the 2D window.
 * <p>
 * Version: 1.0
 */
public class Canvas2D extends Canvas implements MouseListener {

    Image backbuffer;    // Backbuffer image
    Graphics gc;            // Graphics context of backbuffer
    Engine engine;        // Game board
    Random random = new Random();
    Map<Player, Color> playerColorMap = new HashMap<>();
    private int width = 350;
    private int height = 350;
    private Color background = new Color(215, 245, 144);
    private int offset = 10;

    Canvas2D(Engine engine) {
        this.engine = engine;
    }

    public void setBuffer(Image backbuffer) {
        this.backbuffer = backbuffer;
        gc = backbuffer.getGraphics();
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        if (engine != null) {
            render2D(gc, engine.getWorld());
            g.drawImage(backbuffer, 0, 0, this);
        }
    }

    public void mousePressed(MouseEvent e) {
        //board.checkSelection2D(e.getX(), e.getY(), 1);
        System.out.println(e.getX() + "    " + e.getY());
        repaint();
    }

    private Color getPlayerColor(Player player) {
        if (!playerColorMap.containsKey(player)) {
            float r = random.nextFloat() / 2 + 0.5f;
            float g = random.nextFloat() / 2 + 0.5f;
            float b = random.nextFloat() / 2 + 0.5f;
            playerColorMap.put(player, new Color(r, g, b));
        }
        return playerColorMap.get(player);
    }

    /**
     * render a world in 2D.
     *
     * @param world
     */
    private void render2D(Graphics gc, World world) {
        gc.setColor(background);
        gc.fillRect(0, 0, width, height);

        int partX = (width - 2 * offset) / world.getSize().getX();
        int partY = (height - 2 * offset) / world.getSize().getY();
        int blockWidth = (int) ((width / world.getSize().getX()) * 0.75f);
        int blockHeight = (int) ((height / world.getSize().getY()) * 0.75f);

        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                WorldPosition worldPosition = world.getWorldPosition(new Vector3(x, y, 0));
                if (worldPosition.hasGameItem()) {
                    gc.setColor(getPlayerColor(worldPosition.getOwner()));
                } else {
                    gc.setColor(Color.black);
                }
                gc.fillRoundRect((int) (offset + x * partX + 0.5f * partX - 0.5f * blockWidth), (int) (offset + y * partY + 0.5f * partY - 0.5f * blockHeight), blockWidth, blockHeight, 5, 5);
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
