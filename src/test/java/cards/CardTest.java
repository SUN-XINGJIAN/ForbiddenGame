package cards;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Card class.
 * Only tests constructor and field assignment, since Card has no other public logic methods.
 */
public class CardTest {

    @Test
    public void testCardInstantiation() {
        // Test that a Card object can be instantiated and has the correct default size
        Card card = new Card();
        assertNotNull(card);
        assertEquals(50, card.getWidth());
        assertEquals(69, card.getHeight());
    }

    @Test
    public void testCardNameField() {
        // Test that the cardname field can be set and retrieved
        Card card = new Card();
        card.cardname = "/some/path/to/image.png";
        assertEquals("/some/path/to/image.png", card.cardname);
    }

    /**
     * UI drawing methods like draw() depend on JavaFX runtime and image resources,
     * so they are not usually tested with assertions in unit tests.
     * However, you can test that draw() does not throw an exception,
     * provided that cardname points to a valid image resource and JavaFX is available.
     *
     * Uncomment the following test if you have a valid image and JavaFX test environment:
     */
    // @Test
    // public void testDrawNoException() {
    //     Card card = new Card();
    //     card.cardname = "/cards/test.png"; // Make sure this image exists in your project
    //     assertDoesNotThrow(() -> card.draw());
    // }
}
