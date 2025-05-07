package DataStructureProject;

public class DivideOp extends BinaryOp {
    private char divisionSymbol;
    private double Precedence = 2;

    //a constructor
    public DivideOp() {
        super();
        this.divisionSymbol = '/';
    }

    //a getter function that returns the operator's precedence
    @Override
    public double getPrecedence() {
        return this.Precedence;
    }

    //a function that calculates the operation on two operands
    @Override
    public double operate(double left, double right) {
        if (right == 0) {
            throw new ArithmeticException("Can't divide by zero");
        }
        return left / right;
    }

    //a function that returns the string presentation of the division operator
    @Override
    public String toString() {
        return "/";
    }

}
