package cards;

public abstract class Card {
    protected final CardType type;
    protected final String name;

    protected Card(CardType type, String name) {
        this.type = type;
        this.name = name;
    }

    public abstract void playEffect();

    // Getters
    public CardType getType() { return type; }
    public String getName() { return name; }
}
