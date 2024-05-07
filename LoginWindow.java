import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Optional;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private List<ChatWindow> windows;
    private List<String> groupChats;
    private List<User> users;
//fields for loggining in window
    public LoginWindow(List<ChatWindow> windows, List<String> groupChats) {
        this.windows = windows;
        this.groupChats = groupChats;
        this.users = DatabaseManager.loadUsers();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 300, 200);
        setTitle("Login");
        getContentPane().setLayout(null);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(40, 30, 80, 20);
        getContentPane().add(lblUsername);

        usernameField = new JTextField();
        usernameField.setBounds(130, 30, 120, 20);
        getContentPane().add(usernameField);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(40, 70, 80, 20);
        getContentPane().add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(130, 70, 120, 20);
        getContentPane().add(passwordField);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(50, 110, 80, 25);
        getContentPane().add(btnLogin);
        btnLogin.addActionListener(this::loginActionPerformed);

        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(150, 110, 100, 25);
        getContentPane().add(btnRegister);
        btnRegister.addActionListener(this::registerActionPerformed);
    }

    private void loginActionPerformed(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        Optional<User> matchingUser = users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst();

        if (matchingUser.isPresent()) {
            ChatWindow newWindow = new ChatWindow(username, windows, groupChats);
            windows.add(newWindow);
            for (ChatWindow window : windows) {
                window.updateChatWindowList();
            }
            new Thread(() -> EventQueue.invokeLater(() -> newWindow.setVisible(true))).start();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
//registration option
    private void registerActionPerformed(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username or password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (users.stream().anyMatch(user -> user.getUsername().equals(username))) {
            JOptionPane.showMessageDialog(this, "Username already taken", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            User newUser = new User(username, password);
            DatabaseManager.saveUser(newUser);
            users.add(newUser);
            JOptionPane.showMessageDialog(this, "User registered successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}