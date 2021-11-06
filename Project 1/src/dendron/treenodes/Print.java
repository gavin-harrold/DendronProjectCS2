package dendron.treenodes;

import dendron.machine.Soros;

import java.io.PrintWriter;
import java.util.Map;

public class Print implements ActionNode{
    public static final String PRINT_PREFIX = "=== ";

    private ExpressionNode printee;

    public Print(ExpressionNode printee) {
        this.printee = printee;
    }

    @Override
    public void compile(PrintWriter out) {
        this.printee.compile(out);
        out.println(new Soros.Print());
    }

    @Override
    public void execute(Map<String, Integer> symTab) {
        System.out.println(PRINT_PREFIX+ " "+this.printee.evaluate(symTab));
    }

    @Override
    public void infixDisplay() {
        System.out.print("Print ");
        this.printee.infixDisplay();
    }
}
