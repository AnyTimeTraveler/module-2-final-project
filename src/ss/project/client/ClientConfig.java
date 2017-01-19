package ss.project.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ss.project.shared.computerplayer.LinearComputerPlayer;
import ss.project.shared.computerplayer.MinMaxComputerPlayer;
import ss.project.shared.computerplayer.RandomComputerPlayer;

import java.io.*;
import java.util.HashMap;

/**
 * Created by simon on 01.12.16.
 */

public class ClientConfig {

    // Configfile name
    private static final String CONFIGFILE = "client-config.json";
    private static ClientConfig instance;

    //Variables
    public String WindowTitle;
    public HashMap<String, Class> playerTypes;

    private ClientConfig() {
        WindowTitle = "Connect Four 3D";
        playerTypes = new HashMap<>();
        playerTypes.put("Human", HumanPlayer.class);
        playerTypes.put("Linear AI", LinearComputerPlayer.class);
        playerTypes.put("Minmax AI", MinMaxComputerPlayer.class);
        playerTypes.put("Random AI", RandomComputerPlayer.class);
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