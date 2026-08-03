package cinescript;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import cinescript.error.LexerError;
import cinescript.error.ParserError;
import cinescript.error.SemanticError;
import cinescript.interpreter.Interpreter;
import cinescript.lexer.Lexer;
import cinescript.lexer.Token;
import cinescript.parser.Parser;
import cinescript.parser.ast.ProgramNode;
import cinescript.semantic.SemanticAnalyzer;

/**
 * Ponto de entrada do compilador/interpretador CineScript.
 * 
 * Uso: java cinescript.Main <arquivo.cine>
 * 
 * Pipeline de execução:
 * 1. Leitura do arquivo fonte
 * 2. Análise Léxica (tokenização)
 * 3. Análise Sintática (geração da AST)
 * 4. Análise Semântica (verificação de tipos e escopos)
 * 5. Interpretação (execução da AST)
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java cinescript.Main <arquivo.cine>");
            System.out.println("  <arquivo.cine>  Caminho para o arquivo fonte CineScript");
            System.exit(1);
        }

        String filePath = args[0];
        String sourceCode;

        // === Fase 0: Leitura do arquivo ===
        try {
            sourceCode = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + filePath);
            System.err.println(e.getMessage());
            System.exit(1);
            return;
        }

        System.out.println("=== CineScript Compiler ===");
        System.out.println("Arquivo: " + filePath);
        System.out.println();

        // === Fase 1: Análise Léxica ===
        System.out.println(">> Fase 1: Análise Léxica...");
        Lexer lexer = new Lexer(sourceCode);
        List<Token> tokens = lexer.tokenize();

        if (lexer.hasErrors()) {
            System.err.println("Erros léxicos encontrados:");
            for (LexerError error : lexer.getErrors()) {
                System.err.println("  " + error);
            }
            System.exit(2);
            return;
        }
        System.out.println("   " + tokens.size() + " tokens identificados. OK!");
        System.out.println();

        // === Fase 2: Análise Sintática ===
        System.out.println(">> Fase 2: Análise Sintática...");
        Parser parser = new Parser(tokens);
        ProgramNode ast = parser.parse();

        if (parser.hasErrors()) {
            System.err.println("Erros sintáticos encontrados:");
            for (ParserError error : parser.getErrors()) {
                System.err.println("  " + error);
            }
            System.exit(3);
            return;
        }
        System.out.println("   AST gerada com sucesso. OK!");
        System.out.println();

        // === Fase 3: Análise Semântica ===
        System.out.println(">> Fase 3: Análise Semântica...");
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
        semanticAnalyzer.analyze(ast);

        if (semanticAnalyzer.hasErrors()) {
            System.err.println("Erros semânticos encontrados:");
            for (SemanticError error : semanticAnalyzer.getErrors()) {
                System.err.println("  " + error);
            }
            System.exit(4);
            return;
        }
        System.out.println("   Verificação semântica concluída. OK!");
        System.out.println();

        // === Fase 4: Interpretação ===
        System.out.println(">> Fase 4: Executando...");
        System.out.println("--- Saída do Programa ---");
        Interpreter interpreter = new Interpreter();
        try {
            interpreter.execute(ast);
        } catch (cinescript.error.RuntimeError e) {
            System.err.println("Erro de execução:");
            System.err.println("  " + e);
            System.exit(5);
        }
        System.out.println("--- Fim da Execução ---");
    }
}
