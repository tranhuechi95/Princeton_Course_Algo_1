import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints2 {
    private int noOfSegments = 0;
    private LineSegment[] segmentsResult;

    // finds all line segments containing 4 points
    public BruteCollinearPoints2(Point[] points) { // this is the constructor
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Cannot be null");
            }
        }
        for (int i = 0; i < points.length; i++) {
            for (int k = i + 1; k < points.length; k++) {
                if (points[i].compareTo(points[k]) == 0) {
                    throw new IllegalArgumentException("Cannot have repeated points");
                }
            }
        }
        // examine 4 points at a time -> initially use 4 for loops
        LineSegmentWrapper[] collinearPointsEnd = new LineSegmentWrapper[points.length];
        segmentsResult = new LineSegment[points.length];
        /* 1) Loop through all the pairs of points
           2) Calculate their slope
           3) Loop through the array, find all the points that form the same gradient
           4) Add the points to the local collinear array (unique to each gradient)
           5) The attribute collinearPointsEnd will be added with two ends of the linesegment
           6) Find the ends of the array (found linesegment) (using Min and Max method)
           7) Loop through the collinearPointsEnd array, if the ends of the LS are alr inside
            a) do not add the ends
            b) else add in the ends (check the array capacity, if not enough, double the size, increase noOfSegments count
           8) Resize the collinearPointsEnd array
         */

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                Point[] collinear = new Point[points.length];
                collinear[0] = points[i];
                collinear[1] = points[j];
                int countPair = 2;
                // StdOut.println("Considering points " + collinear[0] + " and " + collinear[1]);
                for (int k = 0; k < points.length; k++) {
                    if (i != k && j != k &&
                            Double.compare(points[i].slopeTo(points[k]),
                                           points[i].slopeTo(points[j])) == 0) {
                        // StdOut.println("Adding point " + points[k] + " to the segment.");
                        collinear[countPair] = points[k];
                        countPair += 1;
                    }
                }
                Point max = max(collinear, countPair);
                Point min = min(collinear, countPair);
                if (countPair >= 4) {
                    LineSegmentWrapper lineSegmentEnds = new LineSegmentWrapper(max, min);
                    int flag2 = 1;
                    for (int m = 0; m < collinearPointsEnd.length; m++) {
                        if (collinearPointsEnd[m] == null)
                            break;
                        if (lineSegmentEnds
                                .compareTo(collinearPointsEnd[m])) { // will use the wrapper
                            flag2 = 0;
                            break;
                        }
                    }
                    if (flag2 == 1) {
                        if (collinearPointsEnd.length <= noOfSegments) {
                            collinearPointsEnd = resize(collinearPointsEnd, 2 * noOfSegments);
                        }
                        collinearPointsEnd[noOfSegments] = lineSegmentEnds;
                        segmentsResult[noOfSegments] = new LineSegment(max, min);
                        noOfSegments += 1;
                    }
                }
            }
        }
        segmentsResult = resize(segmentsResult, noOfSegments);
    }

    private Point max(Point[] array, int countPair) {
        Point maxPoint = array[0];
        for (int i = 0; i < countPair; i++) {
            if (maxPoint.compareTo(array[i]) < 0) {
                maxPoint = array[i];
            }
        }
        return maxPoint;
    }

    private Point min(Point[] array, int countPair) {
        Point minPoint = array[0];
        for (int i = 0; i < countPair; i++) {
            if (minPoint.compareTo(array[i]) > 0) {
                minPoint = array[i];
            }
        }
        return minPoint;
    }

    // resize for LineSegmentWrapper
    private LineSegmentWrapper[] resize(LineSegmentWrapper[] array, int capacity) {
        LineSegmentWrapper[] copySegment = new LineSegmentWrapper[capacity];
        for (int j = 0; j < capacity; j++) {
            copySegment[j] = array[j];
        }
        array = copySegment;
        return array;
    }

    private LineSegment[] resize(LineSegment[] array, int capacity) {
        LineSegment[] copySegment = new LineSegment[capacity];
        for (int j = 0; j < capacity; j++) {
            copySegment[j] = array[j];
        }
        array = copySegment;
        return array;
    }

    // Linesegment wrapper
    private class LineSegmentWrapper {
        private final Point p;   // one endpoint of this line segment
        private final Point q;   // the other endpoint of this line segment

        public LineSegmentWrapper(Point p, Point q) {
            if (p == null || q == null) {
                throw new NullPointerException("argument is null");
            }
            this.p = p;
            this.q = q;
        }

        public Point getP() {
            return p;
        }

        public Point getQ() {
            return q;
        }

        public boolean compareTo(LineSegmentWrapper that) {
            return that.q.compareTo(this.q) == 0 && that.p.compareTo(this.p) == 0;
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return noOfSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        return segmentsResult;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints2 collinear = new BruteCollinearPoints2(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
