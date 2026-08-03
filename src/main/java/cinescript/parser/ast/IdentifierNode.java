package cinescript.parser.ast;

/**
 * Nó que representa uma referência a um identificador (nome de variável).
 * Ex: x, total, nome
 */
public class IdentifierNode extends ASTNode {

    private final String name;

    public IdentifierNode(String name, int line) {
        super(line);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitIdentifier(this);
    }
}
