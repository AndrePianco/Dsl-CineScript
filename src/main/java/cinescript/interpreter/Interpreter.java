package cinescript.interpreter;

import cinescript.error.RuntimeError;
import cinescript.parser.ast.*;

/**
 * Interpretador que executa a AST diretamente.
 * Percorre a árvore usando o padrão Visitor e executa cada nó.
 */
public class Interpreter implements ASTVisitor<Object> {

    private Environment currentEnvironment;

    public Interpreter() {
        this.currentEnvironment = new Environment(); // ambiente global
    }

    /**
     * Executa o programa a partir do nó raiz.
     * @param program Nó raiz da AST.
     */
    public void execute(ProgramNode program) {
        program.accept(this);
    }

    // ==================== Visitor Methods ====================

    @Override
    public Object visitProgram(ProgramNode node) {
        for (ASTNode stmt : node.getStatements()) {
            stmt.accept(this);
        }
        return null;
    }

    @Override
    public Object visitBlock(BlockNode node) {
        Environment previousEnv = currentEnvironment;
        currentEnvironment = currentEnvironment.createChild();

        for (ASTNode stmt : node.getStatements()) {
            stmt.accept(this);
        }

        currentEnvironment = previousEnv;
        return null;
    }

    @Override
    public Object visitVarDeclaration(VarDeclarationNode node) {
        Object value = null;
        if (node.getInitializer() != null) {
            value = node.getInitializer().accept(this);
        } else {
            // Valores default por tipo
            switch (node.getTypeName()) {
                case "int": value = 0; break;
                case "float": value = 0.0; break;
                case "string": value = ""; break;
            }
        }
        currentEnvironment.define(node.getVariableName(), value);
        return null;
    }

    @Override
    public Object visitAssignment(AssignmentNode node) {
        Object value = node.getValue().accept(this);
        currentEnvironment.assign(node.getVariableName(), value);
        return value;
    }

    @Override
    public Object visitIf(IfNode node) {
        Object condition = node.getCondition().accept(this);
        if (isTruthy(condition)) {
            node.getThenBranch().accept(this);
        } else if (node.getElseBranch() != null) {
            node.getElseBranch().accept(this);
        }
        return null;
    }

    @Override
    public Object visitWhile(WhileNode node) {
        while (isTruthy(node.getCondition().accept(this))) {
            node.getBody().accept(this);
        }
        return null;
    }

    @Override
    public Object visitBinaryExpr(BinaryExprNode node) {
        Object left = node.getLeft().accept(this);
        Object right = node.getRight().accept(this);
        String op = node.getOperator();

        switch (op) {
            // Aritméticos
            case "+":
                if (left instanceof String || right instanceof String) {
                    return String.valueOf(left) + String.valueOf(right);
                }
                if (left instanceof Double || right instanceof Double) {
                    return toDouble(left) + toDouble(right);
                }
                return toInt(left) + toInt(right);
            case "-":
                if (left instanceof Double || right instanceof Double) {
                    return toDouble(left) - toDouble(right);
                }
                return toInt(left) - toInt(right);
            case "*":
                if (left instanceof Double || right instanceof Double) {
                    return toDouble(left) * toDouble(right);
                }
                return toInt(left) * toInt(right);
            case "/":
                if (left instanceof Double || right instanceof Double) {
                    double divisor = toDouble(right);
                    if (divisor == 0) throw new RuntimeError("Divisão por zero.", node.getLine());
                    return toDouble(left) / divisor;
                }
                int intDivisor = toInt(right);
                if (intDivisor == 0) throw new RuntimeError("Divisão por zero.", node.getLine());
                return toInt(left) / intDivisor;
            case "%":
                return toInt(left) % toInt(right);

            // Comparação
            case "==": return isEqual(left, right);
            case "!=": return !isEqual(left, right);
            case "<":  return toDouble(left) < toDouble(right);
            case "<=": return toDouble(left) <= toDouble(right);
            case ">":  return toDouble(left) > toDouble(right);
            case ">=": return toDouble(left) >= toDouble(right);

            // Lógicos
            case "&&": return isTruthy(left) && isTruthy(right);
            case "||": return isTruthy(left) || isTruthy(right);

            default:
                throw new RuntimeError("Operador desconhecido: '" + op + "'.", node.getLine());
        }
    }

    @Override
    public Object visitUnaryExpr(UnaryExprNode node) {
        Object operand = node.getOperand().accept(this);

        switch (node.getOperator()) {
            case "-":
                if (operand instanceof Double) return -(double) operand;
                return -(int) operand;
            case "!":
                return !isTruthy(operand);
            default:
                throw new RuntimeError("Operador unário desconhecido: '" + node.getOperator() + "'.", node.getLine());
        }
    }

    @Override
    public Object visitLiteral(LiteralNode node) {
        return node.getValue();
    }

    @Override
    public Object visitIdentifier(IdentifierNode node) {
        return currentEnvironment.get(node.getName());
    }

    @Override
    public Object visitPrint(PrintNode node) {
        Object value = node.getExpression().accept(this);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Object visitDomainCommand(DomainCommandNode node) {
        // TODO: Implementar a execução dos comandos de domínio conforme o tema
        System.out.println("[DOMAIN CMD] " + node.getCommandName() + " executado.");
        return null;
    }

    // ==================== Métodos Auxiliares ====================

    private boolean isTruthy(Object value) {
        if (value == null) return false;
        if (value instanceof Boolean) return (boolean) value;
        if (value instanceof Integer) return (int) value != 0;
        if (value instanceof Double) return (double) value != 0.0;
        return true;
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null) return false;
        return a.equals(b);
    }

    private double toDouble(Object value) {
        if (value instanceof Integer) return (double) (int) value;
        if (value instanceof Double) return (double) value;
        throw new RuntimeException("Não é possível converter para double: " + value);
    }

    private int toInt(Object value) {
        if (value instanceof Integer) return (int) value;
        if (value instanceof Double) return (int) (double) value;
        throw new RuntimeException("Não é possível converter para int: " + value);
    }

    private String stringify(Object value) {
        if (value == null) return "null";
        if (value instanceof Double) {
            String text = value.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }
        return value.toString();
    }
}
