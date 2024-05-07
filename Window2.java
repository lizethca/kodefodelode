import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Window2 extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextArea display2;
    private JTextArea text2;
    private Window1 window1Ref;
    private String username;

    public Window2(Window1 window1) {
        this.window1Ref = window1;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        //initializing Window2 GUI components
        JButton send2 = new JButton("SEND");
        send2.addActionListener(e -> sendText(text2.getText()));
        send2.setBounds(310, 230, 117, 32);
        contentPane.add(send2);

        display2 = new JTextArea();
        display2.setBounds(16, 44, 417, 186);
        display2.setEditable(false);
        contentPane.add(display2);

        JButton clear2 = new JButton("CLEAR");
        clear2.addActionListener(e -> display2.setText(""));
        clear2.setBounds(310, 13, 117, 32);
        contentPane.add(clear2);

        text2 = new JTextArea();
        text2.setBounds(16, 234, 282, 28);
        contentPane.add(text2);

        JLabel label2 = new JLabel("Window 2");
        label2.setBounds(23, 16, 150, 16);
        contentPane.add(label2);
    }

    public void receiveMessage(String message) {
        if (!message.isEmpty()) {
            display2.append(window1Ref.getUsername() + ": " + message + "\n");
            NotificationManager.notifyUser("New message in Window 2: " + message);
        }
    }

    public void setWindow1(Window1 window1) {
        this.window1Ref = window1;
    }

    public void sendText(String text) {
        if (!text.isEmpty() && window1Ref != null) {
            display2.append(username + ": " + text + "\n");
            window1Ref.receiveMessage(text);
            text2.setText("");
            NotificationManager.notifyUser("Message sent to Window 1");
        }
    }

    public void setUsername(String username) {
        this.username = username;
        setTitle("Window 2 - " + username);
    }

    public String getUsername() {
        return username;
    }
}