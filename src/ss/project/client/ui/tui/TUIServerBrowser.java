package ss.project.client.ui.tui;

import ss.project.client.Controller;
import ss.project.shared.model.ServerInfo;

import java.util.List;

/**
 * Created by simon on 16.01.17.
 */
public class TUIServerBrowser implements TUIPanel {
    private List<ServerInfo> serverInfos;

    public TUIServerBrowser() {

    }

    @Override
    public void printScreen() {
        System.out.println(ASCIIArt.getHeadline("Servers", 120));
        serverInfos = Controller.getController().pingServers();
        for (int i = 0; i < serverInfos.size(); i++) {
            System.out.println(ASCIIArt.getChoiceItem(i, getServerInfo(serverInfos.get(i)), 120));
        }
        System.out.println("Type 'add <IPADDRESS> <PORT>' to add a server.");
        System.out.println(ASCIIArt.getChoiceItem(serverInfos.size(), "Back", 120));
    }

    private String getServerInfo(ServerInfo serverInfo) {
        StringBuilder result = new StringBuilder();
        ServerInfo.Status status = serverInfo.getStatus();
        if (status.equals(ServerInfo.Status.ONLINE)) {
            result.append(status.toString());
            result.append("  Players: " + serverInfo.getMaxPlayers());
            result.append(" X:" + serverInfo.getMaxDimensionX());
            result.append(" Y:" + serverInfo.getMaxDimensionY());
            result.append(" Z:" + serverInfo.getMaxDimensionZ());
            result.append(" Winlength:" + serverInfo.getMaxWinLength());
            result.append(" Rooms:" + serverInfo.isRoomSupport());
            result.append(" Chat:" + serverInfo.isChatSupport());
        } else {
            result.append(status.toString());
        }
        return result.toString();
    }

    @Override
    public void handleInput(String input) {
        String[] parts = input.split(" ");
        if (parts[0].equalsIgnoreCase("add")) {
            if (parts.length < 3) {
                System.out.println("Type 'add <IPADDRESS> <PORT>' to add a server.");
                return;
            }
            try {
                int port = Integer.parseInt(parts[2]);
                Controller.getController().addServer(parts[1], port);
                return;
            } catch (NumberFormatException e) {
                System.out.println("The port should be a number!");
                return;
            }
        }
        try {
            int number = Integer.parseInt(input);
            if (number < 0 || number > serverInfos.size()) {
                System.out.println("Please type a number in range.");
                return;
            }
            if (number == serverInfos.size()) {
                //Go back.
                Controller.getController().switchTo(Controller.Panel.MAIN_MENU);
                return;
            }
            if (serverInfos.get(number).getStatus().equals(ServerInfo.Status.ONLINE)) {
                Controller.getController().joinServer(serverInfos.get(number));
                System.out.println("Joined server");
            } else {
                System.out.println("This server can not be used!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Please type a number.");
        }
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onLeave() {

    }
}
