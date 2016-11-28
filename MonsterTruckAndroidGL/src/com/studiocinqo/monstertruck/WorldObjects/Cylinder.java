package com.studiocinqo.monstertruck.WorldObjects;

import com.studiocinqo.monstertruck.OpenGL.CollectiveSpriteRenderer;
import com.studiocinqo.monstertruck.OpenGL.GLRenderer;
import TextureMap.TextureMap.Texture;
import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Rect;
import WorldObjects.CylinderBase;

public class Cylinder extends CylinderBase<GLRenderer> {

    private final CollectiveSpriteRenderer.SpriteForRenderer sprite;

    public Cylinder(GLRenderer renderer,String name, Vector2D center, double radius) {
	super(center, radius);
	sprite = new CollectiveSpriteRenderer.SpriteForRenderer(
		new Texture(0, 1440, 2048), new Rect(-(float) radius,
			-(float) radius, (float) radius, (float) radius),
		new Rect(320f, 180f, 500f, 360f));
    }

    @Override
    public void draw(GLRenderer renderer, double[] state, double xOffset) {
	sprite.setPositionAndOritentation(renderer, state[offset] - xOffset,
		state[offset + 1], -state[offset + 4]);

	renderer.getSpriteRenderer().registerSprite(sprite);
    }

}
