package com.studiocinqo.monstertruck.OpenGL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import TextureMap.TextureMap.Texture;
import Auxiliary.ArrayHelpers;
import Auxiliary.Vector2D.Rect;
import android.opengl.GLES20;

public class CollectiveSpriteRenderer implements IRenderer {

    public interface ISpriteRenderer {
	public Texture getTexture();

	public short[] getIndices();

	public float[] getUVS();

	public float[] getVertices();
    }

    public static class SpriteForRenderer implements ISpriteRenderer {

	private final short[] indices;
	private float[] vertices;
	private float[] uvs;
	private final Texture texture;
	private final Rect rect;

	public SpriteForRenderer(Texture texture, Rect outputRect,
		Rect textureRect) {
	    this.texture = texture;
	    setUVS(textureRect);
	    this.indices = new short[] { 0, 1, 2, 0, 2, 3 };
	    this.rect = outputRect;
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

	@Override
	public Texture getTexture() {
	    return texture;
	}

	public void setPositionAndOritentation(GLRenderer renderer, double dx,
		double dy, double rad) {
	    double sin = Math.sin(rad);
	    double cos = Math.cos(rad);// TODO: all cos *rect.x1 etc can be
				       // precomputed
	    dy = renderer.mScreenHeight - dy;
	    vertices = new float[] {
		    (float) (cos * rect.x1 + sin * rect.y2 + dx),
		    (float) (-cos * rect.y2 + sin * rect.x1 + dy), 0.0f,
		    (float) (cos * rect.x1 + sin * rect.y1 + dx),
		    (float) (-cos * rect.y1 + sin * rect.x1 + dy), 0.0f,
		    (float) (cos * rect.x2 + sin * rect.y1 + dx),
		    (float) (-cos * rect.y1 + sin * rect.x2 + dy), 0.0f,
		    (float) (cos * rect.x2 + sin * rect.y2 + dx),
		    (float) (-cos * rect.y2 + sin * rect.x2 + dy), 0.0f };
	}

	public void setPosition(GLRenderer renderer, double dx,
		double dy) {
	    dy = renderer.mScreenHeight - dy;
	    vertices = new float[] { (float) (rect.x1 + dx),
		    (float) (-rect.y2 + dy), 0.0f, (float) (rect.x1 + dx),
		    (float) (-rect.y1 + dy), 0.0f, (float) (rect.x2 + dx),
		    (float) (-rect.y1 + dy), 0.0f, (float) (rect.x2 + dx),
		    (float) (-rect.y2 + dy), 0.0f };
	}

	public void setUVS(Rect rect) {
	    setUVS(rect.x1, rect.y1, rect.x2, rect.y2);
	}

	
	public void setUVS(float x1, float y1, float x2, float y2) {
	    uvs = new float[] { (x1 + 0.5f) / texture.width,
		    (y2 - 0.5f) / texture.height,
		    (x1 + 0.5f) / texture.width,
		    (y1 + 0.5f) / texture.height,
		    (x2 - 0.5f) / texture.width,
		    (y1 + 0.5f) / texture.height,
		    (x2 - 0.5f) / texture.width,
		    (y2 - 0.5f) / texture.height };
	}

    }

    private class BufferContainer {

	private final int length;
	private final FloatBuffer vertexBuffer;
	private final ShortBuffer drawListBuffer;
	private final FloatBuffer uvBuffer;

	private BufferContainer(FloatBuffer vertexBuffer,
		ShortBuffer drawListBuffer, FloatBuffer uvBuffer, int length) {
	    this.length = length;
	    this.drawListBuffer = drawListBuffer;
	    this.vertexBuffer = vertexBuffer;
	    this.uvBuffer = uvBuffer;
	}

    }

    private final Texture texture;
    private List<Float> vertices = new LinkedList<Float>();
    private List<Float> uvs = new LinkedList<Float>();
    private List<Short> indices = new LinkedList<Short>();
    private AtomicReference<BufferContainer> bufferContainer = new AtomicReference<CollectiveSpriteRenderer.BufferContainer>(
	    null);

