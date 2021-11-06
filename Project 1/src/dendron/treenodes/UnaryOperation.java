package dendron.treenodes;

import dendron.Errors;
import dendron.machine.Soros;

import java.io.PrintWriter;
import java.util.*;

public class UnaryOperation implements ExpressionNode{
    public static final String NEG = "_";
    public static final String SQRT = "%";

    private static final Set<String> opSet = new HashSet<>(Arrays.asList(NEG, SQRT));
    public static final Collection<String> OPERATORS = Collections.unmodifiableSet(opSet);

    private String operator;
    private ExpressionNode expr;

    public UnaryOperation(String operator, ExpressionNode expr) {
        this.operator = operator;
        this.expr = expr;
    }

    @Override
    public void compile(PrintWriter out) {
        this.expr.compile(out);

        switch(this.operator) {
            case NEG: {
                out.println(new Soros.Negate());
                break;
            }
            case SQRT: {
                out.println(new Soros.SquareRoot());
                break;
            }

        }
    }

    @Override
    public int evaluate(Map<String, Integer> symTab) {
        if(this.operator.equals(NEG)) {
            return -this.expr.evaluate(symTab);
        }
        else if(this.operator.equals(SQRT)) {
            return (int) Math.sqrt(this.expr.evaluate(symTab));
        }
        else {
            Errors.report(Errors.Type.ILLEGAL_VALUE, "Operator "+operator+" not valid.");
            return 0;
        }
    }

    @Override
    public void infixDisplay() {
        System.out.print(this.operator);
        this.expr.infixDisplay();
    }
}
