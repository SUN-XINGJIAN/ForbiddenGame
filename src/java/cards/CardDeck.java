package cards;

import board.Tile;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class CardDeck {
    private final Stack<Card> drawPile = new Stack<>();
    private final Stack<Card> discardPile = new Stack<>();

    // 初始化标准冒险卡组（需 GameController 提供初始化参数）
    public void initializeAdventureDeck(List<TreasureCard> treasures,
                                        List<SpecialActionCard> actions) {
        drawPile.addAll(treasures);
        drawPile.addAll(actions);
        shuffle();
    }

    // 初始化洪水卡组（需 Board 模块提供所有板块）
    public void initializeFloodDeck(List<Tile> allTiles) {
        for (Tile tile : allTiles) {
            drawPile.add(new FloodCard(tile));
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(drawPile);
    }

    public Card draw() {
        if (drawPile.isEmpty()) {
            reshuffleDiscardPile();
        }
        return drawPile.pop();
    }

    public void discard(Card card) {
        discardPile.push(card);
    }

    private void reshuffleDiscardPile() {
        drawPile.addAll(discardPile);
        discardPile.clear();
        shuffle();
    }
}
