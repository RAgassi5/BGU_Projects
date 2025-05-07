package DataStructureProject;

/**
 * an abstract class for all types of calculators
 */
public abstract class Calculator {
    /**
     * all subclasses implement this method
     * @param expr an expression to be evaluated
     * @return a calculated answer to the given expression
     */
    public abstract double evaluate(String expr);
}
