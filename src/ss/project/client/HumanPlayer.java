package ss.project.client;


import lombok.Getter;
import lombok.Setter;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector2;

public class HumanPlayer extends Player {
    Object waiter = new Object();
    @Getter
    @Setter
    Vector2 selectedCoordinates;

    public HumanPlayer(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void doTurn(Engine engine) {
        System.out.println("do turn");
        engine.getUI().startTurn(waiter, this);
        try {
            synchronized (waiter) {
                waiter.wait();
            }

            //We placed something!
            System.out.println(selectedCoordinates);
            if (!engine.addGameItem(selectedCoordinates, this)) {
                //we failed...
                doTurn(engine);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
