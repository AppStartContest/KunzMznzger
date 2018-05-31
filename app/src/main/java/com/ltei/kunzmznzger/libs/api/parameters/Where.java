package com.ltei.kunzmznzger.libs.api.parameters;

public class Where {
    private String left;
    private String operator = "=";
    private String right;

    Where(String left, String operator, String right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    Where(String left, String right) {
        this.left = left;
        this.right = right;
    }

    public static Where make(String left, String operator, String right) {
        return new Where(left, operator, right);
    }

    public static Where make(String left, String right) {
        return new Where(left, right);
    }

    public String getLeft() {
        return left;
    }

    public String getOperator() {
        return operator;
    }

    public String getRight() {
        return right;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s", left, operator, right);
    }
}
