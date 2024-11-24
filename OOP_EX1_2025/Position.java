/**
 * This class represents a position on a board with row and column values.
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
    public int row() {
        return _row;
    }

    /**
     * Get the column value
     * @return the column value.
     */
    public int col() {
        return _column;
    }

    /**
     *Sets the row value.
     * @param row the new row value.
     */
    public void setRow(int row) {
        this._row = row;
    }

    /**
     * Sets the row value.
     * @param column the new row value.
     */
    public void setColumn(int column) {
        this._column = column;
    }


}
