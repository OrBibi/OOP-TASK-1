import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class GreedyAI extends AIPlayer{
    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    public Move makeMove(PlayableLogic gameStatus) {
        // List of all the position
        List<Position> allPositions = new ArrayList<>();
        for (int j = gameStatus.getBoardSize() - 1; j >= 0; j--) {
            for (int i = gameStatus.getBoardSize() - 1; i >= 0; i--) {
                allPositions.add(new Position(i, j));
            }
        }
        Comparator<Position> flipComparator = new Comparator<Position>() {
            @Override
            public int compare(Position p1, Position p2) {
                int flips1 = gameStatus.countFlips(p1);
                int flips2 = gameStatus.countFlips(p2);

                // First comparing by flips:
                if (flips1 != flips2) {
                    return Integer.compare(flips2, flips1);
                } else {
                    // If the flips are equal: comparing by column
                    if (p1.col() != p2.col()) {
                        return Integer.compare(p2.col(), p1.col());
                    } else {
                        // If the flips and column are equal: comparing by row
                        return Integer.compare(p2.row(), p1.row());
                    }
                }
            }
        };
        // The sort of the position by the comparator, and taking the first
        allPositions.sort(flipComparator);
        Position bestMove = allPositions.getFirst();
        Disc disc = new SimpleDisc(this);
        Move move = new Move(bestMove, disc);
        return move;
    }
}
