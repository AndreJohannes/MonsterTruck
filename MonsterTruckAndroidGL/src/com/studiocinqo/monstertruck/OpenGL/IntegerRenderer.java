package com.studiocinqo.monstertruck.OpenGL;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;

import com.studiocinqo.monstertruck.OpenGL.CollectiveSpriteRenderer.ISpriteRenderer;
import Auxiliary.Vector2D.Rect;
import TextureMap.TextureMap.Texture;

public class IntegerRenderer implements ISpriteRenderer {

    public LinkedList<float[]> stack;
    private float[] uvs;
    private float[] vertices;
    private short[] indices;
    private short[] indicesTemplate = new short[] { 0, 1, 2, 0, 2, 3 };
    private final Dictionary<Integer, float[]> dictUVS;
    private final Rect textureRect;
    private Rect rect;
    private final float offset;
    private final Texture texture;
    private float dx = 0, dy = 0;

    public IntegerRenderer() {
	offset = 8;
	texture = new Texture(0, 1440, 2048);
	rect = new Rect(0, 0, 15, 22);
	textureRect = new Rect(735, 180, 743, 192);
	dictUVS = makeUVSDictionary();
    }

    @Override
    public short[] getIndices() {
	return indices;
    }

    @Override
    public Texture getTexture() {
	return texture;
    }

    @Override
    public float[] getUVS() {
	return uvs;
    }

    @Override
    public float[] getVertices() {
	return vertices;
    }

    public void setInteger(int value) {
	stack = new LinkedList<float[]>();
	if (value == 0)
	    stack.addFirst(dictUVS.get(0));
	else
	    while (value > 0) {
		stack.addFirst(dictUVS.get(value % 10));
		value /= 10;
	    }
	makeArrays();
    }

    public void setPosition(GLRenderer renderer, double dx, double dy) {
	this.dx = (float) dx;
	this.dy = renderer.mScreenHeight-(float) dy;
    }

    private int addElements(float[] array, int index, float[] inArray) {
	for (float value : inArray) {
	    array[index++] = value;
	}
	return index;
    }

    private float[] getUVS(int offsetIndex, float offset) {
	float dx = offset * offsetIndex;
	return new float[] { (textureRect.x1 + 0.5f + dx) / texture.width,
		(textureRect.y2 - 0.5f) / texture.height,
		(textureRect.x1 + 0.5f + dx) / texture.width,
		(textureRect.y1 + 0.5f) / texture.height,
		(textureRect.x2 - 0.5f + dx) / texture.width,
		(textureRect.y1 + 0.5f) / texture.height,
		(textureRect.x2 - 0.5f + dx) / texture.width,
		(textureRect.y2 - 0.5f) / texture.height };
    }

    private float[] getVertices(float dx) {
	return new float[] { (float) (rect.x1 + dx + this.dx),
		(float) (-rect.y2 + this.dy), 0.0f,
		(float) (rect.x1 + this.dx + dx), (float) (-rect.y1 + this.dy),
		0.0f, (float) (rect.x2 + this.dx + dx),
		(float) (-rect.y1 + this.dy), 0.0f,
		(float) (rect.x2 + this.dx + dx), (float) (-rect.y2 + this.dy),
		0.0f };
    }

    private void makeArrays() {
	int size = stack.size();
	this.uvs = new float[8 * size];
	int indexForUVS = 0;
	this.vertices = new float[12 * size];
	int indexForVertices = 0;
	this.indices = new short[6 * size];
	int indexForIndices = 0;

	float dx = 0;
	for (float[] uvs : stack) {
	    int offset = indexForVertices / 3;
	    indexForUVS = addElements(this.uvs, indexForUVS, uvs);
	    indexForVertices = addElements(this.vertices, indexForVertices,
		    getVertices(dx));
	    dx += 15;
	    for (int i = 0; i < indicesTemplate.length; i++) {
		indices[indexForIndices++] = (short) (indicesTemplate[i]
			+ offset);
	    }
	}
    }

    private Dictionary<Integer, float[]> makeUVSDictionary() {
	Dictionary<Integer, float[]> retDic = new Hashtable<Integer, float[]>();
	for (int i = 0; i < 10; i++) {
	    retDic.put(i, getUVS(i, offset));
	}
	return retDic;
    }

}
