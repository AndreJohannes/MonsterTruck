package com.studiocinqo.monstertruck.WorldObjects;

import com.studiocinqo.monstertruck.OpenGL.CollectiveSpriteRenderer;
import com.studiocinqo.monstertruck.OpenGL.GLRenderer;
import TextureMap.TextureMap.Texture;
import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Rect;
import WorldObjects.DoublePendulumBase;

public class DoublePendulum extends DoublePendulumBase<GLRenderer> {

    private final CollectiveSpriteRenderer.SpriteForRenderer cludgel1;
    private final CollectiveSpriteRenderer.SpriteForRenderer cludgel2;
    private final CollectiveSpriteRenderer.SpriteForRenderer beam1;
    private final CollectiveSpriteRenderer.SpriteForRenderer beam2;

    public DoublePendulum(GLRenderer renderer, String name, Vector2D center) {
	super(center);
	cludgel1 = new CollectiveSpriteRenderer.SpriteForRenderer(
		new Texture(0, 1440, 2048),
		new Rect(-(float) 10f, -(float) 10f, (float) 10f, (float) 10f),
		new Rect(320f, 180f, 500f, 360f));
	cludgel2 = new CollectiveSpriteRenderer.SpriteForRenderer(
		new Texture(0, 1440, 2048),
		new Rect(-(float) 15f, -(float) 15f, (float) 15f, (float) 15f),
		new Rect(320f, 180f, 500f, 360f));
	beam1 = new CollectiveSpriteRenderer.SpriteForRenderer(
		new Texture(0, 1440, 2048),
		new Rect(-(float) 2f, -(float) L1, (float) 2, (float) 2),
		new Rect(500f, 180f, 600f, 280f));
	beam2 = new CollectiveSpriteRenderer.SpriteForRenderer(
		new Texture(0, 1440, 2048),
		new Rect(-(float) 2f, -(float) L1, (float) 2, (float) 2),
		new Rect(500f, 180f, 600f, 280f));
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
	beam1.setPositionAndOritentation(renderer, center.X - xOffset, center.Y,
		-theta1);
	beam2.setPositionAndOritentation(renderer, location1.X - xOffset,
		location1.Y, -theta2);
	cludgel1.setPositionAndOritentation(renderer, location1.X - xOffset,
		location1.Y, 0);
	cludgel2.setPositionAndOritentation(renderer, location2.X - xOffset,
		location2.Y, 0);
	renderer.getSpriteRenderer().registerSprite(beam1);
	renderer.getSpriteRenderer().registerSprite(beam2);
	renderer.getSpriteRenderer().registerSprite(cludgel1);
	renderer.getSpriteRenderer().registerSprite(cludgel2);

    }

}
