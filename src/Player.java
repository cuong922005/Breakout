import java.util.List;

public class Player {
    private int id;
    private String username;
    private String password;
    private List<Integer> scores;
    private boolean remember;
    
    public Player(int id, String username, String password, List<Integer> scores, boolean remember) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.scores = scores;
        this.remember = remember;
    }
    public Player() {
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public List<Integer> getScores() {
        return scores;
    }
    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }
    public boolean isRemember() {
        return remember;
    }
    public void setRemember(boolean remember) {
        this.remember = remember;
    }
    @Override
    public String toString() {
        return "Player [id=" + id + ", username=" + username + ", password=" + password + ", scores=" + scores
                + ", remember=" + remember + "]";
    }
    

}
