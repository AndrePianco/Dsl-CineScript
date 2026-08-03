package cinescript.parser.ast;

import java.util.List;

/**
 * Nó que representa um comando específico do domínio da DSL.
 * Possui um nome de comando e uma lista de argumentos.
 * 
 * TODO: Adaptar conforme o tema escolhido para a DSL.
 * Ex: Se o tema for cinema → PLAY "filme.mp4", SUBTITLE "legenda.srt"
 */
public class DomainCommandNode extends ASTNode {

    private final String commandName;
    private final List<ASTNode> arguments;

    public DomainCommandNode(String commandName, List<ASTNode> arguments, int line) {
        super(line);
        this.commandName = commandName;
        this.arguments = arguments;
    }

    public String getCommandName() {
        return commandName;
    }

    public List<ASTNode> getArguments() {
        return arguments;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitDomainCommand(this);
    }
}
