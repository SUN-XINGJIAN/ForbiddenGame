package logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

// Mock Player class for testing
class DummyPlayer extends module.Player {
    public DummyPlayer() { super("dummy"); }
    @Override
    public int getPositionX(logic.ForbiddenGameStarted forbiddenGameStarted) { return 0; }
    @Override
    public int getPositionY(logic.ForbiddenGameStarted forbiddenGameStarted) { return 0; }
    @Override
    public void resetSpecialAbility() {}
}

// Mock ScreenController class for testing
class DummyScreenController extends controller.ScreenController {
    public DummyScreenController() { super(); }
}

public class TurnManageTest {

    @Test
    public void testStepUseAndReset() {
        // Test step decrement and reset logic
        TurnManage tm = new TurnManage(new DummyScreenController());
        assertEquals(3, tm.getStep());
        tm.useStep();
        assertEquals(2, tm.getStep());
        tm.useStep();
        assertEquals(1, tm.getStep());
        tm.useStep();
        assertEquals(0, tm.getStep());
        tm.resetSteps();
        assertEquals(3, tm.getStep());
    }

    @Test
    public void testGetIndex() {
        // Test getIndex logic for player rotation
        TurnManage tm = new TurnManage(new DummyScreenController());
        List<module.Player> players = Arrays.asList(
                new DummyPlayer(), new DummyPlayer(), new DummyPlayer()
        );
        assertEquals(1, tm.getIndex(0, players));
        assertEquals(2, tm.getIndex(1, players));
        assertEquals(0, tm.getIndex(2, players)); // wrap around
    }

    @Test
    public void testGetIndex1() {
        // Test getIndex1 logic for reverse player rotation
        TurnManage tm = new TurnManage(new DummyScreenController());
        List<module.Player> players = Arrays.asList(
                new DummyPlayer(), new DummyPlayer(), new DummyPlayer()
        );
        assertEquals(1, tm.getIndex1(2, players));
        assertEquals(0, tm.getIndex1(1, players));
        assertEquals(2, tm.getIndex1(0, players)); // wrap around to last
    }
}
