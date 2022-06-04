import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

import java.util.ArrayList;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    protected HashMap<Long, ArrayList<Long>> NodeAdjs = new HashMap<>();
    protected HashMap<Long, NodeInfo> NodeInfos = new HashMap<>();
    protected HashMap<String, Long> NameToId = new HashMap<>();
    protected HashMap<Long, WayInfo> WayInfos = new HashMap<>();
    protected HashMap<Long, Long> NodeToWay = new HashMap<>();

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    protected static class NodeInfo {
        public double lat;
        public double lon;
        public String name;

        public NodeInfo(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
            this.name = "unknown place";
        }
    }

    protected static class WayInfo {
        public ArrayList<Long> nodeIds;
        public String name;

        public WayInfo(ArrayList<Long> nodeIds) {
            this.nodeIds = nodeIds;
            this.name = "unknown road";
        }
    }

    /*protected static class way {
        public ArrayList<Long> nodeIds;
        public String name;

        public way(ArrayList<Long> nodeIds) {
            this.nodeIds = nodeIds;
            this.name = "";
        }

        public way(ArrayList<Long> nodeIds, String name) {
            this.nodeIds = nodeIds;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            for (int i=0; i < nodeIds.size(); i += 1) {
                try {
                    if (!Objects.equals(nodeIds.get(i), ((way) o).nodeIds.get(i))) {
                        return false;
                    }
                } catch (IndexOutOfBoundsException e) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public int hashCode() {
            int out = 0;
            for (long id: nodeIds) {
                out = out * 31;
                out += id;
            }
            return out;
        }
    }*/

    protected class SearchNode implements Comparable<SearchNode>{
        public long nodeId;
        public double disFromStart;
        public SearchNode parent;
        public double heuristics;
        public ArrayList<Long> pathToStart = new ArrayList<>();

        public SearchNode(long id, double d, SearchNode p, HashMap<Long, Double> cacheHeuristics,
                          GraphDB gdb, long destination) {
            nodeId = id;
            disFromStart = d;
            parent = p;
            if (cacheHeuristics.containsKey(id)) {
                heuristics = cacheHeuristics.get(id);
            } else {
                heuristics = gdb.distance(id, destination);
            }
        }

        private void pathToStartHelper(SearchNode s) {
            if (s.parent == null) {
                pathToStart.add(s.nodeId);
                return;
            }
            pathToStartHelper(s.parent);
            pathToStart.add(s.nodeId);
        }

        protected ArrayList<Long> pathToInit() {
            pathToStart.clear();
            pathToStartHelper(this);
            return pathToStart;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            return (this.nodeId == ((SearchNode) o).nodeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nodeId);
        }

        @Override
        public int compareTo(SearchNode other) {
            double difference = this.disFromStart + this.heuristics - other.disFromStart - other.heuristics;
            if (difference > 0) {
                return 1;
            } else if (difference == 0) {
                return 0;
            } else if (difference < 0) {
                return -1;
            }
            return 0;
        }
    }
    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */


    private void clean() {
        // TODO: Your code here.
        ArrayList<Long> toBeRemoved = new ArrayList<>();
        for (long id: NodeAdjs.keySet()) {
            if (NodeAdjs.get(id).size() == 0) {
                toBeRemoved.add(id);
            }
        }
        for (long id: toBeRemoved) {
            NodeAdjs.remove(id);
            NodeInfos.remove(id);
        }
    }

    protected void addWayName(long wayId, String name) {
        WayInfos.get(wayId).name = name;
    }

    protected void connectAll(ArrayList<Long> nodeIds, long wayId) {
        long lastId = -1;
        for (long id: nodeIds) {
            if (lastId != -1) {
                connect(id, lastId);
            }
            lastId = id;
            NodeToWay.put(id, wayId);
        }
        WayInfos.put(wayId, new WayInfo(nodeIds));
    }

    protected void addName(long nodeId, String name) {
        NodeInfos.get(nodeId).name = name;
        NameToId.put(name, nodeId);
    }

    protected void addNode(long nodeId, NodeInfo nodeInfo) {
        NodeAdjs.put(nodeId, new ArrayList<>());
        NodeInfos.put(nodeId, nodeInfo);
    }

    protected void addNode(long nodeId, double lat, double lon) {
        NodeInfo nodeInfo = new NodeInfo(lat, lon);
        NodeAdjs.put(nodeId, new ArrayList<>());
        NodeInfos.put(nodeId, nodeInfo);
    }

    protected void addNode(long nodeId, double lat, double lon, String name) {
        NodeInfo nodeInfo = new NodeInfo(lat, lon);
        nodeInfo.name = name;
        NodeAdjs.put(nodeId, new ArrayList<>());
        NodeInfos.put(nodeId, nodeInfo);
    }

    protected void connect(Long n1, Long n2) {
        NodeAdjs.get(n1).add(n2);
        NodeAdjs.get(n2).add(n1);
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return NodeInfos.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return NodeAdjs.get(v);
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        double minDis = Double.MAX_VALUE;
        long minId = -1;
        for (long id: NodeInfos.keySet()) {
            double dis = distance(lon, lat, lon(id), lat(id));
            if (dis < minDis) {
                minDis = dis;
                minId = id;
            }
        }
        return minId;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return NodeInfos.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return NodeInfos.get(v).lat;
    }
}
