package cinescript.error;

/**
 * Representa um erro encontrado durante a análise sintática.
 */
public class ParserError extends RuntimeException {

    private final int line;

    public ParserError(String message, int line) {
        super(message);
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return String.format("[Erro Sintático] Linha %d: %s", line, getMessage());
    }
}
