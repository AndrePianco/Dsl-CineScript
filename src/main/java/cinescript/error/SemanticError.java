package cinescript.error;

/**
 * Representa um erro encontrado durante a análise semântica.
 */
public class SemanticError extends RuntimeException {

    private final int line;

    public SemanticError(String message, int line) {
        super(message);
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return String.format("[Erro Semântico] Linha %d: %s", line, getMessage());
    }
}
