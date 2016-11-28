package WorldObjects;


import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Rect;
import OpenGL.CollectiveSpriteRenderer;
import OpenGL.GLRenderer;
import OpenGL.SpriteRenderer;
import TextureMap.TextureMap;

public class Block extends BlockBase<GLRenderer> {

    private final SpriteRenderer sprite;

    public Block(GLRenderer renderer, String name, Vector2D startPosition,
                 double width, double height) {
        super(startPosition, width, height);
        TextureMap textureMap = renderer.getTextureMap();
        sprite = new CollectiveSpriteRenderer.CSpriteRenderer(
                textureMap.texture,
                new Rect(-(float) width / 2f, -(float) height / 2f,
                        (float) width / 2f, (float) height / 2f),
                textureMap.textureByName.get(name));
    }

    @Override
    public void draw(GLRenderer renderer, double[] state, double xOffset) {
        sprite.setPositionAndOrientation(state[offset] - xOffset,
                state[offset + 1], Math.toDegrees(state[offset + 4]));

        renderer.getSpriteRenderer().queueRenderer(sprite);
    }

}
