package assignment3;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertEquals;

public class Ass3Test {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10);

    //Helpers

    CourseReport GetCourseReport1(){
        return new CourseReport("IOOP", 1, 93);
    }

    CourseReport GetCourseReport2(){
        return new CourseReport("AP", 2, 90);
    }

    Student GetStudent1(){
        return new Student("Amy Stake", 1, 2);
    }

    Student GetStudent2(){
        return new Student("Chris P. Bacon", 2, 2);
    }

    // CourseReport Tests

    @Test
    public void courseReportCtr_1P(){
        CourseReport cr=GetCourseReport1();
        assertEquals("incorrect name", "IOOP", cr.getName());
        assertEquals("incorrect points", 1, cr.getPoints());
        assertEquals("incorrect grade", 93, cr.getGrade(),0);
    }

    @Test
    public void courseReportCopyCtr_1P(){
        CourseReport cr=GetCourseReport1();
        CourseReport copy = new CourseReport(cr);
        assertEquals("incorrect name", "IOOP", copy.getName());
        assertEquals("incorrect points", 1, copy.getPoints());
        assertEquals("incorrect grade", 93, copy.getGrade(),0);
    }

    // Student Tests

    @Test
    public void studentCtr_1P(){
        Student student = new Student("Amy Stake", 1, 2);
        assertEquals("incorrect name", "Amy Stake", student.getName());
        assertEquals("incorrect id", 1, student.getId());
    }

    @Test
    public void addCourseReport_1P(){
        Student student = GetStudent1();
        student.addCourse(GetCourseReport1());
        assertEquals("incorrect name", "IOOP", student.getCourseReports()[0].getName());
        assertEquals("incorrect points", 1, student.getCourseReports()[0].getPoints());
        assertEquals("incorrect grade", 93, student.getCourseReports()[0].getGrade(),0);
    }

    @Test
    public void weightedAverage_1P(){
        Student student = GetStudent1();
        student.addCourse(GetCourseReport1());
        student.addCourse(GetCourseReport2());
        assertEquals("incorrect weighted average", 91, student.getWeightedAverage(),0.000001);
    }

    // GradingSystem Tests

    @Test
    public void gradingSystemCtr_1P(){
        GradingSystem gd = new GradingSystem("BGU",2);
        assertEquals("incorrect name", "BGU", gd.getName());
    }

    @Test
    public void addStudent_1P(){
        GradingSystem gd = new GradingSystem("BGU",2);
        gd.addStudent(GetStudent1());
        assertEquals("incorrect name", "Amy Stake", gd.getStudents()[0].getName());
        assertEquals("incorrect id", 1, gd.getStudents()[0].getId());
    }

    @Test
    public void studentAverage_1P(){
        GradingSystem gd = new GradingSystem("BGU",2);
        Student s1 = GetStudent1();
        s1.addCourse(GetCourseReport1());
        s1.addCourse(GetCourseReport2());
        Student s2 = GetStudent2();
        s2.addCourse(GetCourseReport1());
        gd.addStudent(s1);
        gd.addStudent(s2);
        assertEquals("incorrect weighted average", 92, gd.getAverage(),0.000001);
    }
}
