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
     * Configfile name.
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
    public String windowTitle;
    /**
     * A map that contains all possible playerTypes the player can use.
     */
    public HashMap<String, Class<? extends Player>> playerTypes;
    /**
     * If true, the frame will be shown in full screen.
     * If false in windowed mode.
     */
    @Expose
    public boolean fullscreen;
    /**
     * A list of servers that are added and need to be shown in the serverbrowser.
     */
    @Expose
    public List<Connection> knownServers;
    /**
     * The playername used in multiplayer.
     */
    @Expose
    public String playerName;
    /**
     * The max amount of players this client can handle.
     */
    public int maxPlayers;
    /**
     * If true this client can handle rooms.
     */
    public boolean roomSupport;
    /**
     * The maximum X size of the world this client can handle.
     */
    public int maxDimensionX;
    /**
     * The maximum Y size of the world this client can handle.
     */
    public int maxDimensionY;
    /**
     * The maximum Z size of the world this client can handle.
     */
    public int maxDimensionZ;
    /**
     * The maximum length that is needed to win this client can handle.
     */
    public int maxWinLength;
    /**
     * If true this client can send and recieve chat messages.
     */
    public boolean chatSupport;
    /**
     * If true the client wants to be notified if the server get changes in the rooms.
     * If false the client needs to ask for an update by itself.
     */
    public boolean autoRefresh;
    /**
     * The max amount of chat message shown on the UI.
     */
    public int maxChatMessages;
    /**
     * The playertype the user wants to use in multiplayer.
     *
     * @see ClientConfig#playerTypes
     */
    public String playerType;
    /**
     * The smartness of the playerType if it's an AI.
     */
    public int playerSmartness;
    /**
     * If true a hint is shown after X seconds when playing as a human.
     * If false no hint is shown at all.
     */
    public boolean showHint;

    /**
     * Set all default values of the fields.
     * If a value is saved of a field that value will be loaded.
     */
    private ClientConfig() {
        windowTitle = "Connect Four 3D";
        fullscreen = false;
        playerTypes = new HashMap<>();
        playerTypes.put("Human", HumanPlayer.class);
        playerTypes.put("Linear AI", LinearComputerPlayer.class);
        playerTypes.put("Minmax AI", MinMaxComputerPlayer.class);
        playerTypes.put("Minmax AI 2", MinMaxComputerPlayer2.class);
        playerTypes.put("Minmax alpha beta", MinMaxAlphaBetaComputerPlayer.class);
        playerTypes.put("Random AI", RandomComputerPlayer.class);
        knownServers = new ArrayList<>();
        knownServers.add(new Connection("Localhost", "127.0.0.1", 1234));
        playerName = "Simon";
        maxPlayers = Integer.MAX_VALUE;
        roomSupport = true;
        maxDimensionX = Integer.MAX_VALUE;
        maxDimensionY = Integer.MAX_VALUE;
        maxDimensionZ = Integer.MAX_VALUE;
        maxWinLength = Integer.MAX_VALUE;
        chatSupport = true;
        autoRefresh = true;
        maxPlayers = 50;
        maxChatMessages = 50;
        playerType = "Human";
        playerSmartness = 6;
        showHint = true;
    }

    /**
     * A singleton reference to the Config.
     * If no instance is present, a new one will be created.
     */
    public synchronized static ClientConfig getInstance() {
        if (instance == null) {
            load();
        }
        return instance;
    }

    /**
     * Load the fields from a file. If no file is found default values are used.
     */
    public static void load() {
        instance = fromFile(new File(CONFIGFILE));
        // no config file found
        if (instance == null) {
            instance = fromDefaults();
            instance.toFile();
        }
    }

    /**
     * Create an instance with default values.
     */
    private static ClientConfig fromDefaults() {
        return new ClientConfig();
    }

    /**
     * Create an instance from  a file.
     */
    private static ClientConfig fromFile(File configFile) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            InputStreamReader isr = new InputStreamReader(new FileInputStream(configFile));
            BufferedReader reader = new BufferedReader(isr);
            return gson.fromJson(reader, ClientConfig.class);
        } catch (FileNotFoundException e) {
            System.out.println("ClientConfig file not found!");
            return null;
        }
    }

    /**
     * Save the fields.
     */
    public void toFile() {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
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