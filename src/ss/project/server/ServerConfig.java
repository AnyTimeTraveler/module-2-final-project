package ss.project.server;


/**
 * Created by simon on 01.12.16.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServerConfig {

    // Configfile name
    private static final String CONFIGFILE = "server-config.json";
    private static ServerConfig instance;

    //Variables
    public String Host;
    public int Port;
    public int MaxPlayers;
    public boolean RoomSupport;
    public int MaxDimensionX;
    public int MaxDimensionY;
    public int MaxDimensionZ;
    public int MaxWinLength;
    public boolean ChatSupport;
    public List<LeaderboardEntry> Leaderboard;

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
    }

    public static ServerConfig getInstance() {
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
            e.printStackTrace();
            return null;
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