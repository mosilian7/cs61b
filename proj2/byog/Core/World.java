package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
public class World extends WorldGen implements Serializable {
    @Serial
    private static final long serialVersionUID = 12312312312312L;
    protected Coordinate player;
    protected TETile covered;
    public World(int WIDTH, int HEIGHT, long SEED) {
        super(WIDTH, HEIGHT, SEED);
    }


    protected void drawCovered() {
        covered.draw(player.x,player.y);
    }
    protected void setPlayerAt(Coordinate cd) {
        player = cd;
        covered = world[cd.x][cd.y];
    }

    protected void setPlayerAt(int x,int y) {
        player = new Coordinate(x,y);
        covered = world[x][y];
    }

    public void showPlayer() {
        Tileset.PLAYER.draw(player.x, player.y);
        StdDraw.show();
    }

    protected void playerBorn() {
        ArrayList<Coordinate> l = new ArrayList<>(floors);
        while (true) {
            int r = RANDOM.nextInt(l.size());
            Coordinate cd = l.get(r);
            for (Room room:rooms) {
                if (room.myFloors.contains(cd)) {
                    setPlayerAt(cd);
                    return;
                }
            }
        }
    }


    public void move(char input) {
        Coordinate update = player.move(input);
        if (update.atLegalPos()) {

            player = update;

            covered = world[player.x][player.y];
        }


    }



}
