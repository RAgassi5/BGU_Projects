import assignment2.GaussianElimination;
import assignment3.GradingSystem;
import assignment4.BonusStudent;
import  assignment4.CourseReport;
import assignment4.GradStudent;
import assignment4.Student;

public class Main {
    public static void main(String[] args) {
        CourseReport hi = new CourseReport("gg",25,90);
        CourseReport hi2 = new CourseReport("hh",4,60);
        CourseReport hi3 = new CourseReport("bb",4,20);
        //hi.setGrade(-1);
        Student roii = new Student("Chris. P Bacon", 207455213, 2);
        roii.addCourse(hi);
        roii.addCourse(hi2);
        Student shmolik = new Student("Chris. P Bacon", 207455213, 0);
        //shmolik.addCourse(hi2);
        //shmolik.addCourse(hi);
        GradStudent Hoe = new GradStudent("kylian",22,2,2);
        Hoe.addCourse(hi);
        Hoe.addCourse(hi2);
        GradStudent Joe = new GradStudent("kylian",22,2,-1);
        Joe.addCourse(hi);
        Joe.addCourse(hi2);
        BonusStudent Noa = new BonusStudent("noa",44,1,3);
        Noa.addCourse(hi);
        //System.out.println(hi.toString());
        //System.out.println(roii.getWeightedAverage());
        //System.out.println(Hoe.getWeightedAverage());
        System.out.println(hi.getPoints());
        System.out.println(Noa.toString());
        ;


        CourseReport GetCourseReport1 = new CourseReport("IOOP", 1, 90);
        CourseReport GetCourseReport2 = new CourseReport("AP", 2, 93);
        BonusStudent GetBonusStudent = new BonusStudent("Chris. P Bacon", 207455213, 2, 3);
        GetBonusStudent.addCourse(hi);
        GetBonusStudent.addCourse(hi2);

        System.out.println(GetBonusStudent.toString());
        System.out.println(GetBonusStudent.equals(roii));






    }


}


