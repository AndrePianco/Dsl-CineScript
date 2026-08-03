# Gramática da DSL CineScript

## Notação
- `|` indica alternativa
- `*` indica zero ou mais repetições
- `?` indica opcional
- `MAIÚSCULO` indica token terminal
- `minúsculo` indica não-terminal

---

## Gramática Formal (BNF)

```bnf
programa        ::= statement*

statement       ::= varDeclaration
                  | assignment
                  | ifStatement
                  | whileStatement
                  | printStatement
                  | domainCommand
                  | block

varDeclaration  ::= type IDENTIFIER ('=' expression)? ';'

assignment      ::= IDENTIFIER '=' expression ';'

ifStatement     ::= 'if' '(' expression ')' statement ('else' statement)?

whileStatement  ::= 'while' '(' expression ')' statement

printStatement  ::= 'print' '(' expression ')' ';'

domainCommand   ::= DOMAIN_KEYWORD '(' argumentList? ')' ';'

block           ::= '{' statement* '}'

type            ::= 'int' | 'float' | 'string'

argumentList    ::= expression (',' expression)*
```

---

## Expressões (por precedência, da menor para a maior)

```bnf
expression      ::= orExpr

orExpr          ::= andExpr ('||' andExpr)*

andExpr         ::= equalityExpr ('&&' equalityExpr)*

equalityExpr    ::= comparisonExpr (('==' | '!=') comparisonExpr)*

comparisonExpr  ::= additionExpr (('<' | '<=' | '>' | '>=') additionExpr)*

additionExpr    ::= multExpr (('+' | '-') multExpr)*

multExpr        ::= unaryExpr (('*' | '/' | '%') unaryExpr)*

unaryExpr       ::= ('-' | '!') unaryExpr
                  | primary

primary         ::= INTEGER_LITERAL
                  | FLOAT_LITERAL
                  | STRING_LITERAL
                  | 'true'
                  | 'false'
                  | IDENTIFIER
                  | '(' expression ')'
```

---

## Tokens Léxicos

### Palavras Reservadas
| Token     | Lexema   |
|-----------|----------|
| INT       | `int`    |
| FLOAT     | `float`  |
| STRING    | `string` |
| IF        | `if`     |
| ELSE      | `else`   |
| WHILE     | `while`  |
| FOR       | `for`    |
| PRINT     | `print`  |
| TRUE      | `true`   |
| FALSE     | `false`  |

### Operadores
| Token          | Lexema |
|----------------|--------|
| PLUS           | `+`    |
| MINUS          | `-`    |
| STAR           | `*`    |
| SLASH          | `/`    |
| MODULO         | `%`    |
| EQUAL          | `=`    |
| EQUAL_EQUAL    | `==`   |
| NOT_EQUAL      | `!=`   |
| LESS           | `<`    |
| LESS_EQUAL     | `<=`   |
| GREATER        | `>`    |
| GREATER_EQUAL  | `>=`   |
| AND            | `&&`   |
| OR             | `\|\|` |
| NOT            | `!`    |

### Delimitadores
| Token      | Lexema |
|------------|--------|
| LPAREN     | `(`    |
| RPAREN     | `)`    |
| LBRACE     | `{`    |
| RBRACE     | `}`    |
| SEMICOLON  | `;`    |
| COMMA      | `,`    |

### Literais
| Token            | Descrição                     | Exemplo       |
|------------------|-------------------------------|---------------|
| INTEGER_LITERAL  | Número inteiro                | `42`, `0`     |
| FLOAT_LITERAL    | Número decimal                | `3.14`, `0.5` |
| STRING_LITERAL   | Cadeia entre aspas duplas     | `"hello"`     |
| IDENTIFIER       | Nome de variável              | `x`, `total`  |

### Comentários
```
// Comentário de linha (ignorado pelo lexer)

/* Comentário de bloco
   multi-linha (ignorado pelo lexer) */
```

---

## Tipos de Dados

| Tipo     | Descrição              | Valor Default |
|----------|------------------------|---------------|
| `int`    | Número inteiro         | `0`           |
| `float`  | Número decimal         | `0.0`         |
| `string` | Cadeia de caracteres   | `""`          |

### Regras de Tipo
- `int` e `float` são compatíveis em atribuições (promoção numérica)
- `string` não pode receber valores numéricos diretamente
- O operador `+` entre `string` e qualquer tipo realiza concatenação
- Operações aritméticas (`-`, `*`, `/`, `%`) não são permitidas com `string`

---

## Comandos de Domínio

> **TODO**: Definir os comandos de domínio após escolha do tema.
> A DSL deve ter no mínimo 2 comandos exclusivos do domínio.

### Placeholder
```
// domainCmd1(argumento1, argumento2);
// domainCmd2(argumento1);
```

---

## Exemplos de Código

### Variáveis e Tipos
```
int idade = 25;
float peso = 72.5;
string nome = "João";
```

### Condicional
```
if (idade >= 18) {
    print("Maior de idade");
} else {
    print("Menor de idade");
}
```

### Loop
```
int i = 0;
while (i < 10) {
    print(i);
    i = i + 1;
}
```
