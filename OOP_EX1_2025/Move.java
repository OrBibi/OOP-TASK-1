import java.util.ArrayList;
import java.util.List;

public class Move {
    private Position _position;
    private Disc _disc;


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
