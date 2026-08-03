package cinescript.parser.ast;

/**
 * Nó que representa um valor literal (int, float, string, boolean).
 * Ex: 42, 3.14, "hello", true
 */
public class LiteralNode extends ASTNode {

    private final Object value;
    private final String type;  // "int", "float", "string", "boolean"

    public LiteralNode(Object value, String type, int line) {
        super(line);
        this.value = value;
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitLiteral(this);
    }
}
