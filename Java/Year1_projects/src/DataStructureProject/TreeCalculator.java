package DataStructureProject;

public class TreeCalculator extends Calculator {
    private ExpressionTree currTree;

    /**
     * a function that returns the value of a given postfix expression
     *
     * @param expr a postfix expression
     * @return the calculated value of the given postfix expression
     */
    @Override
    public double evaluate(String expr) {
        ExpressionTree currentTree = new ExpressionTree(expr);
        this.currTree = currentTree;
        return currentTree.evaluateExpression();
    }

    /**
     * a getter function
     *
     * @return the tree's infix expression
     */
    public String getInfix() {
        return this.currTree.getterInfix();
    }

    /**
     * a getter function
     *
     * @return the tree's postfix expression
     */
    public String getPostfix() {
        return this.currTree.getterPostfix();
    }

    /**
     * a getter function
     *
     * @return the tree's prefix expression
     */
    public String getPrefix() {
        return this.currTree.getterPrefix();
    }


}
