import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {


    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     *
     * @param g       The graph to use.
     * @param stlon   The longitude of the start location.
     * @param stlat   The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        ArrayList<Long> solution = new ArrayList<>();
        HashMap<Long, Double> cacheHeuristics = new HashMap<>();
        long start = g.closest(stlon, stlat);
        long destination = g.closest(destlon, destlat);
        double dis;

        PriorityQueue<GraphDB.SearchNode> pq = new PriorityQueue<>();
        HashSet<Long> done = new HashSet<>();

        pq.add(g.new SearchNode(start, 0.0, null, cacheHeuristics, g, destination));

        while (!pq.isEmpty()) {
            GraphDB.SearchNode activate = pq.poll();
            if (done.contains(activate.nodeId)) {
                continue;
            }
            done.add(activate.nodeId);
            if (activate.nodeId == destination) {
                dis = activate.disFromStart;
                solution = activate.pathToInit();
                return solution;
            }
            Iterable<Long> neighbors = g.adjacent(activate.nodeId);
            for (long id : neighbors) {
                double d = g.distance(id, activate.nodeId);
                GraphDB.SearchNode s = g.new SearchNode(id, activate.disFromStart + d, activate,
                        cacheHeuristics, g, destination);
                if (s.equals(activate.parent)) {
                    continue;
                }
                pq.add(s);
            }
        }
        return solution;
    }


    private static int relBearingToDirection(double rb) {
        if (rb > -15 && rb < 15) {
            return 1;
        }
        if (rb > 15 && rb < 30) {
            return 2;
        }
        if (rb > 30 && rb < 100) {
            return 5;
        }
        if (rb > 100) {
            return 6;
        }
        if (rb < -15 && rb > -30) {
            return 3;
        }
        if (rb < -30 && rb > -100) {
            return 4;
        }
        if (rb < -100) {
            return 7;
        }
        return -1;
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     *
     * @param g     The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        ArrayList<NavigationDirection> out = new ArrayList<>();
        long lastWayId = -1;
        long lastNodeId = -1;
        double lastBearingAngle = 0.0;
        double BearingAngle = 0.0;
        int direction = 0;
        double distance = 0.0;
        String lastWayName = "";


        for (long nodeId : route) {
            if (lastNodeId != -1) {
                BearingAngle = g.bearing(lastNodeId, nodeId);
            }

            long wayId = g.NodeToWay.get(nodeId);
            String wayName = g.WayInfos.get(wayId).name;
            if (wayId != lastWayId && !lastWayName.equals(wayName)) {
                if (lastNodeId != -1) {
                    out.add(NavigationDirection.fromString(String.format("%s on %s and continue for %.3f miles.",
                            NavigationDirection.DIRECTIONS[direction], lastWayName, distance)));

                    double relBearing = BearingAngle - lastBearingAngle;
                    direction = relBearingToDirection(relBearing);
                    distance = 0.0;
                }
            }

            if (lastNodeId != -1) {
                distance += g.distance(lastNodeId, nodeId);
            }

            lastWayName = wayName;
            lastNodeId = nodeId;
            lastBearingAngle = BearingAngle;
            lastWayId = wayId;
        }

        long finalNodeId = route.get(route.size() - 1);
        long finalWayId = g.NodeToWay.get(finalNodeId);
        String wayName = g.WayInfos.get(finalWayId).name;
        out.add(NavigationDirection.fromString(String.format("%s on %s and continue for %.3f miles.",
                NavigationDirection.DIRECTIONS[direction], wayName, distance)));
        //System.out.println("add one after for-loop");
        return out;
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /**
         * Integer constants representing directions.
         */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /**
         * Number of directions supported.
         */
        public static final int NUM_DIRECTIONS = 8;

        /**
         * A mapping of integer values to directions.
         */
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /**
         * Default name for an unknown way.
         */
        public static final String UNKNOWN_ROAD = "unknown road";

        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /**
         * The direction a given NavigationDirection represents.
         */
        int direction;
        /**
         * The name of the way I represent.
         */
        String way;
        /**
         * The distance along this way I represent.
         */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         *
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                        && way.equals(((NavigationDirection) o).way)
                        && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
