package DataStructureProject;

public class ValueToken extends CalcToken {
    private double val;

    //a constructor
    public ValueToken(double val) {
        super();
        this.val = val;
    }

    //a getter function for the value of the ValueToken
    public double getValue() {
        return this.val;
    }


    //TODO: go over with idoster
    //a function that returns the string presentation of the ValueToken
    @Override
    public String toString() {
        return String.valueOf(this.getValue());
    }

}
