package DataStructureProject;

public class StackCalculator extends Calculator {

    /**
     * a function that calculates a postfix expression's value
     *
     * @param expr a postfix expression to be calculated
     * @return the calculated result for the wanted expression
     */
    @Override
    public double evaluate(String expr) {

        if (expr == null || expr.isEmpty()) {
            return 0;
        }
        ExpTokenizer TokenedExpr = new ExpTokenizer(expr);
        StackAsArray ExpStack = new StackAsArray();


        while (TokenedExpr.hasMoreElements()) {
            Object currentToken = TokenedExpr.nextElement();

            if (currentToken instanceof BinaryOp) {
                ValueToken firstPop = (ValueToken) ExpStack.pop();
                ValueToken secondPop = (ValueToken) ExpStack.pop();
                double calculation = ((BinaryOp) currentToken).operate(secondPop.getValue(), firstPop.getValue());
                ValueToken operatedValue = new ValueToken(calculation);
                ExpStack.push(operatedValue);
            } else {
                ExpStack.push(currentToken);
            }
        }
        ValueToken result = (ValueToken) ExpStack.pop();
        return result.getValue();
    }

    /**
     * a function that transforms an infix expression to a postfix expression
     *
     * @param expr a string of an infix expression
     * @return a postfix expression
     */
    public String infixToPostfix(String expr) {
        if (expr == null) {
            return "";
        }

        String PostFixResult = "";
        StackAsArray TransformationStack = new StackAsArray();
        ExpTokenizer TokenedExpr = new ExpTokenizer(expr);

        for (int i = 0; TokenedExpr.hasMoreElements(); i++) {
            Object currentToken = TokenedExpr.nextElement();
            if (currentToken instanceof OpenBracket) {
                TransformationStack.push(currentToken);
            } else if (currentToken instanceof CloseBracket) {
                Object popped = TransformationStack.pop();
                while (!(popped instanceof OpenBracket) && popped != null) {
                    PostFixResult += popped.toString() + " ";
                    popped = TransformationStack.pop();
                }
            } else if (currentToken instanceof BinaryOp) {
                if (TransformationStack.isEmpty()) {
                    TransformationStack.push(currentToken);
                } else {
                    Object popped = TransformationStack.pop();
                    if (popped instanceof OpenBracket) {
                        TransformationStack.push(popped);
                        TransformationStack.push(currentToken);
                    } else {
                        while (!(popped instanceof BinaryOp) || ((BinaryOp) popped).getPrecedence() > ((BinaryOp) currentToken).getPrecedence()) {
                            if (popped == null) {
                                break;
                            }

                            if (popped instanceof OpenBracket) {
                                TransformationStack.push(popped);
                                break;
                            }

                            PostFixResult += popped.toString() + " ";
                            popped = TransformationStack.pop();

                        }
                        if (popped instanceof BinaryOp) {
                            TransformationStack.push(popped);
                        }

                        TransformationStack.push(currentToken);
                    }
                }
            } else if (currentToken instanceof ValueToken) {
                PostFixResult += currentToken.toString() + " ";
            }
        }


        while (!TransformationStack.isEmpty()) {
            PostFixResult += TransformationStack.pop().toString() + " ";
        }


        return PostFixResult.substring(0, PostFixResult.length() - 1);
    }

    public static void main(String[] args) {
        StackCalculator Or = new StackCalculator();
        System.out.println(Or.infixToPostfix("2 + 3 * 4 - 5 / 6"));
    }
}
