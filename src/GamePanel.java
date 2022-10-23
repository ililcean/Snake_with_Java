import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;                                                //Size of in-game objects
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT) / UNIT_SIZE;         //Size of in-game units
    static final int DELAY = 75;                                                    //Bigger => slower game speed
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];                                            //Both arrays hold snake coordinates
    int bodyParts = 6;                                                              //Starting body parts
    int applesEaten;                                                                //Score
    int appleX;
    int appleY;                                                                     //Coordinates of new apple
    char direction = 'R';                                                           //Snake direction (on start - right)
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel (){
        random = new Random();

        //Specifies game window
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);

        //Starts listening for key presses
        this.addKeyListener(new MyKeyAdapter());

        startGame();
    }

    //Component that starts the game
    public void startGame() {
        //add an apple upon game start
        newApple();
        running = true;

        //sets and starts timer (we use this because we implement a listener!)
        timer = new Timer(DELAY,this);
        timer.start();
    }

    //Is auto-called by KeyListener
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {

            /*
            //Turns on game grid (uncomment if needed)
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            */

            //Draw apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            //Draw snake
            for (int i = 0; i < bodyParts; i++) {
                //Paint the head of the snake
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                //Paint the body of the snake
                else {
                    g.setColor(new Color(0, 153, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            //Score Text in-game
            g.setColor(Color.RED);
            g.setFont(new Font("Helvetica", Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, ((SCREEN_WIDTH- metrics.stringWidth("Score:" + applesEaten)) / 2), g.getFont().getSize());
        }
        else gameOver(g);
    }

    //Adds new apples randomly
    public void newApple(){
        //Determine apple coordinates (multiply with UNIT_SIZE for even apple distribution in game grid)
        appleX = random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }

    //Moves the snake body and head
    public void move() {

        //Shifts BODY parts from the back
        for(int i = bodyParts; i>0; i--){
            //Moves body parts one by one
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        //Switch for direction. Changes position of HEAD element in array according to direction
        switch (direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    //Checks for apples on tile
    public void checkApple(){

        //If there is an apple "eat" it, add to snake and create new apple
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    //checks for collisions
    public void checkCollisions(){

        //Check if head collides with body
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i])&&(y[0]==y[i])){
                running = false;
            }
        }

        //Check if head collides with ->
        if(x[0] < 0) running = false;                                       //Left Border
        if(x[0] > SCREEN_WIDTH) running = false;                            //Right Border
        if(y[0] < 0) running = false;                                       //Top Border
        if(y[0] > SCREEN_HEIGHT) running = false;                           //Bottom Border

        if(!running) timer.stop();
    }

    //Ends game
    public void gameOver(Graphics g){
        //Score Text post
        g.setColor(Color.RED);
        g.setFont(new Font("Helvetica", Font.BOLD, 30));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, ((SCREEN_WIDTH- metrics1.stringWidth("Score:" + applesEaten)) / 2), g.getFont().getSize());

        //Game Over Text
        g.setColor(Color.RED);
        g.setFont(new Font("Helvetica", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", ((SCREEN_WIDTH- metrics2.stringWidth("Game Over")) / 2), SCREEN_HEIGHT/2);



    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    //Gets Inputs from Keyboard
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){

            //Takes Key Input (e) and controls direction. If clauses to avoid 180 degrees turn => auto game over
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
