import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class test2048 {
    private game2048 game;

    @BeforeEach
    void setUp() {
        game = new game2048();
    }

    @Test
    void testInitialBoardSetup() {
        int[][] board = game.getBoard();
        int nonZeroCount = 0;

        for (int[] row : board) {
            for (int cell : row) {
                if (cell != 0) nonZeroCount++;
            }
        }

        assertEquals(2, nonZeroCount, "board should start with 2 tiles.");
    }

    @Test
    void testAddNewTile() {
        game.addNewTile();
        int[][] board = game.getBoard();
        int nonZeroCount = 0;

        for (int[] row : board) {
            for (int cell : row) {
                if (cell != 0) nonZeroCount++;
            }
        }

        assertTrue(nonZeroCount >= 2 && nonZeroCount <= 16, "board should have between 2 and 16 tiles");
    }

    @Test
    void testMoveLeft() {
        int[][] customBoard = {
            {2, 2, 4, 0},
            {0, 0, 4, 4},
            {2, 0, 0, 2},
            {0, 0, 0, 0}
        };
        setBoard(customBoard);

        game.moveLeft();

        int[][] expectedBoard = {
            {4, 4, 0, 0},
            {8, 0, 0, 0},
            {4, 0, 0, 0},
            {0, 0, 0, 0}
        };

        assertArrayEquals(expectedBoard, game.getBoard(), "Board should reflect a valid move to the left.");
    }

    @Test
    void testMoveRight() {
        int[][] customBoard = {
            {2, 2, 4, 0},
            {0, 0, 4, 4},
            {2, 0, 0, 2},
            {0, 0, 0, 0}
        };
        setBoard(customBoard);

        game.moveRight();

        int[][] expectedBoard = {
            {0, 0, 4, 4},
            {0, 0, 0, 8},
            {0, 0, 0, 4},
            {0, 0, 0, 0}
        };

        assertArrayEquals(expectedBoard, game.getBoard(), "Board should reflect a valid move to the right.");
    }

    @Test
    void testMoveUp() {
        int[][] customBoard = {
            {2, 0, 4, 0},
            {2, 4, 4, 4},
            {0, 4, 0, 0},
            {2, 0, 4, 4}
        };
        setBoard(customBoard);

        game.moveUp();

        int[][] expectedBoard = {
            {4, 8, 8, 8},
            {2, 0, 4, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        };

        assertArrayEquals(expectedBoard, game.getBoard(), "Board should reflect a valid move upward.");
    }

    @Test
    void testMoveDown() {
        int[][] customBoard = {
            {2, 0, 4, 0},
            {2, 4, 4, 4},
            {0, 4, 0, 0},
            {2, 0, 4, 4}
        };
        setBoard(customBoard);

        game.moveDown();

        int[][] expectedBoard = {
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {2, 0, 4, 0},
            {4, 8, 8, 8}
        };

        assertArrayEquals(expectedBoard, game.getBoard(), "Board should reflect a valid move downward.");
    }

    @Test
    void testGameOver() {
        int[][] customBoard = {
            {2, 4, 2, 4},
            {4, 2, 4, 2},
            {2, 4, 2, 4},
            {4, 2, 4, 2}
        };
        setBoard(customBoard);

        assertFalse(game.hasMoves(), "Game should detect no moves left.");
    }

    @Test
    void testScoreUpdates() {
        int[][] customBoard = {
            {2, 2, 4, 4},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        };
        setBoard(customBoard);

        game.moveLeft();

        assertEquals(12, game.getScore(), "Score should be updated correctly after merging tiles.");
    }

    private void setBoard(int[][] customBoard) {
        int[][] board = game.getBoard();
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(customBoard[i], 0, board[i], 0, board[i].length);
        }
    }
}
