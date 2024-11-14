public class Board {
    private Disc[][] _board;

    public Board(int rowColumn) {
        this._board = new Disc[rowColumn][rowColumn];
    }
    public Board getBoardCopy() {
        Board ans = new Board(_board.length);
        for (int i = 0; i < ans.boardSize(); i++) {
            for (int j = 0; j < ans.boardSize(); j++) {
                ans.setDisc(i,j,_board[i][j]);
            }
        }
        return ans;
    }
    public int boardSize(){
        return _board.length;
    }

    public Disc[][] get_board() {
        return _board;
    }

    public void set_board(Disc[][] board) {
        this._board = board;
    }

    public Disc getDisc(int row, int column) {
        return _board[row][column];
    }

    public Disc getDisc(Position p) {
        return this.getDisc(p.getRow(), p.getColumn());
    }

    public void setDisc(int row, int column, Disc disc) {
        _board[row][column] = disc;
    }

    public void setDisc(Position p, Disc disc) {
        setDisc(p.getRow(), p.getColumn(), disc);
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
}
