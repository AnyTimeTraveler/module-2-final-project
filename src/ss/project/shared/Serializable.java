package ss.project.shared;

/**
 * Created by simon on 28.01.17.
 */
public interface Serializable {
    /**
     * Create a string that represents this object in the protocol.
     *
     * @return A new string containing all data that is needed to recreate this object.
     */
    String serialize();
}
