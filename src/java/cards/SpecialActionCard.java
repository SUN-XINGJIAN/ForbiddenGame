package cards;

public class SpecialActionCard extends Card{
    private final SpecialActionCardType actionType;

    public SpecialActionCard(SpecialActionCardType actionType) {
        super();
        this.actionType = actionType;
    }

    public void playEffect() {
        // 需要 GameController 实现具体效果：
        // - HELICOPTER_LIFT: 调用 GameController.helicopterTransport()
        // - SAND_BAGS: 调用 GameController.reinforceTile()
        // - WATER_RESISTANCE: 调用 GameController.addWaterImmunity()
    }

    public SpecialActionCardType getActionType() { return actionType; }
}

