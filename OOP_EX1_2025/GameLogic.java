import java.util.*;
import java.util.function.Predicate;

/**
 * This class implements PlayableLogic interface, this class mange the game, here the game status changes
 */
public class GameLogic implements PlayableLogic{
    private final int BOARD_SIZE = 8;
    private Disc[][] _board = new Disc[BOARD_SIZE][BOARD_SIZE];
    private final Stack<Disc[][]> _allMoves = new Stack<>();
    private Player _player1, _player2;
    private int _player1discs, _player2discs;

    /**
     * here we locate the disc on the board and change the game status:
     * we crate a deep copy of the board
     * First we check if the move(p and the disc) will make flips by "ListWillFlips" function.
     * then if the list is not empty it check if the player still have this type of disc.
     * naw we know that the move is legal, and we will return true in the end of the function
     * first we update the number of discs of the players and print were the player placed the disc
     * then we update the number of the spacial discs
     * then we flip the discs that need to be flipped
     * then we print all the flips
     * then we add the deep copy of the board (of the board before the changes) to the _allMoves stack
     * this will change the turn.
     * we return true
     * @param p The position for locating a new disc on the board.
     * @param disc ()
     * @return true if the move is legal, else it returns false
     */

    @Override
    public boolean locate_disc(Position p, Disc disc) {
        Player currentPlayer = get_currentPlayer();
        Disc[][] copy = this.getBoardCopy(_board);
        Move move = new Move(p,disc);
        List<Disc> willFlip = listWillFlips(move,_board);
        //if the number of disc that will flip is 0 or if the player don't have this kind of disc return false
        if(willFlip.isEmpty())return false;
        if(disc.getType().equals("⭕")&&currentPlayer.getNumber_of_unflippedable()==0) return false;
        if(disc.getType().equals("💣")&&currentPlayer.getNumber_of_bombs()==0) return false;
        //update the number of discs of the players and print were the player placed the disc
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

        //locate the disc
        _board[p.row()][p.col()]=disc;
        //update the number of the spacial discs
        if(disc.getType().equals("⭕")){
            disc.getOwner().reduce_unflippedable();
        }
        if(disc.getType().equals("💣")){
            disc.getOwner().reduce_bomb();
        }
        //flip the discs that need to be flipped
        while (!willFlip.isEmpty()){
            willFlip.getFirst().setOwner(currentPlayer);
            willFlip.removeFirst();
        }
        //print all the flips
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
        //Add the deep copy of the board (of the board before the changes)
        _allMoves.add(copy);
        System.out.println(" ");
        return true;
    }

    /**
     * @param position The position for which to retrieve the disc.
     * @return disc in the position
     */
    @Override
    public Disc getDiscAtPosition(Position position) {
        return _board[position.row()][position.col()];
    }

    /**
     * @return the size of the board
     */
    @Override
    public int getBoardSize() {
        return _board.length;
    }

    /**
     * we check every position on the board,
     * if the current player can locate there disc we will add the position to the list
     * @return list of Position of the legal moves
     */
    @Override
    public List<Position> ValidMoves() {
        List<Position> validMoves = new ArrayList<>();
        Player currentPlayer = get_currentPlayer();
        Disc disc = new SimpleDisc(currentPlayer);
        //check every position on the board
        for (int i = 0; i < this.getBoardSize(); i++){
            for (int j = 0; j < this.getBoardSize(); j++){
                Position temp = new Position(i,j);
                //if the current player can locate there disc, add the position to the list
                boolean validMove = CheckLocateDisc(temp,disc);
                if (validMove){
                    validMoves.add(temp);
                }
            }
        }
        return validMoves;
    }

    /**
     * @param p
     * @return the number of disc that will flip if the current player will place there a disc
     */
    @Override
    public int countFlips(Position p) {
        Player currentPlayer = get_currentPlayer();
        Disc disc = new SimpleDisc(currentPlayer);
        Move move = new Move(p, disc);
        //list of all the disc that will flip if the current player will make that move
        List<Disc> willFlip = listWillFlips(move,_board);
        return willFlip.size();
    }

    /**
     * @return the first player _player1
     */
    @Override
    public Player getFirstPlayer() {
        return _player1;
    }

    /**
     * @return the second player _player2
     */
    @Override
    public Player getSecondPlayer() {
        return _player2;
    }

    /**
     * set yhe players
     * @param player1 The first player.
     * @param player2 The second player.
     */
    @Override
    public void setPlayers(Player player1, Player player2) {
        _player1 = player1;
        _player2 = player2;
    }
    /**
     * check if is the first player turn
     */
    @Override
    public boolean isFirstPlayerTurn() {
        return (_allMoves.size()%2==0);
    }

