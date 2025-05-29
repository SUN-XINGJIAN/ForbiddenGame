package cards;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the TreasureCard class.
 * Tests type assignment, card type/num, cardname path, and unknown type handling.
 */
public class TreasureCardTest {

    @Test
    public void testTypeAssignment() {
        // Test that each number maps to the correct Type
        TreasureCard soil = new TreasureCard(0);
        assertEquals(TreasureCard.Type.SOIL, soil.getType());

        TreasureCard cloud = new TreasureCard(5);
        assertEquals(TreasureCard.Type.CLOUD, cloud.getType());

        TreasureCard fire = new TreasureCard(10);
        assertEquals(TreasureCard.Type.FIRE, fire.getType());

        TreasureCard water = new TreasureCard(15);
        assertEquals(TreasureCard.Type.WATER, water.getType());

        TreasureCard sandbags = new TreasureCard(23);
        assertEquals(TreasureCard.Type.SANDBAGS, sandbags.getType());

        TreasureCard helicopter = new TreasureCard(20);
        assertEquals(TreasureCard.Type.HELICOPTER, helicopter.getType());
    }

    @Test
    public void testCardTypeAndNum() {
        // Test card type and num fields
        TreasureCard card = new TreasureCard(10);
        assertEquals(10, card.getCardType());
        assertEquals(10, card.num);
    }

    @Test
    public void testCardNamePath() {
        // Test that cardname is set correctly for the given number
        TreasureCard card = new TreasureCard(5);
        assertTrue(card.cardname.contains("/image/TreasureCards/5.png"));
    }

    @Test
    public void testUnknownType() {
        // Test that an unknown number results in null type
        TreasureCard card = new TreasureCard(99);
        assertNull(card.getType());
    }
}
