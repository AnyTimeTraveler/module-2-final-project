package ss.project.shared.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import ss.project.client.HumanPlayer;
import ss.project.shared.computerplayer.*;
import ss.project.shared.game.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A model class that stores fields for the client.
 * Uses the gson library.
 * <p>
 * Saves all fields in a file which is loaded on startup.
 * <p>
 * Created by simon on 01.12.16.
 */
public class ClientConfig {

    /**
     * Configfile name
     */
    private static final String CONFIGFILE = "client-config.json";
    /**
     * The singleton field.
     */
    private static ClientConfig instance;

    //Variables
    /**
     * The title of the frame.
     */
    @Expose
    public String WindowTitle;
    /**
     * A map that contains all possible playerTypes the player can use.
     */
    public HashMap<String, Class<? extends Player>> PlayerTypes;
    /**
     * If true, the frame will be shown in full screen.
     * If false in windowed mode.
     */
    @Expose
    public boolean Fullscreen;
    /**
     * A list of servers that are added and need to be shown in the serverbrowser.
     */
    @Expose
    public List<Connection> KnownServers;
    /**
     * The playername used in multiplayer.
     */
    @Expose
    public String PlayerName;
    /**
     * The max amount of players this client can handle.
     */
    public int MaxPlayers;
    /**
     * If true this client can handle rooms.
     */
    public boolean RoomSupport;
    /**
     * The maximum X size of the world this client can handle.
     */
    public int MaxDimensionX;
    /**
     * The maximum Y size of the world this client can handle.
     */
    public int MaxDimensionY;
    /**
     * The maximum Z size of the world this client can handle.
     */
    public int MaxDimensionZ;
    /**
     * The maximum length that is needed to win this client can handle.
     */
    public int MaxWinLength;
    /**
     * If true this client can send and recieve chat messages.
     */
    public boolean ChatSupport;
    /**
     * If true the client wants to be notified if the server get changes in the rooms.
     * If false the client needs to ask for an update by itself.
     */
    public boolean AutoRefresh;
    /**
     * The max amount of chat message shown on the UI.
     */
    public int MaxChatMessages;
    /**
     * The playertype the user wants to use in multiplayer
     *
     * @see ClientConfig#PlayerTypes
     */
    public String playerType;
    /**
     * The smartness of the playerType if it's an AI.
     */
    public int playerSmartness;

    /**
     * Set all default values of the fields.
     * If a value is saved of a field that value will be loaded.
     */
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
        MaxPlayers = Integer.MAX_VALUE;
        RoomSupport = true;
        MaxDimensionX = Integer.MAX_VALUE;
        MaxDimensionY = Integer.MAX_VALUE;
        MaxDimensionZ = Integer.MAX_VALUE;
        MaxWinLength = Integer.MAX_VALUE;
        ChatSupport = true;
        AutoRefresh = true;
        MaxPlayers = 50;
        MaxChatMessages = 50;
        playerType = "Human";
        playerSmartness = 6;
    }

    /**
     * A singleton reference to the ClientConfig.
     * If no instance is made, a new one will be created.
     *
     * @return
     */
    public static ClientConfig getInstance() {
        if (instance == null) {
            load();
        }
        return instance;
    }

    /**
     * Load the fields from a file. If no file is found default values are used.
     *
     * @param file
     */
    public static void load(File file) {
        instance = fromFile(file);
        // no config file found
        if (instance == null) {
            instance = fromDefaults();
            instance.toFile();
        }
    }

    /**
     * Load fields from the default file location.
     *
     * @see ClientConfig#load(File)
     */
    private static void load() {
        load(new File(CONFIGFILE));
    }

    /**
     * Create an instance with default values.
     *
     * @return
     */
    private static ClientConfig fromDefaults() {
        return new ClientConfig();
    }

    /**
     * Create an instance from  a file.
     *
     * @param configFile
     * @return
     */
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

    /**
     * Save to the default config file.
     */
    public void toFile() {
        toFile(new File(CONFIGFILE));
    }

    public void toFile(String file) {
        toFile(new File(file));
    }

    /**
     * Save the fields.
     *
     * @param file The file that needs to be used to write to.
     */
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