/**
 * This class represent Unflappable Disc object
 */
public class UnflippableDisc implements Disc{

    private Player _player;

    public UnflippableDisc(Player player){
        this._player=player;
    }
    @Override
    public Player getOwner() {
        return this._player;
    }

    @Override
    public void setOwner(Player player) {
        this._player=player;
    }

    @Override
    public String getType() {
        return "⭕";
    }

}
