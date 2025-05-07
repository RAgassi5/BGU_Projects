package assignment9;

public class Map<T extends Comparable <T>, E>{
    private BinarySearchTree bst;

    /**
     * a constructor for the Map
     */
    public Map() {
        this.bst = new BinarySearchTree<>();
    }

    /**
     * a function that adds an object to the Map
     * @param key the key of the given object
     * @param value the value of the given object
     * @return a boolean if object was added properly
     */
    public boolean add(T key, E value) {
        return bst.add(key, value);
    }

    /**
     * a function that returns the value of the wanted key
     * @param key the key of the object to be looked for in the Map
     * @return the value of the object with the given key
     */
    public E get(T key) {
        return (E) bst.get(key);
    }

    /**
     * a function that returns the amount of keys in the Map
     * @return an integer that represents the amount of keys currently in the Map
     */
    public int size() {
        return bst.size();
    }

    /**
     * a function that checks whether a given key is in the Map or not
     * @param key the key to search for
     * @return a boolean if the key is currently in the Map or not
     */
    public boolean contains(T key) {
        return bst.contains(key);
    }


}
