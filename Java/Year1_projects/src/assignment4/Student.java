package assignment4;


public class Student {
    private String name;
    private int id;
    private CourseReport[] courseReports;

    //a default constructor
    public Student() {
        name = null;
        courseReports = null;
        id = 0;
    }

    //main constructor
    public Student(String name, int id, int numOfCourses) {
        this.name = name;
        this.id = id;
        this.courseReports = new CourseReport[numOfCourses];
    }

    //a copy constructor
    public Student(Student other) {
        this.name = other.name;
        this.id = other.id;
        this.courseReports = other.courseReports;
    }

    //a getter function for the name
    public String getName() {
        return this.name;
    }

    //a setter function for the name
    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }


    //a getter function for the ID
    public int getId() {
        return id;
    }

    //a setter function for the ID
    public void setId(int id) {
        this.id = id;
    }

    //a getter function for the courseReports
    public CourseReport[] getCourseReports() {
        if (this.courseReports == null) {
            return null;
        }

        CourseReport[] courseReportsCopy = new CourseReport[this.courseReports.length];
        for (int i = 0; i < courseReportsCopy.length; i++) {
            courseReportsCopy[i] = this.courseReports[i];
        }
        return courseReportsCopy;
    }

    //this function adds a new instance of a CourseReport to the courseReport array
    public void addCourse(CourseReport courseReport) {
        if (this.courseReports == null) {
            return;
        }

        for (int i = 0; i < courseReports.length; i++) {
            if (courseReports[i] == null) {
                courseReports[i] = courseReport;
                break;
            }
        }

    }

    //this function calculates the grade average, considering the amount of points for each course
    //if courseReports is empty, the use of this function is not necessary
    public double getWeightedAverage() {
        if (this.courseReports == null) {
            return 0;
        }

        if (courseReports.length == 0) {
            return 0;
        } else {
            double totalAverage = 0;
            int totalPoints = 0;
            for (int i = 0; i < courseReports.length; i++) {
                if (courseReports[i] == null) {
                    continue;
                }
                double courseAverage = courseReports[i].getGrade() * courseReports[i].getPoints();
                totalAverage = totalAverage + courseAverage;
                totalPoints = totalPoints + courseReports[i].getPoints();
            }
            if (totalPoints == 0) {
                return 0;
            }
            return (totalAverage / totalPoints);
        }
    }


    // a toString function that prints out the Student
    public String toString() {
        String courses;
        int nonNullCourses = 0;

        if (this.courseReports != null) {
            courses = "[";
            for (int i = 0; i < courseReports.length; i++) {
                if (courseReports[i] != null) {
                    nonNullCourses++;
                    CourseReport current = courseReports[i];
                    courses += current.toString();
                    courses += ", ";
                }
            }
            if (nonNullCourses != 0) {
                courses = courses.substring(0, courses.length() - 2);
            }
            courses = courses + "]";
        } else {
            courses = "[]";
        }
        return this.getName() + ": " + this.getId() + " " + courses;
    }


    // a function that compares two different students
    public boolean equals(Student obj) {
        if (obj == null) {
            return false;
        } else if (!this.name.equals(obj.name) || this.id != obj.id) {
            return false;
        }

        for (int i = 0; i < obj.getCourseReports().length; i++) {
            if (this.compareCourses(obj.courseReports[i]) == false) {
                return false;
            }
        }
        return true;
    }

    // a boolean function that compares the other student's current CourseReport to the current student
    public boolean compareCourses(CourseReport current) {
        if (current == null) {
            return true;
        }

        for (int n = 0; n < this.getCourseReports().length; n++) {
            if (this.courseReports[n] != null) {
                if (this.courseReports[n].equals(current)) {
                    return true;
                }
            }
        }
        return false;
    }

}
