package cinescript.parser;

import java.util.ArrayList;
import java.util.List;

import cinescript.error.ParserError;
import cinescript.lexer.Token;
import cinescript.lexer.TokenType;
import cinescript.parser.ast.*;

/**
 * Analisador Sintático (Parser).
 * Responsável por consumir a lista de tokens e gerar a Árvore Sintática Abstrata (AST).
 * Utiliza a técnica de Recursive Descent Parsing.
 */
public class Parser {

    private final List<Token> tokens;
    private final List<ParserError> errors;
    private int current;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.errors = new ArrayList<>();
        this.current = 0;
    }

    /**
     * Inicia o parsing e retorna o nó raiz do programa.
     * @return ProgramNode contendo todas as declarações.
     */
    public ProgramNode parse() {
        List<ASTNode> statements = new ArrayList<>();

        while (!isAtEnd()) {
            try {
                ASTNode stmt = parseStatement();
                if (stmt != null) {
                    statements.add(stmt);
                }
            } catch (ParserError e) {
                errors.add(e);
                synchronize();
            }
        }

        return new ProgramNode(statements, 1);
    }

    // ==================== Parsing de Statements ====================

    /**
     * Faz o parsing de um statement individual.
     */
    private ASTNode parseStatement() {
        // TODO: Implementar o parsing de cada tipo de statement
        // Verificar o token atual e despachar para o método correto

        if (check(TokenType.INT) || check(TokenType.FLOAT) || check(TokenType.STRING)) {
            return parseVarDeclaration();
        }
        if (check(TokenType.IF)) {
            return parseIf();
        }
        if (check(TokenType.WHILE)) {
            return parseWhile();
        }
        if (check(TokenType.PRINT)) {
            return parsePrint();
        }
        if (check(TokenType.LBRACE)) {
            return parseBlock();
        }
        // TODO: Adicionar parsing dos comandos de domínio
        // if (check(TokenType.DOMAIN_CMD_1)) { return parseDomainCommand(); }
        // if (check(TokenType.DOMAIN_CMD_2)) { return parseDomainCommand(); }

        if (check(TokenType.IDENTIFIER)) {
            return parseAssignmentOrExpression();
        }

        throw new ParserError("Statement inesperado: '" + peek().getLexeme() + "'.", peek().getLine());
    }

    /**
     * Parsing de declaração de variável.
     * Formato: tipo nome = expressão;
     */
    private ASTNode parseVarDeclaration() {
        Token typeToken = advance(); // consome o tipo (int, float, string)
        Token name = consume(TokenType.IDENTIFIER, "Esperado nome da variável após '" + typeToken.getLexeme() + "'.");

        ASTNode initializer = null;
        if (match(TokenType.EQUAL)) {
            initializer = parseExpression();
        }

        consume(TokenType.SEMICOLON, "Esperado ';' após declaração de variável.");
        return new VarDeclarationNode(typeToken.getLexeme(), name.getLexeme(), initializer, typeToken.getLine());
    }

    /**
     * Parsing de estrutura condicional if/else.
     */
    private ASTNode parseIf() {
        Token ifToken = consume(TokenType.IF, "Esperado 'if'.");
        consume(TokenType.LPAREN, "Esperado '(' após 'if'.");
        ASTNode condition = parseExpression();
        consume(TokenType.RPAREN, "Esperado ')' após condição do if.");

        ASTNode thenBranch = parseStatement();
        ASTNode elseBranch = null;

        if (match(TokenType.ELSE)) {
            elseBranch = parseStatement();
        }

        return new IfNode(condition, thenBranch, elseBranch, ifToken.getLine());
    }

    /**
     * Parsing de laço while.
     */
    private ASTNode parseWhile() {
        Token whileToken = consume(TokenType.WHILE, "Esperado 'while'.");
        consume(TokenType.LPAREN, "Esperado '(' após 'while'.");
        ASTNode condition = parseExpression();
        consume(TokenType.RPAREN, "Esperado ')' após condição do while.");

        ASTNode body = parseStatement();
        return new WhileNode(condition, body, whileToken.getLine());
    }

    /**
     * Parsing de comando print.
     */
    private ASTNode parsePrint() {
        Token printToken = consume(TokenType.PRINT, "Esperado 'print'.");
        consume(TokenType.LPAREN, "Esperado '(' após 'print'.");
        ASTNode expression = parseExpression();
        consume(TokenType.RPAREN, "Esperado ')' após expressão do print.");
        consume(TokenType.SEMICOLON, "Esperado ';' após print.");
        return new PrintNode(expression, printToken.getLine());
    }

    /**
     * Parsing de bloco de código { ... }.
     */
    private ASTNode parseBlock() {
        Token braceToken = consume(TokenType.LBRACE, "Esperado '{'.");
        List<ASTNode> statements = new ArrayList<>();

        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            statements.add(parseStatement());
        }

        consume(TokenType.RBRACE, "Esperado '}' para fechar o bloco.");
        return new BlockNode(statements, braceToken.getLine());
    }

    /**
     * Parsing de atribuição ou expressão standalone.
     */
    private ASTNode parseAssignmentOrExpression() {
        Token name = advance(); // consome o identificador

        if (match(TokenType.EQUAL)) {
            ASTNode value = parseExpression();
            consume(TokenType.SEMICOLON, "Esperado ';' após atribuição.");
            return new AssignmentNode(name.getLexeme(), value, name.getLine());
        }

        // Se não for atribuição, é uma expressão (rewind)
        current--; // volta o token
        ASTNode expr = parseExpression();
        consume(TokenType.SEMICOLON, "Esperado ';' após expressão.");
        return expr;
    }

    /**
     * Parsing de comando de domínio genérico.
     * TODO: Adaptar conforme o tema escolhido.
     */
    private ASTNode parseDomainCommand() {
        Token cmdToken = advance();
        consume(TokenType.LPAREN, "Esperado '(' após comando '" + cmdToken.getLexeme() + "'.");

        List<ASTNode> arguments = new ArrayList<>();
        if (!check(TokenType.RPAREN)) {
            arguments.add(parseExpression());
            while (match(TokenType.COMMA)) {
                arguments.add(parseExpression());
            }
        }

        consume(TokenType.RPAREN, "Esperado ')' após argumentos do comando.");
        consume(TokenType.SEMICOLON, "Esperado ';' após comando de domínio.");

        return new DomainCommandNode(cmdToken.getLexeme(), arguments, cmdToken.getLine());
    }

    // ==================== Parsing de Expressões ====================

    /**
     * Ponto de entrada para parsing de expressões.
     * Segue a precedência: OR < AND < Comparação < Adição < Multiplicação < Unário < Primário
     */
    private ASTNode parseExpression() {
        return parseOr();
    }

    private ASTNode parseOr() {
        ASTNode left = parseAnd();
        while (match(TokenType.OR)) {
            String operator = previous().getLexeme();
            ASTNode right = parseAnd();
            left = new BinaryExprNode(left, operator, right, previous().getLine());
        }
        return left;
    }

    private ASTNode parseAnd() {
        ASTNode left = parseEquality();
        while (match(TokenType.AND)) {
            String operator = previous().getLexeme();
            ASTNode right = parseEquality();
            left = new BinaryExprNode(left, operator, right, previous().getLine());
        }
        return left;
    }

    private ASTNode parseEquality() {
        ASTNode left = parseComparison();
        while (match(TokenType.EQUAL_EQUAL) || match(TokenType.NOT_EQUAL)) {
            String operator = previous().getLexeme();
            ASTNode right = parseComparison();
            left = new BinaryExprNode(left, operator, right, previous().getLine());
        }
        return left;
    }

    private ASTNode parseComparison() {
        ASTNode left = parseAddition();
        while (match(TokenType.LESS) || match(TokenType.LESS_EQUAL) ||
               match(TokenType.GREATER) || match(TokenType.GREATER_EQUAL)) {
            String operator = previous().getLexeme();
            ASTNode right = parseAddition();
            left = new BinaryExprNode(left, operator, right, previous().getLine());
        }
        return left;
    }

    private ASTNode parseAddition() {
        ASTNode left = parseMultiplication();
        while (match(TokenType.PLUS) || match(TokenType.MINUS)) {
            String operator = previous().getLexeme();
            ASTNode right = parseMultiplication();
            left = new BinaryExprNode(left, operator, right, previous().getLine());
        }
        return left;
    }

    private ASTNode parseMultiplication() {
        ASTNode left = parseUnary();
        while (match(TokenType.STAR) || match(TokenType.SLASH) || match(TokenType.MODULO)) {
            String operator = previous().getLexeme();
            ASTNode right = parseUnary();
            left = new BinaryExprNode(left, operator, right, previous().getLine());
        }
        return left;
    }

    private ASTNode parseUnary() {
        if (match(TokenType.MINUS) || match(TokenType.NOT)) {
            String operator = previous().getLexeme();
            ASTNode operand = parseUnary();
            return new UnaryExprNode(operator, operand, previous().getLine());
        }
        return parsePrimary();
    }

    private ASTNode parsePrimary() {
        if (match(TokenType.INTEGER_LITERAL)) {
            return new LiteralNode(previous().getLiteral(), "int", previous().getLine());
        }
        if (match(TokenType.FLOAT_LITERAL)) {
            return new LiteralNode(previous().getLiteral(), "float", previous().getLine());
        }
        if (match(TokenType.STRING_LITERAL)) {
            return new LiteralNode(previous().getLiteral(), "string", previous().getLine());
        }
        if (match(TokenType.TRUE)) {
            return new LiteralNode(true, "boolean", previous().getLine());
        }
        if (match(TokenType.FALSE)) {
            return new LiteralNode(false, "boolean", previous().getLine());
        }
        if (match(TokenType.IDENTIFIER)) {
            return new IdentifierNode(previous().getLexeme(), previous().getLine());
        }
        if (match(TokenType.LPAREN)) {
            ASTNode expr = parseExpression();
            consume(TokenType.RPAREN, "Esperado ')' após expressão.");
            return expr;
        }

        throw new ParserError("Expressão esperada, encontrado: '" + peek().getLexeme() + "'.", peek().getLine());
    }

    // ==================== Métodos Auxiliares ====================

    private boolean match(TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw new ParserError(message, peek().getLine());
    }

    /**
     * Recuperação de erro: avança tokens até encontrar um ponto seguro.
     */
    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            if (previous().getType() == TokenType.SEMICOLON) return;

            switch (peek().getType()) {
                case INT:
                case FLOAT:
                case STRING:
                case IF:
                case WHILE:
                case FOR:
                case PRINT:
                    return;
                default:
                    break;
            }
            advance();
        }
    }

    // ==================== Getters ====================

    public List<ParserError> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