    public CollectiveSpriteRenderer(Texture texture) {
	this.texture = texture;
    }

    public void registerSprite(ISpriteRenderer sprite) {
	if (this.texture.lot != sprite.getTexture().lot)
	    throw new RuntimeException("Textures do not comply");
	int size = vertices.size() / 3;
	for (float vertex : sprite.getVertices())
	    vertices.add(vertex);
	for (float uv : sprite.getUVS())
	    uvs.add(uv);
	for (short index : sprite.getIndices())
	    indices.add((short) (index + size));
    }

    @Override
    public void Render(float[] m) {

	BufferContainer bufferContainer = this.bufferContainer.get();
	if (bufferContainer == null)
	    return;

	int mPositionHandle = GLES20.glGetAttribLocation(GLShaders.sp_Image,
		"vPosition");
	GLES20.glEnableVertexAttribArray(mPositionHandle);
	GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
		0, bufferContainer.vertexBuffer);
	int mTexCoordLoc = GLES20.glGetAttribLocation(GLShaders.sp_Image,
		"a_texCoord");
	GLES20.glEnableVertexAttribArray(mTexCoordLoc);
	GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0,
		bufferContainer.uvBuffer);
	int mtrxhandle = GLES20.glGetUniformLocation(GLShaders.sp_Image,
		"uMVPMatrix");
	GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);
	int mSamplerLoc = GLES20.glGetUniformLocation(GLShaders.sp_Image,
		"s_texture");
	GLES20.glUniform1i(mSamplerLoc, 0);
	int mOffset = GLES20.glGetUniformLocation(GLShaders.sp_Image,
		"f_offset");
	GLES20.glUniform1f(mOffset, 0f);
	GLES20.glDrawElements(GLES20.GL_TRIANGLES, bufferContainer.length,
		GLES20.GL_UNSIGNED_SHORT, bufferContainer.drawListBuffer);
	GLES20.glDisableVertexAttribArray(mPositionHandle);
	GLES20.glDisableVertexAttribArray(mTexCoordLoc);

    }

    public void setAndSealBuffers() {
	FloatBuffer uvBuffer = getUVBuffer(uvs);
	FloatBuffer vertexBuffer = getVertexBuffer(vertices);
	ShortBuffer drawListBuffer = getDrawListBuffer(indices);
	int indicesLength = indices.size();
	uvs.clear();
	vertices.clear();
	indices.clear();
	this.bufferContainer.set(new BufferContainer(vertexBuffer,
		drawListBuffer, uvBuffer, indicesLength));
    }

    private ShortBuffer getDrawListBuffer(List<Short> indices) {
	ShortBuffer drawListBuffer;
	ByteBuffer dlb = ByteBuffer.allocateDirect(indices.size() * 2);
	dlb.order(ByteOrder.nativeOrder());
	drawListBuffer = dlb.asShortBuffer();
	drawListBuffer.put(ArrayHelpers.toShortArray(indices));
	drawListBuffer.position(0);
	return drawListBuffer;
    }

    private FloatBuffer getUVBuffer(List<Float> uvs) {
	FloatBuffer uvBuffer;
	ByteBuffer bb = ByteBuffer.allocateDirect(uvs.size() * 4);
	bb.order(ByteOrder.nativeOrder());
	uvBuffer = bb.asFloatBuffer();
	uvBuffer.put(ArrayHelpers.toFloatArray(uvs));
	uvBuffer.position(0);
	return uvBuffer;
    }

    private FloatBuffer getVertexBuffer(List<Float> vertices) {
	FloatBuffer vertexBuffer;
	ByteBuffer bb = ByteBuffer.allocateDirect(vertices.size() * 4);
	bb.order(ByteOrder.nativeOrder());
	vertexBuffer = bb.asFloatBuffer();
	vertexBuffer.put(ArrayHelpers.toFloatArray(vertices));
	vertexBuffer.position(0);
	return vertexBuffer;
    }

}
