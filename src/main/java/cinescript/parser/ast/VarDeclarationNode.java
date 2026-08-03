package cinescript.parser.ast;

/**
 * Nó que representa a declaração de uma variável.
 * Ex: int x = 10;
 */
public class VarDeclarationNode extends ASTNode {

    private final String typeName;      // "int", "float", "string"
    private final String variableName;
    private final ASTNode initializer;  // pode ser null se não houver inicialização

    public VarDeclarationNode(String typeName, String variableName, ASTNode initializer, int line) {
        super(line);
        this.typeName = typeName;
        this.variableName = variableName;
        this.initializer = initializer;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getVariableName() {
        return variableName;
    }

    public ASTNode getInitializer() {
        return initializer;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitVarDeclaration(this);
    }
}
