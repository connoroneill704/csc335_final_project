/**
 * Names: Connor O'Neill, Eli Jordan, Derek Hoshaw, AJ Becerra
 * Net Ids: connoroneill ejordan3 dthoshaw AJBECERRA
 */

/**
 * Represents a cell in the Minesweeper grid.
 */
public class Cell {
    private boolean isMine; // True if the cell contains a mine
    private boolean isRevealed; // True if the cell has been revealed
    private boolean isFlagged; // True if the cell is flagged
    private int adjacentMines; // Number of adjacent mines

    /**
     * Constructs a new Cell object with default values.
     * - The cell is not a mine by default.
     * - The cell is not revealed or flagged.
     * - The number of adjacent mines is initialized to 0.
     */
    public Cell() {
        this.isMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.adjacentMines = 0;
    }

    /**
     * Checks if the cell contains a mine.
     *
     * @return true if the cell is a mine, false otherwise
     */
    public boolean isMine() {
        return isMine;
    }

    /**
     * Sets whether the cell contains a mine.
     *
     * @param mine true if the cell should contain a mine, false otherwise
     */
    public void setMine(boolean mine) {
        isMine = mine;
    }

    /**
     * Checks if the cell has been revealed.
     *
     * @return true if the cell is revealed, false otherwise
     */
    public boolean isRevealed() {
        return isRevealed;
    }

    /**
     * Marks the cell as revealed.
     */
    public void reveal() {
        isRevealed = true;
    }

    /**
     * Checks if the cell is flagged.
     *
     * @return true if the cell is flagged, false otherwise
     */
    public boolean isFlagged() {
        return isFlagged;
    }

    /**
     * Toggles the flagged status of the cell.
     * - If the cell is flagged, it becomes unflagged.
     * - If the cell is unflagged, it becomes flagged.
     */
    public void toggleFlag() {
        isFlagged = !isFlagged;
    }

    /**
     * Retrieves the number of mines adjacent to the cell.
     *
     * @return the number of adjacent mines
     */
    public int getAdjacentMines() {
        return adjacentMines;
    }

    /**
     * Sets the number of mines adjacent to the cell.
     *
     * @param adjacentMines the number of adjacent mines
     */
    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }
}
