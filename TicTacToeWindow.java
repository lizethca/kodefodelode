import javax.swing.*;
import java.awt.*;
//gui
public class TicTacToeWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private TicTacToeGame game;
    private ChatWindow parentWindow;
    private JTextArea displayArea;

    public TicTacToeWindow(TicTacToeGame game, ChatWindow parentWindow, boolean isReceiving) {
        this.game = game;
        this.parentWindow = parentWindow;
        setTitle("Tic-Tac-Toe");
        setBounds(100, 100, 450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        displayArea = new JTextArea(game.getBoard());
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        if (!isReceiving) {
            JTextField inputField = new JTextField();
            inputField.addActionListener(e -> {
                String move = inputField.getText().trim();
                inputField.setText("");
                String response = game.processMove(parentWindow.getUsername(), move);
                if (response != null) {
                    displayArea.setText(response);
                    parentWindow.forwardMessage(move);
                }
            });
            add(inputField, BorderLayout.SOUTH);
        }
    }

    @Override
    public void dispose() {
        parentWindow.clearCurrentGame();
        super.dispose();
    }

    public void receiveMessage(String message) {
        if (game.isValidMove(message)) {
            String response = game.processMove(parentWindow.getUsername(), message);
            if (response != null) {
                displayArea.setText(response);
            }
        }
    }
}