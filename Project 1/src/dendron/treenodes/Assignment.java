package dendron.treenodes;

import dendron.machine.Soros;

import java.io.PrintWriter;
import java.util.Map;

public class Assignment implements ActionNode{

    private String ident;
    private ExpressionNode rhs;

    public Assignment(String ident, ExpressionNode rhs) {
        this.ident = ident;
        this.rhs = rhs;
    }

    @Override
    public void execute(Map<String, Integer> symTab) {
        symTab.put(this.ident, this.rhs.evaluate(symTab));
    }

    @Override
    public void infixDisplay() {
        System.out.print(this.ident);
        System.out.print(":=");
        this.rhs.infixDisplay();
    }

    @Override
    public void compile(PrintWriter out) {
        this.rhs.compile(out);
        out.println(new Soros.Store(this.ident));
    }
}
