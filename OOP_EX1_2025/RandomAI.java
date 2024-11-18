import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAI extends AIPlayer{
    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        List<Position> validMoves = gameStatus.ValidMoves();
        Random randomMove = new Random();
        int randomIndex = randomMove.nextInt(validMoves.size());
        Position position = validMoves.get(randomIndex);
        randomIndex = randomMove.nextInt(3);
        Disc disc;
        if (randomIndex==0&&number_of_bombs>0){
            disc = new BombDisc(this);
        }
        else if (randomIndex==1&&number_of_unflippedable>0){
            disc = new UnflippableDisc(this);
        }
        else {
            disc = new SimpleDisc(this);
        }
        Move move = new Move(position,disc);
        return move;
    }
}
