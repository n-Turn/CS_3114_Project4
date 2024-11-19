/**
 * This class provides the implementation for some of the methods in a doubly
 * linked list (DoubleLL).
 * It allows operations such as adding, removing, and accessing elements in the
 * list.
 *
 * Each element is stored in a `Node` which maintains links to both the next and
 * previous elements in the list.
 *
 * @author Nimay Goradia (ngoradia) and Nicolas Turner (nicturn)
 * @version 11.14.2024
 */

public class DoubleLL {

    /**
     * This private static class represents a node in the doubly linked list.
     * Each node contains a data field, and references to both the next and
     * previous nodes in the list.
     */
    private static class Node {

        private Node next; // The next node in the list
        private Node previous; // The previous node in the list
        private Block block; // The block stored in the node

        /**
         * This constructor initializes the node with the provided data.
         * 
         * @param b
         *            the block to be stored in the node
         */

        public Node(Block b) {
            block = b;
        }


        /**
         * This method sets the reference to the next node in the list.
         *
         * @param n
         *            the next node to be linked
         */
        public void setNext(Node n) {
            next = n;
        }


        /**
         * This method sets the reference to the previous node in the list.
         * 
         * @param n
         *            the previous node to be linked
         */
        public void setPrevious(Node n) {
            previous = n;
        }


        /**
         * This method returns the next node in the list.
         *
         * @return the next node in the list
         */

        public Node next() {
            return next;
        }


        /**
         * This method returns the previous node in the list.
         *
         * @return the previous node in the list
         */
        public Node previous() {
            return previous;
        }


        /**
         * This method returns the data stored in the node.
         *
         * @return the integer data of the node
         */
        public Block getBlock() {
            return block;
        }
    }

    private int size; // Tracks the number of elements in the list
    private Node head; // The first node in the list
    private Node tail; // The last node in the list

    /**
     * This constructor initializes an empty doubly linked list.
     */
    public DoubleLL() {
        head = null;
        tail = null;
        size = 0;
    }


    /**
     * This method checks if the list is empty.
     *
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }


    /**
     * This method returns the number of elements in the list.
     *
     * @return the size of the list
     */
    public int size() {
        return size;
    }


    /**
     * This method clears the list by setting head and tail to null and
     * resetting the size.
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }


    /**
     * This method returns the data of the node at the specified index.
     *
     * @param index
     *            the position of the node in the list
     * @return the data of the node at the specified index
     */
    public Block get(int index) {
        return getNodeAtIndex(index).getBlock();
    }


    /**
     * This method adds a new entry to the end of the list.
     *
     * @param newBlock
     *            the block to be added to the list
     */
    public void add(Block newBlock) {
        add(size(), newBlock);
    }


    /**
     * This method adds a new entry at the specified index.
     *
     * @param index
     *            the position where the new element should be inserted
     * @param block
     *            the block to be added
     */
    public void add(int index, Block block) {
        Node addition = new Node(block);
        if (index == 0) {
            if (head == null) {
                head = addition;
                tail = addition;
            }
            else {
                addition.setNext(head);
                head.setPrevious(addition);
                head = addition;
            }
        }
        else if (index == size) {
            tail.setNext(addition);
            addition.setPrevious(tail);
            tail = addition;
        }
        else {
            Node nodeAfter = getNodeAtIndex(index);
            Node nodeBefore = nodeAfter.previous();
            addition.setNext(nodeAfter);
            addition.setPrevious(nodeBefore);
            nodeBefore.setNext(addition);
            nodeAfter.setPrevious(addition);
        }
        size++;
    }


    /**
     * This method returns the node at a specific index.
     * 
     * @param index
     *            the position of the node in the list
     * @return the node at the specified index
     * @throws IndexOutOfBoundsException
     *             if the index is out of bounds
     */
    private Node getNodeAtIndex(int index) {
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next();
        }
        return current;
    }


    /**
     * This method removes the node at the specified index.
     *
     * @param index
     *            the position of the node to be removed
     * @return true if the removal was successful, false otherwise
     * @throws IndexOutOfBoundsException
     *             if the index is out of bounds
     */
    public boolean remove(int index) {
        Node nodeToBeRemoved = getNodeAtIndex(index);
        if (nodeToBeRemoved == head) {
            head = head.next();
            if (head != null) {
                head.setPrevious(null);
            }
        }
        else if (nodeToBeRemoved == tail) {
            tail = tail.previous();
            if (tail != null) {
                tail.setNext(null);
            }
        }
        else {
            Node before = nodeToBeRemoved.previous();
            Node after = nodeToBeRemoved.next();
            before.setNext(after);
            after.setPrevious(before);
        }
        if (size == 1) {
            head = null;
            tail = null;
        }
        size--;
        return true;
    }
}
