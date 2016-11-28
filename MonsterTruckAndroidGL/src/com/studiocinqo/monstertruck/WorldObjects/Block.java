package com.studiocinqo.monstertruck.WorldObjects;

import com.studiocinqo.monstertruck.OpenGL.CollectiveSpriteRenderer;
import com.studiocinqo.monstertruck.OpenGL.GLRenderer;

import TextureMap.TextureMap;
import TextureMap.TextureMap.Texture;

import Auxiliary.Vector2D.Rect;
import WorldObjects.BlockBase;

public class Block extends BlockBase<GLRenderer> {

    private final CollectiveSpriteRenderer.SpriteForRenderer sprite;

    public Block(GLRenderer renderer, String name,
	    Auxiliary.Vector2D startPosition, double width, double height) {
	super(startPosition, width, height);
	TextureMap textureMap = renderer.getTextureMap();
	sprite = new CollectiveSpriteRenderer.SpriteForRenderer(
		textureMap.texture,
		new Rect(-(float) width / 2f, -(float) height / 2f,
			(float) width / 2f, (float) height / 2f),
		textureMap.textureByName.get(name));
    }

    @Override
    public void draw(GLRenderer renderer, double[] state, double xOffset) {
	sprite.setPositionAndOritentation(renderer, state[offset] - xOffset,
		state[offset + 1], state[offset + 4]);

	renderer.getSpriteRenderer().registerSprite(sprite);

    }

}
