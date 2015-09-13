package org.tomblobal.sf;

import javax.swing.*;
import java.awt.*;

public class WordCanvas implements WordListener{

    public JLabel label;
    public WordCanvas() {
        initComponents();
    }

    private void initComponents() {
        label = new JLabel("Listening...");
        label.setPreferredSize(new Dimension(1000, 1000));
        label.setFont(new Font("Serif", Font.BOLD, 36));

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
    }

    public void onWordChange(String word) {
        SwingUtilities.invokeLater(() -> label.setText(word));
    }
}
