package ui.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

/**
 *
 * @author PIX
 */
public class ThemeValues {

    private static ThemeValues instance;
    private final int screenWidth;
    private final int screenHeight;

    private ThemeValues() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.screenHeight = screenSize.height;
        this.screenWidth = screenSize.width;
    }
    
    public static ThemeValues getInstance() {
        if (instance == null) {
            instance = new ThemeValues();
        }
        return instance;
    }

    public Dimension getScreenSize() {
        return new Dimension(screenWidth, screenHeight);
    }

    public Font getHeaderFont(int size) {
        return new Font("Impact", Font.PLAIN, size);
    }
    
    public Color getBackgroundColor(){
        return Color.BLUE.darker().darker().darker();
    }
}
