package assignment7;

import java.io.*;

public class CourseReport implements Comparable<CourseReport>, Serializable {
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


    // a function that compares one Coursereport to another
    public int compareTo(CourseReport other) {
        if (this.name.compareTo(other.name) == 0 && this.points == other.points && this.grade == other.grade) {
            return 0;
        }

        if (this.grade < other.grade) {
            return -1;
        } else if (this.points < other.points) {
            return -1;
        } else if (this.name.compareTo(other.name) < 0) {
            return -1;
        }

        return 1;
    }

    //a function that saves the student's data to a file
    public void saveToTextFile(File file) throws IOException {
        try (BufferedWriter WriteToFile = new BufferedWriter(new FileWriter(file))) {
            WriteToFile.write(this.toString());
        }
    }

    // a CourseReport constructor that loads a new instance from a given file
    public CourseReport(File file) {
        try (BufferedReader readFile = new BufferedReader(new FileReader(file))) {
            String line = readFile.readLine();
            if (line != null) {
                line = line.substring(1, line.length() - 1);
                String[] parts = line.split(", ");
                if (parts.length == 3) {
                    this.name = parts[0];
                    this.points = Integer.parseInt(parts[1]);
                    this.grade = Double.parseDouble(parts[2]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading from file " + file);

        }
    }
}
