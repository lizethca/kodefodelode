import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//main
public class ChatApplication {
    public static void main(String[] args) {
        List<ChatWindow> windows = new ArrayList<>();
        List<String> groupChats = new ArrayList<>();
//GUI display
        EventQueue.invokeLater(() -> {
            try {
                LaunchScreen launchScreen = new LaunchScreen(windows, groupChats);
                launchScreen.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
//recursively launch log in
    public static void launchLoginWindow(List<ChatWindow> windows, List<String> groupChats, int remainingWindows) {
        if (remainingWindows > 0) {
            LoginWindow loginWindow = new LoginWindow(windows, groupChats);
            loginWindow.setVisible(true);
            loginWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    launchLoginWindow(windows, groupChats, remainingWindows - 1);
                }
            });
        }
    }
}