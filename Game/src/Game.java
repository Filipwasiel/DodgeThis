import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 500;
    int boardHeight = 600;

    //Images

    Image backgroundImage;
    Image playerCar;

    //Car
    int carWidth = 54;
    int carHeight = 114;
    int carX = boardWidth/2;
    int carY = boardHeight/2;

    class Car {
        int x = carX;
        int y = carY;
        int velocityX = 0;
        int velocityY = 0;
        int width = carWidth;
        int height = carHeight;
        Image img;

        Car(Image img) {
            this.img = img;
        }
    }

    //Game logic

    Car car;
    int engineBreakingSpeed = 2;
    int maneuverSpeed = 8;
    int forwardSpeed = -6;
    int breakingSpeed = 4;
    boolean isSpeeding = false;
    boolean isBreaking = false;
    boolean isTurningLeft = false;
    boolean isTurningRight = false;

    Timer gameLoop;

    Game() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        backgroundImage = new ImageIcon(getClass().getResource("./Gif.jfif")).getImage();
        playerCar = new ImageIcon(getClass().getResource("./UserCar.png")).getImage();

        car = new Car(playerCar);

        //game loop
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);
        g.drawImage(car.img, car.x, car.y, car.width, car.height, null);
    }

    public void move() {

        //Moving upright
        car.velocityY = engineBreakingSpeed;
        if(isSpeeding) {
            if(car.y <= 0) {
                car.velocityY = 0;
            } else {
                car.velocityY = forwardSpeed;
            }
        } else if (isBreaking) {
            if(car.y >= boardHeight-carHeight) {
                car.velocityY = 0;
            } else {
                car.velocityY = breakingSpeed;
            }
        }
        if(car.y <= 0) {
            car.velocityY = engineBreakingSpeed;
        } else if(car.y >= boardHeight-carHeight && !isSpeeding) {
            car.velocityY = 0;
        }
        car.y += car.velocityY;
        
        //Moving horizontally
        if(isTurningLeft) {
            car.velocityX = maneuverSpeed;
            if(car.x <= 0) {
                return;
            } else {
                car.x -= car.velocityX;
            }
        } else if (isTurningRight) {
            car.velocityX = maneuverSpeed;
            if(car.x >= boardWidth-carWidth) {
                return;
            } else {
                car.x += car.velocityX;
            }
        }
        if (car.x >= boardWidth-carWidth || car.x <= 0) {
            car.velocityX = 0;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
            isSpeeding = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
            isBreaking = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
            isTurningLeft = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            isTurningRight = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
            isSpeeding = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
            isBreaking = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
            isTurningLeft = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            isTurningRight = false;
        }
    }
}
