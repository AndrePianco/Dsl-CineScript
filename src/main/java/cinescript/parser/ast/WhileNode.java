package cinescript.parser.ast;

/**
 * Nó que representa um laço de repetição while.
 * Ex: while (x < 10) { ... }
 */
public class WhileNode extends ASTNode {

    private final ASTNode condition;
    private final ASTNode body;

    public WhileNode(ASTNode condition, ASTNode body, int line) {
        super(line);
        this.condition = condition;
        this.body = body;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public ASTNode getBody() {
        return body;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitWhile(this);
    }
}
