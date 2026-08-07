package cinescript.error;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Listener customizado de erros ANTLR4 para capturar erros léxicos
 * e sintáticos com mensagens amigáveis.
 */
public class CineScriptErrorListener extends BaseErrorListener {

    private final List<String> errors = new ArrayList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine, String msg,
                            RecognitionException e) {
        errors.add(String.format("[Linha %d:%d] Erro de sintaxe: %s",
                line, charPositionInLine, msg));
    }

    /**
     * Verifica se houve erros durante a análise.
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * Retorna a lista de mensagens de erro.
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Imprime os erros formatados no stderr.
     */
    public void printErrors() {
        String border = "==================================================";
        System.err.println("+" + border + "+");
        System.err.println("|       ERROS DE COMPILACAO CINESCRIPT           |");
        System.err.println("+" + border + "+");
        for (String error : errors) {
            System.err.println("|  X " + error);
        }
        System.err.println("+" + border + "+");
    }
}
