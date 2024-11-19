/**
 * This class represents a position on a grid with row and column values.
 */

public class Position {
    private int _column, _row;

    /**
     * Constructor for Position.
     * @param row the row index.
     * @param column the column index.
     */
    public Position(int row, int column){
        this._row = row;
        this._column = column;
    }

    /**
     * Gets the row value
     * @return the row value.
     */
    public int getRow() {
        return _row;
    }

    /**
     *Sets the row value.
     * @param row the new row value.
     */
    public void setRow(int row) {
        this._row = row;
    }

    /**
     * Get the column value
     * @return the column value.
     */
    public int getColumn() {
        return _column;
    }

    /**
     * Sets the row value.
     * @param column the new row value.
     */
    public void setColumn(int column) {
        this._column = column;
    }



    /**
     * ///////////////////////////////////////////
     * for the GUI:
     * @return the row index.
    ////////////////////////////////////////////
     */
    public int row() {
        return _row;
    }

    /**
     * ///////////////////////////////////////////
     * for the GUI:
     * @return the column index.
    ////////////////////////////////////////////
     */
    public int col() {
        return _column;
    }

}
