package com.studiocinqo.monstertruck.WorldObjects;


import com.studiocinqo.monstertruck.OpenGL.CollectiveSpriteRenderer.ISpriteRenderer;
import com.studiocinqo.monstertruck.OpenGL.GLRenderer;

import TextureMap.TextureMap;
import TextureMap.TextureMap.Texture;
import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Rect;
import WorldObjects.BridgeBase;

public class Bridge extends BridgeBase<GLRenderer> implements ISpriteRenderer {

    private short[] indices;
    private float[] vertices;
    private float[] uvs;
    private final Texture texture;

    public Bridge(GLRenderer renderer, String name, Vector2D startPoint,
	    Vector2D endPoint, int numberOfLinks,
	    WorldObjects.BridgeBase.Suspension suspension) {
	super(startPoint, endPoint, numberOfLinks, suspension);
	TextureMap textureMap = renderer.getTextureMap();
	Rect textureRect = textureMap.textureByName.get(name);
	texture = textureMap.texture;
	indices = makeIndices();
	uvs = makeUVS(texture, textureRect);
    }

    @Override
    public void draw(GLRenderer renderer, double[] state, double offsetX) {
	vertices = makeVertices(offsetX, state);
	renderer.getSpriteRenderer().registerSprite(this);

    }

    @Override
    public Texture getTexture() {
	return texture;
    }

    @Override
    public short[] getIndices() {
	return indices;
    }

    @Override
    public float[] getUVS() {
	return uvs;
    }

    @Override
    public float[] getVertices() {
	return vertices;
    }

    private short[] makeIndices() {
	short[] retIndices = new short[6 * numberOfLinks];
	for (int i = 0; i < numberOfLinks; i++) {
	    retIndices[6 * i] = (short) (2 * i);
	    retIndices[6 * i + 1] = (short) (2 * i + 1);
	    retIndices[6 * i + 2] = (short) (2 * i + 2);
	    retIndices[6 * i + 3] = (short) (2 * i + 2);
	    retIndices[6 * i + 4] = (short) (2 * i + 1);
	    retIndices[6 * i + 5] = (short) (2 * i + 3);
	}
	return retIndices;
    }

    private float[] makeUVS(Texture texture, Rect rect) {
	float[] retUVS = new float[4 * (numberOfLinks + 1)];
	double max = Math.max(suspension.function(0),suspension.function(1));
	for (int i = 0; i <= (numberOfLinks); i++) {
	    retUVS[4 * i] = (rect.x1
		    + (rect.x2 - rect.x1) * (i / (float) numberOfLinks))
		    / texture.width;
	    retUVS[4 * i + 1] = rect.y2 / texture.height;
	    retUVS[4 * i + 2] = (rect.x1
		    + (rect.x2 - rect.x1) * (i / (float) numberOfLinks))
		    / texture.width;
	    retUVS[4 * i
		    + 3] = ((float) (rect.y2
			    + ((rect.y1 - rect.y2) / max
				    * suspension.function(
					    i / (double) numberOfLinks)))
		    / texture.height);
	}
	return retUVS;
    }

    private float[] makeVertices(double xOffset, double[] state) {
	float[] retVertices = new float[6 * (numberOfLinks + 1)];
	retVertices[0] = (float) startPoint.X - (float) xOffset;
	retVertices[1] = 720f - (float) startPoint.Y;
	retVertices[2] = 0f;
	retVertices[3] = (float) startPoint.X - (float) xOffset;
	retVertices[4] = 720f - (float) startPoint.Y
		+ +(float) suspension.function(0d);
	retVertices[5] = 0f;
	retVertices[6 * numberOfLinks] = (float) endPoint.X - (float) xOffset;
	retVertices[6 * numberOfLinks + 1] = 720f - (float) endPoint.Y;
	retVertices[6 * numberOfLinks + 2] = 0f;
	retVertices[6 * numberOfLinks + 3] = (float) endPoint.X
		- (float) xOffset;
	retVertices[6 * numberOfLinks + 4] = 720f - (float) endPoint.Y
		+ (float) suspension.function(1d);
	retVertices[6 * numberOfLinks + 5] = 0f;
	int offset = this.offset;
	for (int i = 1; i < (numberOfLinks); i++) {
	    retVertices[6 * i] = (float) state[offset] - (float) xOffset;
	    retVertices[6 * i + 1] = 720f - (float) state[offset + 1];
	    retVertices[6 * i + 2] = 0f;
	    retVertices[6 * i + 3] = (float) state[offset] - (float) xOffset;
	    retVertices[6 * i + 4] = 720f - (float) state[offset + 1]
		    + (float) suspension
			    .function(i / (double) (numberOfLinks + 0));
	    retVertices[6 * i + 5] = 0f;
	    offset += 4;
	}
	return retVertices;
    }
}