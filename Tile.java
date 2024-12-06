/**
 * Connor O'Neil 	- connoroneil
 * Eli Jordan		- ejordan3
 * Derek Hoshaw 	- dthoshaw
 * AJ Becerra		- ajbecerra
 */
public class Tile {
    private int value;
    private int row;
    private int col;
    private boolean merged;

    /**
     * Constructs a Tile to pass information to board2048.java about each tile
     * 
     * @param	- row - the current row of the tile
     * @param 	- value - the Value int that you want to display 
     * @param	- col - the current column of the tile
     * @post	- the fields will be filled with the inputted information and the merged field will be false
     */
    public Tile(int value, int row, int col) {
        this.value = value;
        this.row = row;
        this.col = col;
        this.merged = false;
    }

    /**
     * Gets the int value of the tile object
     * 
     * @return	- the value of the current tile object
     */
    public int getValue() {
    	return value;
    }
    
    /**
     * Setter for the value field
     * 
     * @param	- value - the value of the current tile (2, 4, 8, 16, ...)
     * @pre		- value must be playable int (2, 4, 8, 16, ...)
     * @post	- the value field will be set to the inputted value
     */
    public void setValue(int value) { 
    	this.value = value;
    }

    /**
     * Gets the row value of the tile object
     * 
     * @return	- the row of the current tile object
     */
    public int getRow() { 
    	return row;
    }
    
    /**
     * Setter for the row field
     * 
     * @param	- row - the row value for the current tile object
     * @pre		- row must be in range of playable rows
     * @post	- the row field will be set to the inputted row
     */
    public void setRow(int row) { 
    	this.row = row;
    }

    /**
     * Gets the col value of the tile object
     * 
     * @return	- the col of the current tile object
     */
    public int getCol() { 
    	return col;
    }
    
    /**
     * Setter for the col field
     * 
     * @param	- col - the column value for the current tile object
     * @pre		- col must be in range of playable columns
     * @post	- the col field will be set to the inputted row
     */
    public void setCol(int col) { 
    	this.col = col;
    }

    /**
     * Gets the merged status of the tile object
     * 
     * @return	- the merged field of the current tile object
     */
    public boolean isMerged() {
    	return merged;
    }
    
    /**
     * Setter for the merged field
     * 
     * @param	- merged - if the tile has been merged or not
     * @post	- the merged field will be set to the inutted boolean value
     */
    public void setMerged(boolean merged) { 
    	this.merged = merged;
    }

    /**
     * Resets the merged status of the tile
     * 
     * @post	- the merged field will be set to false
     */
    public void reset() {
        merged = false;
    }
}
