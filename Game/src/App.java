import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 500;
        int boardHeight = 600;

        JFrame frame = new JFrame("Dodge");
        frame.setSize(boardWidth, boardHeight);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Game game = new Game();
        frame.add(game);
        frame.pack();
        game.requestFocus();
        frame.setVisible(true);
    }
}
