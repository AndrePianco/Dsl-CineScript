package cinescript.ast;

/**
 * Nó de metadado do roteiro (title, author, date, draft).
 * Armazena um par chave/valor.
 */
public class MetadataNode {

    private final String key;
    private final String value;
    private final int line;
    private final int column;

    public MetadataNode(String key, String value, int line, int column) {
        this.key = key;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
