package cinescript.ast;

import java.util.List;

/**
 * Nó raiz da AST — representa o roteiro completo.
 * Contém metadados, declarações de personagens e cenas.
 */
public class ScriptNode {

    private final List<MetadataNode> metadata;
    private final List<CharacterDeclarationNode> characters;
    private final List<SceneNode> scenes;

    public ScriptNode(List<MetadataNode> metadata,
                      List<CharacterDeclarationNode> characters,
                      List<SceneNode> scenes) {
        this.metadata = metadata;
        this.characters = characters;
        this.scenes = scenes;
    }

    public List<MetadataNode> getMetadata() {
        return metadata;
    }

    public List<CharacterDeclarationNode> getCharacters() {
        return characters;
    }

    public List<SceneNode> getScenes() {
        return scenes;
    }

    /**
     * Busca o valor de um metadado pelo nome da chave.
     * Retorna null se não encontrado.
     */
    public String getMetadataValue(String key) {
        return metadata.stream()
                .filter(m -> m.getKey().equalsIgnoreCase(key))
                .map(MetadataNode::getValue)
                .findFirst()
                .orElse(null);
    }
}
