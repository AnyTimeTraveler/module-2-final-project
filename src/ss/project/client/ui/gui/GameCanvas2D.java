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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class: GameCanvas2D
 * <p>
 * Description: Used to respond to mouse events in the 2D window.
 * <p>
 * Version: 1.0
 */
public class GameCanvas2D extends Canvas {

    Image backbuffer;    // Backbuffer image
    Graphics gc;            // Graphics context of backbuffer
    Engine engine;        // PNLGame board

    private int width = 350;
    private int height = 350;
    private Color background = new Color(215, 245, 144);
    private int offset = 10;
    private int zLayer;
    private PNLGame owner;

    GameCanvas2D(PNLGame owner, Engine engine, int zLayer, int width, int height) {
        this.owner = owner;
        this.engine = engine;
        this.zLayer = zLayer;
        this.width = width;
        this.height = height;
        this.setSize(width, height);
        this.setPreferredSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(width, height));
        this.addMouseListener(new MouseListen());
    }

    public void setBuffer(Image backbuffer) {
        this.backbuffer = backbuffer;
        gc = backbuffer.getGraphics();
    }

    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        if (engine != null) {
            render2D(gc, engine.getWorld());
            g.drawImage(backbuffer, 0, 0, this);
        }
    }

    /**
     * render a world in 2D.
     *
     * @param world
     */
    private void render2D(Graphics gc, World world) {
        gc.setColor(background);
        gc.fillRect(0, 0, width, height);

        float blockWidth = (float) (width - (2 + world.getSize().getX()) * offset) / world.getSize().getX();
        float blockHeight = (float) (height - (2 + world.getSize().getY()) * offset) / world.getSize().getY();
        float partX = blockWidth + offset;
        float partY = blockHeight + offset;

        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                //WorldPosition worldPosition = world.getWorldPosition(new Vector3(x, y, zLayer));
                Player actualPlayer = world.getOwner(new Vector3(x, y, zLayer));
                if (actualPlayer != null) {
                    gc.setColor(owner.getPlayerColor(actualPlayer));
                } else {
                    gc.setColor(Color.black);
                }
                gc.fillRoundRect((int) (offset + x * partX + 0.5f * offset), (int) (offset + y * partY + 0.5f * offset), (int) blockWidth, (int) blockHeight, 5, 5);
            }
        }
    }

    private Vector2 getActualCoordinates(int xPixels, int yPixels, Vector3 worldSize) {
        float blockWidth = (float) (width - (2 + worldSize.getX()) * offset) / worldSize.getX();
        float blockHeight = (float) (height - (2 + worldSize.getY()) * offset) / worldSize.getY();
        float partX = blockWidth + offset;
        float partY = blockHeight + offset;

        int xCoord = (int) ((xPixels - offset) / partX);
        int yCoord = (int) ((yPixels - offset) / partY);
        return new Vector2(xCoord, yCoord);
    }

    private class MouseListen extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            System.out.println(getActualCoordinates(e.getX(), e.getY(), engine.getWorld().getSize()));

            if (owner.getWaiter() != null) {
                owner.getCurrentPlayer().setSelectedCoordinates(getActualCoordinates(e.getX(), e.getY(), engine.getWorld().getSize()));
                synchronized (owner.getWaiter()) {
                    owner.getWaiter().notify();
                }
                //owner.refresh();
            }
        }
    }
}
