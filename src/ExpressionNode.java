import java.io.IOException;


public class ExpressionNode implements INode {

    private static final String NAME = "ExpressionNode";

    private final TermNode term;
    private Lexeme op = null;
    private ExpressionNode expr = null;



    public ExpressionNode(Tokenizer tokenizer) throws ParserException, IOException, TokenizerException {
        if (tokenizer == null)
            throw new IOException("No open file");

        term = new TermNode(tokenizer);

        if (tokenizer.current().token() == Token.ADD_OP || tokenizer.current().token() == Token.SUB_OP) {

            op = tokenizer.current();
            tokenizer.moveNext();

            expr = new ExpressionNode(tokenizer);

        }
    }


    @Override
    public Object evaluate(Object[] args) throws Exception {


        String result = term.evaluate(args) + "";

        if (op != null){
                result += " " + op.token() + " " + expr.evaluate(args);
        }

        return result;
   }

    @Override
    public void buildString(StringBuilder builder, int tabs) {
        buildWithTab(NAME, builder, tabs);
        term.buildString(builder, tabs + 1);
        if (op != null && expr != null) {
            buildWithTab(op.toString(), builder, tabs + 1);
            expr.buildString(builder, tabs + 1);
        }

    }

    public Lexeme getOp() {
        return op;
    }

    public TermNode getTerm() {

        return term;
    }

    public ExpressionNode getExpr() {

        return expr;
    }

    private void buildWithTab(String string, StringBuilder builder, int tabs) {
        builder.append("\t".repeat(tabs)).append(string).append("\n");

    }

}
