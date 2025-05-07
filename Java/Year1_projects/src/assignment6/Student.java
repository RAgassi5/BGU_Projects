package assignment6;


import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Student implements Iterable<CourseReport> {
    private String name;
    private int id;
    private final CourseReport[] courseReports;

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
        System.arraycopy(this.courseReports, 0, courseReportsCopy, 0, courseReportsCopy.length);
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
            throw new AverageCalcException(this.name);
        }

        if (courseReports.length == 0) {
            throw new AverageCalcException(this.name);
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
                throw new AverageCalcException(this.name);
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
            if (!this.compareCourses(obj.courseReports[i])) {
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

    // an iterator function
    @Override
    public Iterator<CourseReport> iterator() {
        return new StudentIterator(this);
    }


    public static class StudentIterator implements Iterator<CourseReport> {
        private int currentIndex = 0;
        private final Student studentInstance;

        //student iterator constructor
        public StudentIterator(Student instance) {
            studentInstance = instance;
        }


        //a function that returns True if a next element exists, implemented from the Iterator interface
        @Override
        public boolean hasNext() {
            for (int currentIndex = 0; currentIndex < studentInstance.courseReports.length; currentIndex++) {
                if (studentInstance.courseReports[currentIndex] != null) {
                    return true;
                }
            }
            return false;
        }


        //this function return the next value or object in an iterable class
        @Override
        public CourseReport next() {
            while (hasNext()) {
                return studentInstance.courseReports[currentIndex++];
            }
            throw new NoSuchElementException();
        }
    }

    //compares averages between two different students
    public static class AverageComparator implements Comparator<Student> {
        @Override
        public int compare(Student o1, Student o2) {
            if (o1.getWeightedAverage() > o2.getWeightedAverage()) {
                return 1;
            } else if (o1.getWeightedAverage() < o2.getWeightedAverage()) {
                return -1;
            }
            return 0;
        }
    }

    // a function that compares one student's points to another student
    public static class CoursePointsComparator implements Comparator<Student> {
        @Override
        public int compare(Student o1, Student o2) {
            int o1Points = 0;
            int o2Points = 0;
            for (int i = 0; i < o1.getCourseReports().length; i++) {
                o1Points += o1.getCourseReports()[i].getPoints();
            }
            for (int j = 0; j < o2.getCourseReports().length; j++) {
                o2Points += o2.getCourseReports()[j].getPoints();
            }
            if (o1Points > o2Points) {
                return 1;
            } else if (o1Points < o2Points) {
                return -1;
            }
            return 0;
        }
    }
}
