package WorldObjects;

import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Rect;
import OpenGL.CollectiveSpriteRenderer;
import OpenGL.GLRenderer;
import OpenGL.SpriteRenderer;
import TextureMap.TextureMap;

public class Foreground extends ForegroundBase<GLRenderer> {

    private final SpriteRenderer sprite;

    public Foreground(GLRenderer renderer, String name, Vector2D pointA,
                      Vector2D pointB) {
        super(pointA, pointB);
        TextureMap textureMap = renderer.getTextureMap();
        Rect textureRect = textureMap.textureByName.get(name);
        sprite = new CollectiveSpriteRenderer.CSpriteRenderer(
                textureMap.texture,
            new Rect((float) pointA.X, -(float) pointA.Y, (float) pointB.X, -(float) pointB.Y),
                textureRect);

    }

    @Override
    public void draw(GLRenderer renderer, double xOffset) {
        sprite.setPositionAndOrientation(-xOffset, 0, 0);
        renderer.getSpriteRenderer().queueRenderer(sprite);
    }

}
