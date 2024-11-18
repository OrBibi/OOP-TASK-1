import java.util.List;
import java.util.Random;

public class GreedyAI extends AIPlayer{
    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        Position bestMove = new Position(7,7);
        int maxFlips = 0;
        for (int j=7; j>=0; j--){
            for (int i=7; i>=0; i--){
                Position temp = new Position(i,j);
                int count = gameStatus.countFlips(temp);
                if (count>maxFlips){
                    maxFlips=count;
                    bestMove.setRow(i);
                    bestMove.setColumn(j);
                }
            }
        }
        Disc disc = new SimpleDisc(this);
        Move move = new Move(bestMove,disc);
        return move;
    }
}
