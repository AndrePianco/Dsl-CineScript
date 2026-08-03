package cinescript.lexer;

/**
 * Enum representando todos os tipos de tokens reconhecidos pelo analisador léxico.
 */
public enum TokenType {

    // === Literais ===
    INTEGER_LITERAL,    // 42
    FLOAT_LITERAL,      // 3.14
    STRING_LITERAL,     // "hello"

    // === Identificador ===
    IDENTIFIER,         // nomeVariavel

    // === Tipos de Dados ===
    INT,                // int
    FLOAT,              // float
    STRING,             // string

    // === Palavras Reservadas - Controle ===
    IF,                 // if
    ELSE,               // else
    WHILE,              // while
    FOR,                // for

    // === Palavras Reservadas - Geral ===
    PRINT,              // print
    TRUE,               // true
    FALSE,              // false

    // === Comandos de Domínio (placeholders - definir com o tema) ===
    DOMAIN_CMD_1,       // TODO: Substituir pelo comando de domínio 1
    DOMAIN_CMD_2,       // TODO: Substituir pelo comando de domínio 2

    // === Operadores Aritméticos ===
    PLUS,               // +
    MINUS,              // -
    STAR,               // *
    SLASH,              // /
    MODULO,             // %

    // === Operadores de Comparação ===
    EQUAL_EQUAL,        // ==
    NOT_EQUAL,          // !=
    LESS,               // <
    LESS_EQUAL,         // <=
    GREATER,            // >
    GREATER_EQUAL,      // >=

    // === Operadores Lógicos ===
    AND,                // &&
    OR,                 // ||
    NOT,                // !

    // === Atribuição ===
    EQUAL,              // =

    // === Delimitadores ===
    LPAREN,             // (
    RPAREN,             // )
    LBRACE,             // {
    RBRACE,             // }
    SEMICOLON,          // ;
    COMMA,              // ,

    // === Especiais ===
    EOF,                // Fim do arquivo
    INVALID             // Token inválido
}
