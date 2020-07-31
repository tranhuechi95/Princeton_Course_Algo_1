// Implement using double-linked list

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueueLL<Item> implements Iterable<Item> {

    private Node first;
    private int sz = 0; // number of items on the queue
    private Node last;

    private class Node {
        public Item item;
        public Node next;
        public Node prev;
    }

    // construct an empty randomized queue
    public RandomizedQueueLL() {
        first = null;
        last = null;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (first == null);
    }

    // return the number of items on the randomized queue
    public int size() {
        Node current = first;
        if (!isEmpty()) {
            sz = 1;
            while (current.next != null) {
                sz++;
                current = current.next;
            }
        }
        return sz;
    }

    // add the item
    public void enqueue(Item item) { // enqueue at the end
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (first == null) {
            Node start = new Node(); // Node() is a method
            start.item = item;
            start.next = null;
            start.prev = null;
            first = start;
            last = first; // queue has one item, both first and last point to the same item
        }
        else {
            Node oldLast = last;
            last = new Node();
            last.item = item;
            last.next = null;
            oldLast.next = last;
            last.prev = oldLast;
        }
    }

    // remove and return a random item
    public Item dequeue() {
        // need to generate the item number to be removed
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int i = StdRandom.uniform(sz); // [0,sz)
        Node pointer = first; // initially point to the start

        // get pointer = pointer.next...next (i times)
        for (int j = 0; j < i; j++) {
            pointer = pointer.next;
        }
        Node a = pointer.prev; // previous node
        Node b = pointer.next; // next node
        Item itemContent = pointer.item;
        a.next = b;
        b.prev = a;
        return itemContent;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int i = StdRandom.uniform(sz); // [0,sz)
        Node pointer = first; // initially point to the start

        // get pointer = pointer.next...next (i times)
        for (int j = 0; j < i; j++) {
            pointer = pointer.next;
        }
        return pointer.item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> { // inheritance
        private int count = 0;
        private int[] index;

        public ListIterator() { // initialize the index array
            index = new int[size()];
            for (int i = 0; i < sz; i++) {
                index[i] = i;
            }
            StdRandom.shuffle(index); // shuffle the index array
        }

        public boolean hasNext() {
            return count < index.length;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            Node pointer = first;
            int k = index[count];
            for (int n = 0; n < k; n++) {
                pointer = pointer.next;
            }
            Item item = pointer.item;
            count++;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueueLL<String> randomQueue = new RandomizedQueueLL<String>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            randomQueue.enqueue(s);
        }
        for (String s : randomQueue) {
            StdOut.println(s);
        }
    }
}
