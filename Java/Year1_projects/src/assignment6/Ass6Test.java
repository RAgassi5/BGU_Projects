package assignment6;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class Ass6Test {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);

    @Test(expected = NoSuchElementException.class)
    public void studentIteratorNextExceptionTest_1P() {
        Student s = new Student("A",1,2);
        Iterator<CourseReport> it = s.iterator();
        assertFalse("iterator should not have elements",it.hasNext());
        it.next();
    }

    @Test(expected =  AverageCalcException.class)
    public void studentAverageCalcExceptionTest_1P(){
        Student s = new Student("A",1,2);
        s.getWeightedAverage();
    }

    @Test
    public void gradingSystemCorruptedAverageTest_1P(){
        GradingSystem gradingSystem= new GradingSystem("BGU", 2);
        gradingSystem.addStudent(new Student());
        assertEquals("Average should be -1 in case with student who raises exception", -1, gradingSystem.getAverage(),0);
    }

    @Test
    public void corruptedAverageStudentNameTest_1P(){
        Student s = new Student("Adam",1,0);
        try{
            s.getWeightedAverage();
            fail("getWeightedAverage() should have thrown an exception");
        } catch (AverageCalcException ex){
            assertEquals("name do not match", "Adam", ex.getStudentName());
        }
    }
}
