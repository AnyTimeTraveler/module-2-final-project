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
    public boolean Spectate;

    //Variables
    /**
     * The ipv4 address of the host.
     */
    public String Host;
    /**
     * The port number of the host
     */
    public int Port;
    /**
     * The maximum amount of players this server can handle.
     */
    public int MaxPlayers;
    /**
     * If true this server has room support and client can create and join rooms.
     * If false clients will automatically join the first room.
     */
    public boolean RoomSupport;
    /**
     * The maximum X size of the world this server can manage.
     */
    public int MaxDimensionX;
    /**
     * The maximum Y size of the world this server can manage.
     */
    public int MaxDimensionY;
    /**
     * The maximum Z size of the world this server can manage.
     */
    public int MaxDimensionZ;
    /**
     * The maximum winlength this server can handle.
     */
    public int MaxWinLength;
    /**
     * If true chat message can be send and recieved to/from this server.
     */
    public boolean ChatSupport;
    /**
     * The leaderboard of this server.
     */
    public List<LeaderboardEntry> Leaderboard;
    /**
     * The time in seconds before a player is considered disconnected.
     */
    public int TimeoutInSeconds;

    /**
     * Set the default values for all fields.
     */
    private ServerConfig() {
        Host = "127.0.0.1";
        Port = 1234;
        MaxPlayers = 2;
        RoomSupport = true;
        MaxDimensionX = 4;
        MaxDimensionY = 4;
        MaxDimensionZ = 4;
        MaxWinLength = 4;
        ChatSupport = true;
        Leaderboard = new ArrayList<>();
        TimeoutInSeconds = 20;
        Spectate = true;
    }

    public synchronized static ServerConfig getInstance() {
        if (instance == null) {
            load();
        }
        return instance;
    }

    public static void load(File file) {
        instance = fromFile(file);
        // no config file found
        if (instance == null) {
            instance = fromDefaults();
            instance.toFile();
        }
    }

    private static void load() {
        load(new File(CONFIGFILE));
    }

    private static ServerConfig fromDefaults() {
        return new ServerConfig();
    }

    private static ServerConfig fromFile(File configFile) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));
            return gson.fromJson(reader, ServerConfig.class);
        } catch (FileNotFoundException e) {
            System.out.println("ClientConfig file not found!");
            return null;
        }
    }

    /**
     * @param name
     * @param type 0==draw, 1==win, 2==lose.
     */
    public void addScoreToBoard(String name, int type) {
        LeaderboardEntry newLeaderBoardEntry = null;
        for (int i = 0; i < Leaderboard.size(); i++) {
            if (Leaderboard.get(i).getPlayerName().equals(name)) {
                newLeaderBoardEntry = Leaderboard.get(i);
                break;
            }
        }
        if (newLeaderBoardEntry == null) {
            newLeaderBoardEntry = new LeaderboardEntry(name, 0, 0, 0);
            Leaderboard.add(newLeaderBoardEntry);
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
    }

    public void toFile() {
        toFile(new File(CONFIGFILE));
    }

    public void toFile(String file) {
        toFile(new File(file));
    }

    public void toFile(File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonConfig = gson.toJson(this);
        FileWriter writer;
        try {
            writer = new FileWriter(file);
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