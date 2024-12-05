import javax.swing.*;
import java.awt.*;

public class TileLabel extends JLabel {
    private int value;
    private Font font;

    public TileLabel(ImageIcon icon, int value, Font font) {
        super(icon);
        this.value = value;
        this.font = font;
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    public void setValue(int value) {
        this.value = value;
        repaint();
    }

    public void setTileIcon(ImageIcon icon) {
        setIcon(icon);
    }

    public void setFont(Font font) {
        this.font = font;
        repaint();
    }
    
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
