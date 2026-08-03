package cinescript.parser.ast;

/**
 * Nó que representa uma atribuição de valor a uma variável.
 * Ex: x = 42;
 */
public class AssignmentNode extends ASTNode {

    private final String variableName;
    private final ASTNode value;

    public AssignmentNode(String variableName, ASTNode value, int line) {
        super(line);
        this.variableName = variableName;
        this.value = value;
    }

    public String getVariableName() {
        return variableName;
    }

    public ASTNode getValue() {
        return value;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitAssignment(this);
    }
}
