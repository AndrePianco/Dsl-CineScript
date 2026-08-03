package cinescript.parser.ast;

import java.util.List;

/**
 * Nó que representa um bloco de código delimitado por chaves { }.
 */
public class BlockNode extends ASTNode {

    private final List<ASTNode> statements;

    public BlockNode(List<ASTNode> statements, int line) {
        super(line);
        this.statements = statements;
    }

    public List<ASTNode> getStatements() {
        return statements;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitBlock(this);
    }
}
