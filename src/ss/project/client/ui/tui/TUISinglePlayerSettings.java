package ss.project.client.ui.tui;

import ss.project.client.Config;
import ss.project.client.Controller;
import ss.project.client.HumanPlayer;
import ss.project.shared.computerplayer.ComputerPlayer;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 16.01.17.
 */
public class TUISinglePlayerSettings implements TUIPanel {
    private Progress progress = Progress.ENTERX;

    private int x, y, z;
    private int winlength;

    private String[] playerTypes;
    private List<Player> players = new ArrayList<>();

    @Override
    public void printScreen() {
        System.out.println(ASCIIArt.getHeadline("Single player", 120));

        System.out.println(ASCIIArt.getChoiceText(progress.getQuestion(), 120));

        if (progress.equals(Progress.ADDPLAYER)) {
            String currentPlayerText = "Current players: ";
            for (int i = 0; i < players.size(); i++) {
                currentPlayerText += players.get(i).getName();
                if (i < players.size() - 1) {
                    currentPlayerText += ",";
                }
            }
            System.out.println(ASCIIArt.getChoiceText(currentPlayerText, 120));

            int i = 0;
            playerTypes = new String[Config.getInstance().PlayerTypes.size()];
            for (String key : Config.getInstance().PlayerTypes.keySet()) {
                System.out.println(ASCIIArt.getChoiceItem(i, key, 120));
                playerTypes[i] = key;
                i++;
            }
        }
    }

    @Override
    public void handleInput(String input) {
        switch (progress) {
            case ENTERX: {
                try {
                    x = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Type a number!");
                    return;
                }
                progress = Progress.ENTERY;
                break;
            }
            case ENTERY: {
                try {
                    y = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Type a number!");
                    return;
                }
                progress = Progress.ENTERZ;
                break;
            }
            case ENTERZ: {
                try {
                    z = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Type a number!");
                    return;
                }
                progress = Progress.ADDPLAYER;
                break;
            }
            case ADDPLAYER: {
                if (input.equalsIgnoreCase("done")) {
                    if (players.size() < 2) {
                        System.out.println("You need at least 2 players!");
                        return;
                    }
                    progress = Progress.SETWINLENGTH;
                } else {
                    String[] parts = input.split(" ");
                    if (parts.length > 1) {
                        //NAME COMPUTERTYPE SMARTNESS
                        try {
                            int playerType = Integer.parseInt(parts[1]);
                            Player player = (Player) Config.getInstance().PlayerTypes.get(playerTypes[playerType]).newInstance();
                            player.setName(parts[0]);
                            player.setId(players.size());
                            if (player instanceof ComputerPlayer && parts.length > 2) {
                                int smartness = Integer.parseInt(parts[2]);
                                ((ComputerPlayer) player).setSmartness(smartness);
                            }
                            players.add(player);
                        } catch (NumberFormatException e) {
                            System.out.println("First type the name, followed by a space, followed by a number representing the playertype and optionally the smartness.");
                            return;
                        } catch (IllegalAccessException | InstantiationException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //NAME
                        Player player = new HumanPlayer(parts[0]);
                        player.setId(players.size());
                        players.add(player);
                    }
                }
                break;
            }
            case SETWINLENGTH: {
                try {
                    winlength = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Type a number!");
                    return;
                }
                //We're done in this screen, start the game.
                Engine engine = new Engine(new Vector3(x, y, z), winlength, players.toArray(new Player[players.size()]));
                Controller.getController().setEngine(engine);
                Controller.getController().switchTo(Controller.Panel.GAME);
                Controller.getController().startGame();

                progress = Progress.ENTERX;

                break;
            }
        }
    }

    enum Progress {
        ENTERX("What's the X size of the world?"),
        ENTERY("What's the Y size of the world?"),
        ENTERZ("What's the Z size of the world?"),
        ADDPLAYER("Add players. Type 'done' if ready with adding. <NAME> <NUMBER> <SMARTNESS>"),
        SETWINLENGTH("What's the required length to win?");

        private String question;

        Progress(String question) {
            this.question = question;
        }

        private String getQuestion() {
            return question;
        }
    }
}
