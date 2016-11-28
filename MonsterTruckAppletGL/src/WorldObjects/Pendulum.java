package WorldObjects;

import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Rect;
import OpenGL.CollectiveSpriteRenderer;
import OpenGL.GLRenderer;
import OpenGL.SpriteRenderer;
import TextureMap.TextureMap.Texture;

public class Pendulum extends PendulumBase<GLRenderer> {

    private final SpriteRenderer cludgel;
    private final SpriteRenderer beam;

    public Pendulum(GLRenderer renderer, String name, Vector2D center, double radius) {
	super(center, radius);
	cludgel = new CollectiveSpriteRenderer.CSpriteRenderer(
		new Texture(0, 1440, 2048),
		new Rect(-(float) 20f, -(float) 20f, (float) 20f, (float) 20f),
		new Rect(320f, 180f, 500f, 360f));
	beam = new CollectiveSpriteRenderer.CSpriteRenderer(
		new Texture(0, 1440, 2048),
		new Rect(-(float) 2f, -(float) 2f, (float) radius, (float) 2),
		new Rect(500f, 180f, 600f, 280f));
    }

    @Override
    public void draw(GLRenderer renderer, double[] state, double xOffset) {
	double positionR = state[offset];
	double sin = Math.sin(positionR);
	double cos = Math.cos(positionR);
	Vector2D location = center.add(cos * radius, sin * radius);
	beam.setPositionAndOrientation( center.X - xOffset, center.Y,
		Math.toDegrees(-positionR));
	cludgel.setPositionAndOrientation(location.X - xOffset,
		location.Y, 0);
	renderer.getSpriteRenderer().queueRenderer(cludgel);
	renderer.getSpriteRenderer().queueRenderer(beam);
    }


}
