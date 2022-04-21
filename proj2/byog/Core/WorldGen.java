package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Random;
import java.util.HashSet;
import java.util.ArrayList;

public class WorldGen implements Serializable{
    @Serial
    private static final long serialVersionUID = -3437227825535099441L;
    protected static int WIDTH;
    protected static int HEIGHT;
    protected TETile[][] world;
    protected Random RANDOM;
    protected long SEED;
    protected HashSet<Coordinate> floors = new HashSet<>(); // Records the coordinates of floors in the world.
    protected HashSet<Room> rooms = new HashSet<>(); // Records rooms in the world.
    protected HashSet<Coordinate> walls = new HashSet<>();

    public TETile[][] world() {return world;}
    public HashSet<Coordinate> floors() {return floors;}
    public HashSet<Coordinate> walls() {return walls;}

    protected void setWidth(int w) {
        WIDTH = w;
    }

    protected void setHeight(int h) {
        HEIGHT = h;
    }

    /**
     * A class of coordinates. equals() and hashCode() are overridden to ensure
     * that HashSet works well.
     */
    protected class Coordinate implements Serializable {
        @Serial
        private static final long serialVersionUID = 1231231231231L;
        public int x;
        public int y;
        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Connect a coordinate with another one. Little verbose. REWRITE?
         * @param other Another coordinate.
         * @return The route.
         */
        public HashSet<Coordinate> connectRoute(Coordinate other) {
            HashSet<Coordinate> out = new HashSet<>();
            boolean d = RANDOM.nextBoolean();
            if (d) {
                if (other.x > x) {
                    for (int i=x;i<=other.x;i+=1) {
                        out.add(new Coordinate(i,y));
                    }
                } else {
                    for (int i=other.x;i<=x;i+=1) {
                        out.add(new Coordinate(i,y));
                    }
                }
                if (other.y > y) {
                    for (int i=y;i<=other.y;i+=1) {
                        out.add(new Coordinate(other.x,i));
                    }
                } else {
                    for (int i=other.y;i<=y;i+=1) {
                        out.add(new Coordinate(other.x,i));
                    }
                }
            } else {
                if (other.x > x) {
                    for (int i=x;i<=other.x;i+=1) {
                        out.add(new Coordinate(i,other.y));
                    }
                } else {
                    for (int i=other.x;i<=x;i+=1) {
                        out.add(new Coordinate(i,other.y));
                    }
                }
                if (other.y > y) {
                    for (int i=y;i<=other.y;i+=1) {
                        out.add(new Coordinate(x,i));
                    }
                } else {
                    for (int i=other.y;i<=y;i+=1) {
                        out.add(new Coordinate(x,i));
                    }
                }
            }
            return out;
        }

        /**
         * Determine if a coordinate is isolated with the given coordinate set.
         * @param cdSet The given coordinate set.
         * @return Isolated or not.
         */
        public boolean isIsolatedFrom(HashSet<Coordinate> cdSet) {
            boolean leftIso = (!cdSet.contains(new Coordinate(x-1,y)));
            boolean rightIso = (!cdSet.contains(new Coordinate(x+1,y)));
            boolean upIso = (!cdSet.contains(new Coordinate(x,y+1)));
            boolean downIso = (!cdSet.contains(new Coordinate(x,y-1)));
            boolean ULIso = (!cdSet.contains(new Coordinate(x-1,y+1)));
            boolean URIso = (!cdSet.contains(new Coordinate(x+1,y+1)));
            boolean DRIso = (!cdSet.contains(new Coordinate(x+1,y-1)));
            boolean DLIso = (!cdSet.contains(new Coordinate(x-1,y-1)));
            return (leftIso && rightIso && upIso && downIso && !cdSet.contains(this)
                    && ULIso && URIso && DRIso && DLIso);
        }

        public boolean isConnectedWith(Coordinate other) {
            HashSet<Coordinate> explored = new HashSet<>();
            HashSet<Coordinate> exploring = new HashSet<>();
            exploring.add(this);
            while (!exploring.isEmpty()) {
                HashSet<Coordinate> nextExploring = new HashSet<>();
                for (Coordinate cd:exploring) {

                    Coordinate left = new Coordinate(cd.x-1,cd.y);
                    Coordinate right = new Coordinate(cd.x+1,cd.y);
                    Coordinate up = new Coordinate(cd.x,cd.y+1);
                    Coordinate down = new Coordinate(cd.x,cd.y-1);
                    HashSet<Coordinate> cds = new HashSet<>();
                    cds.add(left);cds.add(right);cds.add(up);cds.add(down);

                    for (Coordinate direction:cds) {
                        if (direction.equals(other)) {
                            return true;
                        } else if (floors.contains(direction) && !explored.contains(direction)) {
                            nextExploring.add(direction);
                        }
                    }
                    explored.add(cd);
                }
                exploring = nextExploring;
            }
            return false;
        }

        public Coordinate move(char input) {
            if (input == 'w' || input == 'W') {
                return new Coordinate(x,y+1);
            } else if (input == 'a' || input == 'A') {
                return new Coordinate(x-1,y);
            } else if (input == 's' || input == 'S') {
                return new Coordinate(x,y-1);
            } else if (input == 'd' || input == 'D') {
                return new Coordinate(x+1,y);
            }
            return this;
        }

