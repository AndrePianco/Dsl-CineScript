package cinescript.parser.ast;

/**
 * Nó que representa uma estrutura condicional if/else.
 * Ex: if (x > 10) { ... } else { ... }
 */
public class IfNode extends ASTNode {

    private final ASTNode condition;
    private final ASTNode thenBranch;
    private final ASTNode elseBranch;   // pode ser null

    public IfNode(ASTNode condition, ASTNode thenBranch, ASTNode elseBranch, int line) {
        super(line);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public ASTNode getThenBranch() {
        return thenBranch;
    }

    public ASTNode getElseBranch() {
        return elseBranch;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitIf(this);
    }
}
