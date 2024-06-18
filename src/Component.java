import javax.swing.JOptionPane;

public class Component {
    public static void showNotification(String title, String content, int messageType){ 
        JOptionPane.showMessageDialog(GameFrame.getInstance(), content, title, messageType);
    }
    
}
