import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EightBallGame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextArea resultArea;
//different responses
    private String[] responses = {
        "It is certain",
        "It is decidedly so",
        "Without a doubt",
        "Yes – definitely",
        "You may rely on it",
        "As I see it, yes",
        "Most likely",
        "Outlook good",
        "Yes",
        "Signs point to yes",
        "Reply hazy, try again",
        "Ask again later",
        "Better not tell you now",
        "Cannot predict now",
        "Concentrate and ask again",
        "Don't count on it",
        "My reply is no",
        "My sources say no",
        "Outlook not so good",
        "Very doubtful"
    };

    public EightBallGame(String title) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Serif", Font.BOLD, 16));
        getContentPane().add(resultArea, BorderLayout.CENTER);

        JButton askButton = new JButton("Ask the 8-Ball");
        askButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int responseIndex = (int) (Math.random() * responses.length);
                resultArea.setText(responses[responseIndex]);
            }
        });
        getContentPane().add(askButton, BorderLayout.SOUTH);
    }
}