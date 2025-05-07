package assignment9;

import org.junit.Test;

import static org.junit.Assert.*;

public class Ass9Test {

    @Test(timeout = 2000)
    public void bstSize_1P() {
        Map<String, Integer> map = new Map<>();
        assertTrue("successful add returns true", map.add("hello", 2));
        assertTrue("successful add returns true", map.add("world", 3));
        assertFalse("failed add returns false", map.add("world", 3));
        assertEquals("size is not correct after adding", 2, map.size());
    }

    @Test(timeout = 2000)
    public void bstContains_1P() {
        Map<String, Integer> map = new Map<>();
        assertTrue("successful add returns true", map.add("hello", 2));
        assertTrue("successful add returns true", map.add("world", 3));
        assertTrue("contains(hello) should return true", map.contains("hello"));
        assertTrue("contains(world) should return true", map.contains("world"));
        assertFalse("contains(sumo) should return false", map.contains("sumo"));
    }

    @Test(timeout = 2000)
    public void bstGet_1P() {
        Map<String, Integer> map = new Map<>();
        assertTrue("successful add returns true", map.add("hello", 2));
        assertTrue("successful add returns true", map.add("world", 3));
        assertEquals("get(hello) should return 2", 2, (int) map.get("hello"));
        assertEquals("get(world) should return 3", 3, (int) map.get("world"));
        assertNull("get(sumo) should return null", map.get("sumo"));
    }
}
