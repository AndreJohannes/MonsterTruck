package WorldObjects;

import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Rect;
import OpenGL.CollectiveSpriteRenderer;
import OpenGL.GLRenderer;
import OpenGL.SpriteRenderer;
import TextureMap.TextureMap.Texture;

public class Cylinder extends CylinderBase<GLRenderer> {

    private final SpriteRenderer sprite;

    public Cylinder(GLRenderer renderer, String name, Vector2D center, double radius) {
        super(center, radius);
        sprite = new CollectiveSpriteRenderer.CSpriteRenderer(
                new Texture(0, 1440, 2048), new Rect(-(float) radius,
                -(float) radius, (float) radius, (float) radius),
                new Rect(320f, 180f, 500f, 360f));
    }

    @Override
    public void draw(GLRenderer renderer, double[] state, double xOffset) {
        sprite.setPositionAndOrientation( state[offset] - xOffset,
                state[offset + 1], -state[offset + 4]);

        renderer.getSpriteRenderer().queueRenderer(sprite);
    }
}
