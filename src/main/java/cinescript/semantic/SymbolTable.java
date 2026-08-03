package cinescript.semantic;

import java.util.HashMap;
import java.util.Map;

/**
 * Tabela de Símbolos com suporte a escopos aninhados.
 * Cada escopo possui uma referência ao escopo pai, permitindo
 * busca hierárquica de variáveis.
 */
public class SymbolTable {

    private final Map<String, Symbol> symbols;
    private final SymbolTable parent;   // escopo pai (null se for o escopo global)

    /**
     * Cria a tabela de símbolos do escopo global.
     */
    public SymbolTable() {
        this(null);
    }

    /**
     * Cria uma tabela de símbolos com referência ao escopo pai.
     * @param parent Tabela de símbolos do escopo externo.
     */
    public SymbolTable(SymbolTable parent) {
        this.symbols = new HashMap<>();
        this.parent = parent;
    }

    /**
     * Declara um novo símbolo no escopo atual.
     * @param name Nome da variável.
     * @param symbol O símbolo a ser registrado.
     * @return true se declarado com sucesso, false se já existe no escopo atual.
     */
    public boolean declare(String name, Symbol symbol) {
        if (symbols.containsKey(name)) {
            return false; // variável já declarada neste escopo
        }
        symbols.put(name, symbol);
        return true;
    }

    /**
     * Busca um símbolo no escopo atual e nos escopos pais.
     * @param name Nome da variável.
     * @return O símbolo encontrado, ou null se não existir.
     */
    public Symbol lookup(String name) {
        Symbol symbol = symbols.get(name);
        if (symbol != null) {
            return symbol;
        }
        if (parent != null) {
            return parent.lookup(name);
        }
        return null;
    }

    /**
     * Busca um símbolo apenas no escopo atual (sem subir para o pai).
     * @param name Nome da variável.
     * @return O símbolo encontrado, ou null.
     */
    public Symbol lookupLocal(String name) {
        return symbols.get(name);
    }

    /**
     * Atualiza o valor de um símbolo existente.
     * @param name Nome da variável.
     * @param value Novo valor.
     * @return true se atualizado com sucesso, false se não encontrado.
     */
    public boolean update(String name, Object value) {
        Symbol symbol = symbols.get(name);
        if (symbol != null) {
            symbol.setValue(value);
            return true;
        }
        if (parent != null) {
            return parent.update(name, value);
        }
        return false;
    }

    /**
     * Cria um novo escopo filho a partir deste.
     * @return Nova SymbolTable com este como pai.
     */
    public SymbolTable createChildScope() {
        return new SymbolTable(this);
    }

    public SymbolTable getParent() {
        return parent;
    }

    public Map<String, Symbol> getSymbols() {
        return symbols;
    }
}
