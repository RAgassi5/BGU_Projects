package DataStructureProject;

public class MultiplyOp extends BinaryOp {
    private char multiplicationSymbol;
    private double Precedence = 2;

    //a constructor
    public MultiplyOp() {
        super();
        this.multiplicationSymbol = '*';
    }

    //a getter function that returns the operator's precedence
    @Override
    public double getPrecedence() {
        return this.Precedence;
    }

    //a function that calculates the operation on two operands
    @Override
    public double operate(double left, double right) {
        return left * right;
    }

    //a function that returns the string presentation of the multiplication operator
    @Override
    public String toString() {
        return "*";
    }
}
