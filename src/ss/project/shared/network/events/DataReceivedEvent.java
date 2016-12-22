package ss.project.shared.network.events;

import ss.project.shared.network.io.Data;

public class DataReceivedEvent implements Event {

    private Data data;

    public DataReceivedEvent(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    @Override
    public String toString() {
        return "DataReceivedEvent: " + data;
    }
}
