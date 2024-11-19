import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * OrBibi1998
 */
public class GameLogic implements PlayableLogic{
    private final int _BOARDSIZE = 8;
    private Disc[][] _board = new Disc[_BOARDSIZE][_BOARDSIZE];
    private final Stack<Disc[][]> _allMoves = new Stack<>();
    private Player _player1, _player2;
    private int _player1discs, _player2discs;

    @Override
    public boolean locate_disc(Position p, Disc disc) {
        Disc[][] copy = this.getBoardCopy(_board);
        Move move = new Move(p,disc);
        List<Disc> willFlip = CountFlips(move,_board);
        if(willFlip.isEmpty())return false;
        Player currentPlayer = get_currentPlayer();
        int flips = willFlip.size();
        if(currentPlayer==_player1){
            _player1discs=_player1discs+flips+1;
            _player2discs=_player2discs-flips;
            System.out.println("Player 1 placed a "+disc.getType()+" in ("+p.getRow()+","+p.getColumn()+")");

        }
        else{
            _player2discs=_player2discs+flips+1;
            _player1discs=_player1discs-flips;
            System.out.println("Player 2 placed a "+disc.getType()+" in ("+p.getRow()+","+p.getColumn()+")");

        }
        _board[p.getRow()][p.getColumn()]=disc;
        while (!willFlip.isEmpty()){
            willFlip.getFirst().setOwner(currentPlayer);
            willFlip.removeFirst();
        }
        if(currentPlayer==_player1) {
            for (int i = 0; i < _board.length; i++) {
                for (int j = 0; j < _board.length; j++) {
                    if (copy[i][j] != null && copy[i][j].getOwner() != _board[i][j].getOwner()) {
                        System.out.println("Player 1 flipped " + copy[i][j].getType() + " in (" + i + "," + j + ")");

                    }
                }
            }
        }
        if(currentPlayer==_player2) {
            for (int i = 0; i < _board.length; i++) {
                for (int j = 0; j < _board.length; j++) {
                    if (copy[i][j] != null && copy[i][j].getOwner() != _board[i][j].getOwner()) {
                        System.out.println("Player 2 flipped " + copy[i][j].getType() + " in (" + i + "," + j + ")");

                    }
                }
            }
        }
        _allMoves.add(copy);
        System.out.println(" ");
        return true;
    }

