import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class represents a Greedy AI Player that choose the move that will flip the most discs
 */

public class GreedyAI extends AIPlayer{
    /**
     * Constructor for GreedyAI player.
     * @param isPlayerOne Indicates whether this player is Player 1 (true) or Player 2 (false).
     */
    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    /**
     * The method takes all the position and compares them by the flips they will make,
     * it takes the position that flips the most discs.
     * if there are 2 or more positions with the same flips: it takes the most right column.
     * if still there 2 or more positions with the same flips and the same column:
     * it takes the most down row.
     * @param gameStatus
     * @return The greedy move
     */

    public Move makeMove(PlayableLogic gameStatus) {
        // List of all the position
        List<Position> allPositions = new ArrayList<>();
        for (int j = gameStatus.getBoardSize() - 1; j >= 0; j--) {
            for (int i = gameStatus.getBoardSize() - 1; i >= 0; i--) {
                allPositions.add(new Position(i, j));
            }
        }
        Comparator<Position> flipComparator = (p1, p2) -> {
            // First comparing by flips:
            int flips1 = gameStatus.countFlips(p1);
            int flips2 = gameStatus.countFlips(p2);
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
        };
        // The sort of the position by the comparator, and taking the first
        allPositions.sort(flipComparator);
        Position bestMove = allPositions.getFirst();
        Disc disc = new SimpleDisc(this);
        return new Move(bestMove, disc);
    }
}
