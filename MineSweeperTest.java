/**
 * Names: Connor O'Neill, Eli Jordan, Derek Hoshaw, AJ Becerra
 * Net Ids: connoroneill ejordan3 dthoshaw AJBECERRA
 */

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class MineSweeperTest {
    private MineSweeperGame game;
    private Cell cell;

    @BeforeEach
    public void setup() {
        game = new MineSweeperGame(8, 8, 10); // 8x8 grid with 10 mines
        cell = new Cell();
    }

    // Test for Cell class
    @Test
    public void testCellInitialization() {
        assertFalse(cell.isMine());
        assertFalse(cell.isRevealed());
        assertFalse(cell.isFlagged());
        assertEquals(0, cell.getAdjacentMines());
    }

    @Test
    public void testSetAndGetMine() {
        cell.setMine(true);
        assertTrue(cell.isMine());
    }

    @Test
    public void testRevealCell() {
        cell.reveal();
        assertTrue(cell.isRevealed());
    }

    @Test
    public void testToggleFlag() {
        cell.toggleFlag();
        assertTrue(cell.isFlagged());
        cell.toggleFlag();
        assertFalse(cell.isFlagged());
    }

    @Test
    public void testSetAndGetAdjacentMines() {
        cell.setAdjacentMines(3);
        assertEquals(3, cell.getAdjacentMines());
    }

    // Test for MineSweeperGame class
    @Test
    public void testGameInitialization() {
        assertEquals(8, game.getRows());
        assertEquals(8, game.getCols());
    }

    @Test
    public void testMinePlacement() {
        int mineCount = 0;
        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getCols(); j++) {
                if (game.getCell(i, j).isMine()) {
                    mineCount++;
                }
            }
        }
        assertEquals(10, mineCount);
    }

    @Test
    public void testValidCell() {
        assertTrue(game.isValidCell(0, 0));
        assertTrue(game.isValidCell(7, 7));
        assertFalse(game.isValidCell(-1, 0));
        assertFalse(game.isValidCell(8, 8));
    }

    @Test
    public void testCalculateAdjacentMines() {
        // Check that adjacent mine counts are calculated correctly
        int adjacentMineCount = 0;
        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getCols(); j++) {
                adjacentMineCount += game.getCell(i, j).getAdjacentMines();
            }
        }
        assertTrue(adjacentMineCount > 0); // Ensure there are calculated adjacent mines
    }

    // Test for MineSweeperUI class
    @Test
    public void testUIInitialization() {
        MineSweeperUI ui = new MineSweeperUI(game);
        assertNotNull(ui);
    }

    @Test
    public void testToggleFlagOnUI() {
        MineSweeperUI ui = new MineSweeperUI(game);
        game.getCell(0, 0).toggleFlag();
        assertTrue(game.getCell(0, 0).isFlagged());
    }

    @Test
    public void testRevealCellOnUI() {
        MineSweeperUI ui = new MineSweeperUI(game);
        game.getCell(0, 0).reveal();
        assertTrue(game.getCell(0, 0).isRevealed());
    }

    @Test
    public void testGameOverLogic() {
        // Trigger game over by revealing a mine
        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getCols(); j++) {
                if (game.getCell(i, j).isMine()) {
                    game.getCell(i, j).reveal();
                    assertTrue(game.getCell(i, j).isRevealed());
                    break;
                }
            }
        }
    }
}
