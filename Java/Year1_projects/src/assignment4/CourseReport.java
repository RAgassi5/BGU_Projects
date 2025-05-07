package assignment4;

public class CourseReport {
    private String name;
    private int points;
    private double grade;

    //a default constructor
    public CourseReport() {
        name = null;
        points = 0;
        grade = 0;
    }

    //main constructor
    public CourseReport(String name, int points, double grade) {
        this.name = name;
        this.points = points;
        this.grade = grade;
    }

    //a copy constructor
    public CourseReport(CourseReport other) {
        this();
        name = other.name;
        points = other.points;
        grade = other.grade;
    }

    //a getter function for the grade
    public double getGrade() {
        return grade;
    }

    //a setter function for the grade
    public void setGrade(double grade) {
        if (grade < 0) {
            this.grade = 0;
        } else if (grade > 100) {
            this.grade = 100;
        } else {
            this.grade = grade;
        }
    }


    // a getter function for the name
    public String getName() {
        return this.name;
    }

    // a setter function for the name
    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    // a getter function for the points
    public int getPoints() {
        return points;
    }

    // a setter function for the points
    public void setPoints(int points) {
        if (points > 0) {
            this.points = points;
        }
    }

    // a toString function that prints out CourseReport
    public String toString() {
        String name = "";
        if (name != null) {
            name = this.name;
        }

        return "[" + name + ", " + this.points + ", " + this.grade + "]";
    }

    // compares a CourseReport to another CourseReport
    public boolean equals(CourseReport obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        return this.name.equals(obj.name) && this.points == obj.points && this.grade == obj.grade;
    }
}


