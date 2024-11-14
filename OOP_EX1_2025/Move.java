import java.util.ArrayList;
import java.util.List;

public class Move {
    private final Board _board;
    private final Position _position;
    private final Disc _disc;


    public Move(Position position, Disc disc, Board board){
        this._position=position;
        this._disc=disc;
        this._board=board;
    }

    public Position position() {
        return _position;
    }

    public Disc disc() {
        return  _disc;
    }
    public List<Disc> CountFlips(){
        List<Disc> countFlips = new ArrayList<>();
        Player currentPlayer = _disc.getOwner();
        if ((_position.getRow()<0||_position.getRow()>= this._board.boardSize())||(_position.getColumn()<0||_position.getColumn()>= this._board.boardSize())) return countFlips;
        if (this._board.getDisc(_position) != null) return countFlips;
        Disc[] neighbors = this._board.Neighbors(_position);
        for (int i = 0; i < 8; i++){
            if(neighbors[i]!= null&&neighbors[i].getOwner()!=_disc.getOwner()){
                Disc tempdisc = neighbors[i];
                Position tempPosition = this._board.getNeighborPosition(_position,i);
                List<Disc> tempCount = new ArrayList<>();
                while (tempdisc!=null&&tempdisc.getOwner()!= currentPlayer && this._board.isInside(tempPosition)){
                    if (!tempdisc.getType().equals("⭕")&&!tempCount.contains(tempdisc)&&!countFlips.contains(tempdisc))tempCount.add(tempdisc);
                    if (tempdisc.getType().equals("💣")){
                        List<Position> Bombs = new ArrayList<>();
                        Bombs.add(tempPosition);
                        while (!Bombs.isEmpty()){
                            Disc[] bombAdd = this._board.Neighbors(Bombs.getFirst());
                            for (int x = 0; x < 8; x++){
                                if(bombAdd[x]!=null&&bombAdd[x].getOwner()!=currentPlayer&&!tempCount.contains(bombAdd[x])&&!countFlips.contains(bombAdd[x])){
                                    if(!bombAdd[x].getType().equals("⭕")) tempCount.add(bombAdd[x]);
                                    if (bombAdd[x].getType().equals("💣")){
                                        Bombs.add(_board.getNeighborPosition(Bombs.getFirst(),x));
                                    }
                                }
                            }
                            Bombs.removeFirst();
                        }
                    }
                    tempPosition = this._board.getNeighborPosition(tempPosition,i);
                    if (this._board.isInside(tempPosition)){
                        tempdisc = this._board.getDisc(tempPosition);
                    }
                }
                if(tempdisc!=null && tempdisc .getOwner()==currentPlayer){
                    countFlips.addAll(tempCount);
                }
            }
        }
        return countFlips;
    }

}
