public class TokenizerTest {


    public static void main(String[] args) {
        Tokenizer tokenizer = new Tokenizer();
        try {


            tokenizer.open("program1.txt");
            tokenizer.moveNext();

            while (tokenizer.current().token() != Token.EOF){
                System.out.println(tokenizer.current());
                tokenizer.moveNext();
            }
            tokenizer.close();

        }catch (Exception e){
        System.out.println("Exception: " + e);}
    }
}
