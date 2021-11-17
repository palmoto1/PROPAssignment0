import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Evaluator {


    /**
     * Evaluates the result of an expression in reversed polish notation
     */
    public double evaluate(String expr) {
        if (expr == null || expr.trim().isEmpty())
            throw new IllegalArgumentException("Not a valid expression");

        List<String> expression = convertToRpn(expr);
        Stack<Double> numberStack = new Stack<>();

        for (String current : expression) {
            if (isNumber(current)) {
                numberStack.push(Double.parseDouble(current));
            } else {
                // we pop and calculate the top two elements in the number stack and
                // add the result onto the number stack
                double a = numberStack.pop();
                double b = numberStack.pop();
                numberStack.push(calculate(a, b, current)); // current is an operator
            }
        }

        //the only element left in the stack is the result of the expression
        return numberStack.pop();
    }


    /**
     * Converts an arithmetic expression to Reversed Polish Notation to handle operator precedence order
     */
    private List<String> convertToRpn(String expr) {
        List<String> result = new ArrayList<>(); // list storing the result of the conversion
        Stack<String> operatorStack = new Stack<>(); // stack to keep track of the operators precedence


        String[] tokens = expr.split("\s");

        for (String current : tokens) {

            if (isNumber(current))
                result.add(current);

            else if (isArithmeticOp(current)) {

                //loop while there are elements in the stack and the top element in the stack is not a parenthesis
                while (!operatorStack.isEmpty() && (isArithmeticOp(operatorStack.peek()))) {

                    // if precedence of the top operator in the stack is the same or higher than the current operator
                    // precedence we pop the top stack operator att add it to the result list and restart the loop
                    // to check the next operator in the stack if there is any
                    if (precedence(operatorStack.peek()) >= precedence(current)) {
                        result.add(operatorStack.pop());
                        continue;
                    }
                    break; // otherwise we break the loop
                }

                //add the current operator to the stack
                operatorStack.push(current);


            } else if (isLeftParen(current))

                // we add the left parenthesis onto the stack to be able to evaluate
                // the expression inside the parenthesis on its own
                operatorStack.push(current);

            else if (isRightParen(current)) {

                //when we find the right parenthesis we add all the remaining operators
                // in the stack up until the left parenthesis
                while (!operatorStack.isEmpty() && !isLeftParen(operatorStack.peek())) {
                    result.add(operatorStack.pop());
                }

                // we also remove the left parenthesis from the stack since we don't need it anymore
                operatorStack.pop();
            } else
                throw new IllegalStateException("Unexpected token");
        }

        // we add all the remaining operators from the stack
        while (!operatorStack.isEmpty()) {
            result.add(operatorStack.pop());
        }


        return Collections.unmodifiableList(result);
    }

    /**
     * Calculates two numbers based on the operator
     */
    private double calculate(double a, double b, String op) {
        BigDecimal bdX = new BigDecimal(String.valueOf(a));
        BigDecimal bdY = new BigDecimal(String.valueOf(b));

        if (op.equals(Token.ADD_OP.toString()))
            return bdX.add(bdY).doubleValue();

        if (op.equals(Token.SUB_OP.toString()))
            return bdY.subtract(bdX).doubleValue();

        if (op.equals(Token.MULT_OP.toString()))
            return bdX.multiply(bdY).doubleValue();

        if (op.equals(Token.DIV_OP.toString()))
            return bdY.divide(bdX, RoundingMode.HALF_UP).doubleValue();

        throw new IllegalArgumentException("Illegal operator");
    }

    private int precedence(String op) {
        if (isAddOrSubOp(op))
            return 0;
        if (isMultOrDivOp(op))
            return 1;
        throw new IllegalArgumentException("Not a valid operand");
    }

    private boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
    }

    private boolean isArithmeticOp(String op) {
        return isAddOrSubOp(op) || isMultOrDivOp(op);
    }

    private boolean isAddOrSubOp(String op) {
        return op.equals(Token.ADD_OP.toString()) || op.equals(Token.SUB_OP.toString());
    }

    private boolean isMultOrDivOp(String op) {
        return op.equals(Token.MULT_OP.toString()) || op.equals(Token.DIV_OP.toString());
    }


    private boolean isLeftParen(String op) {
        return op.equals(Token.LEFT_PAREN.toString());
    }

    private boolean isRightParen(String op) {
        return op.equals(Token.RIGHT_PAREN.toString());
    }

}
