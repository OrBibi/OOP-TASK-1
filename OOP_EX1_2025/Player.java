/**
 * This Abstract class represents a player in the game.
 * It tracks player status, wins, and amount of bombs and unflippable discs.
 */
public abstract class Player {
    protected boolean isPlayerOne; // Indicates whether the player is a defender or attacker
    protected int wins;
    protected static final int initial_number_of_bombs = 3;
    protected static final int initial_number_of_unflippedable = 2;
    protected int number_of_bombs;
    protected int number_of_unflippedable;

    /**
     * Constructor for Player.
     * Initializes player number and bombs and unflippedable discs and wins.
     * @param isPlayerOne indicates whether this player is Player 1 or 2 by true or false.
     */
    public Player(boolean isPlayerOne) {
        this.isPlayerOne = isPlayerOne;
        reset_bombs_and_unflippedable();
        wins = 0;
    }

    /**
     * Determines whether this player is Player 1.
     *
     * @return true if the player is Player 1, false if the player is Player 2 (or any other player).
     */
    public boolean isPlayerOne() {
        return isPlayerOne;
    }

    /**
     * Retrieves the number of wins accumulated by this player over the course of the game.
     *
     * @return The total number of wins achieved by the player.
     */
    public int getWins() {
        return wins;
    }

    /**
     * Increment the win counter by one when the player wins a round or match.
     */
    public void addWin() {
        this.wins++;
    }
    /**
     * Determines whether this player is human.
     *
     * @return true if the player is human.
     */
    abstract boolean isHuman();

    /**
     * Retrieves the current number of bombs the player has.
     *
     * @return The number of bombs available.
     */
    public int getNumber_of_bombs() {
        return number_of_bombs;
    }

    /**
     * Retrieves the current number of unflippable discs the player has.
     *
     * @return The number of unflippable discs available.
     */
    public int getNumber_of_unflippedable() {
        return number_of_unflippedable;
    }

    /**
     * Reduces the number of bombs by one.
     */
    public void reduce_bomb() {
        number_of_bombs--;
    }

    /**
     * Reduces the number of unflippable discs by one.
     */
    public void reduce_unflippedable() {
        number_of_unflippedable--;
    }

    /**
     * Resets the number of bombs and unflippable discs to their initial values.
     */
    public void reset_bombs_and_unflippedable() {
        this.number_of_bombs = initial_number_of_bombs;
        this.number_of_unflippedable = initial_number_of_unflippedable;
    }

    ////////////////////////additional methods by @moshe_ofer permeation///////////////////////

    /**
     * Increases the number of bombs by one.
     */
    public void add_bomb() {
        number_of_bombs++;
    }

    /**
     * Increases the number of unflippable discs by one.
     */
    public void add_unflippedable() {
        number_of_unflippedable++;
    }
}
