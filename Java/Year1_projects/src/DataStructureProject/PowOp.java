package DataStructureProject;

public class PowOp extends BinaryOp {
    private char powerSymbol;
    private double Precedence = 3;

    //a constructor
    public PowOp() {
        super();
        this.powerSymbol = '^';
    }

    //a getter function that returns the operator's precedence
    @Override
    public double getPrecedence() {
        return this.Precedence;
    }

    //a function that calculates the operation on two operands
    @Override
    public double operate(double left, double right) {
        return Math.pow(left, right);
    }

    //a function that returns the string presentation of the power operator
    @Override
    public String toString() {
        return "^";
    }
}
