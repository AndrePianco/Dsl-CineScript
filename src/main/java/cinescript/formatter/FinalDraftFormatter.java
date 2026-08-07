package cinescript.formatter;

import cinescript.ast.*;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Formatador de roteiro no padrao Final Draft / Industria Cinematografica.
 *
 * Regras de formatacao (baseadas em Courier 12pt, US Letter):
 * - Cabecalho de cena: col 0, ALL CAPS
 * - Acao:              col 0, caixa normal, wrap em 61 chars
 * - Nome personagem:   col 22, ALL CAPS
 * - Parentetico:       col 16, entre parenteses
 * - Dialogo:           col 10, wrap em 35 chars
 */
public class FinalDraftFormatter {

    private static final int PAGE_WIDTH = 61;
    private static final int CHARACTER_INDENT = 22;
    private static final int DIALOGUE_INDENT = 10;
    private static final int DIALOGUE_WIDTH = 35;
    private static final int PARENTHETICAL_INDENT = 16;

    private final Map<String, String> characterNames;

    public FinalDraftFormatter(ScriptNode script) {
        this.characterNames = script.getCharacters().stream()
                .collect(Collectors.toMap(
                        CharacterDeclarationNode::getIdentifier,
                        CharacterDeclarationNode::getFullName
                ));
    }

    /**
     * Formata o roteiro completo no padrao Final Draft.
     */
    public String format(ScriptNode script) {
        StringBuilder sb = new StringBuilder();

        formatTitlePage(sb, script);

        for (int i = 0; i < script.getScenes().size(); i++) {
            if (i > 0) {
                sb.append("\n");
            }
            formatScene(sb, script.getScenes().get(i));
        }

        return sb.toString();
    }

    // =========================================================
    //                  PAGINA DE TITULO
    // =========================================================

    private void formatTitlePage(StringBuilder sb, ScriptNode script) {
        String title = script.getMetadataValue("title");
        String author = script.getMetadataValue("author");
        String date = script.getMetadataValue("date");
        String draft = script.getMetadataValue("draft");

        if (title != null || author != null) {
            sb.append("\n\n");

            if (title != null) {
                sb.append(centerText(title.toUpperCase(), PAGE_WIDTH)).append("\n\n");
            }
            if (author != null) {
                sb.append(centerText("Escrito por", PAGE_WIDTH)).append("\n");
                sb.append(centerText(author, PAGE_WIDTH)).append("\n");
            }
            if (draft != null) {
                sb.append("\n");
                sb.append(centerText(draft, PAGE_WIDTH)).append("\n");
            }
            if (date != null) {
                sb.append(centerText(date, PAGE_WIDTH)).append("\n");
            }

            sb.append("\n\n");
            sb.append("=".repeat(PAGE_WIDTH)).append("\n\n\n");
        }
    }

    // =========================================================
    //                      CENA
    // =========================================================

    private void formatScene(StringBuilder sb, SceneNode scene) {
        // Cabecalho de cena (slug line)
        sb.append(scene.getHeading()).append("\n\n");

        // Elementos da cena
        for (SceneElement element : scene.getElements()) {
            if (element instanceof ActionNode action) {
                formatAction(sb, action);
            } else if (element instanceof DialogueNode dialogue) {
                formatDialogue(sb, dialogue);
            }
        }
    }

    // =========================================================
    //              ELEMENTOS INDIVIDUAIS
    // =========================================================

    private void formatAction(StringBuilder sb, ActionNode action) {
        String wrapped = wordWrap(action.getDescription(), PAGE_WIDTH);
        sb.append(wrapped).append("\n\n");
    }

    private void formatDialogue(StringBuilder sb, DialogueNode dialogue) {
        // Nome do personagem (ALL CAPS, indentado)
        String charName = resolveCharacterName(dialogue.getCharacterId());
        sb.append(indent(charName.toUpperCase(), CHARACTER_INDENT)).append("\n");

        // Parentetico (opcional)
        if (dialogue.hasParenthetical()) {
            String paren = "(" + dialogue.getParenthetical() + ")";
            sb.append(indent(paren, PARENTHETICAL_INDENT)).append("\n");
        }

        // Texto do dialogo (indentado, com word-wrap)
        String wrapped = wordWrap(dialogue.getText(), DIALOGUE_WIDTH);
        for (String line : wrapped.split("\n")) {
            sb.append(indent(line, DIALOGUE_INDENT)).append("\n");
        }
        sb.append("\n");
    }

    // =========================================================
    //               METODOS UTILITARIOS
    // =========================================================

    private String resolveCharacterName(String identifier) {
        return characterNames.getOrDefault(identifier, identifier);
    }

    private String centerText(String text, int width) {
        if (text.length() >= width) return text;
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text;
    }

    private String indent(String text, int spaces) {
        return " ".repeat(spaces) + text;
    }

    private String wordWrap(String text, int maxWidth) {
        if (text.length() <= maxWidth) return text;

        StringBuilder sb = new StringBuilder();
        String[] words = text.split(" ");
        int lineLength = 0;

        for (String word : words) {
            if (lineLength + word.length() + (lineLength > 0 ? 1 : 0) > maxWidth) {
                sb.append("\n");
                lineLength = 0;
            }
            if (lineLength > 0) {
                sb.append(" ");
                lineLength++;
            }
            sb.append(word);
            lineLength += word.length();
        }

        return sb.toString();
    }
}
