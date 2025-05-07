package assignment4;

public class BonusStudent extends Student {
    private double mult = 1;

    // a default constructor
    public BonusStudent() {
        super();

    }

    // main constructor
    public BonusStudent(String name, int id, int numOfCourses, double mult) {
        super(name, id, numOfCourses);
        setMult(mult);
    }

    //a copy constructor
    public BonusStudent(BonusStudent other) {
        super(other);
        this.mult = other.mult;
    }

    //a getter function for the variable "mult"
    public double getMult() {
        return this.mult;
    }

    // a setter function for the variable "mult"
    public void setMult(double mult) {
        if (mult > 1) {
            this.mult = mult;
        }

    }

    // a function that calculates the student's average considering the student's "mult" variable
    public double getWeightedAverage() {
        int totalPoints = 0;

        for (int i = 0; i < this.getCourseReports().length; i++) {
            if (this.getCourseReports()[i] != null) {
                totalPoints = totalPoints + this.getCourseReports()[i].getPoints();
            }

        }
        int updatedPoints = totalPoints / 10;
        return super.getWeightedAverage() + updatedPoints * getMult();
    }
}