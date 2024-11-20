
import java.util.List;
import java.util.Random;

/**
 * This class represents an AI player that selects moves randomly.
 * This player chooses a random valid position and a random disc type.
 */
public class RandomAI extends AIPlayer {

    /**
     * Constructor for RandomAI player.
     * @param isPlayerOne Indicates whether this player is Player 1 (true) or Player 2 (false).
     */
    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    /**
     * Generates a random move from the list of valid moves.
     * The move selected random position and disc type.
     * (If the random disc is special and this kind of special disc finished, it takes a simple disc )
     * @param gameStatus The current state of the game, including valid moves and board state.
     * @return A randomly generated Move object.
     */
    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        // Retrieve the list of valid moves.
        List<Position> validMoves = gameStatus.ValidMoves();

        // Select a random position from the valid moves.
        Random randomMove = new Random();
        int randomIndex = randomMove.nextInt(validMoves.size());
        Position position = validMoves.get(randomIndex);

        // Randomly select a disc type based on availability.
        randomIndex = randomMove.nextInt(3);  // Random value between 0 and 2.
        Disc disc;
        if (randomIndex == 0 && number_of_bombs > 0) {
            disc = new BombDisc(this);
        } else if (randomIndex == 1 && number_of_unflippedable > 0) {
            disc = new UnflippableDisc(this);
        } else {
            disc = new SimpleDisc(this);
        }

        // Return the move consisting of the chosen position and disc.
        return new Move(position, disc);
    }
}
