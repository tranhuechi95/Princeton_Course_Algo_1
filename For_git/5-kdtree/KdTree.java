import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class KdTree {
    private Node root; // 1st node of the tree
    private int size = 0; // initially
    private final RectHV startRect = new RectHV(0.0, 0.0, 1.0, 1.0);

    private class Node {
        private final Point2D point;
        private Node left;
        private Node right;
        private final RectHV rectangle; // the rect correspond to the node
        private final int nodeDimension;

        public Node(Point2D pt, RectHV rect, int dimension) {
            point = pt;
            rectangle = rect;
            nodeDimension = dimension;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null; // initially
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size; // make sure size is updated
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Insert point cannot be null");
        }
        if (root == null) {
            size++;
            root = new Node(p, startRect, 0);
        }
        else {
            double cmp = p.x() - root.point.x();
            if (cmp == 0 && Double.compare(p.y(), root.point.y()) == 0) {
                // do nothing
            }
            else if (cmp < 0) {
                root.left = insert(root, p, 1, cmp);
            }
            else {
                root.right = insert(root, p, 1, cmp);
            }
        }
    }

    // parent is guaranteed not to be null
    private Node insert(Node parent, Point2D point, int nodeDimension, double parentCmp) {
        // check if insertion point is found - either cmp < 0 && parent.left is null
        //                                     or cmp >= && parent.right is null
        if ((parentCmp < 0 && parent.left == null) || (parentCmp >= 0 && parent.right == null)) {
            size++;
            RectHV rect;
            int parentNodeDim = (nodeDimension + 1) % 2;
            if (parentCmp < 0 && parentNodeDim == 0) {
                rect = new RectHV(parent.rectangle.xmin(), parent.rectangle.ymin(),
                                  parent.point.x(), parent.rectangle.ymax());
            }
            else if (parentCmp < 0 && parentNodeDim == 1) {
                rect = new RectHV(parent.rectangle.xmin(), parent.rectangle.ymin(),
                                  parent.rectangle.xmax(), parent.point.y());
            }
            else if (parentCmp >= 0 && parentNodeDim == 0) {
                rect = new RectHV(parent.point.x(), parent.rectangle.ymin(),
                                  parent.rectangle.xmax(), parent.rectangle.ymax());
            }
            else {
                rect = new RectHV(parent.rectangle.xmin(), parent.point.y(),
                                  parent.rectangle.xmax(), parent.rectangle.ymax());
            }

            // return the point for insertion by parent
            return new Node(point, rect, nodeDimension);
        }

        // else, continue to find insertion point
        if (parentCmp < 0)
            parent = parent.left;
        else
            parent = parent.right;
        // parent guaranteed to be not null after the above code

        if (nodeDimension == 0) { // this node uses x-coordinate
            double cmp = point.x() - parent.point.x();
            if (cmp == 0 && Double.compare(point.y(), parent.point.y()) == 0) {
                // do nothing
            }
            else if (cmp < 0) {
                /* x-coord of point is less than x-coord of node x
                -> insert to left-side */
                // the childNode rect is different only in xmax
                parent.left = insert(parent, point, 1, cmp);
            }
            else { // cmp >= 0 and not duplicate point
                // go right
                // the childNode rect is different only in xmin
                parent.right = insert(parent, point, 1, cmp);
            }
        }
        else { // parent.dimension == 1, use y-coordinate
            double cmp = point.y() - parent.point.y();
            if (cmp == 0 && Double.compare(point.x(), parent.point.x()) == 0) {
                // do nth
            }
            else if (cmp < 0) {
                // the childNode rect is different only in ymax
                parent.left = insert(parent, point, 0, cmp);
            }
            else { // cmp >= 0 and not duplicate point
                parent.right = insert(parent, point, 0, cmp);
            }
        }
        return parent;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException(
                    "contains(Point2D) does not accept null input!");
        Node containCheck = contains(root, p);
        return containCheck != null;
    }

    private Node contains(Node parent, Point2D p) {
        Node x = parent;
        int nodeDimension = 0;
        if (x == null) {
            return null;
        }
        while (x != null) {
            if (nodeDimension == 0) { // use x-coord
                double cmp = p.x() - x.point.x();
                if (cmp == 0 && Double.compare(p.y(), x.point.y()) == 0) {
                    return x;
                }
                else if (cmp < 0) {
                    x = x.left;
                    nodeDimension = 1;
                }
                else { // cmp > 0
                    x = x.right;
                    nodeDimension = 1;
                }
            }
            else {
                double cmp = p.y() - x.point.y();
                if (cmp == 0 && Double.compare(p.x(), x.point.x()) == 0) {
                    return x;
                }
                else if (cmp < 0) {
                    x = x.left;
                    nodeDimension = 0;
                }
                else { // cmp > 0
                    x = x.right;
                    nodeDimension = 0;
                }
            }
        }
        return null;
    }

    private Iterable<Node> drawPoints() {
        Queue<Node> drawQueue = new Queue<Node>();
        drawPoints(root, drawQueue);
        return drawQueue;
    }

    private void drawPoints(Node parent, Queue<Node> queue) {
        if (parent != null) {
            drawPoints(parent.left, queue);
            queue.enqueue(parent);
            drawPoints(parent.right, queue);
        }
    }

    private class Queue<Item> implements Iterable<Item> {
        private Node1 first;
        private Node1 last;

        private class Node1 {
            private Item item;
            private Node1 next;
            private Node1 prev;
        }

        public Queue() {
            first = null;
            last = null;
        }

        // add the item
        public void enqueue(Item item) { // enqueue at the end
            if (item == null) {
                throw new IllegalArgumentException();
            }
            if (first == null) {
                Node1 start = new Node1(); // Node() is a method
                start.item = item;
                start.next = null;
                start.prev = null;
                first = start;
                last = first; // queue has one item, both first and last point to the same item
            }
            else {
                Node1 oldLast = last;
                last = new Node1();
                last.item = item;
                last.next = null;
                oldLast.next = last;
                last.prev = oldLast;
            }
        }

        // return an independent iterator over items in random order
        public Iterator<Item> iterator() {
            return new ListIterator();
        }

        private class ListIterator implements Iterator<Item> { // inheritance
            private Node1 curr = first;

            public ListIterator() {
            }

            public boolean hasNext() {
                return curr != null;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

            public Item next() {
                if (curr == null)
                    throw new java.util.NoSuchElementException("No more element to return");
                Item item = curr.item;
                curr = curr.next;
                return item;
            }
        }
    }

    // draw all points to standard draw
    public void draw() {
        // draw the points
        StdDraw.clear();
        Iterable<Node> allPoints = drawPoints();
        // draw the lines (red and blue)
        for (Node point : allPoints) {
            StdDraw.setPenRadius(0.01);
            if (point.nodeDimension == 0) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(point.point.x(), point.rectangle.ymin(), point.point.x(),
                             point.rectangle.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(point.rectangle.xmin(), point.point.y(), point.rectangle.xmax(),
                             point.point.y());
            }

        }
        for (Node Point : allPoints) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            Point.point.draw();
        }

        StdDraw.show();
    }


    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Cannot input null rectangle");
        }
        Queue<Point2D> q = new Queue<Point2D>();
        rangeSearch(root, q, rect);
        return q;
    }

    private void rangeSearch(Node parent, Queue<Point2D> queue, RectHV rect) {
        if (parent == null) return;
        if (parent.rectangle.intersects(rect)) {
            rangeSearch(parent.left, queue, rect);
            if (rect.contains(parent.point)) {
                queue.enqueue(parent.point);
            }
            rangeSearch(parent.right, queue, rect);
        }
    }


    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException(
                    "nearest(Point2D) does not accept null input!");
        if (isEmpty()) {
            return null;
        }
        else {
            // StdOut.println("Root is:" + root.point.toString());
            Point2D nearestNeigh = nearest(root, p, root.point, 0);
            return nearestNeigh;
        }
    }

    /*
     * Want: nearest(Node parent, Point2D query) -> return nearest pt BELONGING TO subtree rooted at parent
     * nearest(Node parent, Point2D query):
     * 1. if parent is null, return null
     * 2. otherwise, nearest point can be:
     *    a. parent
     *    b. someone in parent.left subtree
     *    c. someone in parent.right subtree
     * 3. To check if a. is right, compare dist(parent.point, query) with minDist.
     *    If yes, update the nearest point to be parent and update minDist.
     * 4. To check if b. is right, call nearest(parent.left, query): this will return the nearest point BELONGING TO left subtree.
     *    Compare dist(best pt in left subtree, query) with minDist. If smaller, update nearest point & update minDist.
     * 5. To check if we need to search on the right, compare minDist with dist(queryPt, splittingLine).
     *    If minDist < dist(queryPt, splittingLine), there's no need to search right subtree
     *    Else, call nearest(parent.right, query): this will return the nearest point BELONGING TO right subtree.
     *    Compare dist(dest pt in right subtree, query) with minDist. If smaller, update nearest point & update minDist.
     * 6. Return nearest point obtained so far.
     * */

    private Point2D nearest(Node parent, Point2D query, Point2D minFound, int nodeDimension) {
        if (parent == null) {
            return null; // CHECK THIS!
        }
        double cmp;
        if (nodeDimension == 0)
            cmp = query.x() - parent.point.x();
        else
            cmp = query.y() - parent.point.y();
        // StdOut.println("Checking for parent");
        if (Double.compare(dist(parent.point, query), dist(query, minFound))
                < 0) { // dist to parent is less than current minFound
            // StdOut.println("MinFound for parent x:" + minFound.toString());
            minFound = parent.point; // update minFound
        }
        if (cmp < 0) { // check left node 1st
            // StdOut.println("Checking for childleft x");
            Point2D leftMinPoint = nearest(parent.left, query, minFound,
                                           (nodeDimension + 1) % 2);
            if (leftMinPoint != null
                    && Double.compare(dist(leftMinPoint, query), dist(query, minFound))
                    < 0) { // compare leftMinPoint with Parent
                minFound = leftMinPoint;
            }
            // StdOut.println("Checking for childright x");
            if (parent.right != null && !checkPrune(parent.right, query, dist(minFound, query))) {
                Point2D rightMinPoint = nearest(parent.right, query, minFound,
                                                (nodeDimension + 1) % 2);
                if (rightMinPoint != null
                        && Double.compare(dist(rightMinPoint, query), dist(query, minFound)) < 0) {
                    minFound = rightMinPoint;
                }
            }
            return minFound;
        }
        else { // check right node 1st
            // StdOut.println("Checking for childright y");
            Point2D rightMinPoint = nearest(parent.right, query, minFound,
                                            (nodeDimension + 1) % 2);
            if (rightMinPoint != null
                    && Double.compare(dist(rightMinPoint, query), dist(query, minFound))
                    < 0) { // compare leftMinPoint with Parent
                minFound = rightMinPoint;
            }
            // StdOut.println("Checking for childleft y");
            if (parent.left != null && !checkPrune(parent.left, query, dist(minFound, query))) {
                Point2D leftMinPoint = nearest(parent.left, query, minFound,
                                               (nodeDimension + 1) % 2);
                if (leftMinPoint != null
                        && Double.compare(dist(leftMinPoint, query), dist(query, minFound)) < 0) {
                    minFound = leftMinPoint;
                }
            }
            return minFound;
        }
    }

    private double dist(Point2D a, Point2D b) {
        return a.distanceSquaredTo(b);
    }

    private boolean checkPrune(Node checkNode, Point2D query, double minDist) {
        return checkNode.rectangle.distanceSquaredTo(query) > minDist; // true means needs to prune
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        for (Point2D point : kdtree.range(new RectHV(0.574, 0.48, 0.67, 1.0))) {
            StdOut.println(point.toString());
        }

        // draw the points
        kdtree.draw();
    }
}
