import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdvancedGreedyAI extends AIPlayer {

    public AdvancedGreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    public Move makeMove(PlayableLogic gameStatus) {
        // Choose the disc kind:
        Disc disc;
        if (number_of_unflippedable > 0) {
            disc = new UnflippableDisc(this);
        } else {
            disc = new SimpleDisc(this);
        }
        int discsOnTheBoard = 0;
        // List of all the positions
        List<Position> allPositions = new ArrayList<>();
        for (int j = gameStatus.getBoardSize() - 1; j >= 0; j--) {
            for (int i = gameStatus.getBoardSize() - 1; i >= 0; i--) {
                allPositions.add(new Position(i, j));
                if(gameStatus.getDiscAtPosition(new Position(i,j))!=null){
                    discsOnTheBoard++;
                }
            }
        }

        // If there is available corner choose it
        Position[] corners = new Position[]{
                new Position(0, 0),
                new Position(0, 7),
                new Position(7, 0),
                new Position(7, 7)
        };

        // First check if there's any available corner
        for (Position corner : corners) {
            if (gameStatus.countFlips(corner) > 0) {
                return new Move(corner, disc);  // If there is an available corner, play there
            }
        }

        // Check if a corner belongs to the player and play adjacent positions if valid
        for (Position corner : corners) {
            if (gameStatus.getDiscAtPosition(corner) != null && gameStatus.getDiscAtPosition(corner).getOwner() == this) {
                // We found a corner that belongs to the player, now we select adjacent positions
                Position[] adjacentPositions = getAdjacentPositions(corner);

                for (Position adjacent : adjacentPositions) {
                    if (gameStatus.countFlips(adjacent) > 0) {
                        return new Move(adjacent, disc);  // If the adjacent position is valid, play there
                    }
                }
            }
        }

        // Comparator considering empty corners and avoiding adjacent positions if a corner is empty
        Comparator<Position> flipComparator = (p1, p2) -> {
            int flips1 = gameStatus.countFlips(p1);
            int flips2 = gameStatus.countFlips(p2);

            // Check if there is an empty corner and avoid positions close to empty corners
            int p1Penalty = calculatePenaltyForCornerProximity(p1, gameStatus);
            int p2Penalty = calculatePenaltyForCornerProximity(p2, gameStatus);

            // First compare by flips:
            if (flips1 != flips2) {
                return Integer.compare(flips2, flips1);
            } else {
                // If flips are equal, compare by penalty (avoid corners close to empty corners)
                return Integer.compare(p1Penalty, p2Penalty);
            }
        };
        if(discsOnTheBoard>=11){
            // Sorting all positions based on the comparator and picking the best one
            allPositions.sort(flipComparator);
            Position bestMove = allPositions.getFirst();
            return new Move(bestMove, disc);
        }
        else{
            int x=0;
            int y=0;
            int lowest= 100;
            allPositions.removeIf(position -> gameStatus.countFlips(position) == 0);
            for (Position position : allPositions){
                int temp=gameStatus.countFlips(position);
                if(temp<lowest){
                    x=position.row();
                    y=position.col();
                    lowest=temp;
                }
            }
            return new Move(new Position(x,y), disc);
        }
    }

    // Function to calculate the penalty for positions near empty corners
    private int calculatePenaltyForCornerProximity(Position position, PlayableLogic gameStatus) {
        Position[] corners = new Position[]{
                new Position(0, 0),
                new Position(0, 7),
                new Position(7, 0),
                new Position(7, 7)
        };

        // Initialize the penalty to 0
        int penalty = 0;

        // Iterate over each corner and check if it is empty
        for (Position corner : corners) {
            if (gameStatus.getDiscAtPosition(corner) == null) {
                // If the corner is empty, we add a penalty to positions adjacent to it
                // Check if the position is adjacent to this empty corner
                if (isAdjacentToCorner(position, corner)) {
                    penalty++;
                }
            }
        }
        return penalty;
    }

    // Function to check if a position is adjacent to a given corner
    private boolean isAdjacentToCorner(Position position, Position corner) {
        Position[] adjacentPositions = getAdjacentPositions(corner);
        for (Position adjacent : adjacentPositions) {
            if (position.equals(adjacent)) {
                return true;
            }
        }
        return false;
    }

    // Function to get adjacent positions around a corner
    private Position[] getAdjacentPositions(Position corner) {
        List<Position> adjacentPositions = new ArrayList<>();

        int row = corner.row();
        int col = corner.col();

        // If the corner is (0,0)
        if (row == 0 && col == 0) {
            adjacentPositions.add(new Position(0, 1));  // מימין לפינה
            adjacentPositions.add(new Position(1, 0));  // למטה לפינה
            adjacentPositions.add(new Position(1, 1));  // בזווית לפינה
        }
        // If the corner is (0,7)
        else if (row == 0 && col == 7) {
            adjacentPositions.add(new Position(0, 6));  // משמאל לפינה
            adjacentPositions.add(new Position(1, 7));  // למטה לפינה
            adjacentPositions.add(new Position(1, 6));  // בזווית לפינה
        }
        // If the corner is (7,0)
        else if (row == 7 && col == 0) {
            adjacentPositions.add(new Position(6, 0));  // למעלה לפינה
            adjacentPositions.add(new Position(7, 1));  // מימין לפינה
            adjacentPositions.add(new Position(6, 1));  // בזווית לפינה
        }
        // If the corner is (7,7)
        else if (row == 7 && col == 7) {
            adjacentPositions.add(new Position(7, 6));  // משמאל לפינה
            adjacentPositions.add(new Position(6, 7));  // למעלה לפינה
            adjacentPositions.add(new Position(6, 6));  // בזווית לפינה
        }

        return adjacentPositions.toArray(new Position[0]);
    }
}

