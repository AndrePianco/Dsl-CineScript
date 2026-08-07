package cinescript;

import cinescript.ast.ScriptNode;
import cinescript.error.CineScriptError;
import cinescript.error.CineScriptErrorListener;
import cinescript.formatter.FinalDraftFormatter;
import cinescript.visitor.CineScriptASTBuilder;
import cinescript.generated.CineScriptLexer;
import cinescript.generated.CineScriptParser;

import org.antlr.v4.runtime.*;

import java.io.*;
import java.nio.file.*;

/**
 * Ponto de entrada do compilador CineScript.
 *
 * Uso:
 *   cinescript <arquivo.cine> [opcoes]
 *
 * Opcoes:
 *   --output, -o    Salva resultado em arquivo
 *   --version, -v   Mostra versao
 *   --help,   -h    Mostra ajuda
 */
public class Main {

    private static final String VERSION = "1.0.0";

    public static void main(String[] args) {
        if (args.length == 0 || args[0].equals("--help") || args[0].equals("-h")) {
            printUsage();
            return;
        }

        String inputFile = null;
        String outputFile = null;

        // Parsear argumentos
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--output", "-o" -> {
                    if (i + 1 < args.length) {
                        outputFile = args[++i];
                    } else {
                        System.err.println("Erro: --output requer um arquivo de saida.");
                        System.exit(1);
                    }
                }
                case "--version", "-v" -> {
                    System.out.println("CineScript v" + VERSION);
                    return;
                }
                default -> {
                    if (args[i].startsWith("-")) {
                        System.err.println("Erro: opcao desconhecida: " + args[i]);
                        System.exit(1);
                    }
                    inputFile = args[i];
                }
            }
        }

        if (inputFile == null) {
            System.err.println("Erro: nenhum arquivo de entrada especificado.");
            printUsage();
            System.exit(1);
            return;
        }

        try {
            // Leitura do arquivo
            Path path = Paths.get(inputFile);
            if (!Files.exists(path)) {
                System.err.println("Erro: arquivo nao encontrado: " + inputFile);
                System.exit(1);
                return;
            }

            String input = Files.readString(path);

            // Analise Lexica e Sintatica
            CharStream charStream = CharStreams.fromString(input);
            CineScriptLexer lexer = new CineScriptLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CineScriptParser parser = new CineScriptParser(tokens);

            // Listener de erros customizado
            CineScriptErrorListener errorListener = new CineScriptErrorListener();
            lexer.removeErrorListeners();
            lexer.addErrorListener(errorListener);
            parser.removeErrorListeners();
            parser.addErrorListener(errorListener);

            // Parse
            CineScriptParser.ScriptContext tree = parser.script();

            if (errorListener.hasErrors()) {
                errorListener.printErrors();
                System.exit(1);
                return;
            }

            // Construcao da AST
            CineScriptASTBuilder builder = new CineScriptASTBuilder();
            ScriptNode script = builder.visitScript(tree);

            // Formatacao Final Draft
            FinalDraftFormatter formatter = new FinalDraftFormatter(script);
            String result = formatter.format(script);

            // Saida
            if (outputFile != null) {
                Files.writeString(Paths.get(outputFile), result);
                System.out.println("Roteiro gerado com sucesso: " + outputFile);
            } else {
                System.out.println(result);
            }

        } catch (CineScriptError e) {
            System.err.println("X " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("X Erro de I/O: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void printUsage() {
        System.out.println("+----------------------------------------------------------+");
        System.out.println("|                    CineScript v" + VERSION + "                     |");
        System.out.println("|          DSL para Roteiros de Cinema e Serie           |");
        System.out.println("+----------------------------------------------------------+");
        System.out.println("|                                                          |");
        System.out.println("|  Uso: cinescript <arquivo.cine> [opcoes]                 |");
        System.out.println("|                                                          |");
        System.out.println("|  Opcoes:                                                 |");
        System.out.println("|    --output, -o    Salva resultado em arquivo            |");
        System.out.println("|    --version, -v   Mostra versao                         |");
        System.out.println("|    --help,   -h    Mostra esta ajuda                     |");
        System.out.println("|                                                          |");
        System.out.println("|  Exemplos:                                               |");
        System.out.println("|    cinescript roteiro.cine                                |");
        System.out.println("|    cinescript roteiro.cine -o saida.txt                   |");
        System.out.println("|                                                          |");
        System.out.println("+----------------------------------------------------------+");
    }
}
