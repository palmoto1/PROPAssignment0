public enum Regex {

    ALL("[a-z0-9\s\n\r\t(){}+*/=;-]"),
    ALPHABETIC("[a-z]"),
    NUMERIC("[0-9]"),
    SPECIAL("[(){}+*/=;-]"),
    WHITESPACE("[\s\n\r\t]");


    private final String pattern;

    Regex(String pattern) {
        this.pattern = pattern;
    }

    public String toString(){
        return this.pattern;
    }
}