        public boolean atLegalPos() {
            return !( x>=WIDTH || y>= HEIGHT || walls.contains(this)); //x<0 || y<0 || x>=WIDTH || y>= HEIGHT || walls.contains(this)
        }
        @Override
        public boolean equals(Object other) {
            if (this.getClass() != other.getClass())
                return false;
            if (this == other) return true;
            if (other == null) return false;
            return ((Coordinate) other).x == this.x && ((Coordinate) other).y == this.y;
        }
        @Override
        public int hashCode() {
            return Objects.hash(x,y);
        }
    }


    /**
     * A class of rooms.
     */
    protected class Room implements Serializable{
        public Coordinate corner;
        public int w;
        public int h;
        public HashSet<Coordinate> myFloors = new HashSet<>();
        public Room(Coordinate corner,int w,int h) {
            this.corner = corner;
            this.w = w;
            this.h = h;
            getFloors();
        }

        public void getFloors() {
            for (int dx=0; dx<w; dx+=1) {
                for(int dy=0; dy<h; dy+=1) {
                    myFloors.add(new Coordinate(corner.x+dx,corner.y+dy));
                }
            }
        }

        public boolean isOverlap(Room other) {
            for (Coordinate floor:this.myFloors) {
                if (other.myFloors.contains(floor))
                    return true;
            }
            return false;
        }

        public boolean isIsolatedFrom(Room other) {
            for (Coordinate floor:this.myFloors) {
                if (!floor.isIsolatedFrom(other.myFloors))
                    return false;
            }
            return true;
        }

        /**
         * Connect a room with another room.
         * @param other Another room.
         * @return The route.
         */
        public HashSet<Coordinate> connectRoute(Room other) {
            ArrayList<Coordinate> thisRoom = new ArrayList<>(this.myFloors);
            ArrayList<Coordinate> otherRoom = new ArrayList<>(other.myFloors);
            int r1 = RANDOM.nextInt(thisRoom.size());
            int r2 = RANDOM.nextInt(otherRoom.size());
            return thisRoom.get(r1).connectRoute(otherRoom.get(r2));
        }

        public boolean isConnectedWith(Room other) {
            return this.corner.isConnectedWith(other.corner);
        }
        @Override
        public boolean equals(Object other) {
            if (this.getClass() != other.getClass())
                return false;
            if (this == other) return true;
            if (other == null) return false;
            return ((Room) other).corner.equals(this.corner)
                    && ((Room) other).w == this.w
                    && ((Room) other).h == this.h;
        }
        @Override
        public int hashCode() {
            return Objects.hash(corner,w,h);
        }
    }



    public WorldGen(int WIDTH,int HEIGHT,long SEED) {
        world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        setWidth(WIDTH);
        setHeight(HEIGHT);
        this.SEED = SEED;
        RANDOM = new Random(SEED);
    }

    private int roomNum() {
        return 25 + RANDOM.nextInt(15);
    }

    private int roomWidth() {
        return 4 + RANDOM.nextInt(4);
    }

    private int roomHeight() {
        return 4 + RANDOM.nextInt(4);
    }

    protected void addCoordinate(Coordinate cd,TETile tileStyle) {
        world[cd.x][cd.y] = tileStyle;
    }

    /**
     * Should and must be called after everything has been generated.
     * @param Coordinates coordinate
     * @param tileStyle style of tiles
     */
    public void loadTiles(HashSet<Coordinate> Coordinates, TETile tileStyle) {
        for (Coordinate cd:Coordinates) {
            addCoordinate(cd,tileStyle);
        }
    }

    private HashSet<Coordinate> roomCornerGen() {
        HashSet<Coordinate> out = new HashSet<>();
        int roomNum = roomNum();
        for (int i=0; i<roomNum; i+=1) {
            int x = 1 + RANDOM.nextInt(WIDTH-1);
            int y = 1 + RANDOM.nextInt(HEIGHT-1);
            out.add(new Coordinate(x,y));
        }
        return out;
    }

    /**
     * When this method is done, the coordinates of floors in rooms has been recorded in "floors".
     */
    public void roomGen() {
        HashSet<Coordinate> corners = roomCornerGen();
        for (Coordinate corner:corners) {
            int w = roomWidth();
            int h = roomHeight();
            if (corner.x+w >= WIDTH || corner.y+h >= HEIGHT)
                continue; //to make sure rooms are in the world.
            Room thisRoom = new Room(corner,w,h);
            for (Room r:rooms) {
                if (!r.isIsolatedFrom(thisRoom)) {
                    thisRoom = null; //to make sure rooms don't overlap with each other.
                    break;
                }
            }
            if (thisRoom != null) {
                floors.addAll(thisRoom.myFloors);
                rooms.add(thisRoom);
            }
        }
    }

    /**
     * Should be called after roomGen().
     */
    public void hallwayGen() {
        Room prevRoom = null;

        for (Room room:rooms) {
            if (prevRoom != null && !room.isConnectedWith(prevRoom))
            //if (prevRoom != null)
                floors.addAll(prevRoom.connectRoute(room));
            prevRoom = room;
        }
    }

    public void wallGen() {
        for (int x=0; x<WIDTH; x+=1) {
            for (int y=0; y<HEIGHT; y+=1) {
                Coordinate cd = new Coordinate(x,y);
                if (!cd.isIsolatedFrom(floors) && !floors.contains(cd))
                    walls.add(cd);
            }
        }
    }

    public void worldGen() {
        roomGen();
        hallwayGen();
        wallGen();
        loadTiles(floors,Tileset.FLOOR);
        loadTiles(walls,Tileset.WALL);
    }



}
