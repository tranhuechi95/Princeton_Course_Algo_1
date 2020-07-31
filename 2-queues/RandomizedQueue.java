// Implement using Array

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] s;
    private int N = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        s = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return N;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        s[N++] = item;
        if (N == s.length) {
            reSize(s.length * 2);
        }
    }
    // N =3
    // s[++N](s[4],N =4)
    // s[N++](s[3],N =4)

    // remove and return a random item
    public Item dequeue() {
        // generate the item number to dequeue
        int i = StdRandom.uniform(N);
        Item item = s[i];
        // shift all the items behind the dequeue item to -1 place
        for (int j = i; j < N - 1; j++) {
            s[j] = s[j + 1];
        }
        s[N] = null; // remove loitering
        N = N - 1; // update N
        if (N > 0 && N == s.length / 4) {
            reSize(s.length / 2);
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        // generate the item number to dequeue
        int i = StdRandom.uniform(N);
        return s[i];
    }

    private void reSize(int capacity) {
        // create a new string to copy with 2x initial capacity
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            copy[i] = s[i];
        }
        s = copy; // pass back
    }

    // return an independent iterator over items in random order
    // shuffle the Item array
    public Iterator<Item> iterator() {
        return new RandomizedIterator();
    }

    private class RandomizedIterator implements Iterator<Item> {
        private int count = 0;
        private int[] index;

        public RandomizedIterator() { // constructor
            index = new int[N];
            for (int i = 0; i < N; i++) {
                index[i] = i;
            }
            StdRandom.shuffle(index);
        }

        public boolean hasNext() {
            return count < N;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            Item item = s[index[count]];
            count++;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> randomQueue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            randomQueue.enqueue(s);
        }
        for (String s : randomQueue) {
            StdOut.println(s);
        }
    }
}
