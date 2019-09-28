package edu.csc413.calculator.evaluator;

import edu.csc413.calculator.operators.Operator;

import java.util.Objects;
import java.util.Stack;
import java.util.StringTokenizer;

public class Evaluator {
    private Stack<Operand> operandStack;
    private Stack<Operator> operatorStack;

    private StringTokenizer tokenizer;
    private static final String DELIMITERS = "+-*^/() ";

    public Evaluator() {
        operandStack = new Stack<>();
        operatorStack = new Stack<>();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evaluator evaluator = (Evaluator) o;
        return Objects.equals(operandStack, evaluator.operandStack) &&
                Objects.equals(operatorStack, evaluator.operatorStack) &&
                Objects.equals(tokenizer, evaluator.tokenizer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operandStack, operatorStack, tokenizer);
    }

    public int eval(String expression) {
        String token;
        this.tokenizer = new StringTokenizer(expression, DELIMITERS, true);

        // The 3rd argument is true to indicate that the delimiters should be used
        // as tokens, too. But, we'll need to remember to filter out spaces.

        while (this.tokenizer.hasMoreTokens()) {
            // filter out spaces
            if ( !(token = this.tokenizer.nextToken()).equals(" ")) {
                // check if token is an operand
            if (Operand.check(token)) {
                    operandStack.push(new Operand(token));
            } else {

            if (!Operator.check(token)) {
                        System.out.println("*****invalid token******");
                        System.exit(1);
                    }

            Operator newOperator = Operator.getOperator(token);

                    if (newOperator.priority() == 4) {
                        operatorStack.push(newOperator);
                    }
                    if (newOperator.priority() == 5) {
                        while (operatorStack.peek().priority() != 4) {
                            Operand op2 = operandStack.pop();
                            Operand op1 = operandStack.pop();
                            operandStack.push(operatorStack.pop().execute(op1, op2));
                        }
                        operatorStack.pop();
                    }
                    // TODO Operator is abstract - these two lines will need to be fixed:
                    // The Operator class should contain an instance of a HashMap,
                    // and values will be instances of the Operators.  See Operator class
                    // skeleton for an example.



                    while (!operatorStack.isEmpty() && !(operatorStack.peek().priority() == 4)
                            && operatorStack.peek().priority() >= newOperator.priority()) {
                        Operator oldOpr = operatorStack.pop();
                        Operand op2 = operandStack.pop();
                        Operand op1 = operandStack.pop();
                        operandStack.push(oldOpr.execute(op1, op2));

                    }
                    if (newOperator.priority() != 4 && newOperator.priority() != 5) {
                        operatorStack.push(newOperator);
                    }
                }
            }
        }
        return emptyStack();
    }
private int emptyStack(){
    while (!operatorStack.isEmpty()) {
        Operator oldOpr = operatorStack.pop();
        Operand op2 = operandStack.pop();
        Operand op1 = operandStack.pop();
        operandStack.push(oldOpr.execute(op1, op2));
    }

    return operandStack.pop().getValue();
}
}