package assignment3;

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
        return name;
    }

    //a setter function for the name
    public void setName(String name) {
       if(name != null){
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
        return courseReports;
    }

    //this function adds a new instance of a CourseReport to the courseReport array
    public void addCourse(CourseReport courseReport) {
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
        if (courseReports.length == 0) {
            return 0;
        }
        else {
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
}
