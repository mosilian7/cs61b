package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;


/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    /**
     *
     * @param world Target world.
     * @param x0 x coordinate of tile on left-bottom corner.
     * @param y0 y coordinate of tile on left-bottom corner.
     * @param tileStyle tileStyle
     * @param size Size of hexagon.
     * @return The predicted performance.
     */
    public static void addHexagon(TETile[][] world,int x0,int y0, TETile tileStyle,int size) {
        int[][] coordinates = genCoordinate(x0,y0,size);
        for (int[] cd :coordinates) {
            world[cd[0]][cd[1]] = tileStyle;
        }
    }

    public static void addHexagon(TETile[][] world,int x0,int y0, TETile tileStyle,int size,int dr, int dg, int db) {
        Random r = new Random();
        int[][] coordinates = genCoordinate(x0,y0,size);
        for (int[] cd :coordinates) {
            TETile tileStyleVar = TETile.colorVariant(tileStyle,dr,dg,db,r);
            world[cd[0]][cd[1]] = tileStyleVar;
        }
    }

    private static int xStart(int x0, int y0, int y,int size) {
        if (y-y0<size) {
            return x0 - (y - y0);
        } else {
            return x0 - (y0+2*size-1-y);
        }
    }

    private static int xLength(int y0, int y,int size) {
        if (y-y0<size) {
            return size + 2*(y-y0);
        } else {
            return size + 2*(y0+2*size-1-y);
        }
    }
    private static int xEnd(int x0, int y0, int y,int size) {
        return xStart(x0, y0, y,size) + xLength(y0, y, size);
    }

    private static int[][] genCoordinate(int x0,int y0,int size) {
        int pointer = 0;
        int arraySize = 2 * size * (2 * size - 1);
        int[][] coordinates = new int[arraySize][2];
        for (int y = y0;y < y0+2*size;y+=1) {
            for (int x = xStart(x0,y0,y,size); x< xEnd(x0,y0,y,size); x+=1) {
                coordinates[pointer][0] = x;
                coordinates[pointer][1] = y;
                pointer += 1;
            }
        }
        return coordinates;
    }

    /**
     *
     * @param x0 x0
     * @param direction    1
     *                  6     2
     *                  5     3
     *                     4
     * @param size size
     * @return
     */
    public static int nextX(int x0,int direction,int size) {
        switch (direction) {
            case 1: return x0;
            case 2: return x0 + 2*size - 1;
            case 3: return x0 + 2*size - 1;
            case 4: return x0;
            case 5: return x0 - (2*size - 1);
            case 6: return x0 - (2*size - 1);
            default: return x0;
        }
    }

    public static int nextY(int y0,int direction,int size) {
        switch (direction) {
            case 1: return y0 + 2*size;
            case 2: return y0 + size;
            case 3: return y0 - size;
            case 4: return y0 - 2*size;
            case 5: return y0 - size;
            case 6: return y0 + size;
            default: return y0;
        }
    }

}
