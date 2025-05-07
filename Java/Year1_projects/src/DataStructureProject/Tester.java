package DataStructureProject;

/**
 * This is a testing framework. Use it extensively to verify that your code is working
 * properly.
 */
public class Tester {

    private static boolean testPassed = true;
    private static int testNum = 0;

    /**
     * This entry function will test all classes created in this assignment.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {

        /* TODO - write a function for each class */
        // Each function here should test a different class. You should test ExpTokenizer as well.

        //ExpTokenizer
        testExpTokenizer();
        //Abstract classes
        testAbstractClasses();

        //BinaryOp
        testAddOp();
        testSubtractOP();
        testDivideOP();
        testDivideByZero();
        testMultiplyOP();
        testPowOp();
        //...

        // Calculators
        testStackCalculator();
        testTreeCalculator();
        //...


        // Notifying the user that the code have passed all tests.
        if (testPassed) {
            System.out.println("All " + testNum + " tests passed!");
        }
    }

    /**
     * This utility function will count the number of times it was invoked.
     * In addition, if a test fails the function will print the error message.
     *
     * @param exp The actual test condition
     * @param msg An error message, will be printed to the screen in case the test fails.
     */
    private static void test(boolean exp, String msg) {
        testNum++;

        if (!exp) {
            testPassed = false;
            System.out.println("Test " + testNum + " failed: " + msg);
        }
    }

    private static void testAbstractClasses() {
        AddOp AddOP = new AddOp();
        ValueToken num = new ValueToken(15);
        TreeCalculator tree = new TreeCalculator();
        StackCalculator stack = new StackCalculator();
        test((AddOP instanceof BinaryOp), "AddOp should extend the BinaryOp class");
        test((num instanceof CalcToken), "The ValueToken should implement the abstract class CalToken");
        test((AddOP instanceof CalcToken), "The BinaryOp should implement the abstract class CalToken");
        test(tree instanceof Calculator, "The TreeCalculator class should extend Calculator");
        test(stack instanceof Calculator,"The StackCalculator class should extend Calculator");
         }

    /**
     * a tester function for AddOp class
     */
    private static void testAddOp() {
        AddOp AddOP = new AddOp();
        SubtractOp subtractOp = new SubtractOp();
        MultiplyOp multiplyOp = new MultiplyOp();
        DivideOp divideOp = new DivideOp();
        PowOp powOp = new PowOp();


        test((AddOP.toString().equals("+")), "The string + should be printed.");
        test((AddOP.operate(1.0, 2.5) == 3.5), "The answer should be 3.5 .");
        test((AddOP.operate(-0.92, -1.08) == -2), "The answer should be -2");
        test((AddOP.getPrecedence() == subtractOp.getPrecedence()), "Both operators should have equal precedence");
        test((AddOP.getPrecedence() < multiplyOp.getPrecedence()), "The AddOp has a smaller precedence");
        test((AddOP.getPrecedence() < divideOp.getPrecedence()), "The AddOp has a smaller precedence");
        test((AddOP.getPrecedence() < powOp.getPrecedence()), "The AddOp has a smaller precedence");
    }

    /**
     * a tester function for SubtractOp class
     */
    private static void testSubtractOP() {
        SubtractOp subtractOp = new SubtractOp();
        MultiplyOp multiplyOp = new MultiplyOp();
        DivideOp divideOp = new DivideOp();
        PowOp powOp = new PowOp();


        test(subtractOp.toString().equals("-"), "The string - should be printed");
        test((subtractOp.operate(-2, -3)) == 1, "The answer should be 1");
        test((subtractOp.operate(2.5, -3.5)) == 6, "The answer should be 6");
        test((subtractOp.getPrecedence() < divideOp.getPrecedence()), "The SubtractOp has a smaller precedence");
        test((subtractOp.getPrecedence() < multiplyOp.getPrecedence()), "The SubtractOp has a smaller precedence");
        test((subtractOp.getPrecedence() < powOp.getPrecedence()), "The SubtractOp has a smaller precedence");
    }

    /**
     * a tester function for DivideOp class
     */
    private static void testDivideOP() {
        AddOp AddOP = new AddOp();
        MultiplyOp multiplyOp = new MultiplyOp();
        DivideOp divideOp = new DivideOp();
        PowOp powOp = new PowOp();

        test(divideOp.toString().equals("/"), "The string / should be printed");
        test(divideOp.operate(10, 2) == 5, "The answer should be 5");
        test(divideOp.operate(1.65, 0.5) == 3.3, "The answer should be 3.3");
        test((divideOp.getPrecedence() == multiplyOp.getPrecedence()), "Both operators should have the same precedence");
        test((divideOp.getPrecedence() < powOp.getPrecedence()), "The DivideOp has a smaller precedence");
        test((divideOp.getPrecedence() > AddOP.getPrecedence()), "The DivideOp has a greater precedence");
    }

    /**
     * a tester function for division by 0
     */

    private static void testDivideByZero() {
        DivideOp divideOp = new DivideOp();

        try {
            divideOp.operate(10, 0);
            test(false, "Can't divide by zero");
        } catch (ArithmeticException exception) {
            test(true, "");
        }
    }

    /**
     * a tester function for MultiplyOp class
     */
    private static void testMultiplyOP() {
        SubtractOp subtractOp = new SubtractOp();
        MultiplyOp multiplyOp = new MultiplyOp();
        PowOp powOp = new PowOp();

        test(multiplyOp.toString().equals("*"), "The string * should be printed");
        test((multiplyOp.operate(10, 5)) == 50, "The answer should be 50");
        test((multiplyOp.operate(-10, 5)) == -50, "The answer should be -50");
        test((multiplyOp.operate(0, 5)) == 0, "The answer should be 0");
        test((multiplyOp.getPrecedence() < powOp.getPrecedence()), "The MultiplyOp has a smaller precedence");
        test((multiplyOp.getPrecedence() > subtractOp.getPrecedence()), "The MultiplyOp has a greater precedence");
    }

    /**
     * a tester function for PowOp class
     */
    private static void testPowOp() {
        SubtractOp subtractOp = new SubtractOp();
        MultiplyOp multiplyOp = new MultiplyOp();
        PowOp powOp = new PowOp();

        test(powOp.toString().equals("^"), "The string ^ should be printed");
        test((powOp.operate(3, 2)) == 9, "The answer should be 9");
        test((powOp.operate(5, -2)) == 0.04, "The answer should be 0.04");
        test((powOp.operate(-2, 3)) == -8, "The answer should be -8");
        test((powOp.getPrecedence() > subtractOp.getPrecedence()), "The PowOp has a greater precedence");
        test((powOp.getPrecedence() > multiplyOp.getPrecedence()), "The PowOp has a greater precedence");


    }

    /**
     * a tester function for the StackCalculator class.
     */
    private static void testStackCalculator() {

        StackCalculator tester = new StackCalculator();

        String postExp = tester.infixToPostfix("2 + 3");
        test(postExp.equals("2.0 3.0 +"), "The output of \"2 3 -\" should be  2.0 3.0 +");
        double result = tester.evaluate(postExp);
        test(result == 5.0, "The output of \"2 3 -\" should be 5.0");

        String test2 = "4.0 0.0 / 5.0 2.0 +";
        test((tester.infixToPostfix("( ( 5 + 16 ) * ( 12 ^ 2 ) - ( 100 / 2 ) )").equals("5.0 16.0 + 12.0 2.0 ^ * 100.0 2.0 / -"
        )), "The correct postfix should be 5.0 16.0 + 12.0 2.0 ^ * 100.0 2.0 / -");
        test(tester.evaluate("5 16 + 12 2 ^ * 100 2 / -") == 2974.0, "The answer should be 2974.0");

        try {
            tester.evaluate(test2);
            test(false, "Can't divide by zero");
        } catch (ArithmeticException exception) {
            test(true, "");
        }

        String test3 = null;
        test(tester.infixToPostfix(test3).isEmpty(), "The expression should be empty because null was used");
        test(tester.evaluate(null) == 0, "The answer should be 0 because null was used and can't be calculated");


    }

    /**
     * a tester function for the TreeCalculator class.
     */
    private static void testTreeCalculator() {
        TreeCalculator testTree = new TreeCalculator();
        String tester1 = "5 3 2 ^ +";
        String tester2 = "56";


        //tester 1
        test((testTree.evaluate(tester1) == 14), "The correct answer should be 14");
        test((testTree.getInfix()).equals("( 5.0 + ( 3.0 ^ 2.0 ) )"), "The correct infix expression is ( 5.0 + ( 3.0 ^ 2.0 ) )");
        test((testTree.getPostfix()).equals("5.0 3.0 2.0 ^ +"), "The correct infix expression is 5.0 3.0 2.0 ^ +");
        test((testTree.getPrefix()).equals("+ 5.0 ^ 3.0 2.0"), "The correct infix expression is + 5.0 ^ 3.0 2.0");

        //tester 2
        test((testTree.evaluate(tester2) == 56), "The correct answer should be 56");
        test((testTree.getInfix()).equals("56.0"), "The correct infix expression is 56.0");
        test((testTree.getPostfix()).equals("56.0"), "The correct postfix expression is 56.0 ");
        test((testTree.getPrefix()).equals("56.0"), "The correct prefix expression is 56.0 ");


    }

    //a tester function for the ExpTokenizer class
    private static void testExpTokenizer() {
        String testerExpr1 = "( ( 100 + 2 ) - 52 )";
        ExpTokenizer tester = new ExpTokenizer(testerExpr1);

        test((tester.hasMoreElements()), "The Tokenizer should transform the whole expression to tokens");
        test(tester.nextToken().equals("("), "The next token should be (");
        test(tester.nextElement() instanceof OpenBracket, "The next token should be (");
        test(tester.nextElement() instanceof ValueToken, "The next token should be a Value token of 100.0");
        test(tester.nextToken().equals("+"), "The next token should be +");
        test(tester.nextToken().equals("2"), "The next token should be 2");
        test(tester.nextElement() instanceof CloseBracket, "The next token should be )");
        test(tester.nextElement() instanceof BinaryOp, "The next token should be a BinaryOp (-)");
        test(tester.nextToken().equals("52"), "The next token should be 52");
        test(tester.nextToken().equals(")"), "The next token should be )");

    }

}