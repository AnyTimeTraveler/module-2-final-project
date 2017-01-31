package ss.project.client.ui.gui;

import ss.project.client.Controller;
import ss.project.shared.model.LeaderboardEntry;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by fw on 26/01/2017.
 */
public class PNLLeaderboard extends GUIPanel implements Observer {
    private JPanel leaderBoardList;
    private GridBagConstraints gridBagConstraints;

    public PNLLeaderboard(Controller controller) {
        super();

        this.setLayout(new BorderLayout());
        this.add(GUIUtils.createLabel("Leaderboard", GUIUtils.LabelType.TITLE), BorderLayout.NORTH);

        leaderBoardList = new JPanel();
        leaderBoardList.setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        this.add(leaderBoardList, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> controller.switchTo(Controller.Panel.MULTI_PLAYER_LOBBY));
        this.add(backButton, BorderLayout.SOUTH);
    }

    @Override
    public void onEnter() {
        Controller.getController().addObserver(this);
        Controller.getController().requestLeaderBoard();
        showLeaderBoardEntries(Controller.getController().getLeaderBoard());
    }

    private void showLeaderBoardEntries(java.util.List<LeaderboardEntry> entries) {
        leaderBoardList.removeAll();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        if (entries != null) {
            for (int i = 0; i < entries.size(); i++) {
                gridBagConstraints.gridy = i;
                leaderBoardList.add(new ScoreBoardItem(entries.get(i)), gridBagConstraints);
            }
        }
    }

    @Override
    public void onLeave() {
        Controller.getController().deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Controller) {
            if (arg.equals("UpdateLeaderBoard")) {
                showLeaderBoardEntries(Controller.getController().getLeaderBoard());
            }
        }
    }

    /**
     * An item on the scoreboard.
     */
    class ScoreBoardItem extends JPanel {
        private ScoreBoardItem(LeaderboardEntry leaderboardEntry) {
            JLabel label = new JLabel(leaderboardEntry.getPlayerName() + " wins/losses/draws" + leaderboardEntry.getWins() + "/" + leaderboardEntry.getLosses() + "/" + leaderboardEntry.getDraws());
            this.add(label);
        }
    }
}
