import java.util.ArrayList;
import java.util.List;

public class Move {
    private final Position _position;
    private final Disc _disc;


    public Move(Position position, Disc disc){
        this._position=position;
        this._disc=disc;
    }

    public Disc get_disc() {
        return _disc;
    }

    public Position get_position() {
        return _position;
    }

    public Position position() {
        return _position;
    }

    public Disc disc() {
        return  _disc;
    }

}
