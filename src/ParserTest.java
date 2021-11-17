
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;



public class ParserTest {

    @Test
    void matchesExpectedParseTree() throws Exception {
        String program = "program2.txt";
        String parseTree = "parsetree2.txt";


        String expected = readTextFile(parseTree);
        String actual = parse(program);

        assertEquals(expected, actual);
    }






    public String parse(String file) throws Exception {
        Parser p = new Parser();


        p.open(file);
        INode root = p.parse();
        StringBuilder b = new StringBuilder();
        b.append("PARSE TREE:\n");
        root.buildString(b, 0);
        b.append("\nEVALUATION:\n");
        b.append(root.evaluate(null));
        p.close();

        return b.toString();

    }

    // helper method to read text file
    String readTextFile(String pathName) throws FileNotFoundException {
        StringBuilder readFile = new StringBuilder();
        try {
            FileReader reader = new FileReader(pathName);
            BufferedReader in = new BufferedReader(reader);
            String line;
            while ((line = in.readLine()) != null) {
                readFile.append(line).append("\n");
            }
            in.close();
            reader.close();
        } catch (IOException e) {
            throw new FileNotFoundException("File not found");
        }
        return readFile.toString();
    }


    public static void main(String[] args) throws Exception {
        ParserTest parserTest = new ParserTest();

        System.out.println(parserTest.parse("program1.txt"));
    }
}
