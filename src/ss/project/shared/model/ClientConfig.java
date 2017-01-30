package ss.project.shared.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import ss.project.client.HumanPlayer;
import ss.project.shared.computerplayer.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by simon on 01.12.16.
 */

public class ClientConfig {

    // Configfile name
    private static final String CONFIGFILE = "client-config.json";
    private static ClientConfig instance;

    //Variables
    @Expose
    public String WindowTitle;
    public HashMap<String, Class> PlayerTypes;
    @Expose
    public boolean Fullscreen;
    @Expose
    public List<Connection> KnownServers;
    @Expose
    public String PlayerName;
    public int MaxPlayers;
    public boolean RoomSupport;
    public int MaxDimensionX;
    public int MaxDimensionY;
    public int MaxDimensionZ;
    public int MaxWinLength;
    public boolean ChatSupport;
    public boolean AutoRefresh;
    public int MaxChatMessages;
    public String playerType;
    public int playerSmartness;

    private ClientConfig() {
        WindowTitle = "Connect Four 3D";
        Fullscreen = false;
        PlayerTypes = new HashMap<>();
        PlayerTypes.put("Human", HumanPlayer.class);
        PlayerTypes.put("Linear AI", LinearComputerPlayer.class);
        PlayerTypes.put("Minmax AI", MinMaxComputerPlayer.class);
        PlayerTypes.put("Minmax AI 2", MinMaxComputerPlayer2.class);
        PlayerTypes.put("Minmax alpha beta", MinMaxAlphaBetaComputerPlayer.class);
        PlayerTypes.put("Random AI", RandomComputerPlayer.class);
        KnownServers = new ArrayList<>();
        KnownServers.add(new Connection("Localhost", "127.0.0.1", 1234));
        PlayerName = "Simon";
        MaxPlayers = 10;
        RoomSupport = true;
        MaxDimensionX = 4;
        MaxDimensionY = 4;
        MaxDimensionZ = 4;
        MaxWinLength = 4;
        ChatSupport = true;
        AutoRefresh = true;
        MaxPlayers = 50;
        MaxChatMessages = 50;
        playerType = "Human";
        playerSmartness = 6;
    }

    public static ClientConfig getInstance() {
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

    private static ClientConfig fromDefaults() {
        return new ClientConfig();
    }

    private static ClientConfig fromFile(File configFile) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));
            return gson.fromJson(reader, ClientConfig.class);
        } catch (FileNotFoundException e) {
            System.out.println("ClientConfig file not found!");
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
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
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