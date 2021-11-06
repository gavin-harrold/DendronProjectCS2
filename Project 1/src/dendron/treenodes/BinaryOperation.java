package dendron.treenodes;

import dendron.Errors;
import dendron.machine.Soros;

import java.io.PrintWriter;
import java.util.*;

public class BinaryOperation implements ExpressionNode{
    public static final String ADD = "+";
    public static final String SUB = "-";
    public static final String MUL = "*";
    public static final String DIV = "/";

    private static final Set<String> opsSet = new HashSet<>(Arrays.asList(ADD, SUB, MUL, DIV));
    public static final Collection<String> OPERATORS = Collections.unmodifiableSet(opsSet);

    private String operator;
    private ExpressionNode leftSide;
    private ExpressionNode rightSide;

    public BinaryOperation(String operator, ExpressionNode leftSide, ExpressionNode rightSide) {
        this.operator = operator;
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    @Override
    public void infixDisplay() {

        System.out.print("(");
        this.leftSide.infixDisplay();
        System.out.print(operator);
        this.rightSide.infixDisplay();
        System.out.print(")");
    }

    @Override
    public int evaluate(Map<String, Integer> symTab) {
        if(this.operator.equals(ADD)) {
            return this.leftSide.evaluate(symTab) + this.rightSide.evaluate(symTab);
        }
        else if(this.operator.equals(SUB)) {
            return this.leftSide.evaluate(symTab) - this.rightSide.evaluate(symTab);
        }
        else if(this.operator.equals(MUL)) {
            return this.leftSide.evaluate(symTab) * this.rightSide.evaluate(symTab);
        }
        else if(this.operator.equals(DIV)) {
            if(this.rightSide.evaluate(symTab) == 0) {
                Errors.report(Errors.Type.DIVIDE_BY_ZERO, "Division by 0 is not allowed.");
            }
            return this.leftSide.evaluate(symTab) / this.rightSide.evaluate(symTab);
        }
        else{
            Errors.report(Errors.Type.ILLEGAL_VALUE, "Operator "+operator+" not valid.");
            return 0;
        }
    }

    @Override
    public void compile(PrintWriter out) {
        this.leftSide.compile(out);
        this.rightSide.compile(out);

        switch(this.operator) {
            case ADD: {
                out.println(new Soros.Add());
                break;
            }
            case SUB: {
                out.println(new Soros.Subtract());
                break;
            }
            case MUL: {
                out.println(new Soros.Multiply());
                break;
            }
            case DIV: {
                out.println(new Soros.Divide());
                break;
            }
        }
    }
}
