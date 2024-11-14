import java.util.ArrayList;
import java.util.List;

public class Move {
    private final Disc[][] _board;
    private final Position _position;
    private final Disc _disc;


    public Move(Position position, Disc disc, Disc[][] board){
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
        if ((_position.getRow()<0||_position.getRow()>= _board.length)||(_position.getColumn()<0||_position.getColumn()>= this._board[0].length)) return countFlips;
        if (_board[_position.getRow()][_position.getColumn()] != null) return countFlips;
        Disc[] neighbors = this.Neighbors(_position);
        for (int i = 0; i < 8; i++){
            if(neighbors[i]!= null&&neighbors[i].getOwner()!=_disc.getOwner()){
                Disc tempdisc = neighbors[i];
                Position tempPosition = this.getNeighborPosition(_position,i);
                List<Disc> tempCount = new ArrayList<>();
                while (tempdisc!=null&&tempdisc.getOwner()!= currentPlayer && this.isInside(tempPosition)){
                    if (!tempdisc.getType().equals("⭕")&&!tempCount.contains(tempdisc)&&!countFlips.contains(tempdisc))tempCount.add(tempdisc);
                    if (tempdisc.getType().equals("💣")){
                        List<Position> Bombs = new ArrayList<>();
                        Bombs.add(tempPosition);
                        while (!Bombs.isEmpty()){
                            Disc[] bombAdd = this.Neighbors(Bombs.getFirst());
                            for (int x = 0; x < 8; x++){
                                if(bombAdd[x]!=null&&bombAdd[x].getOwner()!=currentPlayer&&!tempCount.contains(bombAdd[x])&&!countFlips.contains(bombAdd[x])){
                                    if(!bombAdd[x].getType().equals("⭕")) tempCount.add(bombAdd[x]);
                                    if (bombAdd[x].getType().equals("💣")){
                                        Bombs.add(this.getNeighborPosition(Bombs.getFirst(),x));
                                    }
                                }
                            }
                            Bombs.removeFirst();
                        }
                    }
                    tempPosition = this.getNeighborPosition(tempPosition,i);
                    if (this.isInside(tempPosition)){
                        tempdisc = _board[tempPosition.getRow()][tempPosition.getColumn()];
                    }
                }
                if(tempdisc!=null && tempdisc .getOwner()==currentPlayer){
                    countFlips.addAll(tempCount);
                }
            }
        }
        return countFlips;
    }
    public boolean isInside(Position p) {
        return (p.getRow() >= 0 && p.getColumn() >= 0 && p.getRow() < this._board.length && p.getColumn() < this._board[0].length);
    }
    public Disc NeighborUp(Position p){
        Position up = new Position(p.getRow(), p.getColumn()+1);
        if (this.isInside(up)){
            return this.getDisc(up);
        }
        else return null;
    }
    public Disc NeighborDown(Position p){
        Position down = new Position(p.getRow(), p.getColumn()-1);
        if (this.isInside(down)){
            return this.getDisc(down);
        }
        else return null;
    }
    public Disc NeighborRight(Position p){
        Position right = new Position(p.getRow()+1, p.getColumn());
        if (this.isInside(right)){
            return this.getDisc(right);
        }
        else return null;
    }
    public Disc NeighborLeft(Position p){
        Position left = new Position(p.getRow()-1, p.getColumn());
        if (this.isInside(left)){
            return this.getDisc(left);
        }
        else return null;
    }
    public Disc NeighborUpRight(Position p){
        Position upright = new Position(p.getRow()+1, p.getColumn()+1);
        if (this.isInside(upright)){
            return this.getDisc(upright);
        }
        else return null;
    }
    public Disc NeighborUpLeft(Position p){
        Position upleft = new Position(p.getRow()-1, p.getColumn()+1);
        if (this.isInside(upleft)){
            return this.getDisc(upleft);
        }
        else return null;
    }
    public Disc NeighborDownRight(Position p){
        Position downright = new Position(p.getRow()+1, p.getColumn()-1);
        if (this.isInside(downright)){
            return this.getDisc(downright);
        }
        else return null;
    }
    public Disc NeighborDownLeft(Position p){
        Position downleft = new Position(p.getRow()-1, p.getColumn()-1);
        if (this.isInside(downleft)){
            return this.getDisc(downleft);
        }
        else return null;
    }
    public Disc[] Neighbors(Position p){
        Disc[] neighbors = new Disc[8];
        neighbors[0]=this.NeighborUpLeft(p);
        neighbors[1]=this.NeighborUp(p);
        neighbors[2]=this.NeighborUpRight(p);
        neighbors[3]=this.NeighborRight(p);
        neighbors[4]=this.NeighborDownRight(p);
        neighbors[5]=this.NeighborDown(p);
        neighbors[6]=this.NeighborDownLeft(p);
        neighbors[7]=this.NeighborLeft(p);
        return neighbors;
    }
    public Position getNeighborPosition(Position p, int neighbor){
        if(neighbor==0) {
            return new Position(p.getRow() - 1, p.getColumn() + 1);
        }
        if(neighbor==1) {
            return new Position(p.getRow() , p.getColumn() + 1);
        }
        if(neighbor==2) {
            return new Position(p.getRow() + 1, p.getColumn() + 1);
        }
        if(neighbor==3) {
            return new Position(p.getRow() + 1, p.getColumn());
        }
        if(neighbor==4) {
            return new Position(p.getRow() + 1, p.getColumn() - 1);
        }
        if(neighbor==5) {
            return new Position(p.getRow() , p.getColumn() - 1);
        }
        if(neighbor==6) {
            return new Position(p.getRow() - 1, p.getColumn() - 1);
        }
        if(neighbor==7) {
            return new Position(p.getRow() - 1, p.getColumn() );
        }
        return null;
    }
    public Disc getDisc(int row, int column) {
        return _board[row][column];
    }

    public Disc getDisc(Position p) {
        return this.getDisc(p.getRow(), p.getColumn());
    }


}
