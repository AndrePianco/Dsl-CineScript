# CineScript — Documentação da Gramática

## Visão Geral

CineScript é uma DSL (Domain-Specific Language) para roteiros de cinema e série.
A gramática é definida em ANTLR4 e suporta todos os elementos padrão de um roteiro
profissional.

---

## Estrutura de um Arquivo `.cine`

Um arquivo CineScript segue esta estrutura:

```
[metadados]       → title, author, date, draft
[personagens]     → declarações com character ... as ...
[cenas]           → scene "Local" tipo horário { ... }
```

---

## 1. Metadados

Informações da página de título do roteiro.

```cinescript
title: "Nome do Filme"
author: "Nome do Autor"
date: "2024-01-01"
draft: "First Draft"
```

| Campo    | Obrigatório | Descrição                    |
|----------|-------------|------------------------------|
| `title`  | Não         | Título do roteiro            |
| `author` | Não         | Nome do roteirista           |
| `date`   | Não         | Data do roteiro              |
| `draft`  | Não         | Versão do rascunho           |

---

## 2. Declaração de Personagens

Mapeia identificadores curtos para nomes completos usados na formatação.

```cinescript
character Walter as "Walter White"
character Jesse as "Jesse Pinkman"
```

- O **identificador** (ex: `Walter`) é usado nos diálogos e direções de câmera.
- O **nome completo** (ex: `"Walter White"`) aparece na formatação final.

---

## 3. Cenas

Cada cena contém um cabeçalho e um corpo entre chaves.

```cinescript
scene "Local da Cena" tipo_locação horário {
    // elementos da cena
}
```

### Tipos de Locação

| Palavra-chave | Formatação    | Descrição                    |
|---------------|---------------|------------------------------|
| `interior`    | `INT.`        | Cena em ambiente interno     |
| `exterior`    | `EXT.`        | Cena em ambiente externo     |
| `int-ext`     | `INT./EXT.`   | Transição interno/externo    |

### Horários do Dia

| Palavra-chave | Descrição       |
|---------------|-----------------|
| `day`         | Dia             |
| `night`       | Noite           |
| `dawn`        | Amanhecer       |
| `dusk`        | Anoitecer       |
| `continuous`  | Contínuo        |

---

## 4. Elementos de Cena

### 4.1 Ação

Descrição narrativa do que acontece na cena.

```cinescript
action: "Walter se levanta da cadeira lentamente."
```

### 4.2 Diálogo

Fala de um personagem, com parentético opcional.

```cinescript
// Diálogo simples
Walter: "Eu preciso te contar uma coisa."

// Diálogo com parentético (direção de atuação)
Walter ("hesitante"): "Algo importante."
```

### 4.3 Direção de Câmera

Instrução de câmera, com alvo opcional.

```cinescript
// Sem alvo
camera: wide-shot

// Com alvo (personagem)
camera: close-up on Walter
```

#### Tipos de Câmera Disponíveis

| Tipo            | Descrição                         |
|-----------------|-----------------------------------|
| `close-up`      | Plano fechado                     |
| `wide-shot`     | Plano geral/aberto                |
| `medium-shot`   | Plano médio                       |
| `pan-left`      | Panorâmica para esquerda          |
| `pan-right`     | Panorâmica para direita           |
| `zoom-in`       | Zoom de aproximação               |
| `zoom-out`      | Zoom de afastamento               |
| `tracking`      | Câmera em movimento (tracking)    |
| `over-shoulder` | Plano por cima do ombro           |
| `pov`           | Ponto de vista (subjetiva)        |
| `aerial`        | Tomada aérea                      |
| `crane`         | Movimento de grua                 |
| `dolly`         | Movimento de dolly                |
| `steadicam`     | Steadicam                         |
| `handheld`      | Câmera na mão                     |

### 4.4 Transição

Transição entre cenas.

```cinescript
transition: cut-to
```

#### Tipos de Transição Disponíveis

| Tipo             | Formatação       |
|------------------|------------------|
| `cut-to`         | `CUT TO:`        |
| `fade-in`        | `FADE IN:`       |
| `fade-out`       | `FADE OUT:`      |
| `dissolve-to`    | `DISSOLVE TO:`   |
| `smash-cut`      | `SMASH CUT:`     |
| `match-cut`      | `MATCH CUT:`     |
| `jump-cut`       | `JUMP CUT:`      |
| `fade-to-black`  | `FADE TO BLACK:` |
| `iris-out`       | `IRIS OUT:`      |
| `wipe-to`        | `WIPE TO:`       |

### 4.5 Nota

Comentário do roteirista (não aparece na formatação final).

```cinescript
note: "Esta cena é fundamental para o arco do personagem"
```

---

## 5. Comentários

```cinescript
// Comentário de linha

/* Comentário
   de bloco */
```

---

## 6. Palavras Reservadas

As seguintes palavras são reservadas e não podem ser usadas como identificadores
de personagens:

`title`, `author`, `date`, `draft`, `character`, `as`, `scene`, `action`,
`camera`, `transition`, `note`, `on`, `interior`, `exterior`, `int-ext`,
`day`, `night`, `dawn`, `dusk`, `continuous`, e todos os tipos de câmera
e transição listados acima.

---

## 7. Regras da Gramática ANTLR4

```
script      → metadata* declaration* scene+ EOF
metadata    → (TITLE | AUTHOR | DATE | DRAFT) ':' STRING
declaration → CHARACTER IDENTIFIER AS STRING
scene       → SCENE STRING locationType timeOfDay '{' sceneElement* '}'
sceneElement → actionLine | dialogue | cameraDirection | transitionLine | noteLine
actionLine   → ACTION ':' STRING
dialogue     → IDENTIFIER ('(' STRING ')')? ':' STRING
cameraDirection → CAMERA ':' cameraType (ON IDENTIFIER)?
transitionLine  → TRANSITION ':' transitionType
noteLine     → NOTE ':' STRING
```
