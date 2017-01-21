package ss.project.server;


import ss.project.server.Exceptions.InvalidInputException;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector2;
import ss.project.shared.game.World;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkPlayer extends Player {

    private Socket socket;
    private BufferedOutputStream outputStream;
    private BufferedInputStream inputStream;
    private World world;

    public NetworkPlayer(String name, Socket socket) throws IOException {
        super(name);
        this.socket = socket;
        this.outputStream = new BufferedOutputStream(socket.getOutputStream());
        this.inputStream = new BufferedInputStream(socket.getInputStream());
    }

    @Override
    public void doTurn(Engine engine) {
        this.world = engine.getWorld();
//        try {
//            //outputStream.write(Protocol.get().doTurn().getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //Read from our input stream.
        try {
            getMoveCoordinates("REPLACE BY INPUTSTREAM");
        } catch (InvalidInputException e) {
            e.printStackTrace();
        }
    }

    /**
     * Input will be “makeMove 2 3”
     *
     * @param rawInput
     * @return
     */
    private Vector2 getMoveCoordinates(String rawInput) throws InvalidInputException {
        String[] parts = rawInput.split(" ");
        if (parts.length < 3) {
            throw new InvalidInputException(rawInput);
        }

        try {
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);

            return new Vector2(x, y);
        } catch (NumberFormatException e) {
            throw new InvalidInputException(rawInput);
        }
    }

    public BufferedInputStream getInputStream() {
        return inputStream;

    }
}
