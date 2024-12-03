import java.io.*;
import java.util.*;

public class HighScoreManager {
    private final String scoreFile = "scores.dat";
    private List<ScoreEntry> highScores;
    private final int maxEntries = 10; // Top 10 scores

    public HighScoreManager() {
        highScores = new ArrayList<>();
        loadScores();
    }

    private void loadScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(scoreFile))) {
            highScores = (List<ScoreEntry>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // No existing scores or error reading file
            highScores = new ArrayList<>();
        }
    }

    public void saveScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(scoreFile))) {
            oos.writeObject(highScores);
        } catch (IOException e) {
            System.err.println("Error saving scores: " + e.getMessage());
        }
    }

    public void addScore(String name, int score) {
        highScores.add(new ScoreEntry(name, score));
        Collections.sort(highScores);
        if (highScores.size() > maxEntries) {
            highScores = highScores.subList(0, maxEntries);
        }
        saveScores();
    }

    public List<ScoreEntry> getHighScores() {
        return highScores;
    }

    // Inner class for score entries
    public static class ScoreEntry implements Serializable, Comparable<ScoreEntry> {
        private String name;
        private int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() { return name; }
        public int getScore() { return score; }

        @Override
        public int compareTo(ScoreEntry o) {
            return Integer.compare(o.score, this.score); // Descending order
        }
    }
}
