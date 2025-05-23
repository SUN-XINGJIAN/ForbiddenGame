package player;

public enum PlayerRole {
    DIVER("Diver", "/image/Pawns/@2x/Diver@2x.png"),
    EXPLORER("Explorer", "/image/Pawns/@2x/Explorer@2x.png"),
    PILOT("Pilot", "/image/Pawns/@2x/Pilot@2x.png"),
    NAVIGATOR("Navigator", "/image/Pawns/@2x/Navigator@2x.png"),
    ENGINEER("Engineer", "/image/Pawns/@2x/Engineer@2x.png"),
    MESSENGER("Messenger", "/image/Pawns/@2x/Messenger@2x.png");

    private final String name;
    private final String imagePath;

    PlayerRole(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }
}
