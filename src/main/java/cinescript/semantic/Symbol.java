package cinescript.semantic;

/**
 * Representa um símbolo na Tabela de Símbolos.
 * Armazena informações sobre variáveis declaradas (nome, tipo, escopo).
 */
public class Symbol {

    private final String name;
    private final String type;      // "int", "float", "string"
    private Object value;
    private final int declarationLine;

    public Symbol(String name, String type, Object value, int declarationLine) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.declarationLine = declarationLine;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getDeclarationLine() {
        return declarationLine;
    }

    @Override
    public String toString() {
        return String.format("Symbol{name='%s', type='%s', value=%s, line=%d}",
                name, type, value, declarationLine);
    }
}
