package cinescript.ast;

/**
 * Nó de diálogo — fala de um personagem com parentético opcional.
 * Exemplo: Walter ("hesitante"): "Preciso te contar uma coisa."
 */
public class DialogueNode implements SceneElement {

    private final String characterId;
    private final String parenthetical;  // pode ser null
    private final String text;
    private final int line;
    private final int column;

    public DialogueNode(String characterId, String parenthetical, String text,
                        int line, int column) {
        this.characterId = characterId;
        this.parenthetical = parenthetical;
        this.text = text;
        this.line = line;
        this.column = column;
    }

    public String getCharacterId() {
        return characterId;
    }

    public String getParenthetical() {
        return parenthetical;
    }

    public String getText() {
        return text;
    }

    public boolean hasParenthetical() {
        return parenthetical != null;
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
