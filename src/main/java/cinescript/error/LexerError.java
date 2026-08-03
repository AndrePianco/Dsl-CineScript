package cinescript.error;

/**
 * Representa um erro encontrado durante a análise léxica.
 */
public class LexerError extends RuntimeException {

    private final int line;

    public LexerError(String message, int line) {
        super(message);
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return String.format("[Erro Léxico] Linha %d: %s", line, getMessage());
    }
}