    public Disc[][] getBoardCopy(Disc[][] board){
        Disc[][] ans = new Disc[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Disc temp = board[i][j];
                if(temp!=null) {
                    if(temp.getType().equals("⬤")){
                        ans[i][j] = new SimpleDisc(temp.getOwner());
                    }
                    else if(temp.getType().equals("⭕")){
                        ans[i][j] = new UnflippableDisc(temp.getOwner());
                    }
                    else{
                        ans[i][j] = new BombDisc(temp.getOwner());
                    }
                }
            }
        }
        return ans;
    }

    public boolean CheckLocateDisc(Position p, Disc disc){
        Disc[][] board = this.getBoardCopy(_board);
        Move move = new Move(p,disc);
        List<Disc> willFlip = CountFlips(move, board);
        return !willFlip.isEmpty();
    }
    @Override
    public Disc getDiscAtPosition(Position position) {
        return _board[position.getRow()][position.getColumn()];
    }

    @Override
    public int getBoardSize() {
        return _board.length;
    }

    @Override
    public List<Position> ValidMoves() {
        List<Position> validMoves = new ArrayList<>();
        Player currentPlayer = get_currentPlayer();
        Disc disc = new SimpleDisc(currentPlayer);
        for (int i = 0; i < this.getBoardSize(); i++){
            for (int j = 0; j < this.getBoardSize(); j++){
                Position temp = new Position(i,j);
                boolean validMove = CheckLocateDisc(temp,disc);
                if (validMove){
                    validMoves.add(temp);
                }
            }
        }
        return validMoves;
    }

    @Override
    public int countFlips(Position p) {
        Player currentPlayer = get_currentPlayer();
        Disc disc = new SimpleDisc(currentPlayer);
        Disc[][] board = this.getBoardCopy(_board);
        Move move = new Move(p, disc);
        List<Disc> willFlip = CountFlips(move,board);
        return willFlip.size();
    }

    @Override
    public Player getFirstPlayer() {
        return _player1;
    }

    @Override
    public Player getSecondPlayer() {
        return _player2;
    }

    @Override
    public void setPlayers(Player player1, Player player2) {
        _player1 = player1;
        _player2 = player2;
    }

    @Override
    public boolean isFirstPlayerTurn() {
        return (_allMoves.size()%2==0);
    }
    public Player get_currentPlayer(){
        Player currentPlayer;
        if(isFirstPlayerTurn()){
            currentPlayer=_player1;
        }
        else{
            currentPlayer=_player2;
        }
        return currentPlayer;
    }

    @Override
    public boolean isGameFinished() {
        if (!this.ValidMoves().isEmpty()){
            return false;
        }
        else{
            if(_player1discs > _player2discs){
                _player1.addWin();
                System.out.println("Player 1 wins with " +_player1discs + "! Player 2 had " +_player2discs + " discs.");
                System.out.println("  ");
            }
            if(_player1discs < _player2discs){
                _player2.addWin();
                System.out.println("Player 2 wins with " +_player2discs + "! Player 1 had " +_player1discs + " discs.");
                System.out.println(" ");
            }
            if(_player1discs == _player2discs){
                System.out.println("Its a draw with " +_player2discs + " discs for each player:)");
                System.out.println(" ");
            }
            return true;
        }
    }

    @Override
    public void reset() {
        Disc[][] start = new Disc[_BOARDSIZE][_BOARDSIZE];
        Disc disc33 = new SimpleDisc(_player1);
        Disc disc44 = new SimpleDisc(_player1);
        Disc disc34 = new SimpleDisc(_player2);
        Disc disc43 = new SimpleDisc(_player2);
        start[(_BOARDSIZE/2)-1][(_BOARDSIZE/2)-1] = disc33;
        start[(_BOARDSIZE/2)][(_BOARDSIZE/2)] = disc44;
        start[(_BOARDSIZE/2)][(_BOARDSIZE/2)-1] = disc43;
        start[(_BOARDSIZE/2)-1][(_BOARDSIZE/2)] = disc34;
        _player1discs=2;
        _player2discs=2;
        _allMoves.clear();
        _board=start;
    }

    @Override
    public void undoLastMove() {
        if (_player1.isHuman() && _player2.isHuman()) {
            if (!_allMoves.isEmpty()) {
                Player curent = get_currentPlayer();
                List<Position> flipedDiscs = new ArrayList<>();
                Position lastDisc = new Position(0, 0);
                for (int i = 0; i < _board.length; i++) {
                    for (int j = 0; j < _board.length; j++) {
                        if (_board[i][j] != null) {
                            if (_allMoves.peek()[i][j] == null) {
                                lastDisc.setRow(i);
                                lastDisc.setColumn(j);
                            } else {
                                if (_allMoves.peek()[i][j].getOwner() != _board[i][j].getOwner()) {
                                    Position temp = new Position(i, j);
                                    flipedDiscs.add(temp);
                                }
                            }
                        }
                    }
                }
                if (curent==_player1){
                    _player1discs=_player1discs+flipedDiscs.size();
                    _player2discs=_player2discs-(flipedDiscs.size()+1);
                }
                else{
                    _player2discs=_player2discs+flipedDiscs.size();
                    _player1discs=_player1discs-(flipedDiscs.size()+1);
                }
                System.out.println("Undoing last move:");
                System.out.println("\tUndo: removing " + _board[lastDisc.getRow()][lastDisc.getColumn()].getType() + " from (" + lastDisc.getRow() + "," + lastDisc.getColumn() + ")");
                while (!flipedDiscs.isEmpty()) {
                    System.out.println("\tUndo: flipping back " + _board[flipedDiscs.getFirst().getRow()][flipedDiscs.getFirst().getColumn()].getType() + " in (" + flipedDiscs.getFirst().getRow() + "," + flipedDiscs.getFirst().getColumn() + ")");
                    flipedDiscs.removeFirst();
                }
                _board = _allMoves.pop();
            }
            else{
            System.out.println("\tNo previous move available to undo.");
            }
            System.out.println(" ");
        }
    }
    public List<Disc> CountFlips(Move move, Disc[][] board){
        List<Disc> countFlips = new ArrayList<>();
        Player currentPlayer = move.get_disc().getOwner();
        if ((move.get_position().getRow()<0||move.get_position().getRow()>= board.length)||(move.get_position().getColumn()<0||move.get_position().getColumn()>= board[0].length)) return countFlips;
        if (board[move.get_position().getRow()][move.get_position().getColumn()] != null) return countFlips;
        Disc[] neighbors = this.Neighbors(move.get_position());
        for (int i = 0; i < 8; i++){
            if(neighbors[i]!= null&&neighbors[i].getOwner()!=move.get_disc().getOwner()){
                Disc tempdisc = neighbors[i];
                Position tempPosition = this.getNeighborPosition(move.get_position(),i);
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
                        tempdisc = board[tempPosition.getRow()][tempPosition.getColumn()];
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
