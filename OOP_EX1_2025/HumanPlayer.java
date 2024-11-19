/**
 * This class represents a human player in the game.
 * Inherits from the abstract Player class and overrides specific behaviors.
 */
public class HumanPlayer extends Player {

    /**
     * Constructor a HumanPlayer
     * @param isPlayerOne Indicates whether this player is Player 1 (true) or Player 2 (false).
     */
    public HumanPlayer(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    /**
     * Checks if the player is human.
     * @return true, indicating this player is a human.
     */
    @Override
    boolean isHuman() {
        return true;
    }
}
