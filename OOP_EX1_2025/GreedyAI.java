
/**
 * The class represents an AI player that chooses moves based on a greedy algorithm.
 * It selects the move that flips the maximum number of opponent discs.
 */

public class GreedyAI extends AIPlayer{
    /**
     * Constructor for the GreedyAI.
     * @param isPlayerOne Indicates if the player is Player 1.
     */
    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    /**
        ?????????????????????????????????????????????????????????
     * @param gameStatus The current state of the game.
     * @return The best Move according to the greedy algorithm.
     */
    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        Position bestMove = new Position(gameStatus.getBoardSize()-1,gameStatus.getBoardSize()-1);
        int maxFlips = 0;
        for (int j=gameStatus.getBoardSize()-1; j>=0; j--){
            for (int i=gameStatus.getBoardSize()-1; i>=0; i--){
                Position temp = new Position(i,j);
                int count = gameStatus.countFlips(temp);
                if (count>maxFlips){
                    maxFlips=count;
                    bestMove.setRow(i);
                    bestMove.setColumn(j);
                }
            }
        }
        Disc disc = new SimpleDisc(this);
        Move move = new Move(bestMove,disc);
        return move;
    }
    /////////////////////////////////// compertor option///////////////////////////
//    @Override
//    public Move makeMove(PlayableLogic gameStatus) {
//        List<Position> validMoves = gameStatus.ValidMoves();
//
//        // Sort the valid moves based on the criteria
//        validMoves.sort(Comparator
//                .comparingInt((Position pos) -> gameStatus.countFlips(pos)) // Maximize flips
//                .thenComparingInt(Position::getColumn).reversed() // Rightmost column
//                .thenComparingInt(Position::getRow).reversed()); // Bottom-most row
//
//        // The best move is the last one in the sorted list
//        Position bestMove = validMoves.get(validMoves.size() - 1);
//        Disc disc = new SimpleDisc(this);
//        return new Move(bestMove, disc);
//    }
}
