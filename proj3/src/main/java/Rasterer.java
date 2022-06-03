import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private double h;
    private double w;
    private double lrlat;
    private double lrlon;
    private double ullat;
    private double ullon;
    private final double d0_lrlat = 37.82280243352756;
    private final double d0_lrlon = -122.2119140625;
    private final double d0_ullat = 37.892195547244356;
    private final double d0_ullon = -122.2998046875;
    private final double d0_disPerPix = (d0_lrlon - d0_ullon) / 256;

    public Rasterer() {
        // YOUR CODE HERE
    }

    private void parseParams(Map<String, Double> params) {
        h = params.get("h");
        w = params.get("w");
        lrlat = params.get("lrlat");
        lrlon = params.get("lrlon");
        ullat = params.get("ullat");
        ullon = params.get("ullon");
    }

    private void clipParams() {
        if (lrlat < d0_lrlat) {
            lrlat = d0_lrlat;
        }
        if (lrlon > d0_lrlon) {
            lrlon = d0_lrlon;
        }
        if (ullat > d0_ullat) {
            ullat = d0_ullat;
        }
        if (ullon < d0_ullon) {
            ullon = d0_ullon;
        }
    }

    private double disPerPix() {
        double disPerPix = (lrlon - ullon) / w;
        return disPerPix;
    }

    /*
    private int depth() {
        double disPerPix = disPerPix();
        if (disPerPix >= d0_disPerPix) {
            System.out.println("here");
            return 0;
        }
        int depth = (int) Math.ceil(Math.log(d0_disPerPix/disPerPix) / Math.log(2));
        if (depth <= 7) {
            System.out.println("or here");
            return depth;
        } else {
            return 7;
        }
    }*/

    private int depth() {
        double disPerPix = disPerPix();
        if (disPerPix >= d0_disPerPix) {
            return 0;
        }
        int depth = 0;
        while (depth < 7) {
            double dpp = d0_disPerPix / Math.pow(2,depth);
            if (dpp <= disPerPix) {
                System.out.print("dpp & disPerPix: ");
                System.out.println(dpp);
                System.out.println(disPerPix);
                return depth;
            }
            depth += 1;
        }
        return depth;
    }

    private double tileWidth(int depth) {
        return (d0_lrlon - d0_ullon) / Math.pow(2,depth);
    }

    private double tileHeight(int depth) {
        return (d0_ullat - d0_lrlat) / Math.pow(2,depth);
    }

    private double tileUllon(int depth, int x, int y) {
        double tileW = tileWidth(depth);
        return d0_ullon + x * tileW;
    }

    private double tileUllat(int depth, int x, int y) {
        double tileH = tileHeight(depth);
        return d0_ullat - y * tileH;
    }

    private int[] lonlat_to_ulxy(int depth, double lon, double lat) {
        double tileW = tileWidth(depth);
        double tileH = tileHeight(depth);
        int x = (int) Math.floor((lon - d0_ullon) / tileW);
        int y = (int) Math.floor((d0_ullat - lat) / tileH);
        return new int[]{x, y};
    }

    private int[] lonlat_to_lrxy(int depth, double lon, double lat) {
        double tileW = tileWidth(depth);
        double tileH = tileHeight(depth);
        int x = (int) Math.ceil((lon - d0_ullon) / tileW) - 1;
        int y = (int) Math.ceil((d0_ullat - lat) / tileH) - 1;
        return new int[]{x, y};
    }


    private String[][] generate_render_grid(int depth, int ulx, int uly, int lrx, int lry) {
        String[][] out = new String[lry + 1 - uly][lrx + 1 - ulx];
        for (int i = 0; i < lry + 1 - uly; i += 1) {
            for (int j=0; j < lrx + 1 - ulx; j += 1) {
                out[i][j] = String.format("d%1$d_x%2$d_y%3$d.png",
                        depth, ulx + j, uly + i);
            }
        }
        return out;
    }

    private String gridToString(String[][] render_grid) {
        String out = "[";
        for (String[] row : render_grid) {
            out += "[";
            for (String element: row) {
                out += element + ", ";
            }
            out += "]";
        }
        out += "]";
        return out;
    }


    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        System.out.println(params);

        parseParams(params);
        int d = depth();
        clipParams();


        int[] ulxy = lonlat_to_ulxy(d, ullon, ullat);
        int[] lrxy = lonlat_to_lrxy(d, lrlon, lrlat);
        int ulx = ulxy[0]; int uly = ulxy[1];
        int lrx = lrxy[0]; int lry = lrxy[1];

        String[][] render_grid = generate_render_grid(d, ulx, uly, lrx, lry);

        double raster_ul_lon = tileUllon(d, ulx, uly);
        double raster_ul_lat = tileUllat(d, ulx, uly);
        double raster_lr_lon = tileUllon(d, lrx + 1, lry + 1);
        double raster_lr_lat = tileUllat(d, lrx + 1, lry + 1);

        Map<String, Object> results = new HashMap<>();
        results.put("render_grid", render_grid);
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("depth", d);
        results.put("query_success", true);
        System.out.println(results);
        System.out.println(gridToString(render_grid));
        return results;
    }

}
