package assignment6;


public class GradingSystem {
    private String name;
    private Student[] students;

    //default constructor
    public GradingSystem() {
        name = null;
        students = null;
    }

    //main constructor
    public GradingSystem(String name, int studentCount) {
        this.name = name;
        this.students = new Student[studentCount];
    }

    //a copy constructor
    public GradingSystem(GradingSystem other) {
        this.name = other.name;
        this.students = other.students;
    }

    // a getter function for the name
    public String getName() {
        return name;
    }

    //a setter function for the name
    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    //a getter function for the Students array
    public Student[] getStudents() {
        return students;
    }

    //a function that adds a new instance of Student to the students array
    public void addStudent(Student student) {
        for (int i = 0; i < students.length; i++) {
            if (students[i] == null) {
                students[i] = student;
                break;
            }
        }
    }

    //this function calculates the total average for all the students that are currently in the GradingSystem
    public double getAverage() {
        double averageSum = 0;
        if (students == null ) {
            return 0;
        }
        if (students.length == 0) {
            return 0;
        } else {
            try {
                for (int i = 0; i < students.length; i++) {
                    if (students[i] == null) {
                        continue;
                    }
                    double StudentAverageSum = students[i].getWeightedAverage();
                    averageSum = averageSum + StudentAverageSum;
                }
                return (averageSum / students.length);
            } catch (AverageCalcException calcException) {
                return -1;
            }

        }
    }
}

