import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.border.EmptyBorder;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Window3 extends JFrame {
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

    public Window3(String username, List<ChatWindow> windows, List<String> groupChats) {
        this.username = username;
        this.windows = windows;
        this.groupChats = groupChats;
        this.currentConversation = "All"; 
        //nitializing currentConversation

        //gui settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
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
        sendButton.setBounds(310, 230, 117, 32);
        contentPane.add(sendButton);

        display = new JTextArea();
        display.setBounds(16, 44, 417, 186);
        display.setEditable(false); //makeing display read-only
        contentPane.add(display);

        JButton clearButton = new JButton("CLEAR");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                display.setText("");
            }
        });
        clearButton.setBounds(310, 13, 117, 32);
        contentPane.add(clearButton);

        input = new JTextArea();
        input.setBounds(16, 234, 282, 28);
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
    }

    private void sendMessage() {
        String text = input.getText().trim();
        String recipient = (String) recipientDropdown.getSelectedItem();
    
        if (!text.isEmpty() && recipient != null) {
            suppressDisplay = true; //prevents echoing message back to sender
    
            String timeStamp = new SimpleDateFormat("HH:mm").format(new Date());
    
            String message = timeStamp + " " + username + ": " + text;
    
            //updates conversation history
            conversationHistories.putIfAbsent(recipient, new ArrayList<>());
            conversationHistories.get(recipient).add(message);
    
            if (recipient.equals("All")) {
                for (ChatWindow window : windows) {
                    window.receiveMessage("All", message);
                }
            } else if (joinedGroups.contains(recipient)) {
                //broadcasts message to all windows in the selected group
                for (ChatWindow window : windows) {
                    if (window.isInGroup(recipient)) {
                        window.receiveMessage(recipient, message);
                    }
                }
            } else {
                //sends message to specific window
                for (ChatWindow window : windows) {
                    if (window.getUsername().equals(recipient)) {
                        window.receiveMessage(username, message); 
                        break;
                    }
                }
            }
    
            if (recipient.equals(currentConversation)) {
                display.append(message + "\n");
            }
            suppressDisplay = false;
            input.setText("");
        }
    }

    private void groupChatActionPerformed(ActionEvent e) {
        String groupName = JOptionPane.showInputDialog(this, "Enter a name for the group chat:", "Create Group Chat", JOptionPane.PLAIN_MESSAGE);
        if (groupName != null && !groupName.trim().isEmpty() && !groupChats.contains(groupName)) {
            groupChats.add(groupName.trim());
            updateChatWindowList();
            joinedGroups.add(groupName.trim()); 
            display.setText(""); //cears chat history when creating a new group chat
            conversationHistories.put(groupName, new ArrayList<>());
        }
    }

    private void joinGroupChatActionPerformed(ActionEvent e) {
        if (!groupChats.isEmpty()) {
            String groupName = (String) JOptionPane.showInputDialog(this, "Select a group chat to join:", "Join Group Chat", JOptionPane.PLAIN_MESSAGE, null, groupChats.toArray(), groupChats.get(0));
            if (groupName != null && !joinedGroups.contains(groupName)) {
                joinedGroups.add(groupName);
                updateChatWindowList();
                display.setText(""); 

                //loading conversation history for the selected group
                loadConversationHistory(groupName);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No group chats available", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void receiveMessage(String chatKey, String message) {
        if (!suppressDisplay) {
            String timeStamp = new SimpleDateFormat("HH:mm").format(new Date());
            conversationHistories.putIfAbsent(chatKey, new ArrayList<>());
            
            if (!message.startsWith(timeStamp)) {
                message = timeStamp + " | " + message;
            }
            
            conversationHistories.get(chatKey).add(message);
    
            display.append(message + "\n");
            display.setCaretPosition(display.getDocument().getLength()); 
            NotificationManager.notifyUser("New message for " + username + ": " + message);
        }
    }

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
        recipientDropdown.addItem("All");
        for (ChatWindow window : windows) {
            if (!window.getUsername().equals(username)) {
                recipientDropdown.addItem(window.getUsername());
            }
        }
        for (String groupChat : groupChats) {
            recipientDropdown.addItem(groupChat);
        }
    }
}