# 🎬 CineScript

**DSL para Roteiros de Cinema e Série**

CineScript é uma linguagem de domínio específico (DSL) enxuta e focada para escrever roteiros de filmes e séries. O compilador lê arquivos `.cine` e formata o roteiro no padrão profissional da indústria (estilo Final Draft).

---

## 📋 Recursos

- ✅ Metadados do roteiro (título, autor, data, versão)
- ✅ Declaração de personagens com nomes completos para facilitar a escrita
- ✅ Cenas com locação, tipo (interior/exterior) e horário
- ✅ Linhas de ação/descrição
- ✅ Diálogos com parentéticos opcionais (direções de atuação)
- ✅ Formatação automática rigorosa no padrão Final Draft (Courier, margens corretas, etc.)

---

## 🚀 Início Rápido

### Pré-requisitos

- **Java 17+** (JDK)
- **Gradle 8+**

### Build

```bash
# Compilar o projeto (gera lexer/parser ANTLR4 automaticamente)
./gradlew build
```

### Uso

O compilador lê o arquivo `.cine` e exibe o roteiro formatado no terminal ou salva em um arquivo.

```bash
# Formatar roteiro no padrão Final Draft (imprime no terminal)
./gradlew run --args="examples/example.cine"

# Salvar saída formatada em arquivo de texto
./gradlew run --args="examples/example.cine -o output.txt"
```

---

## 📝 Sintaxe

```cinescript
// Metadados
title: "Meu Filme"
author: "Autor"

// Personagens (atalho 'Walter' gera 'WALTER WHITE' na formatação final)
character Walter as "Walter White"

// Cenas
scene "White Residence - Kitchen" interior day {

    action: "Walter sentado à mesa do café da manhã. Ovos e bacon intocados."

    Walter: "Preciso te contar algo."

    Walter ("hesitante"): "Algo importante."
}
```

### Elementos Suportados

| Elemento     | Sintaxe                                     |
|-------------|----------------------------------------------|
| Metadados   | `title: "valor"`, `author: "valor"`, etc.    |
| Personagem  | `character ID as "Nome Completo"`            |
| Cena        | `scene "Local" tipo horário { ... }`         |
| Ação        | `action: "descrição"`                        |
| Diálogo     | `ID: "fala"` ou `ID ("nota"): "fala"`        |

---

## 📐 Formatação Final Draft

O formatador gera a saída no padrão profissional da indústria cinematográfica, ajustando automaticamente recuos e larguras:

```
                    BREAKING BAD - PILOT

                       Escrito por
                     Vince Gilligan

=============================================================


INT. WHITE RESIDENCE - KITCHEN - DAY

Walter White, 50 anos, de cueca e uma camiseta bege,
sentado à mesa do café da manhã.

                      WALTER WHITE
          Preciso te contar uma coisa.

                      WALTER WHITE
                (hesitante)
          Algo importante.
```

---

## 🏗️ Arquitetura

O projeto utiliza **ANTLR4** para gerar o Lexer e Parser a partir da gramática definida em `CineScript.g4`, e então processa a árvore sintática (AST) para formatar a saída.

```
src/
├── antlr4/
│   └── CineScript.g4              ← Gramática ANTLR4
└── main/java/cinescript/
    ├── Main.java                   ← CLI (ponto de entrada)
    ├── ast/                        ← Nós da AST (Cenas, Ações, Diálogos)
    ├── visitor/
    │   └── CineScriptASTBuilder.java  ← Parse tree → AST customizada
    ├── formatter/
    │   └── FinalDraftFormatter.java   ← Motor de formatação (margins/word-wrap)
    └── error/
        └── CineScriptErrorListener.java ← Tratamento de erros
```

---

## 📄 Licença

Este projeto é para fins acadêmicos.
