package dendron.treenodes;

import dendron.Errors;
import dendron.machine.Soros;

import java.io.PrintWriter;
import java.util.Map;

public class Variable implements ExpressionNode{
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public void compile(PrintWriter out) {
        out.println(new Soros.Load(this.name));
    }

    @Override
    public int evaluate(Map<String, Integer> symTab) {
        if(symTab.get(this.name) == null){
            Errors.report(Errors.Type.UNINITIALIZED, this.name +" is not initialized.");
        }
        else {
            return symTab.get(this.name);
        }
        return -1;
    }

    @Override
    public void infixDisplay() {
        System.out.print(this.name);
    }
}
