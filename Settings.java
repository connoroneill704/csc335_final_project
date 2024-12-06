import java.util.Properties;
import java.io.*;

/**
 * Settings for the 2048 game
 * 
 * Connor O'Neil 	- connoroneil
 * Eli Jordan		- ejordan3
 * Derek Hoshaw 	- dthoshaw
 * AJ Becerra		- ajbecerra
 */
public class Settings {
    private Properties properties;
    private final String settingsFile = "settings.properties";

    /**
     * Constructs a Settings unit for the 2048 game
     * 
     * @post 	- properties field is set to Properties object
     */
    public Settings() {
        properties = new Properties();
        loadSettings();
    }

    /**
     * Loads settings
     * 
     * @post	- loads settings from settings.properties settingsFile
     * @post	- if file not found, set to default settings
     */
    private void loadSettings() {
        try (InputStream input = new FileInputStream(settingsFile)) {
            properties.load(input);
        } catch (IOException e) {
            // Default settings if file not found
            properties.setProperty("volume", "0.5");
            properties.setProperty("theme", "Default");
        }
    }

    /**
     * Saves settings
     * 
     * @post	- loads settings from settings.properties settingsFile
     * @post	- if file not found, show error
     */
    public void saveSettings() {
        try (OutputStream output = new FileOutputStream(settingsFile)) {
            properties.store(output, null);
        } catch (IOException e) {
            System.err.println("Error saving settings: " + e.getMessage());
        }
    }

    /**
     * Getter for the volume field
     * 
     * @return	- the volume settings of the game
     */
    public double getVolume() {
        return Double.parseDouble(properties.getProperty("volume"));
    }

    /**
     * Setter for the volume field
     * 
     * @param	- volume - the volume you want to set the volume to
     * @post	- the volume will be set to the inputted volume
     */
    public void setVolume(double volume) {
        properties.setProperty("volume", String.valueOf(volume));
    }

    /**
     * Getter for the theme field
     * 
     * @return	- the theme settings of the game
     */
    public String getTheme() {
        return properties.getProperty("theme");
    }

    /**
     * Setter for the theme field
     * 
     * @param	- theme - the theme you want to set the volume to
     * @post	- the theme will be set to the inputted t
     */
    public void setTheme(String theme) {
        properties.setProperty("theme", theme);
    }
}
