package assignment4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class Ass4Test {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(500);

    //Helpers

    CourseReport GetCourseReport1() {
        return new CourseReport("IOOP", 1, 90);
    }

    CourseReport GetCourseReport2() {
        return new CourseReport("AP", 2, 93);
    }

    CourseReport GetCourseReport3() {
        return new CourseReport("SUMO", 25, 90);
    }

    CourseReport GetCourseReport4() {
        return new CourseReport("SUMO", 24, 90.56);
    }

    GradStudent GetGradStudent() {
        return new GradStudent("Amy Stake", 1, 2, 5);
    }

    BonusStudent GetBonusStudent() {
        return new BonusStudent("Chris P. Bacon", 2, 2, 3);
    }

    Student GetStudent() {
        return new Student("Chris P. Bacon", 2, 3);
    }

    @Test
    public void courseReportEquality_1P() {
        CourseReport c1 = GetCourseReport3();
        CourseReport c2 = GetCourseReport4();
        assertNotEquals("Non-equal courses are reported equals", c1, c2);
    }

    @Test
    public void courseReportToString_1P() {
        CourseReport c2 = GetCourseReport4();
        assertEquals("toString problems", "[SUMO, 24, 90.56]", c2.toString());
    }

    @Test
    public void gradStudentCtr_1P() {
        GradStudent s1 = GetGradStudent();
        assertEquals("incorrect name", "Amy Stake", s1.getName());
        assertEquals("incorrect id", 1, s1.getId());
        assertEquals("incorrect bonus", 5, s1.getBonus(), 0);
    }

    @Test
    public void gradStudentAverage_1P() {
        GradStudent s1 = GetGradStudent();
        s1.addCourse(GetCourseReport1());
        s1.addCourse(GetCourseReport2());
        assertEquals("Grad student average is incorrect", 87, s1.getWeightedAverage(), 0);
    }

    @Test
    public void bonusStudent_1P() {
        BonusStudent s1 = GetBonusStudent();
        assertEquals("incorrect name", "Chris P. Bacon", s1.getName());
        assertEquals("incorrect id", 2, s1.getId());
        assertEquals("incorrect mult", 3, s1.getMult(), 0);
    }

    @Test
    public void bonusStudentAverage_1P() {
        BonusStudent s2 = GetBonusStudent();
        s2.addCourse(GetCourseReport3());
        assertEquals("Bonus student average is incorrect", 96, s2.getWeightedAverage(), 0);
    }

    @Test
    public void studentEquality_1P() {
        Student s1 = GetBonusStudent();
        Student s2 = GetStudent();
        assertTrue("Equal students with no courses are reported not equal", s1.equals(s2) && s2.equals(s1));
        s1.addCourse(GetCourseReport1());
        assertFalse("Students with different courses are reported equal", s1.equals(s2) && s2.equals(s1));
        s1.addCourse(GetCourseReport2());
        s2.addCourse(GetCourseReport2());
        s2.addCourse(GetCourseReport1());
        assertTrue("Students with same courses are reported not equal", s1.equals(s2) && s2.equals(s1));
    }

    @Test
    public void studentToString_1P() {
        Student s1 = GetBonusStudent();
        assertEquals("student toString is wrong", "Chris P. Bacon: 2 []", s1.toString());
        s1.addCourse(GetCourseReport1());
        assertEquals("student toString is wrong", "Chris P. Bacon: 2 [[IOOP, 1, 90.0]]", s1.toString());
        s1.addCourse(GetCourseReport2());
        assertEquals("student toString is wrong", "Chris P. Bacon: 2 [[IOOP, 1, 90.0], [AP, 2, 93.0]]", s1.toString());
    }
}
