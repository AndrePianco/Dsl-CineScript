package cinescript.lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cinescript.error.LexerError;

/**
 * Analisador Léxico (Scanner).
 * Responsável por transformar o código fonte em uma lista de tokens.
 * Reporta caracteres inválidos indicando a linha.
 */
public class Lexer {

    private final String source;
    private final List<Token> tokens;
    private final List<LexerError> errors;
    private int start;
    private int current;
    private int line;

    private static final Map<String, TokenType> keywords = new HashMap<>();

    static {
        // Tipos de dados
        keywords.put("int", TokenType.INT);
        keywords.put("float", TokenType.FLOAT);
        keywords.put("string", TokenType.STRING);

        // Controle de fluxo
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("while", TokenType.WHILE);
        keywords.put("for", TokenType.FOR);

        // Geral
        keywords.put("print", TokenType.PRINT);
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);

        // TODO: Adicionar palavras reservadas dos comandos de domínio
        // keywords.put("comando1", TokenType.DOMAIN_CMD_1);
        // keywords.put("comando2", TokenType.DOMAIN_CMD_2);
    }

    public Lexer(String source) {
        this.source = source;
        this.tokens = new ArrayList<>();
        this.errors = new ArrayList<>();
        this.start = 0;
        this.current = 0;
        this.line = 1;
    }

    /**
     * Realiza a análise léxica completa do código fonte.
     * @return Lista de tokens identificados.
     */
    public List<Token> tokenize() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    /**
     * Identifica o próximo token no código fonte.
     */
    private void scanToken() {
        char c = advance();
        switch (c) {
            // Delimitadores
            case '(': addToken(TokenType.LPAREN); break;
            case ')': addToken(TokenType.RPAREN); break;
            case '{': addToken(TokenType.LBRACE); break;
            case '}': addToken(TokenType.RBRACE); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case ',': addToken(TokenType.COMMA); break;

            // Operadores aritméticos
            case '+': addToken(TokenType.PLUS); break;
            case '-': addToken(TokenType.MINUS); break;
            case '*': addToken(TokenType.STAR); break;
            case '%': addToken(TokenType.MODULO); break;

            // Operadores que podem ser duplos
            case '!': addToken(match('=') ? TokenType.NOT_EQUAL : TokenType.NOT); break;
            case '=': addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL); break;
            case '<': addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS); break;
            case '>': addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER); break;

            // Operadores lógicos
            case '&':
                if (match('&')) {
                    addToken(TokenType.AND);
                } else {
                    errors.add(new LexerError("Caractere inválido '&'. Esperado '&&'.", line));
                }
                break;
            case '|':
                if (match('|')) {
                    addToken(TokenType.OR);
                } else {
                    errors.add(new LexerError("Caractere inválido '|'. Esperado '||'.", line));
                }
                break;

            // Barra: divisão ou comentário
            case '/':
                if (match('/')) {
                    // Comentário de linha: ignora até o final da linha
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else if (match('*')) {
                    // Comentário de bloco
                    blockComment();
                } else {
                    addToken(TokenType.SLASH);
                }
                break;

            // Whitespace
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;

            // String literal
            case '"':
                stringLiteral();
                break;

            default:
                if (isDigit(c)) {
                    numberLiteral();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    errors.add(new LexerError(
                            "Caractere inválido '" + c + "'.", line));
                    addToken(TokenType.INVALID);
                }
                break;
        }
    }

    // ==================== Métodos Auxiliares ====================

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keywords.getOrDefault(text, TokenType.IDENTIFIER);
        addToken(type);
    }

    private void numberLiteral() {
        while (isDigit(peek())) advance();

        if (peek() == '.' && isDigit(peekNext())) {
            advance(); // consome o '.'
            while (isDigit(peek())) advance();
            addToken(TokenType.FLOAT_LITERAL, Double.parseDouble(source.substring(start, current)));
        } else {
            addToken(TokenType.INTEGER_LITERAL, Integer.parseInt(source.substring(start, current)));
        }
    }

    private void stringLiteral() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            errors.add(new LexerError("String não terminada.", line));
            return;
        }

        advance(); // Fecha as aspas
        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING_LITERAL, value);
    }

    private void blockComment() {
        while (!isAtEnd()) {
            if (peek() == '\n') line++;
            if (peek() == '*' && peekNext() == '/') {
                advance(); // consome *
                advance(); // consome /
                return;
            }
            advance();
        }
        errors.add(new LexerError("Comentário de bloco não fechado.", line));
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        current++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private char advance() {
        return source.charAt(current++);
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
               c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    // ==================== Getters ====================

    public List<Token> getTokens() {
        return tokens;
    }

    public List<LexerError> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
