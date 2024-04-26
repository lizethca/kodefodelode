import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class Window2 extends JFrame {
    private static final long serialVersionUID = 1L;
    static String username2 = "User2";
    private JPanel contentPane;
    public static JTextArea display2;
    public static JTextArea text2;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Window2 frame = new Window2();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Window2() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton send2 = new JButton("SEND");
        send2.addActionListener(e -> {
            String s = text2.getText();
            if (!s.isEmpty()) {
                display2.append(username2 + ": " + s + "\n");
                Window1.sendText(s);
                text2.setText("");
                NotificationManager.notifyUser("Message sent to Window 1");
            }
        });
        send2.setBounds(310, 230, 117, 32);
        contentPane.add(send2);

        display2 = new JTextArea();
        display2.setBounds(16, 44, 417, 186);
        contentPane.add(display2);

        JButton clear2 = new JButton("CLEAR");
        clear2.addActionListener(e -> display2.setText(""));
        clear2.setBounds(310, 13, 117, 32);
        contentPane.add(clear2);

        text2 = new JTextArea();
        text2.setBounds(16, 234, 282, 28);
        contentPane.add(text2);

        JLabel label2 = new JLabel("Window 2");
        label2.setBounds(23, 16, 61, 16);
        contentPane.add(label2);
    }

    public static void sendText(String text) {
        if (!text.isEmpty()) {
            Window1.display1.append(username2 + ": " + text + "\n");
            NotificationManager.notifyUser("New message in Window 2: " + text);
        }
    }
}