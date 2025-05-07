package DataStructureProject;

public class SubtractOp extends BinaryOp {
    private char subtractionSymbol;
    private double Precedence = 1;

    //a constructor
    public SubtractOp() {
        super();
        this.subtractionSymbol = '-';
    }

    //a getter function that returns the operator's precedence
    @Override
    public double getPrecedence() {
        return this.Precedence;
    }

    //a function that calculates the operation on two operands
    @Override
    public double operate(double left, double right) {
        return left - right;
    }

    //a function that returns the string presentation of the subtraction operator
    @Override
    public String toString() {
        return "-";
    }
}
