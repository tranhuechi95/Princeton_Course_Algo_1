import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomQueue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            randomQueue.enqueue(s);
        }
        Iterator<String> i = randomQueue.iterator();
        int count = 0;
        while (i.hasNext() && count < k) {
            String s = i.next();
            StdOut.println(s);
            count++;
        }
    }
}
