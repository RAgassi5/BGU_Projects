package DataStructureProject;

public class ExpTokenizer {
    //fields
    private String[] result;
    private int index;

    /**
     * constructor
     *
     * @param exp the expression to tokenize
     */
    public ExpTokenizer(String exp) {
        this.result = exp.split(" ");
        this.index = 0;
    }

    /**
     * returns the next element
     *
     * @return the next element
     */
    public Object nextElement() { //we create for each token the suitable object. there are no other
        CalcToken resultToken = null;
        String token = nextToken();
        if (token.equals("+"))
            resultToken = new AddOp();
        else if (token.equals("*"))
            resultToken = new MultiplyOp();
        else if (token.equals("-"))
            resultToken = new SubtractOp();
        else if (token.equals("/"))
            resultToken = new DivideOp();
        else if (token.equals("^"))
            resultToken = new PowOp();
        else if (token.equals("("))
            resultToken = new OpenBracket();
        else if (token.equals(")"))
            resultToken = new CloseBracket();
            // if the token is none of the above, it has to be a number. therefore, we would create valueToken.
        else
            resultToken = new ValueToken(Double.parseDouble(token));

        return resultToken;
    }

    /**
     * returns if there are any more elements
     *
     * @return true or false
     */
    public boolean hasMoreElements() {
        return (this.index != this.result.length);
    }

    /**
     * returns the next token
     *
     * @return the next token
     */
    public String nextToken() {
        String ret;
        ret = this.result[this.index];
        this.index++;
        return ret;
    }


    /**
     * returns how many tokens have been created
     *
     * @return number of tokens created
     */
    public int countTokens() {
        return (this.result.length - this.index);
    }


}
