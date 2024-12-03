public class Tile {
    private int value;
    private int row;
    private int col;

    public Tile(int value, int row, int col) {
        this.value = value;
        this.row = row;
        this.col = col;
    }

    // Getter and setter methods
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }

    public int getCol() { return col; }
    public void setCol(int col) { this.col = col; }

    public boolean isMerged() { return merged; }
    public void setMerged(boolean merged) { this.merged = merged; }

    public void reset() {
        merged = false;
    }
}
