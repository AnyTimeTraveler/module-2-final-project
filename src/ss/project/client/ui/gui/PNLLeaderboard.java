package ss.project.client.ui.gui;

import ss.project.client.Controller;
import ss.project.server.LeaderboardEntry;

import javax.swing.*;
import java.awt.*;

/**
 * Created by fw on 26/01/2017.
 */
public class PNLLeaderboard extends GUIPanel {
    JPanel leaderBoardList;

    public PNLLeaderboard(Controller controller) {
        super();

        this.setLayout(new BorderLayout());
        this.add(GUIUtils.createLabel("Leaderboard", GUIUtils.LabelType.TITLE), BorderLayout.NORTH);

        leaderBoardList = new JPanel();
        leaderBoardList.setLayout(new BoxLayout(leaderBoardList, BoxLayout.Y_AXIS));
        this.add(leaderBoardList, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> controller.switchTo(Controller.Panel.MULTI_PLAYER_LOBBY));
        this.add(backButton, BorderLayout.SOUTH);
    }

    @Override
    public void onEnter() {
        //TODO: get the leaderboard entries from the server.
        LeaderboardEntry[] entries = new LeaderboardEntry[3];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = new LeaderboardEntry("player " + i, i + 1, i - 1, i);
        }
        showLeaderBoardEntries(entries);
    }

    private void showLeaderBoardEntries(LeaderboardEntry[] entries) {
        leaderBoardList.removeAll();
        for (int i = 0; i < entries.length; i++) {
            leaderBoardList.add(new ScoreBoardItem(entries[i]));
        }
    }

    @Override
    public void onLeave() {

    }

    class ScoreBoardItem extends JPanel {
        private ScoreBoardItem(LeaderboardEntry leaderboardEntry) {
            JLabel label = new JLabel(leaderboardEntry.getPlayerName() + " " + leaderboardEntry.getWins() + "/" + leaderboardEntry.getLosses());
            this.add(label);
        }
    }
}
