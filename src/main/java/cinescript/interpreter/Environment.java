package cinescript.interpreter;

import java.util.HashMap;
import java.util.Map;

/**
 * Ambiente de execução para o interpretador.
 * Gerencia os valores das variáveis em tempo de execução, com suporte a escopos.
 */
public class Environment {

    private final Map<String, Object> values;
    private final Environment parent;

    /**
     * Cria o ambiente global.
     */
    public Environment() {
        this(null);
    }

    /**
     * Cria um ambiente filho com referência ao pai.
     * @param parent Ambiente do escopo externo.
     */
    public Environment(Environment parent) {
        this.values = new HashMap<>();
        this.parent = parent;
    }

    /**
     * Define uma variável no ambiente atual.
     */
    public void define(String name, Object value) {
        values.put(name, value);
    }

    /**
     * Obtém o valor de uma variável, buscando nos escopos pais se necessário.
     */
    public Object get(String name) {
        if (values.containsKey(name)) {
            return values.get(name);
        }
        if (parent != null) {
            return parent.get(name);
        }
        throw new RuntimeException("Variável não definida: '" + name + "'.");
    }

    /**
     * Atualiza o valor de uma variável existente.
     */
    public void assign(String name, Object value) {
        if (values.containsKey(name)) {
            values.put(name, value);
            return;
        }
        if (parent != null) {
            parent.assign(name, value);
            return;
        }
        throw new RuntimeException("Variável não definida: '" + name + "'.");
    }

    /**
     * Cria um ambiente filho a partir deste.
     */
    public Environment createChild() {
        return new Environment(this);
    }

    public Environment getParent() {
        return parent;
    }
}
