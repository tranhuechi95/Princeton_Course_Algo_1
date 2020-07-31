import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private final int noOfSegments;
    private final LineSegment[] segmentsResult;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Array elements cannot be null");
            }
        }

        for (int i = 0; i < points.length; i++) {
            for (int k = i + 1; k < points.length; k++) {
                if (points[i].compareTo(points[k]) == 0) {
                    throw new IllegalArgumentException("Cannot have repeated points");
                }
            }
        }
        // create an array to store all the gradients of a point with the rest
        int mutableSegments = 0;
        LineSegment[] mutableSegmentsResult = new LineSegment[points.length];
        PairPoint[] collinearPointsEnd = new PairPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            Point[] copy = new Point[points.length - 1];
            for (int j = 0; j < copy.length; j++) {
                if (j < i) {
                    copy[j] = points[j]; // copy all the points except point i
                }
                else {
                    copy[j] = points[j + 1];
                }
            }
            // sort the array according to the slope it makes with point i
            Arrays.sort(copy, points[i].slopeOrder());

            int start = 0;
            int end = 1;
            int flag = 0;
            /* 1) loop through the sorted array
            2) if the gradient matches, keep start, increase end
            3) else increase both start and end */
            while (end <= copy.length) {
                // when the whole segment has been found
                if (flag == 1 && (end - start) >= 3
                        && (end == copy.length || (Double.compare(points[i].slopeTo(copy[start]),
                                                                  points[i].slopeTo(copy[end])))
                        != 0)) {
                    PointWrapper[] lineSegmentPt = new PointWrapper[end - start
                            + 1]; // the points[i] itself
                    for (int m = start; m < end; m++) {
                        lineSegmentPt[m - start] = new PointWrapper(copy[m]);
                    }
                    lineSegmentPt[end - start] = new PointWrapper(points[i]);
                    Arrays.sort(lineSegmentPt, lineSegmentPt[0].coordinateOrder());
                    if (mutableSegments >= collinearPointsEnd.length) {
                        collinearPointsEnd = resize(collinearPointsEnd,
                                                    2 * collinearPointsEnd.length);
                    }
                    collinearPointsEnd[mutableSegments] = new PairPoint(lineSegmentPt[0].getPoint(),
                                                                        lineSegmentPt[end - start]
                                                                                .getPoint());
                    mutableSegments += 1; // update 1st, will remove duplicate later
                    // update start and end again
                    start = end;
                    end = end + 1;
                    flag = 0;
                }
                else if (end < copy.length && points[i].slopeTo(copy[start]) == points[i]
                        .slopeTo(copy[end])) {
                    flag = 1;
                    end++;
                }
                // no segment found yet
                else {
                    start++;
                    end++;
                }
            }
        }
        // resize array to remove null LineSegment
        collinearPointsEnd = resize(collinearPointsEnd, mutableSegments);

        // i build my own HashSet
        if (mutableSegments > 0) { // for the case when collinearPointsEnd array is empty
            Arrays.sort(collinearPointsEnd, collinearPointsEnd[0].pairPointComparator());
            int s = 0;
            int e = 1;
            int flag2 = 0;
            mutableSegments = 0; // reset the mutableSegments
            while (e <= collinearPointsEnd.length) {
                // either we have found a duplicated segment ___xxx, or the segment is not duplicated
                if (flag2 == 1
                        && (e == collinearPointsEnd.length || !collinearPointsEnd[s]
                        .compareTo(
                                collinearPointsEnd[e]))) { // reach the end of the duplicate region
                    if (mutableSegments >= mutableSegmentsResult.length) {
                        mutableSegmentsResult = resize(mutableSegmentsResult,
                                                       2 * mutableSegmentsResult.length);
                    }
                    mutableSegmentsResult[mutableSegments] = new LineSegment(
                            collinearPointsEnd[s].getP(),
                            collinearPointsEnd[s].getQ());
                    mutableSegments += 1;
                    flag2 = 0; // for other duplicate regions
                    s = e;
                    e += 1;

                }
                else if (e == collinearPointsEnd.length) {
                    if (mutableSegments >= mutableSegmentsResult.length) {
                        mutableSegmentsResult = resize(mutableSegmentsResult,
                                                       2 * mutableSegmentsResult.length);
                    }
                    mutableSegmentsResult[mutableSegments] = new LineSegment(
                            collinearPointsEnd[s].getP(),
                            collinearPointsEnd[s].getQ());
                    mutableSegments += 1;

                }
                else if (collinearPointsEnd[s].compareTo(collinearPointsEnd[e])) {
                    flag2 = 1;
                    e++; // keep the s and increase the e
                }
                else {
                    if (mutableSegments >= mutableSegmentsResult.length) {
                        mutableSegmentsResult = resize(mutableSegmentsResult,
                                                       2 * mutableSegmentsResult.length);
                    }
                    mutableSegmentsResult[mutableSegments] = new LineSegment(
                            collinearPointsEnd[s].getP(),
                            collinearPointsEnd[s].getQ());
                    mutableSegments += 1;
                    s++;
                    e++;
                }
            }
        }

        // now we have an array mutableSegmentsResult with no duplicated linesegments
        // resize the array
        mutableSegmentsResult = resize(mutableSegmentsResult, mutableSegments);
        noOfSegments = mutableSegments;
        segmentsResult = mutableSegmentsResult;
    }

    private class PairPoint {
        private final Point p;   // one endpoint of this line segment
        private final Point q;   // the other endpoint of this line segment

        public PairPoint(Point p, Point q) {
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

        public boolean compareTo(PairPoint that) {
            return that.q.compareTo(this.q) == 0 && that.p.compareTo(this.p) == 0;
        }

        public Comparator<PairPoint> pairPointComparator() {
            return new BySegment();
        }

        private class BySegment implements Comparator<PairPoint> {
            public int compare(PairPoint line1, PairPoint line2) {
                if (line1.p.compareTo(line2.p) > 0) {
                    return 1;
                }
                else if (line1.p.compareTo(line2.p) == 0) { // break tie by the other point
                    if (line1.q.compareTo(line2.q) > 0) {
                        return 1;
                    }
                    else if (line1.q.compareTo(line2.q) == 0) {
                        return 0;
                    }
                    else {
                        return -1;
                    }
                }
                else {
                    return -1;
                }
            }
        }
    }

    private class PointWrapper implements Comparable<PointWrapper> {
        private final Point p;

        public PointWrapper(Point p) { // constructor
            this.p = p;
        }

        public Point getPoint() {
            return p;
        }

        public int compareTo(PointWrapper that) {
            return this.p.compareTo(that.getPoint());
        }

        public Comparator<PointWrapper> coordinateOrder() {
            return new ByCoordinate();
        }

        private class ByCoordinate implements Comparator<PointWrapper> {
            public int compare(PointWrapper left, PointWrapper right) {
                return left.getPoint().compareTo(right.getPoint());
            }
        }
    }

    private PairPoint[] resize(PairPoint[] array, int capacity) {
        PairPoint[] copy = new PairPoint[capacity];
        for (int i = 0; i < Math.min(capacity, array.length); i++) {
            copy[i] = array[i];
        }
        return copy;
    }

    private LineSegment[] resize(LineSegment[] array, int capacity) {
        LineSegment[] copy = new LineSegment[capacity];
        for (int i = 0; i < Math.min(capacity, array.length); i++) {
            copy[i] = array[i];
        }
        return copy;
    }

    // the number of line segments
    public int numberOfSegments() {
        return noOfSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] defensiveSegmentsResult = segmentsResult.clone();
        return defensiveSegmentsResult;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
