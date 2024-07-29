import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Game extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 500;
    int boardHeight = 600;

    //Images

    Image backgroundImage;
    Image playerCar;
    Image hostileCarImage;

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
    //Hostile cars
    int hCarWidth = 40;
    int hCarHeight = 57;
    int hCarX = 0;
    int hCarY = -50;

    class HCar {
        int x = hCarX;
        int y = hCarY;
        int width = hCarWidth;
        int height = hCarHeight;
        Image img;
        boolean passed = false;

        HCar(Image img) {
            this.img = img;
        }
    }

    //Game logic

    Car car;
    int hostileCarSpeed = 3;
    int engineBreakingSpeed = 2;
    int maneuverSpeed = 8;
    int forwardSpeed = -6;
    int breakingSpeed = 4;
    boolean isSpeeding = false;
    boolean isBreaking = false;
    boolean isTurningLeft = false;
    boolean isTurningRight = false;

    ArrayList<HCar> hCars;
    Random random = new Random();

    Timer gameLoop;
    Timer palceHostileCarsTimer;

    Game() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        backgroundImage = new ImageIcon(getClass().getResource("./Gif.jfif")).getImage();
        playerCar = new ImageIcon(getClass().getResource("./UserCar.png")).getImage();
        hostileCarImage = new ImageIcon(getClass().getResource("/EnemyCar.png")).getImage();

        car = new Car(playerCar);
        hCars = new ArrayList<HCar>();

        //place cars timer
        palceHostileCarsTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeCars();
            }
        });
        palceHostileCarsTimer.start();

        //game loop
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void placeCars() {
        int randomCarX = (int) (hCarX + boardWidth-hCarWidth - Math.random()*(boardWidth-hCarWidth));
        HCar hCar = new HCar(hostileCarImage);
        hCar.x = randomCarX;
        hCars.add(hCar);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);
        g.drawImage(car.img, car.x, car.y, car.width, car.height, null);
        // hostile cars
        for (int i = 0; i < hCars.size(); i++) {
            HCar hostileCar = hCars.get(i);
            g.drawImage(hostileCar.img, hostileCar.x, hostileCar.y, hostileCar.width, hostileCar.height, null);
        }
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
                car.velocityX = 0;
            } else {
                car.x -= car.velocityX;
            }
        } else if (isTurningRight) {
            car.velocityX = maneuverSpeed;
            if(car.x >= boardWidth-carWidth) {
                car.velocityX = 0;
            } else {
                car.x += car.velocityX;
            }
        }
        if (car.x >= boardWidth-carWidth || car.x <= 0) {
            car.velocityX = 0;
        }

        // moving hostile cars

        for (int i = 0; i < hCars.size(); i++) {
            HCar hostileCar = hCars.get(i);
            hostileCar.y += hostileCarSpeed;
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
