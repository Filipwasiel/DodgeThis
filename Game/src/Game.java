import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public class Game {
    int boardWidth = 400;
    int boardHeight = 450;

    JFrame frame = new JFrame("Dodge");
    JPanel board = new JPanel();

    Game() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.black);

        frame.setVisible(true);
    }
}
