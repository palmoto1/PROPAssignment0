import java.io.IOException;

public class BlockNode implements INode {

    //TODO: try implement so multiple blocks are allowed just for fun

    private static final String NAME = "BlockNode";

    private final Lexeme leftCurly;
    private final Lexeme rightCurly;
    private final StatementNode stmts;


    public BlockNode(Tokenizer tokenizer) throws ParserException, IOException, TokenizerException {
        if (tokenizer == null)
            throw new IOException("No open file");

        if (tokenizer.current().token() != Token.LEFT_CURLY)
            throw new ParserException("No left curly bracket found. Was: " + tokenizer.current().toString());

        leftCurly = tokenizer.current();
        tokenizer.moveNext();

        stmts = new StatementNode(tokenizer);

        if (tokenizer.current().token() != Token.RIGHT_CURLY)
            throw new ParserException("No right curly bracket found. Was: " + tokenizer.current().toString());

        rightCurly = tokenizer.current();
        tokenizer.moveNext();

        if (tokenizer.current().token() != Token.EOF)
            throw new ParserException("Token after block.");

    }

    @Override
    public Object evaluate(Object[] args) throws Exception {
        return stmts.evaluate(null);
    }

    @Override
    public void buildString(StringBuilder builder, int tabs) {
        buildWithTab(NAME, builder, tabs);
        buildWithTab(leftCurly.toString(), builder, tabs);
        stmts.buildString(builder, tabs + 1);
        buildWithTab(rightCurly.toString(), builder, tabs);
    }

    private void buildWithTab(String string, StringBuilder builder, int tabs){
        builder.append("\t".repeat(tabs)).append(string).append("\n");

    }

}
