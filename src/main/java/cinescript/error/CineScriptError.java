package cinescript.error;

/**
 * Exceção customizada do CineScript com informação de localização
 * no arquivo fonte (linha e coluna).
 */
public class CineScriptError extends RuntimeException {

    private final int line;
    private final int column;

    /**
     * Cria um erro com localização no arquivo fonte.
     */
    public CineScriptError(String message, int line, int column) {
        super(String.format("Erro na linha %d, coluna %d: %s", line, column, message));
        this.line = line;
        this.column = column;
    }

    /**
     * Cria um erro sem localização específica.
     */
    public CineScriptError(String message) {
        super(message);
        this.line = -1;
        this.column = -1;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
