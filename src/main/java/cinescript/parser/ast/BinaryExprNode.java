package cinescript.parser.ast;

/**
 * Nó que representa uma expressão binária (dois operandos + operador).
 * Ex: a + b, x > 10, flag && ready
 */
public class BinaryExprNode extends ASTNode {

    private final ASTNode left;
    private final String operator;
    private final ASTNode right;

    public BinaryExprNode(ASTNode left, String operator, ASTNode right, int line) {
        super(line);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public ASTNode getLeft() {
        return left;
    }

    public String getOperator() {
        return operator;
    }

    public ASTNode getRight() {
        return right;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitBinaryExpr(this);
    }
}
