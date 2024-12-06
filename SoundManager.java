import javax.sound.sampled.*;
import java.io.File;

/**
 * Connor O'Neil 	- connoroneil
 * Eli Jordan		- ejordan3
 * Derek Hoshaw 	- dthoshaw
 * AJ Becerra		- ajbecerra
 */
public class SoundManager {
    private Clip backgroundClip;

    /**
     * Plays background music from inputted file
     * 
     * @param	- filePath - the background music file you want played
     * @pre		- filePath must be .wav file
     * @post	- file from filePath will be playing in background 
     */
    public void playBackgroundMusic(String filePath) {
        try {
            File file = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundClip.start();
        } catch (Exception e) {
            System.err.println("Error playing background music: " + e.getMessage());
        }
    }

    /**
     * Stops background music
     * 
     * @post	- background music will no longer be playing 
     */
    public void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
        }
    }

    /**
     * Plays sound effect from inputted file
     * 
     * @param	- soundFile - the sound effect file you want played
     * @pre		- soundFile must be .wav file
     * @post	- file from soundFile will be played once
     */
    public void playSoundEffect(String soundFile) {
        try {
            File file = new File(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }
}
