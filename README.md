# CineScript - DSL Compiler

## Descrição
CineScript é uma Linguagem de Domínio Específico (DSL) customizada com compilador implementado em Java.

## Estrutura do Projeto

```
Dsl-CineScript/
├── src/
│   └── main/
│       └── java/
│           └── cinescript/
│               ├── Main.java                  # Ponto de entrada
│               ├── lexer/
│               │   ├── Lexer.java             # Analisador Léxico
│               │   ├── Token.java             # Representação de Token
│               │   └── TokenType.java         # Enum dos tipos de token
│               ├── parser/
│               │   ├── Parser.java            # Analisador Sintático
│               │   └── ast/
│               │       ├── ASTNode.java        # Nó base da AST
│               │       ├── ProgramNode.java    # Nó raiz do programa
│               │       ├── VarDeclarationNode.java
│               │       ├── AssignmentNode.java
│               │       ├── IfNode.java
│               │       ├── WhileNode.java
│               │       ├── BinaryExprNode.java
│               │       ├── UnaryExprNode.java
│               │       ├── LiteralNode.java
│               │       ├── IdentifierNode.java
│               │       ├── BlockNode.java
│               │       ├── DomainCommandNode.java  # Comandos específicos do domínio
│               │       └── PrintNode.java
│               ├── semantic/
│               │   ├── SemanticAnalyzer.java   # Analisador Semântico
│               │   ├── SymbolTable.java        # Tabela de Símbolos
│               │   └── Symbol.java             # Representação de símbolo
│               ├── interpreter/
│               │   ├── Interpreter.java        # Interpretador (executa AST)
│               │   └── Environment.java        # Ambiente de execução
│               └── error/
│                   ├── LexerError.java         # Erros léxicos
│                   ├── ParserError.java        # Erros sintáticos
│                   ├── SemanticError.java       # Erros semânticos
│                   └── RuntimeError.java        # Erros de execução
├── tests/
│   ├── valid/                                  # Casos de teste válidos
│   │   ├── test_variables.cine
│   │   ├── test_control_flow.cine
│   │   ├── test_loops.cine
│   │   └── test_domain_commands.cine
│   └── invalid/                                # Casos de teste inválidos
│       ├── test_lexer_errors.cine
│       ├── test_syntax_errors.cine
│       └── test_semantic_errors.cine
├── docs/
│   └── grammar.md                              # Documentação da gramática
├── README.md
└── LICENSE
```

## Tipos de Dados Suportados
- `int` - Números inteiros
- `float` - Números decimais
- `string` - Cadeias de caracteres

## Fases do Compilador
1. **Análise Léxica** — Tokenização do código fonte
2. **Análise Sintática** — Parsing e geração da AST
3. **Análise Semântica** — Verificação de tipos, escopos e declarações
4. **Interpretador** — Execução da AST

## Como Rodar

```bash
# Compilar o projeto
javac -d out src/main/java/cinescript/**/*.java src/main/java/cinescript/*.java

# Executar um arquivo .cine
java -cp out cinescript.Main <arquivo.cine>
```

## Gramática
Consulte [docs/grammar.md](docs/grammar.md) para a especificação completa da gramática.
