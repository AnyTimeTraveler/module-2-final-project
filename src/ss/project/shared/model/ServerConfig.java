package ss.project.shared.model;


/**
 * Created by simon on 01.12.16.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A model class that contains all fields that need to be saved and loaded for the server.
 *
 * @see ClientConfig
 */
public class ServerConfig {

    // Configfile name
    private static final String CONFIGFILE = "server-config.json";
    private static ServerConfig instance;
    public boolean spectate;

    //Variables
    /**
     * The ipv4 address of the host.
     */
    public String host;
    /**
     * The port number of the host.
     */
    public int port;
    /**
     * The maximum amount of players this server can handle.
     */
    public int maxPlayers;
    /**
     * If true this server has room support and client can create and join rooms.
     * If false clients will automatically join the first room.
     */
    public boolean roomSupport;
    /**
     * The maximum X size of the world this server can manage.
     */
    public int maxDimensionX;
    /**
     * The maximum Y size of the world this server can manage.
     */
    public int maxDimensionY;
    /**
     * The maximum Z size of the world this server can manage.
     */
    public int maxDimensionZ;
    /**
     * The maximum winlength this server can handle.
     */
    public int maxWinLength;
    /**
     * If true chat message can be send and recieved to/from this server.
     */
    public boolean chatSupport;
    /**
     * The leaderboard of this server.
     */
    public List<LeaderboardEntry> leaderboard;
    /**
     * The time in seconds before a player is considered disconnected.
     */
    public int timeoutInSeconds;

    /**
     * Set the default values for all fields.
     */
    private ServerConfig() {
        host = "127.0.0.1";
        port = 1234;
        maxPlayers = 2;
        roomSupport = true;
        maxDimensionX = 4;
        maxDimensionY = 4;
        maxDimensionZ = 4;
        maxWinLength = 4;
        chatSupport = true;
        leaderboard = new ArrayList<>();
        timeoutInSeconds = 20;
        spectate = false;
    }

    public synchronized static ServerConfig getInstance() {
        if (instance == null) {
            load();
        }
        return instance;
    }

    private static void load() {
        instance = fromFile(new File(CONFIGFILE));
        // no config file found
        if (instance == null) {
            instance = fromDefaults();
            instance.toFile();
        }
    }

    private static ServerConfig fromDefaults() {
        return new ServerConfig();
    }

    private static ServerConfig fromFile(File configFile) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            InputStreamReader in = new InputStreamReader(new FileInputStream(configFile));
            BufferedReader reader = new BufferedReader(in);
            return gson.fromJson(reader, ServerConfig.class);
        } catch (FileNotFoundException e) {
            System.out.println("ClientConfig file not found!");
            return null;
        }
    }

    /**
     * Adds a score to the leaderboard.
     *
     * @param name Name of the player.
     * @param type 0==draw, 1==win, 2==lose.
     */
    public void addScoreToBoard(String name, int type) {
        LeaderboardEntry newLeaderBoardEntry = null;
        for (LeaderboardEntry aLeaderboard : leaderboard) {
            if (aLeaderboard.getPlayerName().equals(name)) {
                newLeaderBoardEntry = aLeaderboard;
                break;
            }
        }
        if (newLeaderBoardEntry == null) {
            newLeaderBoardEntry = new LeaderboardEntry(name, 0, 0, 0);
            leaderboard.add(newLeaderBoardEntry);
        }
        switch (type) {
            case 0:
                newLeaderBoardEntry.addDraw();
                break;
            case 1:
                newLeaderBoardEntry.addWin();
                break;
            case 2:
                newLeaderBoardEntry.addLose();
                break;
        }
        toFile();
    }

    public void toFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonConfig = gson.toJson(this);
        FileWriter writer;
        try {
            writer = new FileWriter(new File(CONFIGFILE));
            writer.write(jsonConfig);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}