package com.studiocinqo.monstertruck.WorldObjects;

import com.studiocinqo.monstertruck.OpenGL.CollectiveSpriteRenderer;
import com.studiocinqo.monstertruck.OpenGL.GLRenderer;

import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Rect;
import TextureMap.TextureMap;
import TextureMap.TextureMap.Texture;
import WorldObjects.ForegroundBase;

public class Foreground extends ForegroundBase<GLRenderer> {

    private final CollectiveSpriteRenderer.SpriteForRenderer sprite;

    public Foreground(GLRenderer renderer, String name, Vector2D pointA,
	    Vector2D pointB) {
	super(pointA, pointB);
	TextureMap textureMap = renderer.getTextureMap();
	Rect textureRect = textureMap.textureByName.get(name);
	sprite = new CollectiveSpriteRenderer.SpriteForRenderer(
		textureMap.texture, new Rect((float) pointA.X, (float) pointA.Y,
			(float) pointB.X, (float) pointB.Y),
		textureRect);
    }

    @Override
    public void draw(GLRenderer renderer, double xOffset) {
	sprite.setPosition(renderer, -xOffset, 0);
	renderer.getSpriteRenderer().registerSprite(sprite);
    }

}
