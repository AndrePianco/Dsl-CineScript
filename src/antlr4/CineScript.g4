/**
 * CineScript — DSL para Roteiros de Cinema e Serie
 *
 * Gramatica combinada ANTLR4 (Lexer + Parser)
 * Suporta: metadados, personagens, cenas, acoes e dialogos.
 */
grammar CineScript;

// =========================================================
//                     PARSER RULES
// =========================================================

script
    : metadata* declaration* scene+ EOF
    ;

// -- Metadados do roteiro ---------------------------------

metadata
    : TITLE COLON STRING          # TitleMeta
    | AUTHOR COLON STRING         # AuthorMeta
    | DATE COLON STRING           # DateMeta
    | DRAFT COLON STRING          # DraftMeta
    ;

// -- Declaracao de personagens ----------------------------

declaration
    : CHARACTER IDENTIFIER AS STRING
    ;

// -- Cenas ------------------------------------------------

scene
    : SCENE STRING locationType timeOfDay LBRACE sceneElement* RBRACE
    ;

locationType
    : INTERIOR
    | EXTERIOR
    | INT_EXT
    ;

timeOfDay
    : DAY
    | NIGHT
    | DAWN
    | DUSK
    | CONTINUOUS
    ;

// -- Elementos de cena ------------------------------------

sceneElement
    : actionLine
    | dialogue
    ;

actionLine
    : ACTION COLON STRING
    ;

dialogue
    : IDENTIFIER LPAREN STRING RPAREN COLON STRING    # DialogueWithParenthetical
    | IDENTIFIER COLON STRING                          # DialoguePlain
    ;

// =========================================================
//                     LEXER RULES
// =========================================================

// -- Palavras-chave ---------------------------------------

TITLE       : 'title' ;
AUTHOR      : 'author' ;
DATE        : 'date' ;
DRAFT       : 'draft' ;
CHARACTER   : 'character' ;
AS          : 'as' ;
SCENE       : 'scene' ;
ACTION      : 'action' ;

// -- Tipos de locacao -------------------------------------

INTERIOR    : 'interior' ;
EXTERIOR    : 'exterior' ;
INT_EXT     : 'int-ext' ;

// -- Horarios do dia --------------------------------------

DAY         : 'day' ;
NIGHT       : 'night' ;
DAWN        : 'dawn' ;
DUSK        : 'dusk' ;
CONTINUOUS  : 'continuous' ;

// -- Simbolos ---------------------------------------------

COLON   : ':' ;
LBRACE  : '{' ;
RBRACE  : '}' ;
LPAREN  : '(' ;
RPAREN  : ')' ;

// -- Literais ---------------------------------------------

STRING  : '"' (~["\r\n])* '"' ;

// -- Identificadores --------------------------------------

IDENTIFIER : [a-zA-Z_][a-zA-Z0-9_]* ;

// -- Espacos e comentarios (ignorados) --------------------

WS              : [ \t\r\n]+ -> skip ;
LINE_COMMENT    : '//' ~[\r\n]* -> skip ;
BLOCK_COMMENT   : '/*' .*? '*/' -> skip ;
