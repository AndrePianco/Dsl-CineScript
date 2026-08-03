package cinescript.parser.ast;

/**
 * Nó que representa o comando print para saída de dados.
 * Ex: print(x);
 */
public class PrintNode extends ASTNode {

    private final ASTNode expression;

    public PrintNode(ASTNode expression, int line) {
        super(line);
        this.expression = expression;
    }

    public ASTNode getExpression() {
        return expression;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitPrint(this);
    }
}
