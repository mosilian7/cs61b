package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class HexWorldLauncher {
    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;
    private static final int[] DIRECTIONS = new int[]{0,1,1,2,4,4,4,3,1,1,1,1,3,4,4,4,2,1,1};
    private static final Object[] TileStyles =  new Object[]{Tileset.GRASS,Tileset.GRASS,
            Tileset.MOUNTAIN,Tileset.GRASS,Tileset.MOUNTAIN,Tileset.MOUNTAIN,Tileset.FLOWER,Tileset.MOUNTAIN,
            Tileset.MOUNTAIN,Tileset.MOUNTAIN,Tileset.MOUNTAIN,Tileset.TREE,Tileset.FLOWER,Tileset.SAND,
            Tileset.TREE,Tileset.MOUNTAIN,Tileset.SAND,Tileset.TREE,Tileset.FLOWER};

    private static int size = 3;

    public static void main(String[] args) {


        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);


        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        int x0 = 3;
        int y0 = 7;
        for (int i=0;i<19;i+=1) {
            x0 = HexWorld.nextX(x0,DIRECTIONS[i],size);
            y0 = HexWorld.nextY(y0,DIRECTIONS[i],size);
            TETile nowStyle = (TETile) TileStyles[i];
            if (nowStyle.equals((Object) Tileset.MOUNTAIN) || nowStyle.equals((Object) Tileset.SAND)) {
                HexWorld.addHexagon(world,x0,y0,nowStyle,size,20,20,20);
            } else {
                HexWorld.addHexagon(world,x0,y0,nowStyle,size);
            }
        }
        ter.renderFrame(world);
    }
}
