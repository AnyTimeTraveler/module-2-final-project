package ss.project.server;


import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
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
    }

    public BufferedInputStream getInputStream() {
        return inputStream;

    }
}
