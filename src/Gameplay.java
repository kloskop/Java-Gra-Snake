import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Random;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

    private int[] snakeXLength = new int[750];
    private int[] snakeYLength = new int[750];
    private int lengthOfSnake = 3;

    private int moves =0;

    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;

    private ImageIcon rightMouth;
    private ImageIcon leftMouth;
    private ImageIcon downMouth;
    private ImageIcon upMouth;
    private ImageIcon snakeImage;

    private Timer timer;
    private int delay = 100;


    private ImageIcon titleImage;

    private ImageIcon enemyImage;
    private int[] enemyXPos={25,50,75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550,575,600,625,650,675,700,725,750,775,800,825,850}; // JPRDL XD
    private int[] enemyYPos={75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550,575,600,625};
    private Random randomizer = new Random();
    private int xPos = randomizer.nextInt(enemyXPos.length);
    private int yPos = randomizer.nextInt(enemyYPos.length);

    private int score = 0;

    public Gameplay(){ //Konstruktor
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay,this);
        timer.start();
    }

    public void paint(Graphics g) //metoda rysująca w oknie
    {

        if (moves == 0) { //Początkowa pozycja węża
            snakeXLength[2] = 50;
            snakeXLength[1] = 75;
            snakeXLength[0] = 100;

            snakeYLength[2] = 100;
            snakeYLength[1] = 100;
            snakeYLength[0] = 100;


        }

        // granice tytułu
        g.setColor(Color.white);
        g.drawRect(24, 10, 851, 55);
        // obraz tytułowy
        titleImage = new ImageIcon("snaketitle.jpg");
        titleImage.paintIcon(this, g, 25, 11);
        //granice planszy
        g.setColor(Color.WHITE);
        g.drawRect(24, 74, 851, 577);
        //Tło planszy
        g.setColor(Color.black);
        g.fillRect(25, 75, 850, 575);
        //ustawienie gracza
        rightMouth = new ImageIcon("rightmouth.png");
        rightMouth.paintIcon(this, g, snakeXLength[0], snakeYLength[0]);
        //wyświetlanie wyników
        g.setColor(Color.white);
        g.setFont(new Font("system", Font.PLAIN, 20));
        g.drawString("Wynik: " + score, 760, 35);
        //wyświetlanie długości węża
        g.setColor(Color.white);
        g.setFont(new Font("system", Font.PLAIN, 20));
        g.drawString("Długość: " + lengthOfSnake, 760, 55);

        for (int x = 0; x < lengthOfSnake; x++) { //Rysowanie węża
            if (x == 0 && right) {
                rightMouth = new ImageIcon("rightmouth.png");
                rightMouth.paintIcon(this, g, snakeXLength[x], snakeYLength[x]);
            }
            if (x == 0 && left) {
                leftMouth = new ImageIcon("leftmouth.png");
                leftMouth.paintIcon(this, g, snakeXLength[x], snakeYLength[x]);
            }
            if (x == 0 && up) {
                upMouth = new ImageIcon("upmouth.png");
                upMouth.paintIcon(this, g, snakeXLength[x], snakeYLength[x]);
            }
            if (x == 0 && down) {
                downMouth = new ImageIcon("downmouth.png");
                downMouth.paintIcon(this, g, snakeXLength[x], snakeYLength[x]);
            }
            if (x != 0) {
                snakeImage = new ImageIcon("snakeimage.png");
                snakeImage.paintIcon(this, g, snakeXLength[x], snakeYLength[x]);
            }
            try {
                String soundName = "Move1.wav";
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            }
            catch (Exception exc) {
                System.err.println(exc.getMessage());
            }

        }

        enemyImage = new ImageIcon("enemy.png");

        if (enemyXPos[xPos] == snakeXLength[0] && enemyYPos[yPos] == snakeYLength[0]) {
            score += 10;
            lengthOfSnake++;
            xPos = randomizer.nextInt(enemyXPos.length);
            yPos = randomizer.nextInt(enemyYPos.length);

            try {
                String soundName = "Eat.wav";
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            }
            catch (Exception exc) {
                System.err.println(exc.getMessage());
            }
        }

        enemyImage.paintIcon(this, g, enemyXPos[xPos], enemyYPos[yPos]);

        for (int c = 1; c < lengthOfSnake; c++) {
            if (snakeXLength[c] == snakeXLength[0] && snakeYLength[c] == snakeYLength[0]) { //detekcja kolizji z samym sobą
                right = left = up = down = false;

                g.setColor(Color.white);
                g.setFont(new Font("Sytem", Font.BOLD, 50));
                g.drawString("KONIEC GRY", 300, 300);

                g.setFont(new Font("Sytem", Font.BOLD, 25));
                g.drawString("Naciśnij SPACJĘ, żeby zacząć od nowa", 240, 340);

                try {
                    String soundName = "Gameover.wav";
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                }
                catch (Exception exc) {
                    System.err.println(exc.getMessage());
                }
            }




        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if(right){
            for(int r = lengthOfSnake-1;r>=0;r--){
                snakeYLength[r+1]=snakeYLength[r];
            }
            for(int r = lengthOfSnake;r>=0;r--){
                if(r==0){
                    snakeXLength[r]=snakeXLength[r]+25;
                }
                else{
                    snakeXLength[r]=snakeXLength[r-1];
                }
                if(snakeXLength[r]>850){
                    snakeXLength[r]=25;
                }

            }
            repaint();
        }

        if(left) {
            for (int r = lengthOfSnake - 1; r >= 0; r--) {
                snakeYLength[r + 1] = snakeYLength[r];
            }
            for (int r = lengthOfSnake; r >= 0; r--) {
                if (r == 0) {
                    snakeXLength[r] = snakeXLength[r] - 25;
                } else {
                    snakeXLength[r] = snakeXLength[r - 1];
                }
                if (snakeXLength[r] < 25) {
                    snakeXLength[r] = 850;
                }

            }
            repaint();
        }

        if(down) {
            for (int r = lengthOfSnake - 1; r >= 0; r--) {
                snakeXLength[r + 1] = snakeXLength[r];
            }
            for (int r = lengthOfSnake; r >= 0; r--) {
                if (r == 0) {
                    snakeYLength[r] = snakeYLength[r] + 25;
                } else {
                    snakeYLength[r] = snakeYLength[r - 1];
                }
                if (snakeYLength[r] > 625) {
                    snakeYLength[r] = 75;
                }

            }
            repaint();
        }

        if(up) {
            for (int r = lengthOfSnake - 1; r >= 0; r--) {
                snakeXLength[r + 1] = snakeXLength[r];
            }
            for (int r = lengthOfSnake; r >= 0; r--) {
                if (r == 0) {
                    snakeYLength[r] = snakeYLength[r] - 25;
                } else {
                    snakeYLength[r] = snakeYLength[r - 1];
                }
                if (snakeYLength[r] < 75) {
                    snakeYLength[r] = 625;
                }

            }
            repaint();
        }




    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            moves++;
            right = true;
            if(!left){
                right=true;
            }
            else{
                right=false;
                left=true;
            }
                up=false;
                down=false;
            }

        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            moves++;
            left = true;
            if(!right){
                left=true;
            }
            else{
                left=false;
                right=true;
            }
            up=down=false;
        }

        if(e.getKeyCode()==KeyEvent.VK_UP){
            moves++;
            up = true;
            if(!down){
                up=true;
            }
            else{
                up=false;
                down=true;
            }
            left=right=false;
        }

        if(e.getKeyCode()==KeyEvent.VK_DOWN){
            moves++;
            down = true;
            if(!up){
                down=true;
            }
            else{
                down=false;
                up=true;
            }
            left=right=false;
        }
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            moves=0;
            score=0;
            lengthOfSnake=3;
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }
    @Override
    public void keyReleased(KeyEvent e) { }

}
