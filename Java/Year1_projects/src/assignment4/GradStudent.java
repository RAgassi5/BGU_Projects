package assignment4;

public class GradStudent extends Student {
    private double bonus = 0;


    //a default constructor
    public GradStudent(String s, int i, int i1) {
        super();
    }

    // main constructor
    public GradStudent(String name, int id, int numOfCourses, double bonus) {
        super(name, id, numOfCourses);
        setBonus(bonus);
    }

    // a copy constructor
    public GradStudent(GradStudent other) {
        super();
        this.bonus = other.bonus;
    }

    // a getter function for the bonus points
    public double getBonus() {
        return this.bonus;
    }

    // a setter function fot the bonus points
    public void setBonus(double bonus) {
        if (bonus > 0) {
            this.bonus = bonus;
        }
    }

    // a function that calculates the student's average considering the student's bonus points
    public double getWeightedAverage() {
        int totalPoints = 0;
        for (int i = 0; i < this.getCourseReports().length; i++) {
            totalPoints = totalPoints + this.getCourseReports()[i].getPoints();
        }
        if (totalPoints >= 10) {
            return super.getWeightedAverage() + getBonus();
        }
        else {
            return super.getWeightedAverage() - getBonus();
        }
    }
}