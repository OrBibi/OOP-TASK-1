import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * OrBibi
 */
public class GameLogic implements PlayableLogic{
    private Disc[][] _board = new Disc[8][8];
    private final Stack<Disc[][]> _allMoves = new Stack<>();
    private Player _player1, _player2;
    private int _player1discs, _player2discs;

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
    @Override
    public boolean locate_disc(Position p, Disc disc) {
        Disc[][] copy = this.getBoardCopy(_board);
        Move move = new Move(p,disc,_board);
        List<Disc> willFlip = move.CountFlips();
        if(willFlip.isEmpty())return false;
        Player currentPlayer = get_currentPlayer();
        int flips = willFlip.size();
        if(currentPlayer==_player1){
            _player1discs=_player1discs+flips+1;
            _player2discs=_player2discs-flips;
        }
        else{
            _player2discs=_player2discs+flips+1;
            _player1discs=_player1discs-flips;
        }
        _board[p.getRow()][p.getColumn()]=disc;
        while (!willFlip.isEmpty()){
            willFlip.getFirst().setOwner(currentPlayer);
            willFlip.removeFirst();
        }
        _allMoves.add(copy);
        System.out.println(" ");
        return true;
    }
    public boolean CheckLocateDisc(Position p, Disc disc){
        Disc[][] board = this.getBoardCopy(_board);
        Move move = new Move(p,disc,board);
        List<Disc> willFlip = move.CountFlips();
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
        Move move = new Move(p, disc,board);
        List<Disc> willFlip = move.CountFlips();
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
            }
            if(_player1discs < _player2discs){
                _player2.addWin();
                System.out.println("Player 2 wins with " +_player2discs + "! Player 1 had " +_player1discs + " discs.");
            }
            if(_player1discs == _player2discs){
                System.out.println("Its a draw with " +_player2discs + " discs for each player:)");
            }
            return true;
        }
    }

    @Override
    public void reset() {
        Disc[][] start = new Disc[8][8];
        Disc disc33 = new SimpleDisc(_player1);
        Disc disc44 = new SimpleDisc(_player1);
        Disc disc34 = new SimpleDisc(_player2);
        Disc disc43 = new SimpleDisc(_player2);
        start[3][3] = disc33;
        start[4][4] = disc44;
        start[4][3] = disc43;
        start[3][4] = disc34;
        _player1discs=2;
        _player2discs=2;
        _allMoves.clear();
        _board=start;
    }

    @Override
    public void undoLastMove() {
        if(!_allMoves.isEmpty()){
            _board = _allMoves.pop();
            _player1discs=0;
            _player2discs=0;
            for (int i = 0; i < this.getBoardSize(); i++){
                for (int j = 0; j < this.getBoardSize(); j++){
                    if(_board[i][j]!=null){
                        if(_board[i][j].getOwner()==_player1){
                            _player1discs=_player1discs+1;
                        }
                        else {
                            _player2discs=_player2discs+1;
                        }
                    }
                }
            }
        }
    }
    public Disc[][] get_board() {
        return _board;
    }

}
