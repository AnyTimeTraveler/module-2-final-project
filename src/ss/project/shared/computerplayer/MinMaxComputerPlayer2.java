package ss.project.shared.computerplayer;

/**
 * Created by fw on 16/01/2017.
 */
public class MinMaxComputerPlayer2 extends MinMaxComputerPlayer {

    /**
     * create a computer player with the specified AI.
     */
    public MinMaxComputerPlayer2(int depth) {
        super(depth);
    }

    public MinMaxComputerPlayer2(String name, int depth) {
        super(name, depth);
    }

    public MinMaxComputerPlayer2() {
        super();
    }

    @Override
    protected int getHeuristicValue(int depth) {
        if (depth == getDepth()) {
            System.out.println(depth);
            return Integer.MAX_VALUE;
        }
        return (int) Math.pow(10, depth);
    }
}
