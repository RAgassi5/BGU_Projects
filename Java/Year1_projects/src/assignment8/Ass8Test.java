package assignment8;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class Ass8Test {

    @Test(timeout = 2000)
    public void bstSize_1P() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue("successful add returns true", bst.add(2));
        assertTrue("successful add returns true", bst.add(1));
        assertEquals("size is not correct after adding", 2, bst.size());
    }

    @Test(timeout = 2000)
    public void bstIterator_1P() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (int i : new int[]{2, 9, 7, 1, 10, 3, 6, 8, 5, 4}) {
            assertTrue("successful add returns true", bst.add(i));
        }
        Iterator<Integer> it = bst.iterator();
        for (int i = 1; i < 11; i++) {
            assertTrue("hasnext should return true when there are more items", it.hasNext());
            assertEquals("next returned item not in order", i, (int) it.next());
        }
    }

    @Test(timeout = 2000)
    public void bstStructure_1P() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.add(2);
        bst.add(3);
        assertEquals("root should be 2", 2, (int) bst.getRoot().getValue());
        assertNotNull("right should not be null", bst.getRoot().getRight());
        assertEquals("right should be 3", 3, (int) bst.getRoot().getRight().getValue());
        assertNull("left should be null", bst.getRoot().getLeft());
    }

    @Test(timeout = 2000)
    public void bstContains_1P() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.add(2);
        bst.add(3);
        assertTrue("contains(2) should return true", bst.contains(2));
        assertTrue("contains(3) should return true", bst.contains(3));
        assertFalse("contains(1) should return false", bst.contains(1));
    }

    @Test(timeout = 2000)
    public void bstGet_1P() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.add(2);
        bst.add(3);
        assertEquals("get(2) should return 2", 2, (int) bst.get(2));
        assertEquals("get(3) should return 3", 3, (int) bst.get(3));
        assertNull("get(1) should return null", bst.get(1));
    }
}
