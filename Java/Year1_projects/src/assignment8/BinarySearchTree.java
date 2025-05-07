package assignment8;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinarySearchTree<T extends Comparable<T>> implements Iterable<T>{
    private Node<T> root;
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
     * @return the current tree's size
     */
    public int size() {
        return this.size;
    }

    /**
     * a getter function for the tree's root
     * @return the root of the tree
     */
    public Node<T> getRoot() {
        return this.root;
    }

    /**
     * a function that checks whether a value is in the tree or not
     * @param value the value to be searched for
     * @return a boolean, if the value is in the tree
     */
    public boolean contains(T value) {
        return this.get(value) != null;
    }

    /**
     * a function that calls a recursive method, that gets a specific value from the tree
     * @param value the value to get
     * @return the wanted value
     */
    public T get(T value) {
        return this.getRecursive(this.root, value);
    }

    /**
     *  a recursive funtion that finds and gets a specific value from the tree
     * @param currentNode the node that is currently used
     * @param value the value to get
     * @return the wanted value
     */
    private T getRecursive(Node<T> currentNode, T value){
        if(currentNode == null){
            return null;
        }

        int valuesComparison = value.compareTo(currentNode.getValue());


        if (valuesComparison < 0){
            return getRecursive(currentNode.getLeft(),value);
        }

        else if(valuesComparison > 0){
            return getRecursive(currentNode.getRight(),value);
        }
        else{
            return currentNode.getValue();
        }
    }

    /**
     * a function that returns the tree's iterator
     * @return an instance of the InOrderIterator
     */
    public Iterator<T> iterator() {
        return new InOrderIterator<>(this.root);
    }

    /**
     * a function that calls a recursive function that adds a new node to the tree
     * @param value the value of the node to be added to the tree
     * @return a boolean if node was added properly
     */
    public boolean add(T value) {
        if (this.root == null){
            this.root = new Node<>(value);
            this.size += 1 ;
            return true;
        }
        else {
           boolean added = addRecursive(this.root, value);
           if(added){
               this.size += 1 ;
           }
           return added;
        }
    }

    /**
     * a recursive function that adds a new node to the tree
     * @param currentNode the current node
     * @param value the value of the node that will be added to the tree
     * @return a boolean if the new node was added properly
     */
    private boolean addRecursive(Node<T> currentNode,T value){
        int valueComparison = value.compareTo(currentNode.getValue());
        if (valueComparison < 0){
            if(currentNode.getLeft() == null){
                currentNode.setLeft(new Node<>(value));
                return true;
            }
            else{
                return addRecursive(currentNode.getLeft(), value);
            }
        }
        else if (valueComparison > 0){
            if(currentNode.getRight() == null){
                currentNode.setRight(new Node<>(value));
                return true;
            }
            else {
                return addRecursive(currentNode.getRight(), value);
            }
        }
        else {
            return false;
        }
    }






    public static class Node<T> {
        private Node<T> right;
        private Node<T> left;
        private T value;

        /**
         * a constructor
         * @param value the Node's value
         */
        Node(T value) {
            this.value = value;
        }

        /**
         * a getter function for the right child
         * @return the node to the right of the current node
         */
        public Node<T> getRight() {
            return right;
        }


        /**
         * a getter function for the left child
         * @return the node to the left of the current node
         */
        public Node<T> getLeft() {
            return left;
        }

        /**
         * a getter function for the value of the node
         * @return the value of the current node
         */
        public T getValue() {
            return value;
        }

        /**
         * a setter function for the right child of the current node
         * @param rightNode a node to be set to the right of the current node
         */
        public void setRight(Node<T> rightNode){
            this.right = rightNode;
        }


        /**
         * a setter function for the left child of the current node
         * @param leftNode a node to be set to the left of the current node
         */
        public void setLeft(Node<T> leftNode){
            this.left = leftNode;
        }


    }

    public static class InOrderIterator<T> implements Iterator<T> {
        private Node<T> currentNode;
        private Node<T> previousNode;

        /**
         * a constructor for the InOrderIterator
         * @param root the root of the tree
         */
        InOrderIterator(Node<T> root){
            this.currentNode = root;
        }

        /**
         * a boolean function that determines if the instance has another iterable object
         * @return true/false
         */
        @Override
        public boolean hasNext() {
            return this.currentNode != null;
        }

        /**
         * a function that returns the next iterable value in a InOrder formation
         * @return the next InOrder value
         */
        @Override
        public T next() {
            if (hasNext() == false){
                throw new NoSuchElementException();
            }
            while(this.currentNode != null){
                if (this.currentNode.getLeft() == null){
                    T value = this.currentNode.getValue();
                    this.currentNode = this.currentNode.getRight();
                    return value;
                }
                else{
                    this.previousNode = this.currentNode.getLeft();
                    while(this.previousNode.getRight() != null && this.previousNode.getRight() != this.currentNode){
                        this.previousNode = this.previousNode.getRight();
                    }
                    if(this.previousNode.getRight() == null){
                        this.previousNode.setRight(this.currentNode);
                        this.currentNode = this.currentNode.getLeft();
                    }
                    else{
                        this.previousNode.setRight(null);
                        T value = this.currentNode.getValue();
                        this.currentNode = this.currentNode.getRight();
                        return value;
                    }

                }
            }
            throw new NoSuchElementException();
        }
    }
}
