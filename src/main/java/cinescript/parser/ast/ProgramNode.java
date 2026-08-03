package cinescript.parser.ast;

import java.util.List;

/**
 * Nó raiz da AST que contém todas as declarações/statements do programa.
 */
public class ProgramNode extends ASTNode {

    private final List<ASTNode> statements;

    public ProgramNode(List<ASTNode> statements, int line) {
        super(line);
        this.statements = statements;
    }

    public List<ASTNode> getStatements() {
        return statements;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitProgram(this);
    }
}
