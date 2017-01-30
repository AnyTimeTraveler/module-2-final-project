package ss.project.shared;

/**
 * Objects that implement this interface can be send through the network.
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
