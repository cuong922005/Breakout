public class Auth {
    private static Auth instant;
    private Player player;

    public static Auth getInstant() {
        if (instant == null) {
            instant = new Auth();
        }
        return instant;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    
}
