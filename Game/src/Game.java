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
    int carWidth = 40;
    int carHeight = 60;
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
    int hCarWidth = 50;
    int hCarHeight = 82;
    int hCarX = 0;
    int hCarY = -50;

    class HCar {
        int x = hCarX;
        int y = hCarY;
        int width = hCarWidth;
        int height = hCarHeight;
        int velocityY;
        Image img;
        boolean passed = false;

        HCar(Image img) {
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

    ArrayList<HCar> hCars;
    Random random = new Random();

    Timer gameLoop;
    Timer placeHostileCarsTimer;
    Timer decreaseDelayTime;
    int placingCarsDelay = 1500;

    boolean gameOver = false;
    int score = 0;

    Game() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        backgroundImage = new ImageIcon(getClass().getResource("./Gif.jfif")).getImage();
        playerCar = new ImageIcon(getClass().getResource("./UserCar2.png")).getImage();
        hostileCarImage = new ImageIcon(getClass().getResource("/EnemyCarv2.png")).getImage();

        car = new Car(playerCar);
        hCars = new ArrayList<HCar>();

        //place cars timer

        placeHostileCarsTimer = new Timer(placingCarsDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeCars();
            }
        });

        placeHostileCarsTimer.start();

        //game loop
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void placeCars() {
        int randomCarX = (int) (hCarX + boardWidth-hCarWidth - Math.random()*(boardWidth-hCarWidth));

        // !!! still needs fixing
        for (int i = 0; i < hCars.size(); i++) {
            while  (hCars.get(i).x <= randomCarX && randomCarX <= hCars.get(i).x + hCarWidth ||
                 randomCarX <= hCars.get(i).x && hCars.get(i).x <= randomCarX + hCarWidth) {
                randomCarX = (int) (hCarX + boardWidth-hCarWidth - Math.random()*(boardWidth-hCarWidth));
            }
        }
        HCar hCar = new HCar(hostileCarImage);
        hCar.x = randomCarX;
        hCar.velocityY = (int) (2 + score/5 + Math.random() * 4);
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

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game over: " + String.valueOf(score), 10, 35);
        } else {
            g.drawString("Score: " + String.valueOf(score), 10, 32);
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
            hostileCar.y += hostileCar.velocityY;
            

            if (!hostileCar.passed && car.y + car.height < hostileCar.y) {
                hostileCar.passed = true;
                score += 1;
            }

            hCarOutOfBounds(hostileCar);

            if (collision(car, hostileCar)) {
                gameOver = true;
            }
        }
    }

    public boolean collision(Car a, HCar b) {
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    public void hCarOutOfBounds(HCar b) {
        if(b.y > boardHeight) {
            hCars.remove(b);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver) {
            gameLoop.stop();
            placeHostileCarsTimer.stop();
        }
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
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (gameOver) {
                car.y = carY;
                car.x = carX;
                hCars.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placeHostileCarsTimer.start();
            }
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
