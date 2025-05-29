package board;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TreasureTest {
    @Test
    public void testConstructorAndPosition() {
        // Test constructor and position fields
        Treasure treasure = new Treasure(1, 50, 60);
        assertEquals(50, treasure.getLayoutX());
        assertEquals(60, treasure.getLayoutY());
    }

    @Test
    public void testDrawNoException() {
        // Test draw method does not throw exception
        Treasure treasure = new Treasure(2, 0, 0);
        assertDoesNotThrow(() -> treasure.draw());
    }

    @Test
    public void testImagePath() {
        // Test image path logic (no exception expected)
        Treasure treasure = new Treasure(3, 0, 0);
        assertDoesNotThrow(() -> treasure.draw());
    }

    @Test
    public void testConstructorEdgeCases() {
        // Test constructor with edge case values
        Treasure treasure = new Treasure(0, -10, -20);
        assertEquals(-10, treasure.getLayoutX());
        assertEquals(-20, treasure.getLayoutY());
    }
} 