public class BombDisc implements Disc{

    private Player _player;


    public BombDisc(Player player){
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
        return "💣";
    }

}