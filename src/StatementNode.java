
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import java.util.List;

public class StatementNode implements INode {

    private static final String NAME = "StatementsNode";

    private AssignmentNode assign = null;
    private StatementNode stmts = null;


    public StatementNode(Tokenizer tokenizer) throws IOException, TokenizerException, ParserException {

        if (tokenizer == null)
            throw new IOException("No open file");

        if (tokenizer.current().token() != Token.RIGHT_CURLY) {
            assign = new AssignmentNode(tokenizer);
            stmts = new StatementNode(tokenizer);
        }
    }

    @Override
    public Object evaluate(Object[] args) throws Exception {
        List<Variable> variables = new ArrayList<>();

        if (args != null) {
            for (Object o : args)
                variables.add((Variable) o);
        }

        String value = "";

        if (assign != null && stmts != null){
            Variable var = (Variable) assign.evaluate(variables.toArray());
            value += var + "\n";
            variables.add(var);
            value += stmts.evaluate(variables.toArray());
        }
        return value;
    }

    @Override
    public void buildString(StringBuilder builder, int tabs) {
        builder.append("\t".repeat(tabs)).append(NAME).append("\n");
        if (assign != null && stmts != null) {
            assign.buildString(builder, tabs + 1);
            stmts.buildString(builder, tabs + 1);
        }
    }
}
