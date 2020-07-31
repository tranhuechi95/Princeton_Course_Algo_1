import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET { // Brute force
    private final SET<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        pointSet = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.size() == 0;
    }

    // number of points in the set
    public int size() {
        return pointSet.size(); // method size() of class SET
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Cannot insert null point");
        }
        pointSet.add(p); // method add() of class SET
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Cannot find null point");
        }
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : pointSet) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Cannot find range for null rectangle");
        }
        // create a new Set to store these points
        SET<Point2D> insideSet = new SET<Point2D>();
        insideCheck(rect, pointSet, insideSet);
        return insideSet;
    }

    private void insideCheck(RectHV rect, SET<Point2D> givenPointSet, SET<Point2D> outputSet) {
        /* this function will check for whether each point in the givenPointSet is in the rect
        add the point into the outputSet if it is */
        for (Point2D point : givenPointSet) {
            if (rect.contains(point)) {
                // add in the point
                outputSet.add(point);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Cannot find nearest for null point");
        }
        if (!isEmpty()) {
            Point2D min = null; // initialize a point
            for (Point2D point : pointSet) {
                if (min == null
                        || Double.compare(point.distanceSquaredTo(p), min.distanceSquaredTo(p))
                        < 0) {
                    min = point; // update min point
                }
            }
            return min;
        }
        else return null;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }

        // process range search queries
        StdDraw.enableDoubleBuffering();
        while (true) {

            // StdOut.println("collecting location of the mouse");
            // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            Point2D query = new Point2D(x, y);

            // StdOut.println("drawing all the points");
            // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            brute.draw();

            // StdOut.println("drawing the nearest neighbor");
            // draw in red the nearest neighbor (using brute-force algorithm)
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.RED);
            brute.nearest(query).draw();
            StdDraw.setPenRadius(0.02);

            StdDraw.show();
            StdDraw.pause(20);
        }
    }
}
