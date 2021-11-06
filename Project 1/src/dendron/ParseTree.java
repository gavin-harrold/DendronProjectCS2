package dendron;

import dendron.machine.Soros;
import dendron.treenodes.*;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;

/**
 * Operations that are done on a Dendron code parse tree.
 *
 * @author Gavin Harrold
 */
public class ParseTree {

    public static final String ASSIGN = ":=";
    public static final String PRINT = "#";

    private Program program = new Program();
    private int current = 0;
    /**
     * Parse the entire list of program tokens. The program is a
     * sequence of actions (statements), each of which modifies something
     * in the program's set of variables. The resulting parse tree is
     * stored internally.
     * @param tokens the token list (Strings). This list may be destroyed
     *                by this constructor.
     */
    public ParseTree( List< String > tokens ) {
        // TODO
        while(current < tokens.size()) {
            if(tokens.get(current).equals(PRINT) || tokens.get(current).equals(ASSIGN)) {
                program.addAction(parseAction(tokens));
            }
            else {
                Errors.report(Errors.Type.PREMATURE_END, tokens.get(current)+" is not allowed here. End of statement reached.");
            }

        }
    }

    private ActionNode parseAction(List<String> tokens) {
        if(tokens.get(current).equals(ASSIGN)) {
            tokens.get(current++);
            return parseAssignment(tokens);
        }
        else if(tokens.get(current).equals(PRINT)) {
            tokens.get(current++);
            return parsePrint(tokens);
        }
        else {
            return null;
        }
    }

    private Print parsePrint(List<String> tokens) {
        return new Print(parseExpr(tokens));
    }

    /**
     * Print the program the tree represents in a more typical
     * infix style, and with one statement per line.
     * @see ActionNode#infixDisplay()
     */
    public void displayProgram() {
        // TODO
        System.out.println("The Program with expressions in infix notation.");
        System.out.println();
        this.program.infixDisplay();
        System.out.println();
    }

    /**
     * Run the program represented by the tree directly
     * @see ActionNode#execute(Map)
     */
    public void interpret() {
        // TODO
        System.out.println("Interpreting the parse tree");
        Map<String, Integer> table = new HashMap<String, Integer>();
        program.execute(table);
        System.out.println("Interpretation complete.");
        System.out.println();
    }

    /**
     * Build the list of machine instructions for
     * the program represented by the tree.
     *
     * @param out where to print the Soros instruction list
     */
    public void compileTo( PrintWriter out ) {
        // TODO
        System.out.println("Symbol Table Contents");
        System.out.println("=======================");
        System.out.println();
        this.program.compile(out);
    }


    private boolean isVar(List<String> tokens) {
        return tokens.get(current).matches("^[a-zA-Z].*");
    }

    private boolean isConst(List<String> tokens) {
        return tokens.get(current).matches("[-+]?\\d+");
    }

    private Assignment parseAssignment(List<String> tokens) {
        if(!isVar(tokens)) {
            Errors.report(Errors.Type.ILLEGAL_VALUE, tokens.get(current)+" is not a valid identifier.");
        }
        String identifier = tokens.get(current++);
        ExpressionNode rhs = null;

        rhs = this.parseExpr(tokens);

        Assignment result = new Assignment(identifier, rhs);
        return result;
    }

    private ExpressionNode parseExpr(List<String> tokens) {
        String exp = tokens.get(current);
        if(BinaryOperation.OPERATORS.contains(exp)) {
            return parseBinaryOperator(tokens, exp);
        }
        else if(isVar(tokens)) {
            return parseVariable(tokens);
        }
        else if(isConst(tokens)) {
            return parseConstant(tokens);
        }
        else if(UnaryOperation.OPERATORS.contains(exp)) {
            return parseUnaryOperator(tokens, exp);
        }
        else{
            return null;
        }
    }

    private Variable parseVariable(List<String> tokens) {
        return new Variable(tokens.get(current++));
    }

    private Constant parseConstant(List<String> tokens) {
        return new Constant(Integer.parseInt(tokens.get(current++)));
    }

    private BinaryOperation parseBinaryOperator(List<String> tokens, String exp) {
        tokens.get(current++);
        return new BinaryOperation(exp, parseExpr(tokens), parseExpr(tokens));
    }

    private UnaryOperation parseUnaryOperator(List<String> tokens, String exp) {
        tokens.get(current++);
        return new UnaryOperation(exp, parseExpr(tokens));
    }
}
