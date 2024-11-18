import java.util.ArrayList;
import java.util.List;

public class Move {
    private Position _position;
    private Disc _disc;


    public Move(Position position, Disc disc){
        this._position=position;
        this._disc=disc;
    }

    public void set_disc(Disc _disc) {
        this._disc = _disc;
    }

    public void set_position(Position _position) {
        this._position = _position;
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
