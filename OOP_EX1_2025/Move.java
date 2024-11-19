/**
 * This class represents a move in the game.
 * move is located disc in a position on the board.
 */
public class Move {
    private Position _position;
    private Disc _disc;

    /**
     * Constructor for move.
     * @param position The position on the board where the disc will be placed.
     * @param disc The type of the disc
     */
    public Move(Position position, Disc disc) {
        this._position = position;
        this._disc = disc;
    }

    /**
     * Get the disc associated with this move.
     * @return The disc placed on the board.
     */
    public Disc get_disc() {
        return _disc;
    }

    /**
     * Get the position associated with this move.
     * @return The position on the board where the disc is placed.
     */
    public Position get_position() {
        return _position;
    }






    /**
     * ///////////////////////////////////////////
     * for the GUI:
     * Alternative method to retrieve the position.
     * @return The position on the board where the disc is placed.
     * /////////////////////////////////////////////
     */
    public Position position() {
        return _position;
    }

    /**
     * ///////////////////////////////////////////
     * for the GUI:
     * Alternative method to retrieve the disc.
     * @return The disc placed on the board.
     * /////////////////////////////////////////////
     */
    public Disc disc() {
        return _disc;
    }
}
