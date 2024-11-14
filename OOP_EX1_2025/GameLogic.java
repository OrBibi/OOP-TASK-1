import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameLogic implements PlayableLogic{
    private Board _board = new Board(8);
    private final Stack<Board> _allMoves = new Stack<>();
    private Player _player1, _player2;
    @Override
    public boolean locate_disc(Position p, Disc disc) {
        Board board = _board.getBoardCopy();
        Board copy = _board.getBoardCopy();
        Move move = new Move(p,disc,board);
        List<Disc> willFlip = move.CountFlips();
        if(willFlip.isEmpty())return false;
        _board.setDisc(p,disc);
        Player currentPlayer = get_currentPlayer();
        while (!willFlip.isEmpty()){
            willFlip.getFirst().setOwner(currentPlayer);
            willFlip.removeFirst();
        }
        _allMoves.add(copy);
        return true;
    }
    public boolean CheckLocateDisc(Position p, Disc disc){
        Board board = _board.getBoardCopy();
        Move move = new Move(p,disc,board);
        List<Disc> willFlip = move.CountFlips();
        return !willFlip.isEmpty();
    }
    @Override
    public Disc getDiscAtPosition(Position position) {
        _board.getDisc(position);
        return null;
    }

    @Override
    public int getBoardSize() {
        return _board.boardSize();
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
        Board board = _board.getBoardCopy();
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
        return this.ValidMoves().isEmpty();
    }

    @Override
    public void reset() {
        Board start = new Board(8);
        Disc disc33 = new SimpleDisc(_player1);
        Disc disc44 = new SimpleDisc(_player1);
        Disc disc34 = new SimpleDisc(_player2);
        Disc disc43 = new SimpleDisc(_player2);
        start.setDisc(3,3,disc33);
        start.setDisc(4,4,disc44);
        start.setDisc(3,4,disc34);
        start.setDisc(4,3,disc43);
        _allMoves.clear();
        _board=start;
    }

    @Override
    public void undoLastMove() {
        if(!_allMoves.isEmpty()){
            _board = _allMoves.pop();
        }
    }
}
