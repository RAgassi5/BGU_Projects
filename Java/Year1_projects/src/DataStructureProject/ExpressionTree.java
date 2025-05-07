package DataStructureProject;

public class ExpressionTree {
    private TreeNode root;
    private ValueToken result;
    private String treeInfix = "";
    private String treePostfix = "";
    private String treePrefix = "";

    /**
     * a constructor
     *
     * @param expression a postfix expression to make a tree from
     */
    public ExpressionTree(String expression) {
        BuildExpressionTree(expression);
        postorderPostfix(this.root);
        InorderInfix(this.root);
        preorderPrefix(this.root);
    }

    /**
     * a default constructor
     */
    public ExpressionTree() {
        this.root = null;
    }


    /**
     * a function that builds the Expression Tree
     *
     * @param postfixExp a postfix expression
     */
    private void BuildExpressionTree(String postfixExp) {
        ExpTokenizer TokenedExpr = new ExpTokenizer(postfixExp);
        StackAsArray treeStack = new StackAsArray();

        while (TokenedExpr.hasMoreElements()) {
            CalcToken currentToken = (CalcToken) TokenedExpr.nextElement();
            if (currentToken instanceof ValueToken) {
                TreeNode leafNode = new TreeNode(currentToken);
                treeStack.push(leafNode);
            } else if (currentToken instanceof BinaryOp) {
                TreeNode right = (TreeNode) treeStack.pop();
                TreeNode left = (TreeNode) treeStack.pop();
                TreeNode rootNode = new TreeNode(currentToken, left, right);
                treeStack.push(rootNode);

            }
        }
        this.root = (TreeNode) treeStack.pop();
    }

    /**
     * calls the recursive function
     *
     * @return the vale of the postfix expression
     */
    public double evaluateExpression() {
        if (this.root == null) {
            throw new NullPointerException("The tree's root is empty");
        }
        return this.calculateExpression(this.root);
    }

    /**
     * a recursive function that calculates the expression's value
     *
     * @param node a root in the tree
     * @return the value of the wanted expression
     */
    private double calculateExpression(TreeNode node) {
        if (node.data instanceof BinaryOp) {
            double operand1 = calculateExpression(node.left);
            double operand2 = calculateExpression(node.right);
            this.result = new ValueToken(((BinaryOp) node.data).operate(operand1, operand2));

        } else if (node.data instanceof ValueToken) {
            this.result = new ValueToken(((ValueToken) node.data).getValue());
        }
        return this.result.getValue();
    }


    /**
     * a function that returns the inorder representation of the tree
     *
     * @param node the root of the current tree
     */
    private void InorderInfix(TreeNode node) {
        if (node == null) {
            return;
        }
        if (node.getLeft() != null || node.getRight() != null) {
            this.treeInfix += "( ";

        }
        if (node.getLeft() != null) {
            InorderInfix(node.getLeft());
        }
        this.treeInfix += node.data.toString() + " ";

        if (node.getRight() != null) {
            InorderInfix(node.getRight());
        }
        if (node.getLeft() != null || node.getRight() != null) {
            this.treeInfix += ") ";
        }


    }


    /**
     * a function that returns the postorder representation of the tree
     *
     * @param node the root of the current tree
     */
    private void postorderPostfix(TreeNode node) {
        if (node == null) {
            return;
        }
        postorderPostfix(node.getLeft());
        postorderPostfix(node.getRight());
        this.treePostfix += node.data.toString() + " ";
    }


    /**
     * a function that returns the preorder representation of the tree
     *
     * @param node the root of the current tree
     */
    private void preorderPrefix(TreeNode node) {
        if (node == null) {
            return;
        }
        this.treePrefix += node.data.toString() + " ";
        preorderPrefix(node.getLeft());
        preorderPrefix(node.getRight());
    }

    /**
     * a getter function for the tree inorder representation
     *
     * @return the tree's infix expression
     */
    public String getterInfix() {
        if (this.root == null) {
            throw new NullPointerException("The tree's root is empty");
        }
        return this.treeInfix.substring(0, this.treeInfix.length() - 1);
    }

    /**
     * a getter function for the tree postorder representation
     *
     * @return the tree's postfix expression
     */
    public String getterPostfix() {
        if (this.root == null) {
            throw new NullPointerException("The tree's root is empty");
        }
        return this.treePostfix.substring(0, this.treePostfix.length() - 1);
    }

    /**
     * a getter function for the tree preorder representation
     *
     * @return the tree's prefix expression
     */
    public String getterPrefix() {
        if (this.root == null) {
            throw new NullPointerException("The tree's root is empty");
        }
        return this.treePrefix.substring(0, this.treePrefix.length() - 1);
    }


}
