import javax.swing.*; //GUI
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//chat window GUI tings
public class ChatWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextArea display;
    private JTextArea input;
    private String username;
    private List<ChatWindow> windows;
    private List<String> groupChats;
    private JComboBox<String> recipientDropdown;
    private boolean suppressDisplay = false;
    private List<String> joinedGroups = new ArrayList<>();
    private Map<String, List<String>> conversationHistories = new HashMap<>();
    private String currentConversation;
    private TicTacToeGame currentGame;

//chat window functionalities
    public ChatWindow(String username, List<ChatWindow> windows, List<String> groupChats) {
        this.username = username;
        this.windows = windows;
        this.groupChats = groupChats;
        this.currentConversation = "All";

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 550, 350);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton sendButton = new JButton("SEND");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        sendButton.setBounds(410, 230, 117, 32);
        contentPane.add(sendButton);

        display = new JTextArea();
        display.setBounds(16, 44, 511, 186);
        display.setEditable(false);
        contentPane.add(display);

        JButton clearButton = new JButton("CLEAR");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                display.setText("");
            }
        });
        clearButton.setBounds(410, 13, 117, 32);
        contentPane.add(clearButton);

        input = new JTextArea();
        input.setBounds(16, 234, 382, 28);
        contentPane.add(input);

        JLabel label = new JLabel("Chat - " + username);
        label.setBounds(23, 16, 150, 16);
        contentPane.add(label);

        recipientDropdown = new JComboBox<>();
        recipientDropdown.setBounds(16, 10, 150, 28);
        recipientDropdown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentConversation = (String) recipientDropdown.getSelectedItem();
                loadConversationHistory(currentConversation);
            }
        });
        updateChatWindowList();
        contentPane.add(recipientDropdown);

        JButton btnGroupChat = new JButton("Create Group Chat");
        btnGroupChat.setBounds(170, 10, 140, 25);
        btnGroupChat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                groupChatActionPerformed(e);
            }
        });
        contentPane.add(btnGroupChat);

        JButton btnJoinGroupChat = new JButton("Join Group Chat");
        btnJoinGroupChat.setBounds(310, 10, 120, 25);
        btnJoinGroupChat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                joinGroupChatActionPerformed(e);
            }
        });
        contentPane.add(btnJoinGroupChat);

        //adds game buttons 
        JButton eightBallButton = new JButton("Play 8-Ball");
        eightBallButton.setBounds(170, 270, 120, 25);
        eightBallButton.addActionListener(e -> new EightBallGame("8-Ball").setVisible(true));
        contentPane.add(eightBallButton);

        JButton ticTacToeButton = new JButton("Challenge Tic-Tac-Toe");
        ticTacToeButton.setBounds(310, 270, 120, 25);
        ticTacToeButton.addActionListener(e -> challengeTicTacToe());
        contentPane.add(ticTacToeButton);
    }
    //sending messages
    private void sendMessage() {
        String text = input.getText().trim();
        String recipient = (String) recipientDropdown.getSelectedItem();

        if (!text.isEmpty() && recipient != null) {
            suppressDisplay = true;
            String message = username + ": " + text;

            if (currentGame != null && currentGame.isPlaying()) {
                //should be processing each ttt move
                String response = currentGame.processMove(username, text);
                if (response != null) {
                    for (ChatWindow window : windows) {
                        if (window.getUsername().equals(recipient)) {
                            window.receiveMessage(username, response);
                            break;
                        }
                    }
                }
            } else {
                //reutrns back to normal messaging
                conversationHistories.putIfAbsent(recipient, new ArrayList<>());
                conversationHistories.get(recipient).add(message);

                if (recipient.equals("All")) {
                    for (ChatWindow window : windows) {
                        window.receiveMessage("All", message);
                    }
                } else if (joinedGroups.contains(recipient)) {
                    for (ChatWindow window : windows) {
                        if (window.isInGroup(recipient)) {
                            window.receiveMessage(recipient, message);
                        }
                    }
                } else {
                    for (ChatWindow window : windows) {
                        if (window.getUsername().equals(recipient)) {
                            window.receiveMessage(username, message);
                            break;
                        }
                    }
                }
            }

            if (recipient.equals(currentConversation)) {
                display.append(message + "\n");
                if (currentGame != null && currentGame.isPlaying()) {
                    display.append(currentGame.getBoard() + "\n");
                }
            }
            suppressDisplay = false;
            input.setText("");
        }
    }
    //this works but it won't alternate turns so that both users can play 
    private void challengeTicTacToe() {
        String opponent = (String) recipientDropdown.getSelectedItem();
        if (opponent != null && !opponent.equals("All") && !opponent.startsWith("-- ")) {
            String challengeMessage = username + " challenges you to Tic-Tac-Toe!";
            for (ChatWindow window : windows) {
                if (window.getUsername().equals(opponent)) {
                    window.receiveMessage(username, challengeMessage);
                    window.startTicTacToeGame(username, opponent);
                    break;
                }
            }
            startTicTacToeGame(username, opponent);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid opponent from the dropdown", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
//tictactoe game
    public void startTicTacToeGame(String player1, String player2) {
        currentGame = new TicTacToeGame(player1, player2);
        TicTacToeWindow gameWindow = new TicTacToeWindow(currentGame, this, player2.equals(username));
        gameWindow.setVisible(true);
    }

    private void groupChatActionPerformed(ActionEvent e) {
        String groupName = JOptionPane.showInputDialog(this, "Enter a name for the group chat:", "Create Group Chat", JOptionPane.PLAIN_MESSAGE);
        if (groupName != null && !groupName.trim().isEmpty() && !groupChats.contains(groupName)) {
            groupChats.add(groupName.trim());
            updateChatWindowList();
            joinedGroups.add(groupName.trim());
            display.setText("");
            conversationHistories.put(groupName, new ArrayList<>());
        }
    }
//groupchats
    private void joinGroupChatActionPerformed(ActionEvent e) {
        if (!groupChats.isEmpty()) {
            String groupName = (String) JOptionPane.showInputDialog(this, "Select a group chat to join:", "Join Group Chat", JOptionPane.PLAIN_MESSAGE, null, groupChats.toArray(), groupChats.get(0));
            if (groupName != null && !joinedGroups.contains(groupName)) {
                joinedGroups.add(groupName);
                updateChatWindowList();
                display.setText("");
                loadConversationHistory(groupName);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No group chats available", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void receiveMessage(String chatKey, String message) {
        conversationHistories.putIfAbsent(chatKey, new ArrayList<>());
        conversationHistories.get(chatKey).add(message);

        if (chatKey.equals(currentConversation) && !suppressDisplay) {
            display.append(message + "\n");

            if (currentGame != null && currentGame.isPlaying() && currentGame.isValidMove(message)) {
                String response = currentGame.processMove(username, message);
                if (response != null) {
                    display.append(currentGame.getBoard() + "\n");
                }
            }
        }

        NotificationManager.notifyUser("New message for " + username + ": " + message);
    }
//conversation history
    private void loadConversationHistory(String conversation) {
        display.setText("");
        List<String> messages = conversationHistories.getOrDefault(conversation, new ArrayList<>());
        for (String message : messages) {
            display.append(message + "\n");
        }
    }
    public String getUsername() {
        return username;
    }
    public boolean isInGroup(String groupName) {
        return joinedGroups.contains(groupName);
    }
    public void updateChatWindowList() {
        recipientDropdown.removeAllItems();

        recipientDropdown.addItem("-- Direct Messages --");
        for (ChatWindow window : windows) {
            if (!window.getUsername().equals(username)) {
                recipientDropdown.addItem(window.getUsername());
            }
        }
        if (!groupChats.isEmpty()) {
            recipientDropdown.addItem("-- Group Chats --");
            for (String groupChat : groupChats) {
                recipientDropdown.addItem(groupChat);
            }
        }
        recipientDropdown.setRenderer(new ComboBoxRenderer());
    }
    public void clearCurrentGame() {
        currentGame = null;
    }
    public void forwardMessage(String move) {
        sendMessage(move);
    }
    private void sendMessage(String move) {
        input.setText(move);
        sendMessage();
    }
}
class ComboBoxRenderer extends DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;
    private static final String LABEL_PREFIX = "-- ";

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof String && ((String) value).startsWith(LABEL_PREFIX)) {
            label.setFont(label.getFont().deriveFont(Font.ITALIC));
            label.setForeground(Color.GRAY);
        }
        return label;
    }
}