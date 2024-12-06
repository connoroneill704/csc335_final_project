import javax.swing.*;
import java.awt.*;
/**
 * Displays Tile Label for GUI
 * 
 * Connor O'Neil 	- connoroneil
 * Eli Jordan		- ejordan3
 * Derek Hoshaw 	- dthoshaw
 * AJ Becerra		- ajbecerra
 */
public class TileLabel extends JLabel {
    private int value;
    private Font font;

    /**
     * Constructs a Tile Label to display on each tile object
     * 
     * @param	- font - the Font type you want to use for the Tile Label
     * @param 	- value - the Value int that you want to display 
     * @param	- icon - the ImageIcon for the Tile Label
     * @post	- the fields will be set to the inputted values and the location will be set to centered
     */
    public TileLabel(ImageIcon icon, int value, Font font) {
        super(icon);
        this.value = value;
        this.font = font;
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
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
        repaint();
    }
    
    /**
     * Setter for the icon field
     * 
     * @param	- icon - the ImageIcon for the back of the tile
     * @post	- the icon field will be set to the inputted icon
     */
    public void setTileIcon(ImageIcon icon) {
        setIcon(icon);
    }

    /**
     * Setter for the font field
     * 
     * @param	- font - the Font type you want to use for the Tile Label
     * @pre		- font must be usable Font type
     * @post	- the font field will be set to the inputted font
     */
    public void setFont(Font font) {
        this.font = font;
        repaint();
    }
    
    /**
     * Setter for location of the tile label
     * 
     * @param	- x - the x coordinate for the tile label location
     * @param	- y - the y coordinate for the tile label location
     * @pre		- 0 < x < size of tile
     * @pre		- 0 < y < size of tile
     * @post	- the tile label will be set to the (x,y) coordinate position
     */
    public void setPosition(int x, int y) {
        setLocation(x, y);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (value > 0) {
            Graphics2D g2d = (Graphics2D) g.create();
            // Set rendering hints for smooth text
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            // Set font and color
            g2d.setFont(font);
            g2d.setColor(Color.BLACK); // Choose a color that contrasts with your image
            // Get font metrics for positioning
            FontMetrics fm = g2d.getFontMetrics();
            String text = String.valueOf(value);
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
            // Calculate position for bottom right corner
            int x = getWidth() - textWidth - 5; // 5 pixels from the right edge
            int y = getHeight() - fm.getDescent() - 5; // 5 pixels from the bottom edge
            // Draw the text
            g2d.drawString(text, x, y);
            g2d.dispose();
        }
    }
}
