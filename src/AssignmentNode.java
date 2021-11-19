import java.io.IOException;


public class AssignmentNode implements INode {

    private static final String NAME = "AssignmentNode";
    private final Lexeme id;
    private final Lexeme assignOp;
    private final ExpressionNode expr;
    private final Lexeme semiColon;


    public AssignmentNode(Tokenizer tokenizer) throws ParserException, IOException, TokenizerException {
        if (tokenizer == null)
            throw new IOException("No open file");

        if (tokenizer.current().token() != Token.IDENT)
            throw new ParserException("No identifier found. Was: " + tokenizer.current().toString());

        id = tokenizer.current();
        tokenizer.moveNext();

        if (tokenizer.current().token() != Token.ASSIGN_OP)
            throw new ParserException("No assignment operand found. Was: " + tokenizer.current().toString());

        assignOp = tokenizer.current();
        tokenizer.moveNext();

        expr = new ExpressionNode(tokenizer);

        if (tokenizer.current().token() != Token.SEMICOLON)
            throw new ParserException("No semicolon found. Was: " + tokenizer.current().toString());

        semiColon = tokenizer.current();
        tokenizer.moveNext();
    }

    @Override
    public Object evaluate(Object[] args) throws Exception {

        Evaluator evaluator = new Evaluator();

        double exprValue = evaluator.evaluate((String) expr.evaluate(args));
        return new Variable(id.value().toString(), exprValue);
    }

    @Override
    public void buildString(StringBuilder builder, int tabs) {
        buildWithTab(NAME, builder, tabs);
        buildWithTab(id.toString(), builder, tabs + 1);
        buildWithTab(assignOp.toString(), builder, tabs + 1);
        expr.buildString(builder, tabs + 1);
        buildWithTab(semiColon.toString(), builder, tabs + 1);

    }

    private void buildWithTab(String string, StringBuilder builder, int tabs){
        builder.append("\t".repeat(tabs)).append(string).append("\n");

    }

}
