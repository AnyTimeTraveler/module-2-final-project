package ss.project.server;


/**
 * Created by simon on 01.12.16.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class Config {

    // Configfile name
    private static final String CONFIGFILE = "server-config.json";
    private static Config instance;

    //Variables
    public String Host;
    public int Port;

    private Config() {
        Host = "127.0.0.1";
        Port = 1234;
    }

    public static Config getInstance() {
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
            instance.toFile(CONFIGFILE);
        }
    }

    private static void load() {
        load(new File(CONFIGFILE));
    }

    private static Config fromDefaults() {
        Config config = new Config();
        return config;
    }

    private static Config fromFile(File configFile) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));
            return gson.fromJson(reader, Config.class);
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