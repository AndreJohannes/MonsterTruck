package WorldObjects;


import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Rect;
import OpenGL.CollectiveSpriteRenderer;
import OpenGL.GLRenderer;
import OpenGL.SpriteRenderer;
import TextureMap.TextureMap.Texture;

public class DoublePendulum extends DoublePendulumBase<GLRenderer> {

    private final SpriteRenderer cludgel1;
    private final SpriteRenderer cludgel2;
    private final SpriteRenderer beam1;
    private final SpriteRenderer beam2;

    public DoublePendulum(GLRenderer renderer, String name, Vector2D center) {
	super(center);
	cludgel1 = new CollectiveSpriteRenderer.CSpriteRenderer(
		new Texture(0, 1440, 2048),
		new Rect(-(float) 10f, -(float) 10f, (float) 10f, (float) 10f),
		new Rect(323f, 184f, 424f, 284f));
	cludgel2 = new CollectiveSpriteRenderer.CSpriteRenderer(
		new Texture(0, 1440, 2048),
		new Rect(-(float) 15f, -(float) 15f, (float) 15f, (float) 15f),
		new Rect(323f, 184f, 424f, 284f));
	beam1 = new CollectiveSpriteRenderer.CSpriteRenderer(
		new Texture(0, 1440, 2048),
		new Rect(-(float) 2f, -(float) L1, (float) 2, (float) 2),
		new Rect(432f, 180f, 452f, 280f));
	beam2 = new CollectiveSpriteRenderer.CSpriteRenderer(
		new Texture(0, 1440, 2048),
		new Rect(-(float) 2f, -(float) L1, (float) 2, (float) 2),
		new Rect(432f, 180f, 452f, 280f));
    }

    @Override
    public void draw(GLRenderer renderer, double[] state, double xOffset) {
	double theta1 = state[offset];
	double theta2 = state[offset + 1];
	double sinTheta1 = Math.sin(theta1);
	double cosTheta1 = Math.cos(theta1);
	double sinTheta2 = Math.sin(theta2);
	double cosTheta2 = Math.cos(theta2);
	Vector2D location1 = new Vector2D(center.X + L1 * sinTheta1,
		center.Y + L1 * cosTheta1);
	Vector2D location2 = location1
		.add(new Vector2D(L2 * sinTheta2, L2 * cosTheta2));
	beam1.setPositionAndOrientation(center.X - xOffset, center.Y,
		Math.toDegrees(theta1));
	beam2.setPositionAndOrientation(location1.X - xOffset,
		location1.Y, Math.toDegrees(theta2));
	cludgel1.setPositionAndOrientation(location1.X - xOffset,
		location1.Y, 0);
	cludgel2.setPositionAndOrientation(location2.X - xOffset,
		location2.Y, 0);
	renderer.getSpriteRenderer().queueRenderer(beam1);
	renderer.getSpriteRenderer().queueRenderer(beam2);
	renderer.getSpriteRenderer().queueRenderer(cludgel1);
	renderer.getSpriteRenderer().queueRenderer(cludgel2);

    }

}
