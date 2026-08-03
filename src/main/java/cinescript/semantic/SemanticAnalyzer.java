package cinescript.semantic;

import java.util.ArrayList;
import java.util.List;

import cinescript.error.SemanticError;
import cinescript.parser.ast.*;

/**
 * Analisador Semântico.
 * Responsável por:
 * - Verificar variáveis não declaradas
 * - Verificar variáveis duplicadas no mesmo escopo
 * - Checagem de tipos (type checking)
 * - Gerenciar escopos via Tabela de Símbolos
 */
public class SemanticAnalyzer implements ASTVisitor<String> {

    private SymbolTable currentScope;
    private final List<SemanticError> errors;

    public SemanticAnalyzer() {
        this.currentScope = new SymbolTable(); // escopo global
        this.errors = new ArrayList<>();
    }

    /**
     * Realiza a análise semântica na AST.
     * @param program O nó raiz do programa.
     */
    public void analyze(ProgramNode program) {
        program.accept(this);
    }

    // ==================== Visitor Methods ====================

    @Override
    public String visitProgram(ProgramNode node) {
        for (ASTNode stmt : node.getStatements()) {
            stmt.accept(this);
        }
        return null;
    }

    @Override
    public String visitBlock(BlockNode node) {
        // Cria novo escopo para o bloco
        SymbolTable previousScope = currentScope;
        currentScope = currentScope.createChildScope();

        for (ASTNode stmt : node.getStatements()) {
            stmt.accept(this);
        }

        // Restaura o escopo anterior
        currentScope = previousScope;
        return null;
    }

    @Override
    public String visitVarDeclaration(VarDeclarationNode node) {
        String type = node.getTypeName();
        String name = node.getVariableName();

        // Verifica se já existe no escopo atual
        if (currentScope.lookupLocal(name) != null) {
            errors.add(new SemanticError(
                    "Variável '" + name + "' já declarada neste escopo.", node.getLine()));
            return type;
        }

        // Verifica tipo do inicializador
        if (node.getInitializer() != null) {
            String initType = node.getInitializer().accept(this);
            if (initType != null && !isCompatibleType(type, initType)) {
                errors.add(new SemanticError(
                        "Tipo incompatível: não é possível atribuir '" + initType + "' a variável do tipo '" + type + "'.",
                        node.getLine()));
            }
        }

        // Registra na tabela de símbolos
        currentScope.declare(name, new Symbol(name, type, null, node.getLine()));
        return type;
    }

    @Override
    public String visitAssignment(AssignmentNode node) {
        String name = node.getVariableName();
        Symbol symbol = currentScope.lookup(name);

        if (symbol == null) {
            errors.add(new SemanticError(
                    "Variável '" + name + "' não foi declarada.", node.getLine()));
            return null;
        }

        String valueType = node.getValue().accept(this);
        if (valueType != null && !isCompatibleType(symbol.getType(), valueType)) {
            errors.add(new SemanticError(
                    "Tipo incompatível: não é possível atribuir '" + valueType + "' a variável '" + name + "' do tipo '" + symbol.getType() + "'.",
                    node.getLine()));
        }

        return symbol.getType();
    }

    @Override
    public String visitIf(IfNode node) {
        node.getCondition().accept(this);
        node.getThenBranch().accept(this);
        if (node.getElseBranch() != null) {
            node.getElseBranch().accept(this);
        }
        return null;
    }

    @Override
    public String visitWhile(WhileNode node) {
        node.getCondition().accept(this);
        node.getBody().accept(this);
        return null;
    }

    @Override
    public String visitBinaryExpr(BinaryExprNode node) {
        String leftType = node.getLeft().accept(this);
        String rightType = node.getRight().accept(this);

        if (leftType == null || rightType == null) return null;

        String operator = node.getOperator();

        // Verificação de tipos para operações aritméticas
        if (isArithmeticOp(operator)) {
            if (leftType.equals("string") || rightType.equals("string")) {
                // Concatenação de strings com +
                if (operator.equals("+")) {
                    return "string";
                }
                errors.add(new SemanticError(
                        "Operação '" + operator + "' não suportada entre '" + leftType + "' e '" + rightType + "'.",
                        node.getLine()));
                return null;
            }
            // int op float -> float
            if (leftType.equals("float") || rightType.equals("float")) {
                return "float";
            }
            return "int";
        }

        // Operações de comparação retornam boolean
        if (isComparisonOp(operator)) {
            return "boolean";
        }

        // Operações lógicas
        if (isLogicalOp(operator)) {
            return "boolean";
        }

        return null;
    }

    @Override
    public String visitUnaryExpr(UnaryExprNode node) {
        String operandType = node.getOperand().accept(this);

        if (node.getOperator().equals("-")) {
            if (operandType != null && operandType.equals("string")) {
                errors.add(new SemanticError(
                        "Operador '-' não pode ser aplicado ao tipo 'string'.", node.getLine()));
            }
            return operandType;
        }

        if (node.getOperator().equals("!")) {
            return "boolean";
        }

        return operandType;
    }

    @Override
    public String visitLiteral(LiteralNode node) {
        return node.getType();
    }

    @Override
    public String visitIdentifier(IdentifierNode node) {
        Symbol symbol = currentScope.lookup(node.getName());
        if (symbol == null) {
            errors.add(new SemanticError(
                    "Variável '" + node.getName() + "' não foi declarada.", node.getLine()));
            return null;
        }
        return symbol.getType();
    }

    @Override
    public String visitPrint(PrintNode node) {
        node.getExpression().accept(this);
        return null;
    }

    @Override
    public String visitDomainCommand(DomainCommandNode node) {
        // TODO: Implementar verificação semântica dos comandos de domínio
        // Verificar tipos dos argumentos conforme o comando
        for (ASTNode arg : node.getArguments()) {
            arg.accept(this);
        }
        return null;
    }

    // ==================== Métodos Auxiliares ====================

    private boolean isCompatibleType(String expected, String actual) {
        if (expected.equals(actual)) return true;
        // int pode receber float e vice-versa (promoção numérica)
        if ((expected.equals("int") || expected.equals("float")) &&
            (actual.equals("int") || actual.equals("float"))) {
            return true;
        }
        return false;
    }

    private boolean isArithmeticOp(String op) {
        return op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/") || op.equals("%");
    }

    private boolean isComparisonOp(String op) {
        return op.equals("==") || op.equals("!=") || op.equals("<") || op.equals("<=") ||
               op.equals(">") || op.equals(">=");
    }

    private boolean isLogicalOp(String op) {
        return op.equals("&&") || op.equals("||");
    }

    // ==================== Getters ====================

    public List<SemanticError> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
