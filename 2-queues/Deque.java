import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    // implement as double linked-list
    private Node first;
    private Node last;
    private int sz = 0;

    private class Node {
        Item item;
        Node prev; // point to the previous node
        Node next; // point to the next node
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (first == null);
    }

    // return the number of items on the deque
    public int size() {
        Node current = first;
        if (!isEmpty()) {
            sz = 1;
            while (current.next != null) {
                sz += 1;
                current = current.next;
            }
        }
        return sz;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Must not be null");
        }
        if (first == null) { // for 1st item
            Node start = new Node();
            start.item = item;
            start.next = null;
            start.prev = null;
            first = start;
            last = first;
        }
        else {
            Node oldFirst = first;
            first = new Node();
            first.item = item;
            first.next = oldFirst;
            oldFirst.prev = first;
        }

    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Must not be null");
        }
        if (last == null) { // for 1st item
            Node start = new Node();
            start.item = item;
            start.next = null;
            start.prev = null;
            last = start;
            first = last;
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

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Item itemContent = first.item;
        first = first.next;
        if (first != null) {
            first.prev = null;
        }
        if (isEmpty()) {
            last = null;
        }
        return itemContent;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Item itemContent = last.item;
        last = last.prev;
        if (last != null) {
            last.next = null;
        }
        if (isEmpty()) {
            first = null;
        }
        return itemContent;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        private int count = 0;

        public boolean hasNext() {
            return (count < size());
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            Item item = current.item;
            current = current.next;
            count++;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> queue = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            queue.addLast(s);
        }
        for (String s : queue) {
            StdOut.println(s);
        }
    }
}

