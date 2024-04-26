import javax.swing.JOptionPane;

public class NotificationManager {
    public static void notifyUser(String message) {
        JOptionPane.showMessageDialog(null, message, "New Message Notification", JOptionPane.INFORMATION_MESSAGE);
    }
}
