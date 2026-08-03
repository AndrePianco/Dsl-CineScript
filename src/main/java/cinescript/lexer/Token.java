package cinescript.lexer;

/**
 * Representa um token identificado pelo analisador léxico.
 * Cada token possui um tipo, o valor lexema, e a linha onde foi encontrado.
 */
public class Token {

    private final TokenType type;
    private final String lexeme;
    private final Object literal;
    private final int line;

    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Object getLiteral() {
        return literal;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return String.format("Token{type=%s, lexeme='%s', literal=%s, line=%d}",
                type, lexeme, literal, line);
    }
}