    /**
     * check if the game is finish, if he did finish it will make the needed changes
     * @return boolean
     */
    @Override
    public boolean isGameFinished() {
        //If there are valid moves the game is not finish
        if (!this.ValidMoves().isEmpty()){
            return false;
        }
        //There are 0 valid moves, so we add win to the winner and print it
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

    /**
     * reset the game
     */
    @Override
    public void reset() {
        //Build a board of the start
        Disc[][] start = new Disc[BOARD_SIZE][BOARD_SIZE];
        Disc disc33 = new SimpleDisc(_player1);
        Disc disc44 = new SimpleDisc(_player1);
        Disc disc34 = new SimpleDisc(_player2);
        Disc disc43 = new SimpleDisc(_player2);
        start[(BOARD_SIZE /2)-1][(BOARD_SIZE /2)-1] = disc33;
        start[(BOARD_SIZE /2)][(BOARD_SIZE /2)] = disc44;
        start[(BOARD_SIZE /2)][(BOARD_SIZE /2)-1] = disc43;
        start[(BOARD_SIZE /2)-1][(BOARD_SIZE /2)] = disc34;
        //Reset the number of the discs of the players and reset the last moves stack
        _player1discs=2;
        _player2discs=2;
        _player1.reset_bombs_and_unflippedable();
        _player2.reset_bombs_and_unflippedable();
        _allMoves.clear();
        //We change the board to the start board that we build
        _board=start;
    }

    /**
     * Undo the last move
     */
    @Override
    public void undoLastMove() {
        //If there is an AI player it is illegal to undo the last move
        if (_player1.isHuman() && _player2.isHuman()) {
            //If there is no last move to undo don't enter!
            if (!_allMoves.isEmpty()) {
                Player current = get_currentPlayer();
                List<Position> flipedDiscs = new ArrayList<>();
                Position lastDisc = new Position(0, 0);
                //We find the disc that added and the discs that changes owners in the last move
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
                //Print what we need to print and update the numbers of the players discs
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
                //Change the board
                _board = _allMoves.pop();
            }
            //If there is no last move to undo i print that:
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
        Disc[][] ans = new Disc[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(board[i][j]!=null) {
                    if(board[i][j].getType().equals("⬤")){
                        ans[i][j] = new SimpleDisc(board[i][j].getOwner());
                    }
                    if(board[i][j].getType().equals("⭕")){
                        ans[i][j] = new UnflippableDisc(board[i][j].getOwner());
                    }
                    if(board[i][j].getType().equals("💣")){
                        ans[i][j] = new BombDisc(board[i][j].getOwner());
                    }
                }
                else{
                    ans[i][j]=null;
                }
            }
        }
        return ans;
    }
    /**
     * This function makes a deep copy of the board
     * @param position and disc
     * @return deep copy of the board
     */

    public boolean CheckLocateDisc(Position position, Disc disc){
        Disc[][] board = this.getBoardCopy(_board);
        Move move = new Move(position,disc);
        List<Disc> willFlip = listWillFlips(move, board);
        //If pot that disc in that position will flip 1 or more discs return true
        return !willFlip.isEmpty();
    }


    /**
     * This function return list of discs that will flip
     * @param move and board
     * @return list of discs
     */
    public List<Disc> listWillFlips(Move move, Disc[][] board){
        //Build list of disc that will flip after the move
        List<Disc> countFlips = new ArrayList<>();
        //If the move is outside the board or on unavailable position return the list empty
        if ((!this.isInside(move.position()))||(board[move.position().row()][move.position().col()] != null)) return countFlips;


        //Find the currentPlayer
        Player currentPlayer = move.disc().getOwner();
        //Get array of neighbors disks of the move position
        Disc[] neighbors = this.Neighbors(move.position());

        //Loop that run over the neighbors and check for flips
        for (int i = 0; i < 8; i++){
            //Check if the current neighbor is disc of another player
            if(neighbors[i]!= null && neighbors[i].getOwner()!=currentPlayer){
                //We found neighbor disc that belong to another player,
                //so we make a temp disc and position of that neighbor + we made a temp list of discs
                Disc tempdisc = neighbors[i];
                Position tempPosition = this.getNeighborPosition(move.position(),i);
                List<Disc> tempCount = new ArrayList<>();
                //We check the discs in the current neighbor direction all the way until we get to:
                //Null disc(empty position/outside the board) / disc of current player
                while (tempdisc!=null && tempdisc.getOwner()!= currentPlayer){
                    //If the temp disc is not unFlippableDisc and not in the lists: we add it to the temp list of discs
                    if (!tempdisc.getType().equals("⭕")&&!tempCount.contains(tempdisc))
                        tempCount.add(tempdisc);
                    //If the temp disc is bombDisc we add the relevant neighbors including the chain reaction:
                    if (tempdisc.getType().equals("💣")){
                        List<Position> Bombs = new ArrayList<>();
                        Bombs.add(tempPosition);
                        while (!Bombs.isEmpty()){
                            Disc[] bombNeighbors = this.Neighbors(Bombs.getFirst());
                            for (int x = 0; x < 8; x++){
                                    if(bombNeighbors[x]!=null&&bombNeighbors[x].getOwner()!=currentPlayer&&!tempCount.contains(bombNeighbors[x])){
                                        if(!bombNeighbors[x].getType().equals("⭕")) tempCount.add(bombNeighbors[x]);
                                        if (bombNeighbors[x].getType().equals("💣")){
                                            Bombs.add(this.getNeighborPosition(Bombs.getFirst(),x));
                                        }
                                    }
                            }
                            Bombs.removeFirst();
                        }
                    }
                    //Here we make the move in the direction of current neighbor
                    tempPosition = this.getNeighborPosition(tempPosition,i);
                    if (this.isInside(tempPosition)){
                        tempdisc = board[tempPosition.row()][tempPosition.col()];
                    }
                    else{
                        tempdisc=null;
                    }
                }
                //If we arrive to disc that belong to the currentPlayer we add the temp list to the countFlips list
                if(tempdisc!=null && !tempCount.isEmpty()){
                    countFlips.addAll(tempCount);
                }
            }
        }
        //Protection from wrong double income
        Set<Disc> uniqueDiscs = new HashSet<>(countFlips);
        List<Disc> countFlipsWithoutDuplicates = new ArrayList<>(uniqueDiscs);
        return countFlipsWithoutDuplicates;
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
     * This function gets position and number of neighbor (0-7) and return the wanted neighbor
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


    /**
     * @param row int
     * @param column int
     * @return the disc in that row column position
     */
    public Disc getDisc(int row, int column) {
        return _board[row][column];
    }

    /**
     * @param p position
     * @return the disc in that position
     */
    public Disc getDisc(Position p) {
        return this.getDisc(p.row(), p.col());
    }



}
