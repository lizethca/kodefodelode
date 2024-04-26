import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class Window1 extends JFrame {
    private static final long serialVersionUID = 1L;
    static String username1 = "User1";
    private JPanel contentPane;
    public static JTextArea display1;
    public static JTextArea text1;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Window1 frame = new Window1();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Window1() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton send1 = new JButton("SEND");
        send1.addActionListener(e -> {
            String s = text1.getText();
            if (!s.isEmpty()) {
                display1.append(username1 + ": " + s + "\n");
                Window2.sendText(s);
                text1.setText("");
                NotificationManager.notifyUser("Message sent to Window 2");
            }
        });
        send1.setBounds(310, 230, 117, 32);
        contentPane.add(send1);

        display1 = new JTextArea();
        display1.setBounds(16, 44, 417, 186);
        contentPane.add(display1);

        JButton clear1 = new JButton("CLEAR");
        clear1.addActionListener(e -> display1.setText(""));
        clear1.setBounds(310, 13, 117, 29);
        contentPane.add(clear1);

        text1 = new JTextArea();
        text1.setBounds(16, 234, 282, 28);
        contentPane.add(text1);

        JLabel label1 = new JLabel("Window 1");
        label1.setBounds(23, 16, 61, 16);
        contentPane.add(label1);
    }

    public static void sendText(String text) {
        if (!text.isEmpty()) {
            Window2.display2.append(username1 + ": " + text + "\n");
            NotificationManager.notifyUser("New message in Window 1: " + text);
        }
    }
}