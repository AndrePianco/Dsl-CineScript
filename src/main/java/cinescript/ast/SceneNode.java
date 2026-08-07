package cinescript.ast;

import java.util.List;

/**
 * Nó de cena — contém o cabeçalho (local, tipo, horário)
 * e todos os elementos internos (ações, diálogos, etc).
 */
public class SceneNode {

    private final String location;
    private final String locationType;  // "INT.", "EXT.", "INT./EXT."
    private final String timeOfDay;     // "DAY", "NIGHT", "DAWN", "DUSK", "CONTINUOUS"
    private final List<SceneElement> elements;
    private final int line;
    private final int column;

    public SceneNode(String location, String locationType, String timeOfDay,
                     List<SceneElement> elements, int line, int column) {
        this.location = location;
        this.locationType = locationType;
        this.timeOfDay = timeOfDay;
        this.elements = elements;
        this.line = line;
        this.column = column;
    }

    public String getLocation() {
        return location;
    }

    public String getLocationType() {
        return locationType;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public List<SceneElement> getElements() {
        return elements;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    /**
     * Gera o cabeçalho de cena no formato padrão de roteiro.
     * Exemplo: "INT. WHITE RESIDENCE - KITCHEN - DAY"
     */
    public String getHeading() {
        return locationType + " " + location.toUpperCase() + " - " + timeOfDay;
    }
}
