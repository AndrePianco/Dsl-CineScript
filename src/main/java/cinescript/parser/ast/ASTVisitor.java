package cinescript.parser.ast;

/**
 * Interface Visitor para travessia da AST.
 * Cada tipo de nó possui seu próprio método visit.
 * @param <T> Tipo de retorno dos métodos visit.
 */
public interface ASTVisitor<T> {

    T visitProgram(ProgramNode node);
    T visitBlock(BlockNode node);
    T visitVarDeclaration(VarDeclarationNode node);
    T visitAssignment(AssignmentNode node);
    T visitIf(IfNode node);
    T visitWhile(WhileNode node);
    T visitBinaryExpr(BinaryExprNode node);
    T visitUnaryExpr(UnaryExprNode node);
    T visitLiteral(LiteralNode node);
    T visitIdentifier(IdentifierNode node);
    T visitPrint(PrintNode node);
    T visitDomainCommand(DomainCommandNode node);
}
