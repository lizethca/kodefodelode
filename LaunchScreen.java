import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
//GUI
public class LaunchScreen extends JFrame {
    private static final long serialVersionUID = 1L;

    public LaunchScreen(List<ChatWindow> windows, List<String> groupChats) {
        setTitle("Launch Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 400);
        getContentPane().setLayout(new BorderLayout());

        //loading and scaling image
        ImageIcon originalIcon = new ImageIcon("Bow.png");
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(500, 300, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel label = new JLabel(scaledIcon);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        getContentPane().add(label, BorderLayout.CENTER);

        JButton proceedButton = new JButton("Proceed to Chat");
        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ChatApplication.launchLoginWindow(windows, groupChats, 3);
            }
        });
        getContentPane().add(proceedButton, BorderLayout.SOUTH);
    }
}