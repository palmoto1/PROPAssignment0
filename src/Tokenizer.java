import java.io.IOException;


public class Tokenizer implements ITokenizer {


    private Scanner scanner = null;
    private Lexeme lexeme = null;
    private Token token = null;

    private boolean searchingAdjacentAlphabetic = false;
    private boolean searchingAdjacentNumeric = false;


    @Override
    public void open(String fileName) throws IOException {
        scanner = new Scanner();
        scanner.open(fileName);
        scanner.moveNext();

    }

    @Override
    public Lexeme current() {
        if (lexeme == null)
            return new Lexeme(Scanner.NULL, Token.NULL);
        return lexeme;
    }

    @Override
    public void moveNext() throws IOException, TokenizerException {
        if (scanner == null)
            throw new IOException("No open file.");

        StringBuilder builder = new StringBuilder();

        while (Character.isWhitespace(scanner.current())) {
            scanner.moveNext();
        }
        char ch = scanner.current();
        boolean searching;

        if (isAlphabeticOrNumeric(ch)) {
            searching = true;
            while (searching) {
                if (Character.isAlphabetic(scanner.current())) {
                    assignAlphabetic(scanner.current(), builder);
                    scanner.moveNext();
                    continue; // restart loop to check for eventual adjacent character
                }
                if (Character.isDigit(scanner.current())) {
                    assignNumeric(scanner.current(), builder);
                    scanner.moveNext();
                    continue; // restart loop to check for eventual adjacent character
                }
                // if this statement is reached searching for adjacent numeric/alphabetic characters
                // it means there is no more such adjacent characters to be found
                if (searchingAdjacentChar()) {
                    setSearchingAdjacentFalse();
                    String value = builder.toString();

                    //formats the value to double with one decimal if the value is numeric
                    if (isNumber(value))
                        value = String.valueOf(Double.parseDouble(value));
                    lexeme = new Lexeme(value, token);
                    searching = false;
                }
            }
        }

        else if (isSpecialChar(ch)) {
            assignSpecialChar(ch);
            lexeme = new Lexeme(ch, token);
            scanner.moveNext();
        }

        else if (ch == Scanner.EOF)
            lexeme = new Lexeme(Scanner.EOF, Token.EOF); //end of file is reached

        else
            throw new TokenizerException(scanner.current() + " is not a valid character");
    }

    @Override
    public void close() throws IOException {
        scanner.close();
    }


    private void assignAlphabetic(char ch, StringBuilder builder) throws TokenizerException {

        // Throws an exception if we are constructing numeric lexeme
        // since the value of numeric lexemes cannot contain letters
        if (searchingAdjacentNumeric && !searchingAdjacentAlphabetic)
            throw new TokenizerException("Alphabetic character " + ch + " not allowed in numeric token");

        // Assigns character to the builder and sets the token to ident
        builder.append(ch);
        token = Token.IDENT;

        searchingAdjacentAlphabetic = true; // to allow eventual adjacent alphabetic characters
    }


    private void assignNumeric(char ch, StringBuilder builder) {

        // An alphabetic token can contain numeric characters if the first character is not numeric
        token = searchingAdjacentAlphabetic ? Token.IDENT : Token.INT_LIT;
        builder.append(ch);
        searchingAdjacentNumeric = true; // to allow eventual adjacent digit characters
    }


    //sets token to right special character
    private void assignSpecialChar(char ch) {
        switch (ch) {
            case '{' -> token = Token.LEFT_CURLY;
            case '}' -> token = Token.RIGHT_CURLY;
            case '(' -> token = Token.LEFT_PAREN;
            case ')' -> token = Token.RIGHT_PAREN;
            case '=' -> token = Token.ASSIGN_OP;
            case '+' -> token = Token.ADD_OP;
            case '-' -> token = Token.SUB_OP;
            case '*' -> token = Token.MULT_OP;
            case '/' -> token = Token.DIV_OP;
            case ';' -> token = Token.SEMICOLON;
        }
    }


    private boolean searchingAdjacentChar() {
        return searchingAdjacentNumeric || searchingAdjacentAlphabetic;
    }

    private void setSearchingAdjacentFalse() {
        searchingAdjacentAlphabetic = false;
        searchingAdjacentNumeric = false;
    }

    private boolean isNumber(String value) {
        try {
            Double.parseDouble(value);
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean isAlphabeticOrNumeric(char ch){
        return Character.isDigit(ch) || Character.isAlphabetic(ch);
    }

    private boolean isSpecialChar(char ch) {
        String specials = "[(){}+*/=;-]";
        return String.valueOf(ch).matches(specials);
    }

}
