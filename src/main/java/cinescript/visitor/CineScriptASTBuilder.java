package cinescript.visitor;

import cinescript.ast.*;
import cinescript.generated.CineScriptBaseVisitor;
import cinescript.generated.CineScriptParser;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Visitor ANTLR4 que constroi a AST customizada do CineScript
 * a partir do parse tree gerado pelo ANTLR.
 */
public class CineScriptASTBuilder extends CineScriptBaseVisitor<Object> {

    // =========================================================
    //                   SCRIPT (raiz)
    // =========================================================

    @Override
    public ScriptNode visitScript(CineScriptParser.ScriptContext ctx) {
        List<MetadataNode> metadata = ctx.metadata().stream()
                .map(m -> (MetadataNode) visit(m))
                .collect(Collectors.toList());

        List<CharacterDeclarationNode> characters = ctx.declaration().stream()
                .map(d -> (CharacterDeclarationNode) visit(d))
                .collect(Collectors.toList());

        List<SceneNode> scenes = ctx.scene().stream()
                .map(s -> (SceneNode) visit(s))
                .collect(Collectors.toList());

        return new ScriptNode(metadata, characters, scenes);
    }

    // =========================================================
    //                    METADADOS
    // =========================================================

    @Override
    public MetadataNode visitTitleMeta(CineScriptParser.TitleMetaContext ctx) {
        return new MetadataNode("title", stripQuotes(ctx.STRING().getText()),
                ctx.start.getLine(), ctx.start.getCharPositionInLine());
    }

    @Override
    public MetadataNode visitAuthorMeta(CineScriptParser.AuthorMetaContext ctx) {
        return new MetadataNode("author", stripQuotes(ctx.STRING().getText()),
                ctx.start.getLine(), ctx.start.getCharPositionInLine());
    }

    @Override
    public MetadataNode visitDateMeta(CineScriptParser.DateMetaContext ctx) {
        return new MetadataNode("date", stripQuotes(ctx.STRING().getText()),
                ctx.start.getLine(), ctx.start.getCharPositionInLine());
    }

    @Override
    public MetadataNode visitDraftMeta(CineScriptParser.DraftMetaContext ctx) {
        return new MetadataNode("draft", stripQuotes(ctx.STRING().getText()),
                ctx.start.getLine(), ctx.start.getCharPositionInLine());
    }

    // =========================================================
    //                   PERSONAGENS
    // =========================================================

    @Override
    public CharacterDeclarationNode visitDeclaration(CineScriptParser.DeclarationContext ctx) {
        return new CharacterDeclarationNode(
                ctx.IDENTIFIER().getText(),
                stripQuotes(ctx.STRING().getText()),
                ctx.start.getLine(),
                ctx.start.getCharPositionInLine()
        );
    }

    // =========================================================
    //                      CENAS
    // =========================================================

    @Override
    public SceneNode visitScene(CineScriptParser.SceneContext ctx) {
        String location = stripQuotes(ctx.STRING().getText());
        String locationType = resolveLocationType(ctx.locationType());
        String timeOfDay = resolveTimeOfDay(ctx.timeOfDay());

        List<SceneElement> elements = ctx.sceneElement().stream()
                .map(e -> (SceneElement) visit(e))
                .collect(Collectors.toList());

        return new SceneNode(location, locationType, timeOfDay, elements,
                ctx.start.getLine(), ctx.start.getCharPositionInLine());
    }

    // =========================================================
    //               ELEMENTOS DE CENA
    // =========================================================

    @Override
    public SceneElement visitSceneElement(CineScriptParser.SceneElementContext ctx) {
        return (SceneElement) super.visitSceneElement(ctx);
    }

    @Override
    public ActionNode visitActionLine(CineScriptParser.ActionLineContext ctx) {
        return new ActionNode(
                stripQuotes(ctx.STRING().getText()),
                ctx.start.getLine(),
                ctx.start.getCharPositionInLine()
        );
    }

    @Override
    public DialogueNode visitDialogueWithParenthetical(CineScriptParser.DialogueWithParentheticalContext ctx) {
        return new DialogueNode(
                ctx.IDENTIFIER().getText(),
                stripQuotes(ctx.STRING(0).getText()),
                stripQuotes(ctx.STRING(1).getText()),
                ctx.start.getLine(),
                ctx.start.getCharPositionInLine()
        );
    }

    @Override
    public DialogueNode visitDialoguePlain(CineScriptParser.DialoguePlainContext ctx) {
        return new DialogueNode(
                ctx.IDENTIFIER().getText(),
                null,
                stripQuotes(ctx.STRING().getText()),
                ctx.start.getLine(),
                ctx.start.getCharPositionInLine()
        );
    }

    // =========================================================
    //                METODOS AUXILIARES
    // =========================================================

    private String stripQuotes(String text) {
        if (text != null && text.length() >= 2
                && text.startsWith("\"") && text.endsWith("\"")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }

    private String resolveLocationType(CineScriptParser.LocationTypeContext ctx) {
        if (ctx.INTERIOR() != null)  return "INT.";
        if (ctx.EXTERIOR() != null)  return "EXT.";
        if (ctx.INT_EXT() != null)   return "INT./EXT.";
        return "INT.";
    }

    private String resolveTimeOfDay(CineScriptParser.TimeOfDayContext ctx) {
        if (ctx.DAY() != null)        return "DAY";
        if (ctx.NIGHT() != null)      return "NIGHT";
        if (ctx.DAWN() != null)       return "DAWN";
        if (ctx.DUSK() != null)       return "DUSK";
        if (ctx.CONTINUOUS() != null) return "CONTINUOUS";
        return "DAY";
    }
}
