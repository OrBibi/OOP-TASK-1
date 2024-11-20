import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 * This
 */
public class GameLogic implements PlayableLogic{
    private final int BOARD_SIZE = 8;
    private Disc[][] _board = new Disc[BOARD_SIZE][BOARD_SIZE];
    private final Stack<Disc[][]> _allMoves = new Stack<>();
    private Player _player1, _player2;
    private int _player1discs, _player2discs;

    @Override
    public boolean locate_disc(Position p, Disc disc) {
        Player currentPlayer = get_currentPlayer();
        Disc[][] copy = this.getBoardCopy(_board);
        Move move = new Move(p,disc);
        List<Disc> willFlip = listWillFlips(move,_board);
        if(willFlip.isEmpty())return false;
        if(disc.getType().equals("⭕")&&currentPlayer.getNumber_of_unflippedable()==0) return false;
        if(disc.getType().equals("💣")&&currentPlayer.getNumber_of_bombs()==0) return false;
        int flips = willFlip.size();
        if(currentPlayer==_player1){
            _player1discs=_player1discs+flips+1;
            _player2discs=_player2discs-flips;
            System.out.println("Player 1 placed a "+disc.getType()+" in ("+p.row()+","+p.col()+")");

        }
        else{
            _player2discs=_player2discs+flips+1;
            _player1discs=_player1discs-flips;
            System.out.println("Player 2 placed a "+disc.getType()+" in ("+p.row()+","+p.col()+")");

        }
        _board[p.row()][p.col()]=disc;
        if(disc.getType().equals("⭕")){
            disc.getOwner().reduce_unflippedable();
        }
        if(disc.getType().equals("💣")){
            disc.getOwner().reduce_bomb();
        }
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
    @Override
    public Disc getDiscAtPosition(Position position) {
        return _board[position.row()][position.col()];
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
        List<Disc> willFlip = listWillFlips(move,board);
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
        Disc[][] start = new Disc[BOARD_SIZE][BOARD_SIZE];
        Disc disc33 = new SimpleDisc(_player1);
        Disc disc44 = new SimpleDisc(_player1);
        Disc disc34 = new SimpleDisc(_player2);
        Disc disc43 = new SimpleDisc(_player2);
        start[(BOARD_SIZE /2)-1][(BOARD_SIZE /2)-1] = disc33;
        start[(BOARD_SIZE /2)][(BOARD_SIZE /2)] = disc44;
        start[(BOARD_SIZE /2)][(BOARD_SIZE /2)-1] = disc43;
        start[(BOARD_SIZE /2)-1][(BOARD_SIZE /2)] = disc34;
        _player1discs=2;
        _player2discs=2;
        _player1.reset_bombs_and_unflippedable();
        _player2.reset_bombs_and_unflippedable();
        _allMoves.clear();
        _board=start;
    }

    @Override
    public void undoLastMove() {
        if (_player1.isHuman() && _player2.isHuman()) {
            if (!_allMoves.isEmpty()) {
                Player current = get_currentPlayer();
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
                if (current==_player1){
                    _player1discs=_player1discs+flipedDiscs.size();
                    _player2discs=_player2discs-(flipedDiscs.size()+1);
                    if(_board[lastDisc.row()][lastDisc.col()].getType().equals("⭕"))_player2.add_unflippedable();
                    if(_board[lastDisc.row()][lastDisc.col()].getType().equals("💣"))_player2.add_bomb();
                }
                else{
                    _player2discs=_player2discs+flipedDiscs.size();
                    _player1discs=_player1discs-(flipedDiscs.size()+1);
                    if(_board[lastDisc.row()][lastDisc.col()].getType().equals("⭕"))_player1.add_unflippedable();
                    if(_board[lastDisc.row()][lastDisc.col()].getType().equals("💣"))_player1.add_bomb();
                }
                System.out.println("Undoing last move:");
                System.out.println("\tUndo: removing " + _board[lastDisc.row()][lastDisc.col()].getType() + " from (" + lastDisc.row() + "," + lastDisc.col() + ")");
                while (!flipedDiscs.isEmpty()) {
                    System.out.println("\tUndo: flipping back " + _board[flipedDiscs.getFirst().row()][flipedDiscs.getFirst().col()].getType() + " in (" + flipedDiscs.getFirst().row() + "," + flipedDiscs.getFirst().col() + ")");
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


///////////////////////////////additional methods////////////////////////////////////////

    /**
     *The function check who is the current player, 1 or 2
     * @return player 1 or 2
     */
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

    /**
     * This function makes a deep copy of the board
     * @param board of discs
     * @return deep copy of the board
     */
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
        List<Disc> willFlip = listWillFlips(move, board);
        return !willFlip.isEmpty();
    }



    public List<Disc> listWillFlips(Move move, Disc[][] board){
        //build list of disc that will flip after the move
        List<Disc> countFlips = new ArrayList<>();
        //if the move is outside the board or on unavailable position return the list empty
        if ((!this.isInside(move.position()))||(board[move.position().row()][move.position().col()] != null)) return countFlips;


        //find the currentPlayer
        Player currentPlayer = move.disc().getOwner();
        //get array of neighbors disks
        Disc[] neighbors = this.Neighbors(move.position());

        //loop that run over the neighbors and check for flips
        for (int i = 0; i < 8; i++){
            //check if the current neighbor is disc of another player
            if(neighbors[i]!= null && neighbors[i].getOwner()!=move.disc().getOwner()){
                //we found neighbor disc that belong to another player,
                //so we make a temp disc and position of that neighbor + we made a temp list of discs
                Disc tempdisc = neighbors[i];
                Position tempPosition = this.getNeighborPosition(move.position(),i);
                List<Disc> tempCount = new ArrayList<>();
                //we check the discs in the current neighbor direction all the way until we get to:
                //null disc(empty position/outside the board) / disc of current player
                while (tempdisc!=null && tempdisc.getOwner()!= currentPlayer){
                    //if the temp disc is not unFlippableDisc and not in the lists: we add it to the temp list of discs
                    if (!tempdisc.getType().equals("⭕")&&!tempCount.contains(tempdisc) && !countFlips.contains(tempdisc))
                        tempCount.add(tempdisc);
                    //if the temp disc is bombDisc we add the relevant neighbors including the chain reaction:
                    if (tempdisc.getType().equals("💣")){
                        List<Position> Bombs = new ArrayList<>();
                        Bombs.add(tempPosition);
                        while (!Bombs.isEmpty()){
                            Disc[] bombAdd = this.Neighbors(Bombs.getFirst());
                            for (int x = 0; x < 8; x++){
                                if(bombAdd[x]!=null&&bombAdd[x].getOwner()!=currentPlayer&&
                                        !tempCount.contains(bombAdd[x])&&!countFlips.contains(bombAdd[x])){
                                    if(!bombAdd[x].getType().equals("⭕")) tempCount.add(bombAdd[x]);
                                    if (bombAdd[x].getType().equals("💣")){
                                        Bombs.add(this.getNeighborPosition(Bombs.getFirst(),x));
                                    }
                                }
                            }
                            Bombs.removeFirst();
                        }
                    }
                    //here we make the move in the direction of current neighbor
                    tempPosition = this.getNeighborPosition(tempPosition,i);
                    if (this.isInside(tempPosition)){
                        tempdisc = board[tempPosition.row()][tempPosition.col()];
                    }
                    else{
                        tempdisc=null;
                    }
                }
                //if we arrive to disc that belong to the currentPlayer we add the temp list to the countFlips list
                if(tempdisc!=null && this.isInside(tempPosition) && tempdisc.getOwner()==currentPlayer){
                    countFlips.addAll(tempCount);
                }
            }
        }
        countFlips.removeIf(Objects::isNull);
        return countFlips;
    }
    /**
     * This function checks if a position is inside the board
     * @param p Position
     * @return true if inside, false if not
     */
    public boolean isInside(Position p) {
        return (p.row() >= 0 && p.col() >= 0 &&
                p.row() < this._board.length && p.col() < this._board[0].length);
    }

    /**
     * This function creates an array of neighbors of a disc position.
     * @param p Position
     * @return array of 8 neighbors.
     */
    public Disc[] Neighbors(Position p) {
        Disc[] neighbors = new Disc[8];

        // Directions: {row value, column value}
        int[][] directions = {
                {-1, -1}, // UpLeft
                {-1,  0}, // Up
                {-1,  1}, // UpRight
                { 0,  1}, // Right
                { 1, 1}, // DownRight
                { 1, 0}, // Down
                { 1, -1}, // DownLeft
                {0, -1}  // Left
        };
        for (int i = 0; i < directions.length; i++) {
            Position neighborPos = new Position(p.row() + directions[i][0]  ,
                                               p.col() + directions[i][1]);
            if (this.isInside(neighborPos)) {
                neighbors[i] = this.getDisc(neighborPos);
            } else {
                neighbors[i] = null;
            }
        }

        return neighbors;
    }

    /**
     * This function gets position andnumber of neighbor (0-7)
     * @param p position
     * @param neighbor- number of neighbor
     * @return the position of the neighbor
     */
    public Position getNeighborPosition(Position p, int neighbor) {

        // Directions: {row value, column value}
        int[][] directions = {
                {-1, -1}, // UpLeft
                {-1,  0}, // Up
                {-1,  1}, // UpRight
                { 0,  1}, // Right
                { 1, 1}, // DownRight
                { 1, 0}, // Down
                { 1, -1}, // DownLeft
                {0,  -1}  // Left
        };
        int newRow = p.row() + directions[neighbor][0];
        int newColumn = p.col() + directions[neighbor][1];
        return new Position(newRow, newColumn);
    }



    public Disc getDisc(int row, int column) {
        return _board[row][column];
    }

    public Disc getDisc(Position p) {
        return this.getDisc(p.row(), p.col());
    }



}
