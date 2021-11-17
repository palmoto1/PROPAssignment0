import java.io.IOException;

public class FactorNode implements INode {

    private static final String NAME = "FactorNode";

    private Lexeme integer = null;
    private Lexeme id = null;
    private Lexeme leftParen = null;
    private ExpressionNode expr = null;
    private Lexeme rightParen = null;


    public FactorNode(Tokenizer tokenizer) throws ParserException, IOException, TokenizerException {
        if (tokenizer.current().token() == Token.INT_LIT) {
            integer = tokenizer.current();
            tokenizer.moveNext();
        }
        else if (tokenizer.current().token() == Token.IDENT) {
            id = tokenizer.current();
            tokenizer.moveNext();
        }
        else if (tokenizer.current().token() == Token.LEFT_PAREN){
            leftParen = tokenizer.current();
            tokenizer.moveNext();

            expr = new ExpressionNode(tokenizer);

            if (tokenizer.current().token() != Token.RIGHT_PAREN)
                throw new ParserException("Incomplete parenthesis. Was: " + tokenizer.current().toString());

            rightParen = tokenizer.current();
            tokenizer.moveNext();
        } else
            throw new ParserException("No valid factor found. Was: " + tokenizer.current().toString());
    }

    @Override
    public Object evaluate(Object[] args) throws Exception {
        if (id != null) {
            for (Object var : args){
                if (((Variable) var).getId().equals(id.value().toString())){
                    return ((Variable) var).getValue();
            }}
            throw new ParserException("Variable " + id.value() + " has not been assigned a value!");
        }
        if (expr != null)
            return leftParen.token() + " " + expr.evaluate(args) + " " + rightParen.token();
        return Double.parseDouble(integer.value().toString());
    }

    @Override
    public void buildString(StringBuilder builder, int tabs) {
        buildWithTab(NAME, builder, tabs);

        if (integer != null)
            buildWithTab(integer.toString(), builder, tabs + 1);

        else if (id != null)
            buildWithTab(id.toString(), builder, tabs + 1);

        else if (leftParen != null && expr != null && rightParen != null){
            buildWithTab(leftParen.toString(), builder, tabs + 1);

            expr.buildString(builder, tabs + 1);

            buildWithTab(rightParen.toString(), builder, tabs + 1);
        }
    }


    private void buildWithTab(String string, StringBuilder builder, int tabs) {
        builder.append("\t".repeat(tabs)).append(string).append("\n");


    }
}
