import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static int SCREEN_WIDTH = 1000;
    static int SCREEN_HEIGHT = 600;
    static int UNIT_SIZE = 20;
    static int GAME_UNITS = SCREEN_WIDTH*SCREEN_HEIGHT/UNIT_SIZE;
    static int EASY = 100;
    static int MEDIUM = 80;
    static int HARD = 60;
    static int VERY_HARD = 40;
    int x[];
    int y[];
    int bodyParts;
    int applesEaten;
    int appleX;
    int appleY;
    char direction;
    boolean running;
    boolean gameover;
    Timer timer;
    Random random;

    GamePanel(){
        this.random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.startGame();
    }
    public void startGame(){
        this.newApple();
        this.running = true;
        this.gameover = false;
        this.x = new int[GAME_UNITS];
        this.y = new int[GAME_UNITS];
        this.bodyParts = 1;
        this.applesEaten = 1;
        this.direction = 'R';
        this.timer = new Timer(EASY,this);
        this.timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.draw(g);
    }
    public void draw(Graphics g){
        /*
        for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
        }
        for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }
        */
        g.setColor(Color.red);
        g.fillOval(this.appleX, this.appleY, UNIT_SIZE, UNIT_SIZE);

        for (int i = 0; i < this.bodyParts; i++) {
            if (i == 0) {
                g.setColor(Color.blue);
                g.fillRect(this.x[0], this.y[0], UNIT_SIZE, UNIT_SIZE);
            } else {
                g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                g.fillRect(this.x[i], this.y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + this.applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + this.applesEaten)) / 2, g.getFont().getSize());
    }
    public void newApple(){
        this.appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        this.appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }
    public void move(){
        for (int i = bodyParts;i>0;i--){
            this.x[i] = this.x[i-1];
            this.y[i] = this.y[i-1];
        }
        switch(this.direction) {
            case 'U':
                int tmp = this.y[0] - UNIT_SIZE;
                if (tmp>=0){
                    this.y[0] = tmp;
                }
                else{
                    this.y[0] = SCREEN_HEIGHT - UNIT_SIZE;
                }
                break;
            case 'D':
                tmp = this.y[0] + UNIT_SIZE;
                if (tmp<SCREEN_HEIGHT){
                    this.y[0] = tmp;
                }
                else {
                    this.y[0] = 0;
                }
                break;
            case 'L':
                tmp = this.x[0] - UNIT_SIZE;
                if (tmp>=0){
                    this.x[0] = tmp;
                }
                else{
                    this.x[0] = SCREEN_WIDTH - UNIT_SIZE;
                }
                break;
            case 'R':
                tmp = this.x[0] + UNIT_SIZE;
                if (tmp<SCREEN_WIDTH){
                    this.x[0] = tmp;
                }
                else{
                    this.x[0] = 0;
                }
                break;
        }
    }
    public void checkApple(){
        if ((this.x[0]==this.appleX)&&(this.y[0]==this.appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions(){
        // checks if head collides with body
        for (int i=bodyParts;i>0;i--){
            if ((this.x[0]==this.x[i])&&(this.y[0]==this.y[i])){
                this.gameover = true;
            }
        }
        /*
        // check if head touches left border
        if (this.x[0]<0){
            this.running =false;
        }
        // check if head touches right border
        if (this.x[0]>=SCREEN_WIDTH){
            this.running =false;
        }
        // check if head touches top border
        if (this.y[0]<0){
            this.running =false;
        }
        // check if head touches bottom border
        if (this.y[0]>=SCREEN_HEIGHT){
            this.running =false;
        }
        */
    }
    public void gameOver(Graphics g){
        //Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.running && !this.gameover){
            this.move();
            this.checkApple();
            this.checkCollisions();
        }
        if (this.running && !this.gameover) {
            this.repaint();
        }
        else if (this.gameover){
            this.gameOver(this.getGraphics());
        }
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U'){
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_1:
                    timer.setDelay(EASY);
                    break;
                case KeyEvent.VK_2:
                    timer.setDelay(MEDIUM);
                    break;
                case KeyEvent.VK_3:
                    timer.setDelay(HARD);
                    break;
                case KeyEvent.VK_4:
                    timer.setDelay(VERY_HARD);
                    break;
                case KeyEvent.VK_SPACE:
                    running = !running;
                    break;
            }
        }
    }
}
