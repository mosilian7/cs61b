package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private String enc;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40,seed);
        game.startGame();
    }

    public MemoryGame(int width, int height,int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i=0;i<n;i+=1) {
            int r = rand.nextInt(26);
            sb.append(CHARACTERS[r]);
        }
        return sb.toString();
    }

    private double deviationFromCenter(int i,int length,double interval) {
        if (length%2==0) {
            return (i - (length/2))*interval + interval/2;
        } else {
            return (i - (length/2))*interval;
        }
    }

    private void drawUI(String state,String encouragement) {
        Font prevFont = StdDraw.getFont();
        Font newFont = new Font("Monaco", Font.PLAIN, 20);
        StdDraw.setFont(newFont);
        StdDraw.text(4.0,height-1.0,"Round:"+round);
        StdDraw.text(20.0,height-1.0,state);
        StdDraw.text(34.0,height-1.0,encouragement);
        StdDraw.line(0,height-2.0,width,height-2.0);
        StdDraw.setFont(prevFont);
    }
    public void drawFrame(String s) {

        StdDraw.clear();
        drawUI("Watch!",enc);
        Font font = new Font("Arial", Font.BOLD, 60);
        StdDraw.setFont(font);
        double xCenter = (double) width/2;
        double yCenter = (double) height/2;
        StdDraw.text(xCenter,yCenter,s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {

        String[] la = letters.split("");
        for (String letter:la) {
            drawFrame(letter);
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException x) {}
            StdDraw.clear();
            drawUI("Watch!",enc);
            StdDraw.show();
            try {
                Thread.currentThread().sleep(500);
            } catch (InterruptedException x) {}
        }
    }

    public String solicitNCharsInput(int n) {
        StdDraw.clear();
        drawUI("Type!",enc);
        StdDraw.text((double) width/2,(double) height/2,"Type your answer");
        StdDraw.show();
        StdDraw.setPenColor(StdDraw.GRAY);
        StringBuilder sb = new StringBuilder(n);
        StringBuilder show = new StringBuilder(n);
        int length = 0;
        while (n>0) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                sb.append(c);
                show.append(c);
                n -= 1;
                length += 1;
                if (length > 12)
                    show.deleteCharAt(0);
                drawFrame(show.toString());
            }


        }
        StdDraw.setPenColor(StdDraw.BLACK);
        return sb.toString();
    }

    public void startGame() {
        boolean pass = true;
        round = 1;
        double xCenter = (double) width/2;
        double yCenter = (double) height/2;
        Font font = new Font("Arial", Font.BOLD, 60);
        StdDraw.setFont(font);

        while (pass) {
            enc = ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)];
            StdDraw.clear();
            drawUI("Watch!",enc);
            StdDraw.text(xCenter,yCenter,"Round:"+round);
            StdDraw.show();

            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException x) {}


            String answer = generateRandomString(round);
            flashSequence(answer);
            String get = solicitNCharsInput(round);
            if (get.equals(answer)) {
                round += 1;
            } else {
                pass = false;
                String info = "You made it to round:" ;

                StdDraw.clear();
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.text(xCenter,yCenter+6,"Game Over!");
                StdDraw.text(xCenter,yCenter,info);
                StdDraw.text(xCenter,yCenter-6,String.valueOf(round));
                StdDraw.show();
            }

            try {
                Thread.currentThread().sleep(500);
            } catch (InterruptedException x) {}
        }
    }

}
