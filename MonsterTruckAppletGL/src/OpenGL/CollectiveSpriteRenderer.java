package OpenGL;

import Auxiliary.Vector2D.Rect;
import TextureMap.TextureMap.Texture;
import com.jogamp.opengl.GL2;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CollectiveSpriteRenderer {

    public static class CSpriteRenderer extends SpriteRenderer{

        public CSpriteRenderer(Texture texture, Rect outputRect, Rect textureRect){
            super(texture, outputRect, textureRect);
            CollectiveSpriteRenderer.getInstance().register(this);
        }
    }

    List<IRenderer> regList = new LinkedList<>();
    List<IRenderer> queue = new LinkedList<>();
    AtomicReference<List<IRenderer>> currentSprites = new AtomicReference<>();

    private static CollectiveSpriteRenderer _instance = null;

    public static CollectiveSpriteRenderer getInstance(){
        if (_instance==null){
            _instance = new CollectiveSpriteRenderer();
        }
        return _instance;
    }

    private CollectiveSpriteRenderer() {
    }

    public void queueRenderer(IRenderer sprite) {
        queue.add(sprite);
    }

    public void register(IRenderer sprite) {
        regList.add(sprite);
    }

    public void init(GL2 gl2, GLRenderer.ShaderAndTexture shaderAndTexture) {
        for (IRenderer sprite : regList) {
            sprite.init(gl2, shaderAndTexture);
        }
    }

    public void Render(GL2 gl2) {
        List<IRenderer> sprites = currentSprites.get();
        for (IRenderer sprite : sprites) {
            sprite.Render(gl2);
        }
    }

    public void setAndSeal() {
        currentSprites.set(queue);
        queue = new LinkedList<>();
    }
}