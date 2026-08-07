package cinescript.ast;

/**
 * Nó de ação — descrição narrativa do que acontece na cena.
 * Exemplo: action: "Walter se levanta da cadeira."
 */
public class ActionNode implements SceneElement {

    private final String description;
    private final int line;
    private final int column;

    public ActionNode(String description, int line, int column) {
        this.description = description;
        this.line = line;
        this.column = column;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public int getColumn() {
        return column;
    }
}
