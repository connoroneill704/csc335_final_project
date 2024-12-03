import java.util.Properties;
import java.io.*;

public class Settings {
    private Properties properties;
    private final String settingsFile = "settings.properties";

    public Settings() {
        properties = new Properties();
        loadSettings();
    }

    private void loadSettings() {
        try (InputStream input = new FileInputStream(settingsFile)) {
            properties.load(input);
        } catch (IOException e) {
            // Default settings if file not found
            properties.setProperty("volume", "0.5");
            properties.setProperty("theme", "Default");
            // Add other default settings
        }
    }

    public void saveSettings() {
        try (OutputStream output = new FileOutputStream(settingsFile)) {
            properties.store(output, null);
        } catch (IOException e) {
            System.err.println("Error saving settings: " + e.getMessage());
        }
    }

    // Getter and setter methods
    public double getVolume() {
        return Double.parseDouble(properties.getProperty("volume"));
    }

    public void setVolume(double volume) {
        properties.setProperty("volume", String.valueOf(volume));
    }

    public String getTheme() {
        return properties.getProperty("theme");
    }

    public void setTheme(String theme) {
        properties.setProperty("theme", theme);
    }

    // Methods for key bindings and other settings...
}
