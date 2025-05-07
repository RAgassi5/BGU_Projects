package assignment6;

public class AverageCalcException extends RuntimeException {
    String studentName;

    //a constructor
    public AverageCalcException(String studentName) {
        super("Invalid average calculation: " + studentName);
        this.studentName = studentName;
    }

    //a getter function
    public String getStudentName() {
        return this.studentName;
    }
}
