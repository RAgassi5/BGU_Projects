package assignment9;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinarySearchTree<T extends Comparable<T>, E> implements Iterable<T> {
    private Node<T, E> root;
    private int size;

    /**
     * a default constructor for BinarySearchTree
     */
    public BinarySearchTree() {
        this.root = null;
        this.size = 0;
    }

    /**
     * a function that returns the size of the tree
     *
     * @return the current tree's size
     */
    public int size() {
        return this.size;
    }

    /**
     * a getter function for the tree's root
     *
     * @return the root of the tree
     */
    public Node<T, E> getRoot() {
        return this.root;
    }

    /**
     * a function that checks whether a key is in the tree or not
     *
     * @param key the key to be searched for
     * @return a boolean, if the key is in the tree
     */
    public boolean contains(T key) {
        return this.get(key) != null;
    }

    /**
     * a function that calls a recursive method, that gets a specific key from the tree
     *
     * @param key the key to get
     * @return the value of the node that has the given key
     */
    public E get(T key) {
        return this.getRecursive(this.root, key);
    }

    /**
     * a recursive function that finds and gets a specific value from the tree
     *
     * @param currentNode the node that is currently used
     * @param key         the key of the node to get its value
     * @return the value of the node with the given key
     */
    private E getRecursive(Node<T, E> currentNode, T key) {
        if (currentNode == null) {
            return null;
        }

        int valuesComparison = key.compareTo(currentNode.getKey());


        if (valuesComparison < 0) {
            return getRecursive(currentNode.getLeft(), key);
        } else if (valuesComparison > 0) {
            return getRecursive(currentNode.getRight(), key);
        } else {
            return currentNode.getValue();
        }
    }

    /**
     * a function that returns the tree's iterator
     *
     * @return an instance of the InOrderIterator
     */
    public Iterator<T> iterator() {
        return new InOrderIterator<>(this.root);
    }

    /**
     * a function that calls a recursive function that adds a new node to the tree
     *
     * @param key   the key of the node that will be added to the tree
     * @param value the value of the node to be added to the tree
     * @return a boolean if node was added properly
     */
    public boolean add(T key, E value) {
        if (this.root == null) {
            this.root = new Node<>(key, value);
            this.size += 1;
            return true;
        } else {
            boolean added = addRecursive(this.root, key, value);
            if (added) {
                this.size += 1;
            }
            return added;
        }
    }

    /**
     * a recursive function that adds a new node to the tree
     *
     * @param currentNode the current node
     * @param key         the key of the node that will be added to the tree
     * @param val         the value of the node that will be added to the tree
     * @return a boolean if the new node was added properly
     */
    private boolean addRecursive(Node<T, E> currentNode, T key, E val) {
        int KeyComparison = key.compareTo(currentNode.getKey());
        if (KeyComparison < 0) {
            if (currentNode.getLeft() == null) {
                currentNode.setLeft(new Node<>(key, val));
                return true;
            } else {
                return addRecursive(currentNode.getLeft(), key, val);
            }
        } else if (KeyComparison > 0) {
            if (currentNode.getRight() == null) {
                currentNode.setRight(new Node<>(key, val));
                return true;
            } else {
                return addRecursive(currentNode.getRight(), key, val);
            }
        } else {
            return false;
        }
    }


    public static class Node<T, E> {
        private Node<T, E> right;
        private Node<T, E> left;
        private E value;
        private T key;

        /**
         * a constructor
         *
         * @param value the Node's value
         */
        Node(T key, E value) {
            this.value = value;
            this.key = key;
        }

        /**
         * a getter function for the right child
         *
         * @return the node to the right of the current node
         */
        public Node<T, E> getRight() {
            return this.right;
        }


        /**
         * a getter function for the left child
         *
         * @return the node to the left of the current node
         */
        public Node<T, E> getLeft() {
            return this.left;
        }

        /**
         * a getter function for the value of the node
         *
         * @return the value of the current node
         */
        public E getValue() {
            return this.value;
        }

        /**
         * a setter function for the right child of the current node
         *
         * @param rightNode a node to be set to the right of the current node
         */
        public void setRight(Node<T, E> rightNode) {
            this.right = rightNode;
        }


        /**
         * a setter function for the left child of the current node
         *
         * @param leftNode a node to be set to the left of the current node
         */
        public void setLeft(Node<T, E> leftNode) {
            this.left = leftNode;
        }

        /**
         * a getter function for the key of the node
         *
         * @return the node's key
         */
        public T getKey() {
            return this.key;
        }


    }

    public static class InOrderIterator<T, E> implements Iterator<T> {
        private Node<T, E> currentNode;
        private Node<T, E> previousNode;

        /**
         * a constructor for the InOrderIterator
         *
         * @param root the root of the tree
         */
        InOrderIterator(Node<T, E> root) {
            this.currentNode = root;
        }

        /**
         * a boolean function that determines if the instance has another iterable object
         *
         * @return true/false
         */
        @Override
        public boolean hasNext() {
            return this.currentNode != null;
        }

        /**
         * a function that returns the next iterable value in a InOrder formation
         *
         * @return the next InOrder value
         */
        @Override
        public T next() {
            if (hasNext() == false) {
                throw new NoSuchElementException();
            }
            while (this.currentNode != null) {
                if (this.currentNode.getLeft() == null) {
                    T key = this.currentNode.getKey();
                    this.currentNode = this.currentNode.getRight();
                    return key;
                } else {
                    this.previousNode = this.currentNode.getLeft();
                    while (this.previousNode.getRight() != null && this.previousNode.getRight() != this.currentNode) {
                        this.previousNode = this.previousNode.getRight();
                    }
                    if (this.previousNode.getRight() == null) {
                        this.previousNode.setRight(this.currentNode);
                        this.currentNode = this.currentNode.getLeft();
                    } else {
                        this.previousNode.setRight(null);
                        T key = this.currentNode.getKey();
                        this.currentNode = this.currentNode.getRight();
                        return key;
                    }

                }
            }
            throw new NoSuchElementException();
        }
    }
}
