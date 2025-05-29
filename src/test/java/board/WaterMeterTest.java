package board;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WaterMeterTest {
    @Test
    public void testStageCalculation() {
        // Test stage calculation for different water levels
        WaterMeter wm1 = new WaterMeter(1);
        assertEquals(2, wm1.getStage());
        WaterMeter wm3 = new WaterMeter(3);
        assertEquals(3, wm3.getStage());
        WaterMeter wm6 = new WaterMeter(6);
        assertEquals(4, wm6.getStage());
        WaterMeter wm8 = new WaterMeter(8);
        assertEquals(5, wm8.getStage());
    }

    @Test
    public void testDrawNoException() {
        // Test draw method does not throw exception
        WaterMeter wm = new WaterMeter(2);
        assertDoesNotThrow(() -> wm.draw());
    }

    @Test
    public void testGetStage() {
        // Test getStage method for a specific value
        WaterMeter wm = new WaterMeter(5);
        assertEquals(3, wm.getStage());
    }

    @Test
    public void testConstructorEdgeCases() {
        // Test constructor with edge case values
        WaterMeter wm = new WaterMeter(0);
        assertEquals(2, wm.getStage());
    }
} 