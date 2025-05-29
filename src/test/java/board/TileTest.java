package board;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TileTest {
    @Test
    public void testStateChange() {
        // Test state transitions: normal -> flooded -> removed -> reset
        Tile tile = new Tile(1, 100, 200);
        assertEquals(0, tile.getState()); // initial state
        tile.incrementState();
        assertEquals(1, tile.getState()); // flooded
        tile.setState(2);
        assertEquals(2, tile.getState()); // removed (for drawing)
        tile.setState(0);
        assertEquals(0, tile.getState()); // reset to normal
    }

    @Test
    public void testPosition() {
        // Test position fields
        Tile tile = new Tile(5, 123, 456);
        assertEquals(123, tile.getPositionX());
        assertEquals(456, tile.getPositionY());
    }

    @Test
    public void testNameAndImage() {
        // Test name and image path
        Tile tile = new Tile(7, 0, 0);
        assertEquals("7", tile.getName());
        assertTrue(tile.getTileName1().contains("7"));
    }

    @Test
    public void testRemovedFlag() {
        // Test removed logic: state==2 means removed for drawing, but isRemoved() is a separate field
        Tile tile = new Tile(2, 0, 0);
        assertFalse(tile.isRemoved()); // removed field is false by default
        tile.setState(2);
        assertEquals(2, tile.getState()); // state==2 means removed for drawing
        // tile.isRemoved() remains false unless set explicitly
    }

    @Test
    public void testDrawNoException() {
        // Test draw method does not throw exception
        Tile tile = new Tile(3, 0, 0);
        assertDoesNotThrow(() -> tile.draw());
    }

    @Test
    public void testConstructorEdgeCases() {
        // Test constructor with edge case values
        Tile tile = new Tile(0, -100, -200);
        assertEquals(-100, tile.getPositionX());
        assertEquals(-200, tile.getPositionY());
        assertEquals("0", tile.getName());
    }
}
