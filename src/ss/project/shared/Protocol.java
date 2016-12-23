package ss.project.shared;

/**
 * Created by simon on 23.12.16.
 */
public abstract class Protocol {

    private static Protocol instance;

    public static Protocol get() {
        if (instance == null)
            instance = new Version1();
        return instance;
    }

    public abstract String doTurn();
}

class Version1 extends Protocol {
    @Override
    public String doTurn() {
        return "doturn";
    }
}
