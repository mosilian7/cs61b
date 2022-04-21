package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Game implements Serializable{
    /* Feel free to change the width and height. */
    public static final int WIDTH = 60;
    public static final int HEIGHT = 40;
    public static final double xCenter = (double) WIDTH/2;
    public static final double yCenter = (double) HEIGHT/2;
    TERenderer ter = new TERenderer();
    World w;
    World.Coordinate worldCoordinate;
    HashMap<World.Coordinate,World> WorldMap = new HashMap<>();
    boolean startWithKeyboard;
    String inputStr;
    int pointer;

    public Object[] saveInfo() {
        return new Object[]{worldCoordinate,WorldMap};
    }


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT);
        startWithKeyboard = true;
        mainMenu();
    }

    private void drawMessage(Font font,Color color,double x,double y,String text) {
        if (startWithKeyboard) {
            Color prevColor = StdDraw.getPenColor();
            Font prevFont = StdDraw.getFont();
            StdDraw.setPenColor(color);
            StdDraw.setFont(font);
            StdDraw.text(x, y, text);
            StdDraw.show();
            StdDraw.setPenColor(prevColor);
            StdDraw.setFont(prevFont);
        }
    }

    private void seedErrorInfo() {
        if (startWithKeyboard) {
            StdDraw.clear(StdDraw.BLACK);
            Font font = new Font("Arial", Font.PLAIN, 30);
            drawMessage(font, StdDraw.WHITE, xCenter, yCenter + 3, "The seed is too long or too short!");
            drawMessage(font, StdDraw.ORANGE, xCenter, yCenter - 3, "PRESS ANY KEY");
        }
    }

    private void pressAnyKey() {
        while (true) {
            if (reachEnd())
                return;
            if (StdDraw.hasNextKeyTyped()) {
                StdDraw.clear(StdDraw.BLACK);
                mainMenu();
            }
        }
    }

    private void loadErrorInfo() {
        if (startWithKeyboard) {
            StdDraw.clear(StdDraw.BLACK);
            Font font = new Font("Arial", Font.PLAIN, 30);
            drawMessage(font, StdDraw.WHITE, xCenter, yCenter + 3, "No game save files found!");
            drawMessage(font, StdDraw.ORANGE, xCenter, yCenter - 3, "PRESS ANY KEY");
        }
    }

    protected void mainMenu() {
        drawMenu();
        while (true) {
            if (reachEnd())
                return;
            boolean hnkt = hasNextKeyTyped();
            if (hnkt) {
                char input = nextKeyTyped();
                if (input == 'n' || input == 'N') {
                    try {
                        seedSelection();
                    } catch (NumberFormatException x) {
                        seedErrorInfo();
                        pressAnyKey();
                    }
                } else if (input == 'q' || input == 'Q') {
                    System.exit(0);
                } else if (input == 'l' || input == 'L'){
                    Object[] saveInfo = GameSave.load();
                    if (saveInfo == null) {
                        loadErrorInfo();
                        pressAnyKey();
                    } else {
                        loadGame(saveInfo);
                        startPlaying();
                    }
                }
            }
        }
    }

    private void loadGame(Object[] saveInfo) {
        worldCoordinate =(WorldGen.Coordinate) saveInfo[0];
        WorldMap = (HashMap<World.Coordinate, World>) saveInfo[1];
        w = WorldMap.get(worldCoordinate);
        /*
         * HERE is a very strange bug.
         * The remedy is not essential, but it does work.
         */
        w.setWidth(WIDTH);w.setHeight(HEIGHT);
    }

    private boolean isNumber(char input) {
        char[] numbers = "1234567890".toCharArray();
        for (int i=0;i<10;i+=1) {
            if (numbers[i] == input) {
                return true;
            }
        }
        return false;
    }

    private long seedCalculator(World w,World.Coordinate cd) {
        return w.SEED + cd.x + 114514L *cd.y;
    }

    protected void seedSelection() {
        StringBuilder sb = new StringBuilder();
        while (true) {
            if (reachEnd())
                return;
            boolean hnkt = hasNextKeyTyped();
            if (hnkt) {
                char input = nextKeyTyped();
                if (input == 's' || input == 'S') {
                    long seed = Long.parseLong(sb.toString());
                    w = new World(WIDTH,HEIGHT,seed);
                    worldCoordinate = w.new Coordinate(0,0);
                    WorldMap.put(worldCoordinate,w);
                    break;
                } else if (isNumber(input)) {
                    sb.append(input);
                }
            }

            drawSeedSelection(sb.toString());
        }
        worldInit();
        startPlaying();
    }

    protected void drawSeedSelection(String seed) {
        if (startWithKeyboard) {
            StdDraw.clear(StdDraw.BLACK);
            Color prevColor = StdDraw.getPenColor();
            StdDraw.setPenColor(StdDraw.WHITE);
            Font prevFont = StdDraw.getFont();
            Font font = new Font("Arial", Font.PLAIN, 30);
            StdDraw.setFont(font);
            StdDraw.text(xCenter, yCenter + 5, "Type your seed:");
            StdDraw.text(xCenter, yCenter - 5, "Start(S)");
            StdDraw.text(xCenter, yCenter, seed);
            StdDraw.show();
            StdDraw.setFont(prevFont);
            StdDraw.setPenColor(prevColor);
        }
    }


    protected void drawMenu() {
        if (startWithKeyboard) {
            Color prevColor = StdDraw.getPenColor();
            StdDraw.setPenColor(StdDraw.WHITE);
            Font prevFont = StdDraw.getFont();
            Font font = new Font("Arial", Font.PLAIN, 30);
            StdDraw.setFont(font);
            StdDraw.text(xCenter, yCenter, "New Game(N)");
            StdDraw.text(xCenter, yCenter - 5, "Load Game(L)");
            StdDraw.text(xCenter, yCenter - 10, "Quit(Q)");
            font = new Font("Arial", Font.BOLD, 40);
            StdDraw.setFont(font);
            StdDraw.text(xCenter, yCenter + 12, "Zelda:");
            StdDraw.text(xCenter, yCenter + 7, "the legend of tiles");
            StdDraw.show();
            StdDraw.setFont(prevFont);
            StdDraw.setPenColor(prevColor);
        }
    }


    private void drawUI(int w,int h,String tileName) {
        if (startWithKeyboard) {
            Color prevColor = StdDraw.getPenColor();
            StdDraw.setPenColor(StdDraw.GRAY);
            StdDraw.filledRectangle(3, h - 1, 1.7, 0.6);
            Font prevFont = StdDraw.getFont();
            StdDraw.setPenColor(StdDraw.WHITE);
            Font font = new Font("Arial", Font.PLAIN, 14);
            StdDraw.setFont(font);
            StdDraw.text(3, h - 1.05, tileName);
            StdDraw.show();
            StdDraw.setFont(prevFont);
            StdDraw.setPenColor(prevColor);
        }
    }

    private String getDescription(TETile[][] world,double x,double y) {
        TETile tile = world[(int) x][(int) y];
        return tile.description();
    }

    protected void worldInit() {
        w.worldGen();
        w.playerBorn();
    }

    private boolean isMoveKey(char input) {
        char[] moveKeys = "wasdWASD".toCharArray();
        for (char i:moveKeys) {
            if (input == i) {
                return true;
            }
        }
        return false;
    }

    private boolean isSaveSign(ArrayList<Character> last2Keys) {
        ArrayList<Character> saveSign = new ArrayList<>();
        saveSign.add(':');saveSign.add('q');
        ArrayList<Character> saveSign2 = new ArrayList<>();
        saveSign2.add(':');saveSign2.add('Q');
        return (last2Keys.equals(saveSign) || last2Keys.equals(saveSign2));
    }

    private void UHD() {
        if (startWithKeyboard) {
            try {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                if (w.player.equals(w.new Coordinate((int) x,(int) y))) {
                    drawUI(WIDTH, HEIGHT, "player");
                } else {
                    String tileName = getDescription(w.world(), x, y);
                    drawUI(WIDTH, HEIGHT, tileName);
                }
            } catch (ArrayIndexOutOfBoundsException x) {
                drawUI(WIDTH, HEIGHT, "");
            }
        }
    }

    protected void startPlaying() {

        if (startWithKeyboard) {
            ter.renderFrame(w.world());
            w.showPlayer();
        }

        ArrayList<Character> last2Keys = new ArrayList<>();

        while (true) {
            if (reachEnd()) {
                return;
            }
            boolean hnkt = hasNextKeyTyped();
            if (hnkt) {
                char input = nextKeyTyped();
                if (isMoveKey(input)) {
                    if (startWithKeyboard)
                        w.drawCovered();
                    w.move(input);
                    if (startWithKeyboard)
                        w.showPlayer();
                }

                last2Keys.add(input);
                if (last2Keys.size() > 2) {
                    last2Keys.remove(0);
                }
                if (isSaveSign(last2Keys)) {
                    GameSave.save(this);
                    System.exit(0);
                }
            }
            UHD();
        }
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        startWithKeyboard = false;
        inputStr = input;
        pointer = 0;
        mainMenu();
        TETile[][] out = w.world().clone();
        out[w.player.x][w.player.y] = Tileset.PLAYER;
        System.out.println(out.toString());
        return out;
    }
    private boolean reachEnd() {
        return (!startWithKeyboard) && (pointer == inputStr.length());
    }

    private boolean hasNextKey() {
        return (pointer < inputStr.length());
    }

    private char nextKey() {
        char out = inputStr.charAt(pointer);
        pointer += 1;
        return out;
    }

    private boolean hasNextKeyTyped() {
        if (startWithKeyboard) {
            return StdDraw.hasNextKeyTyped();
        } else {
            return hasNextKey();
        }
    }

    private char nextKeyTyped() {
        if (startWithKeyboard) {
            return StdDraw.nextKeyTyped();
        } else {
            return nextKey();
        }
    }


}
