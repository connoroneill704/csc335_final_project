import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class AllTests2048 {
    @Test
    public void testTileClass() {
        Tile tile = new Tile(2, 0, 0);
        assertEquals(2, tile.getValue());
        assertEquals(0, tile.getRow());
        assertEquals(0, tile.getCol());
        assertFalse(tile.isMerged());

        tile.setValue(4);
        assertEquals(4, tile.getValue());

        tile.setRow(1);
        assertEquals(1, tile.getRow());

        tile.setCol(1);
        assertEquals(1, tile.getCol());

        tile.setMerged(true);
        assertTrue(tile.isMerged());

        tile.reset();
        assertFalse(tile.isMerged());
    }
    @Test
    public void testBoard2048Class() {
        board2048 board = new board2048(4);
        Tile[][] tiles = board.getBoard();
        int nonNullTiles = 0;

        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile != null) {
                    nonNullTiles++;
                    assertTrue(tile.getValue() == 2 || tile.getValue() == 4);
                }
            }
        }
        assertEquals(2, nonNullTiles);
        for (int i = 0; i < board.getGridSize(); i++) {
            for (int j = 0; j < board.getGridSize(); j++) {
                tiles[i][j] = null;
            }
        }

        board.addNewTile();
        nonNullTiles = 0;

        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile != null) {
                    nonNullTiles++;
                    assertTrue(tile.getValue() == 2 || tile.getValue() == 4);
                }
            }
        }
        assertEquals(1, nonNullTiles);

        Tile tile = new Tile(8, 1, 1);
        board.setTile(1, 1, tile);
        Tile retrievedTile = board.getTile(1, 1);
        assertEquals(tile, retrievedTile);
        for (Tile[] row : tiles) {
            for (Tile t : row) {
                if (t != null) {
                    t.setMerged(true);
                }
            }
        }

        board.resetMergedFlags();

        for (Tile[] row : tiles) {
            for (Tile t : row) {
                if (t != null) {
                    assertFalse(t.isMerged());
                }
            }
        }

        Tile[][] copiedBoard = board.copyBoard();
        assertNotSame(tiles, copiedBoard);

        for (int i = 0; i < board.getGridSize(); i++) {
            for (int j = 0; j < board.getGridSize(); j++) {
                Tile originalTile = tiles[i][j];
                Tile copiedTile = copiedBoard[i][j];

                if (originalTile == null) {
                    assertNull(copiedTile);
                } else {
                    assertNotSame(originalTile, copiedTile);
                    assertEquals(originalTile.getValue(), copiedTile.getValue());
                    assertEquals(originalTile.getRow(), copiedTile.getRow());
                    assertEquals(originalTile.getCol(), copiedTile.getCol());
                }
            }
        }
    }
    @Test
    public void testLogic2048Class() {
        logic2048 logic = new logic2048(4);

        // Initial state checks
        assertEquals(0, logic.getScore());
        assertFalse(logic.isWon());
        assertTrue(logic.hasMoves());

        Tile[][] board = logic.getGameBoard().getBoard();
        for (Tile[] row : board) {
            Arrays.fill(row, null);
        }

        // Test moveLeft
        board[0][1] = new Tile(2, 0, 1);
        board[0][2] = new Tile(4, 0, 2);
        board[0][3] = new Tile(8, 0, 3);
        logic.moveLeft();

        assertTrue(logic.hasMadeMove());
        assertEquals(2, board[0][0].getValue());
        assertEquals(4, board[0][1].getValue());
        assertEquals(8, board[0][2].getValue());
        assertNull(board[0][3]);
        assertEquals(0, logic.getScore());

        // Test merging tiles
        board[0][0] = new Tile(2, 0, 0);
        board[0][1] = new Tile(2, 0, 1);
        logic.moveLeft();

        assertTrue(logic.hasMadeMove());
        assertEquals(4, board[0][0].getValue());
        assertEquals(4, logic.getScore());
        assertTrue(logic.hasMoves());

        // Test full board with no moves
        int value = 2;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Tile(value, i, j);
                value += 2;
            }
        }

        assertFalse(logic.hasMoves());

        // Test undo functionality
        logic.undoMove();
        board = logic.getGameBoard().getBoard();

        assertEquals(2, board[0][0].getValue());
        assertEquals(2, board[0][1].getValue());

        // Test winning condition
        board[0][0] = new Tile(1024, 0, 0);
        board[0][1] = new Tile(1024, 0, 1);
        logic.moveLeft();

        assertTrue(logic.isWon());
        assertEquals(2048, board[0][0].getValue());
        assertEquals(2048, logic.getScore());

        // Test moveRight
        board[0][3] = board[0][0];
        board[0][0] = null;
        logic.moveRight();

        assertTrue(logic.hasMadeMove());
        assertEquals(2048, board[0][3].getValue());

        // Test moveUp
        board[0][3] = new Tile(2, 0, 3);
        board[1][3] = new Tile(2, 1, 3);
        logic.moveUp();

        assertTrue(logic.hasMadeMove());
        assertEquals(4, board[0][3].getValue());

        // Test moveDown
        board[2][3] = board[0][3];
        board[0][3] = null;
        logic.moveDown();

        assertTrue(logic.hasMadeMove());
        logic.randomizeBoard();
    }
    @Test
    public void testSettingsClass() {
        File file = new File("settings.properties");
        if (file.exists()) {
            file.delete();
        }

        Settings settings = new Settings();
        assertEquals(0.5, settings.getVolume(), 0.001);
        assertEquals("Default", settings.getTheme());

        settings.setVolume(0.8);
        assertEquals(0.8, settings.getVolume(), 0.001);

        settings.setTheme("Dark");
        assertEquals("Dark", settings.getTheme());

        settings.saveSettings();

        Settings newSettings = new Settings();
        assertEquals(0.8, newSettings.getVolume(), 0.001);
        assertEquals("Dark", newSettings.getTheme());
    }
    @Test
    public void testHighScoreManagerClass() {
        File file = new File("scores.dat");
        if (file.exists()) {
            file.delete();
        }

        HighScoreManager manager = new HighScoreManager();
        manager.addScore("Alice", 3000);
        manager.addScore("Bob", 2000);
        manager.addScore("Charlie", 4000);

        List<HighScoreManager.ScoreEntry> scores = manager.getHighScores();
        assertEquals(3, scores.size());
        assertEquals("Charlie", scores.get(0).getName());
        assertEquals(4000, scores.get(0).getScore());
        for (int i = 0; i < 15; i++) {
            manager.addScore("Player" + i, i * 100);
        }

        scores = manager.getHighScores();
        assertEquals(10, scores.size());
        manager.saveScores();

        HighScoreManager newManager = new HighScoreManager();
        scores = newManager.getHighScores();
    }
    @Test
    public void testSoundManagerClass() {
        SoundManager soundManager = new SoundManager();
        soundManager.playBackgroundMusic("invalidFile.wav");

        soundManager.stopBackgroundMusic();

        soundManager.playSoundEffect("invalidSound.wav");
    }


    @Test
    public void testManager2048() {
        String inputCommands = "w\ns\na\nu\nd\nexit\nConnor\n";
        InputStream inputStream = new ByteArrayInputStream(inputCommands.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);
        manager2048 manager = new manager2048("Traditional", scanner);
        manager.startGame();
        assertTrue(manager.isGameOver());
        assertEquals("Traditional", manager.getMode());
        logic2048 logic = manager.getGameLogic();
        assertNotNull(logic);
        assertNotNull(logic.getGameBoard());
        assertTrue(logic.hasMadeMove());
        List<HighScoreManager.ScoreEntry> highScores = manager.getHighScoreManager().getHighScores();
        boolean found = false;
        for (HighScoreManager.ScoreEntry entry : highScores) {
            if (entry.getName().equals("Connor") && entry.getScore() == logic.getScore()) {
                found = true;
                break;
            }
        }
        assertTrue(found, "High score should be recorded with the username 'TestUser'");
    }
    @Test
    public void testManager2048b() {
        String inputCommands = "w\nw\nw\nw\nexit\nConnor\n";
        InputStream inputStream = new ByteArrayInputStream(inputCommands.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);
        manager2048 manager = new manager2048("Time Trial", scanner);
        manager.startGame();
        assertTrue(manager.isGameOver());
        assertEquals("Time Trial", manager.getMode());
        logic2048 logic = manager.getGameLogic();
        assertNotNull(logic);
        assertNotNull(logic.getGameBoard());
        assertTrue(logic.hasMadeMove());
        List<HighScoreManager.ScoreEntry> highScores = manager.getHighScoreManager().getHighScores();
        boolean found = false;
        for (HighScoreManager.ScoreEntry entry : highScores) {
            if (entry.getName().equals("Connor") && entry.getScore() == logic.getScore()) {
                found = true;
                break;
            }
        }
        assertTrue(found, "High score should be recorded with the username 'TestUser'");
    }
    @Test
    public void testManager2048c() {
        String inputCommands = "s\nd\ns\nd\na\s\nd\ns\nd\nexit\nConnor\n";
        InputStream inputStream = new ByteArrayInputStream(inputCommands.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);
        manager2048 manager = new manager2048("Move Limit", scanner);
        manager.startGame();
        assertTrue(manager.isGameOver());
        assertEquals("Move Limit", manager.getMode());
        logic2048 logic = manager.getGameLogic();
        assertNotNull(logic);
        assertNotNull(logic.getGameBoard());
    }
    @Test
    public void testManager2048d() {
        manager2048 manager = new manager2048("Move Limit");
        manager2048 manager1 = new manager2048("Traditional");
        manager2048 manager2 = new manager2048("Time Trial");
    }
    
}
