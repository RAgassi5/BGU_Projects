package assignment5;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Iterator;

import static org.junit.Assert.*;

public class Ass5Test {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);

    @Test
    public void courseReportCompare1_1P() {
        CourseReport cr1 = new CourseReport("IOOP", 2, 3);
        CourseReport cr2 = new CourseReport("IOOP", 2, 2);
        assertTrue("greater courseReport reoprted lesser or equal", cr1.compareTo(cr2) > 0);
    }

    @Test
    public void courseReportCompare2_1P() {
        CourseReport cr1 = new CourseReport("IOOP", 2, 3);
        CourseReport cr2 = new CourseReport("IOOP", 2, 3);
        assertEquals("Equal courses reported as non-equal", 0, cr1.compareTo(cr2));
    }

    @Test
    public void courseReportCompare3_1P() {
        CourseReport cr1 = new CourseReport("AOOP", 2, 3);
        CourseReport cr2 = new CourseReport("IOOP", 2, 3);
        assertTrue("course lexicographically lesser is reported not", cr1.compareTo(cr2) < 0);
    }

    @Test
    public void studentEmptyIterator_1P() {
        Student student = new Student("Dad", 2, 3);
        Iterator<CourseReport> iterator = student.iterator();
        assertFalse("Empty student reports having students", iterator.hasNext());

    }

    @Test
    public void studentIterator_1P() {
        Student student = new Student("Dad", 2, 3);
        CourseReport courseReport = new CourseReport("Sumo", 1, 1);
        student.addCourse(courseReport);
        Iterator<CourseReport> iterator = student.iterator();
        assertTrue("non-empty student reports having no students", iterator.hasNext());
        CourseReport courseReportFromIterator = iterator.next();
        assertSame("iterator should return the object and not a copy", student.getCourseReports()[0], courseReportFromIterator);
        assertEquals("iterator returned non-equal object to the one entered", courseReport, courseReportFromIterator);
        assertFalse("there appears to be more students than there should be", iterator.hasNext());
    }

    @Test
    public void studentAverageComparator1_1P() {
        Student student1 = new Student("First Dad", 2, 3);
        Student student2 = new Student("Second Dad", 2, 3);
        CourseReport cr1 = new CourseReport("Sumo", 1, 90);
        CourseReport cr2 = new CourseReport("Sumo Again", 3, 95);
        student1.addCourse(cr1);
        student2.addCourse(cr1);
        Student.AverageComparator comparator = new Student.AverageComparator();
        assertEquals("equal averages are reported non-equal", 0, comparator.compare(student1, student2));
    }

    @Test
    public void studentAverageComparator2_1P() {
        Student student2 = new Student("Second Dad", 2, 3);
        Student student3 = new Student("Step Dad", 2, 3);
        CourseReport cr1 = new CourseReport("Sumo", 1, 90);
        CourseReport cr2 = new CourseReport("Sumo Again", 3, 95);
        student2.addCourse(cr1);
        student3.addCourse(cr1);
        student3.addCourse(cr2);
        Student.AverageComparator comparator = new Student.AverageComparator();
        assertTrue("lesser average is reported as not", comparator.compare(student2, student3) < 0);
    }
}
