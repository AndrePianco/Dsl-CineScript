package cinescript.error;

/**
 * Representa um erro encontrado durante a execução (interpretação).
 */
public class RuntimeError extends RuntimeException {

    private final int line;

    public RuntimeError(String message, int line) {
        super(message);
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return String.format("[Erro de Execução] Linha %d: %s", line, getMessage());
    }
}
