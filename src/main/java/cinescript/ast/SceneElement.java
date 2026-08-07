package cinescript.ast;

/**
 * Interface marcadora para todos os elementos que podem
 * aparecer dentro de uma cena (ação, diálogo, câmera, etc).
 */
public interface SceneElement {

    /**
     * Retorna a linha no arquivo fonte onde este elemento aparece.
     */
    int getLine();

    /**
     * Retorna a coluna no arquivo fonte onde este elemento aparece.
     */
    int getColumn();
}
