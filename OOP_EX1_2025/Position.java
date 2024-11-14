public class Position {
    private int _column, _row;
    public Position(int row, int column){
        this._row=row;
        this._column=column;
    }
    public int getRow() {
        return _row;
    }
    public int row(){return _row;}

    public void setRow(int row) {
        this._row = row;
    }

    public int getColumn() {return _column;}
    public int col(){return _column;}

    public void setColumn(int column) {
        this._column = column;
    }
}
