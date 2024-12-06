import java.io.*;
import java.util.*;

/**
 * Connor O'Neil 	- connoroneil
 * Eli Jordan		- ejordan3
 * Derek Hoshaw 	- dthoshaw
 * AJ Becerra		- ajbecerra
 */
public class HighScoreManager {
    private final String scoreFile = "scores.dat";
    private List<ScoreEntry> highScores;
    private final int maxEntries = 10; // Top 10 scores

    /**
     * Constructs a high score manager
     * 
     * @post	- high scores field will be filled wth the high scores from scores.dat
     */
    public HighScoreManager() {
        highScores = new ArrayList<>();
        loadScores();
    }

    /**
     * add scores from scores.dat into the high scores arraylist
     * 
     * @post	- the high scores field will have the scores from the scoreFile added to its array
     */
    private void loadScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(scoreFile))) {
            highScores = (List<ScoreEntry>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // No existing scores or error reading file
            highScores = new ArrayList<>();
        }
    }

    /**
     * saves the current high scores field into the scoreFile
     * 
     * @post	- the high scores field will be copied to the scoreFile 'scores.dat'
     */
    public void saveScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(scoreFile))) {
            oos.writeObject(highScores);
        } catch (IOException e) {
            System.err.println("Error saving scores: " + e.getMessage());
        }
    }

    /**
     * Adds a new high score to the high scores list
     * 
     * @param	- name - the name of the high scorer
     * @param	- score - the score of the high score to be added
     * @pre		- name & score != NULL
     * @post	- a entry with (name, score) will be added to the high scores list
     */
    public void addScore(String name, int score) {
        highScores.add(new ScoreEntry(name, score));
        Collections.sort(highScores);
        if (highScores.size() > maxEntries) {
            highScores = highScores.subList(0, maxEntries);
        }
        saveScores();
    }

    /**
     * Getter for the high scores list
     * 
     * @return	 - the high scores list
     */
    public List<ScoreEntry> getHighScores() {
        return highScores;
    }

    /**
     * Subclass for Score Entry
     */
    public static class ScoreEntry implements Serializable, Comparable<ScoreEntry> {
        private String name;
        private int score;

        /**
         * Constructs a ScoreEntry object
         * 
         * @param	- name - name of the high scorer
         * @param 	- score - int value of the high score
         * @pre		- name & score != NULL
         * @post	- name and score feilds will be filled with inputted values
         */
        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }

        /**
         * Getter for the name field
         * 
         * @return	 - the name of the entry
         */
        public String getName() { 
        	return name;
        }
        
        /**
         * Getter for the score field
         * 
         * @return	 	- the score of the entry
         */
        public int getScore() { 
        	return score;
        }

        /**
         * Compares values of two scores
         * 
         * @param		- the other score being added
         * @return		- compares this score and other score in descneding order
         */
        @Override
        public int compareTo(ScoreEntry o) {
            return Integer.compare(o.score, this.score); // Descending order
        }
    }
}
