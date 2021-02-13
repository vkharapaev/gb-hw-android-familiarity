package com.headmostlab.calculator.calc.expression;

import java.util.LinkedList;
import java.util.List;

import static com.headmostlab.calculator.calc.expression.ExpUtil.isLeftBracket;
import static com.headmostlab.calculator.calc.expression.ExpUtil.isNum;
import static com.headmostlab.calculator.calc.expression.ExpUtil.isOperator;
import static com.headmostlab.calculator.calc.expression.ExpUtil.isRightBracket;

public class InfixExpCreator {

    private static final String LEFT_BRACKET = "(";
    private static final String RIGHT_BRACKET = ")";

    private final LinkedList<String> infixExp;
    private int leftBrackets;
    private int rightBrackets;

    public InfixExpCreator() {
        infixExp = new LinkedList<>();
    }

    private InfixExpCreator(InfixExpCreator creator) {
        this.leftBrackets = creator.leftBrackets;
        this.rightBrackets = creator.rightBrackets;
        this.infixExp = new LinkedList<>(creator.infixExp);
    }

    /**
     * Add a token to the expression
     *
     * @param token New token
     * @return The current infix creator
     */
    public InfixExpCreator add(String token) {
        String last = getLast();

        if (last == null) {
            if (isNum(token)) {
                addToken(token);
            } else if (isLeftBracket(token)) {
                addLeftBracket();
            }

        } else if (isNum(last)) {
            if (isOperator(token)) {
                addToken(token);
            } else if (isRightBracket(token)) {
                addRightBracket();
            }

        } else if (isLeftBracket(last)) {
            if (isLeftBracket(token)) {
                addLeftBracket();
            } else if (isNum(token)) {
                addToken(token);
            }

        } else if (isRightBracket(last)) {
            if (isOperator(token)) {
                addToken(token);
            } else if (isRightBracket(token)) {
                addRightBracket();
            }

        } else if (isOperator(last)) {
            if (isLeftBracket(token)) {
                addLeftBracket();
            } else if (isNum(token)) {
                addToken(token);
            }
        }

        return this;
    }

    public void clear() {
        infixExp.clear();
        leftBrackets = 0;
        rightBrackets = 0;
    }

    private String getLast() {
        return infixExp.peekLast();
    }

    private void addToken(String token) {
        infixExp.add(token);
    }

    private InfixExpCreator finalizeExpression() {
        InfixExpCreator c = new InfixExpCreator(this);
        String last = c.getLast();

        if (last == null) {
            return c;
        }

        c.removeLeftBrackets();

        last = c.getLast();
        if (isOperator(last)) {
            c.removeLast();
        }

        last = c.getLast();
        if (isNum(last)) {
            while (c.rightBrackets < c.leftBrackets) {
                c.addRightBracket();
            }
        }

        return c;
    }

    private void addRightBracket() {
        if (rightBrackets < leftBrackets) {
            rightBrackets++;
            infixExp.add(RIGHT_BRACKET);
        }
    }

    private void addLeftBracket() {
        leftBrackets++;
        infixExp.add(LEFT_BRACKET);
    }

    private void removeLeftBrackets() {
        while (isLeftBracket(getLast())) {
            leftBrackets--;
            removeLast();
        }
    }

    /**
     * Remove the last token. Do nothing if empty
     */
    public void removeLast() {
        if (!infixExp.isEmpty()) {
            infixExp.removeLast();
        }
    }

    /**
     * Return a finalized infix expression
     *
     * @return Expression
     */
    public List<String> get() {
        return finalizeExpression().infixExp;
    }

    /**
     * Return a finalized infix expression
     *
     * @return Expression
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        infixExp.forEach(token -> sb.append(token).append(" "));
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    /**
     * Set new expression.
     *
     * @param expression Expression to set
     */
    public void set(String expression) {
        if (expression == null) {
            return;
        }
        clear();
        for (String token : expression.split("\\s")) {
            add(token);
            if (isLeftBracket(token)) {
                leftBrackets++;
            } else if (isRightBracket(token)) {
                rightBrackets++;
            }
        }
    }

    /**
     * Return a finalized infix expression
     *
     * @return Expression
     */
    public String toFinalizedString() {
        StringBuffer sb = new StringBuffer();
        get().forEach(token -> sb.append(token).append(" "));
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    /**
     * Whether the expression is empty
     *
     * @return True - empty, False - not
     */
    public boolean isEmpty() {
        return infixExp.isEmpty();
    }
}
