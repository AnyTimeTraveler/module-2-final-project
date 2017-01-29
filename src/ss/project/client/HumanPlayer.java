package ss.project.client;


import lombok.Getter;
import lombok.Setter;
import ss.project.shared.computerplayer.MinMaxComputerPlayer;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector2;
import ss.project.shared.game.Vector3;

import javax.swing.*;

public class HumanPlayer extends Player {
    private final Object hintSync = new Object();
    Object waiter = new Object();
    @Getter
    @Setter
    Vector2 selectedCoordinates;
    MinMaxComputerPlayer hintPlayer = new MinMaxComputerPlayer(4);
    Vector3 hintPos;
    private Timer hintTimer;

    public HumanPlayer(String name, int id) {
        super(name);
        setId(id);
    }

    public HumanPlayer(String name) {
        super(name);
    }

    public HumanPlayer() {
        super();
    }

    @Override
    public void doTurn(Engine engine) {
        hintTimer = new Timer(5000, e -> doHint(engine));
        hintTimer.start();

        engine.getUI().startTurn(waiter, this);

        hintTimer.start();
        try {
            synchronized (waiter) {
                waiter.wait();
            }

            //We placed something!
            if (hintPos != null) {
                synchronized (hintSync) {
                    engine.getUI().removeHint(hintPos.getX(), hintPos.getY(), hintPos.getZ());
                }
            }

            if (!engine.addGameItem(selectedCoordinates, this)) {
                //we failed...
                doTurn(engine);
            } else {
                hintTimer.stop();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doHint(Engine engine) {
        synchronized (hintSync) {
            hintPos = engine.getWorld().getWorldPosition(hintPlayer.getMove(engine));
            engine.getUI().showHint(hintPos.getX(), hintPos.getY(), hintPos.getZ());
            hintTimer.stop();
        }
    }
}
