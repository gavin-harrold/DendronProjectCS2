package dendron.treenodes;

import dendron.machine.Soros;

import java.io.PrintWriter;
import java.util.Map;

public class Constant implements ExpressionNode{
    private int value;

    public Constant(int value) {
        this.value = value;
    }

    @Override
    public void compile(PrintWriter out) {
        out.println(new Soros.PushConst(this.value));
    }

    @Override
    public int evaluate(Map<String, Integer> symTab) {
        return this.value;
    }

    @Override
    public void infixDisplay() {
        System.out.print(this.value);
    }
}
