package cinescript.parser.ast;

/**
 * Nó que representa uma expressão unária (operador + operando).
 * Ex: -x, !flag
 */
public class UnaryExprNode extends ASTNode {

    private final String operator;
    private final ASTNode operand;

    public UnaryExprNode(String operator, ASTNode operand, int line) {
        super(line);
        this.operator = operator;
        this.operand = operand;
    }

    public String getOperator() {
        return operator;
    }

    public ASTNode getOperand() {
        return operand;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitUnaryExpr(this);
    }
}
