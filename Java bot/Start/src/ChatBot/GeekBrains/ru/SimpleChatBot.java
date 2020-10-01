package ChatBot.GeekBrains.ru;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SimpleChatBot extends JFrame implements ActionListener {

    final String TITLE_OF_PROGRAM = "Chatter: simple chatbot";
    final int START_LOCATION = 200;
    final int WINDOW_WIDTH = 350;
    final int WINDOW_HEIGHT = 450;

    JTextArea dialogue;
    JCheckBox ai;
    JTextField msg;
    SimpleChatBot sbot;

    public static void main(String[] args) {
        new SimpleChatBot();
    }

    SimpleChatBot() {
        setTitle(TITLE_OF_PROGRAM);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(START_LOCATION, START_LOCATION, WINDOW_WIDTH, WINDOW_HEIGHT);

        dialogue = new JTextArea();
        dialogue.setLineWrap(false);
        JScrollPane scrollbar = new JScrollPane(dialogue);

        JPanel bp = new JPanel();
        bp.setLayout(new BoxLayout(bp, BoxLayout.X_AXIS));
        ai = new JCheckBox("AI");

        msg = new JTextField();
        msg.addActionListener(this);
        JButton enter = new JButton("Enter");
        enter.addActionListener(this);
        bp.add(ai);
        bp.add(msg);
        bp.add(enter);

        add(BorderLayout.CENTER, scrollbar);
        add(BorderLayout.SOUTH, bp);


        setVisible(true);

        sbot = new SimpleChatBot();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (msg.getText().trim().length() > 0) {
            dialogue.append(msg.getText() + "\n");
            dialogue.append(TITLE_OF_PROGRAM.substring(0, 9) + sbot.sayInReturn(msg.getText()), ai.isSelected() + "\n");
        }
        msg.setText("");
        msg.requestFocusInWindow();
    }
}
