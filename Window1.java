import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Window1 extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextArea display1;
    private JTextArea text1;
    private Window2 window2Ref;
    private String username;

    public Window1(Window2 window2) {
        this.window2Ref = window2;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        //initializing Window1 GUI components
        JButton send1 = new JButton("SEND");
        send1.addActionListener(e -> sendText(text1.getText()));
        send1.setBounds(310, 230, 117, 32);
        contentPane.add(send1);

        display1 = new JTextArea();
        display1.setBounds(16, 44, 417, 186);
        display1.setEditable(false);
        contentPane.add(display1);

        JButton clear1 = new JButton("CLEAR");
        clear1.addActionListener(e -> display1.setText(""));
        clear1.setBounds(310, 13, 117, 29);
        contentPane.add(clear1);

        text1 = new JTextArea();
        text1.setBounds(16, 234, 282, 28);
        contentPane.add(text1);

        JLabel label1 = new JLabel("Window 1");
        label1.setBounds(23, 16, 150, 16);
        contentPane.add(label1);
    }

    public void receiveMessage(String message) {
        if (!message.isEmpty()) {
            display1.append(window2Ref.getUsername() + ": " + message + "\n");
            NotificationManager.notifyUser("New message in Window 1: " + message);
        }
    }

    public void setWindow2(Window2 window2) {
        this.window2Ref = window2;
    }

    public void sendText(String text) {
        if (!text.isEmpty() && window2Ref != null) {
            display1.append(username + ": " + text + "\n");
            window2Ref.receiveMessage(text);
            text1.setText("");
            NotificationManager.notifyUser("Message sent to Window 2");
        }
    }

    public void setUsername(String username) {
        this.username = username;
        setTitle("Window 1 - " + username);
    }

    public String getUsername() {
        return username;
    }
}