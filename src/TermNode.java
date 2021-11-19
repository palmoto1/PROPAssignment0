import java.io.IOException;

public class TermNode implements INode {

    private static final String NAME = "TermNode";

    private final FactorNode factor;
    private Lexeme op = null;
    private TermNode term = null;


    public TermNode(Tokenizer tokenizer) throws TokenizerException, ParserException, IOException {
        if (tokenizer == null)
            throw new IOException("No open file");

       factor = new FactorNode(tokenizer);

        if (tokenizer.current().token() == Token.MULT_OP || tokenizer.current().token() == Token.DIV_OP) {

            op = tokenizer.current();
            tokenizer.moveNext();

            term = new TermNode(tokenizer);

        }


    }


    @Override
    public Object evaluate(Object[] args) throws Exception {

        String result = factor.evaluate(args) + "";

        if (op != null){
            result += " " + op.token() + " " + term.evaluate(args);}

        return result;

    }

    @Override
    public void buildString(StringBuilder builder, int tabs) {
        buildWithTab(NAME, builder, tabs);
        factor.buildString(builder, tabs + 1);

        if (op != null && term != null) {
            buildWithTab(op.toString(), builder, tabs + 1);
            term.buildString(builder, tabs + 1);
        }

    }
    private void buildWithTab(String string, StringBuilder builder, int tabs) {
        builder.append("\t".repeat(tabs)).append(string).append("\n");


    }
}
