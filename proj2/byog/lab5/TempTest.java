package byog.lab5;
import byog.Core.WorldGen;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

public class TempTest {
    private static class Coordinate{
        public int x;
        public int y;
        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public boolean equals(Object other) {
            if (((Coordinate) other).x == x && ((Coordinate) other).y == y) {
                return true;
            } else {
                return false;
            }
        }
        @Override
        public int hashCode() {
            return Objects.hash(x,y);
        }
        @Override
        public String toString() {
            return ("x:"+x+"   y:"+y);
        }
    }
    public static void main(String[] args) {
        System.out.println(Long.parseLong("43423"));





    }
}
