package cinescript.parser.ast;

/**
 * Interface base para todos os nós da Árvore Sintática Abstrata (AST).
 * Utiliza o padrão Visitor para permitir travessia da árvore.
 */
public abstract class ASTNode {

    private final int line;

    public ASTNode(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    /**
     * Aceita um visitor para travessia da AST.
     * @param visitor O visitor que irá processar este nó.
     * @param <T> Tipo de retorno do visitor.
     * @return Resultado do processamento.
     */
    public abstract <T> T accept(ASTVisitor<T> visitor);
}
