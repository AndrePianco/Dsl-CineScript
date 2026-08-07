package cinescript.ast;

/**
 * Nó de declaração de personagem.
 * Mapeia um identificador curto (ex: "Walter") a um nome completo (ex: "Walter White").
 */
public class CharacterDeclarationNode {

    private final String identifier;
    private final String fullName;
    private final int line;
    private final int column;

    public CharacterDeclarationNode(String identifier, String fullName, int line, int column) {
        this.identifier = identifier;
        this.fullName = fullName;
        this.line = line;
        this.column = column;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getFullName() {
        return fullName;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
