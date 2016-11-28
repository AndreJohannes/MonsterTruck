package com.studiocinqo.monstertruck.OpenGL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.atomic.AtomicReference;

import Auxiliary.Vector2D.Rect;
import android.opengl.GLES20;
import TextureMap.TextureMap.Texture;

public class SpriteRenderer implements IRenderer {

    private final short[] indices;
    private AtomicReference<FloatBuffer> vertexBuffer = new AtomicReference<FloatBuffer>(
	    null);
    private final ShortBuffer drawListBuffer;
    private final FloatBuffer uvBuffer;
    private final Texture texture;
    private final Rect outputRect;

    public SpriteRenderer(Texture texture, Rect outputRect, Rect textureRect) {
	this.texture = texture;
	this.outputRect = outputRect;
	float[] uvs = new float[] { (textureRect.x1 + 0.5f) / texture.width,
		(textureRect.y1 + 0.5f) / texture.height,
		(textureRect.x1 + 0.5f) / texture.width,
		(textureRect.y2 - 0.5f) / texture.height,
		(textureRect.x2 - 0.5f) / texture.width,
		(textureRect.y2 - 0.5f) / texture.height,
		(textureRect.x2 - 0.5f) / texture.width,
		(textureRect.y1 + 0.5f) / texture.height };
	uvBuffer = getUVBuffer(uvs);
	vertexBuffer.set(getVertexBuffer(getVertexArray(outputRect)));
	indices = new short[] { 0, 1, 2, 0, 2, 3 };
	drawListBuffer = getDrawListBuffer(indices);
    }

    public static SpriteRenderer carRender = new SpriteRenderer(
	    new Texture(0, 1440, 2048), new Rect(-98f, -36f, 98f, 36f),
	    new Rect(200f, 360f, 396f, 431f));

    public static SpriteRenderer getWheelRenderer() {
	return new SpriteRenderer(new Texture(0, 1440, 2048),
		new Rect(-26f, -26f, 26f, 26f),
		new Rect(200f, 450f, 253f, 503f));
    }

    public void setPositionAndOrientation(double dx, double dy, double rad) {
	vertexBuffer
		.set(getVertexBuffer(getVertexArray(outputRect, dx, dy, rad)));
    }

    public void Render(float[] m) {

	// get handle to vertex shader's vPosition member
	int mPositionHandle = GLES20.glGetAttribLocation(GLShaders.sp_Image,
		"vPosition");

	// Enable generic vertex attribute array
	GLES20.glEnableVertexAttribArray(mPositionHandle);

	// Prepare the triangle coordinate data
	GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
		0, vertexBuffer.get());

	// Get handle to texture coordinates location
	int mTexCoordLoc = GLES20.glGetAttribLocation(GLShaders.sp_Image,
		"a_texCoord");

	// Enable generic vertex attribute array
	GLES20.glEnableVertexAttribArray(mTexCoordLoc);

	// Prepare the texturecoordinates
	GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0,
		uvBuffer);

	// Get handle to shape's transformation matrix
	int mtrxhandle = GLES20.glGetUniformLocation(GLShaders.sp_Image,
		"uMVPMatrix");

	// Apply the projection and view transformation
	GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

	// Get handle to textures locations
	int mSamplerLoc = GLES20.glGetUniformLocation(GLShaders.sp_Image,
		"s_texture");

	// Set the sampler texture unit to 0, where we have saved the texture.
	GLES20.glUniform1i(mSamplerLoc, 0);

	int mOffset = GLES20.glGetUniformLocation(GLShaders.sp_Image,
		"f_offset");

	GLES20.glUniform1f(mOffset, 0f);

	// Draw the triangle
	GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
		GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

	// Disable vertex array
	GLES20.glDisableVertexAttribArray(mPositionHandle);
	GLES20.glDisableVertexAttribArray(mTexCoordLoc);
    }

    private FloatBuffer getUVBuffer(float[] uvs) {
	FloatBuffer uvBuffer;
	ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
	bb.order(ByteOrder.nativeOrder());
	uvBuffer = bb.asFloatBuffer();
	uvBuffer.put(uvs);
	uvBuffer.position(0);
	return uvBuffer;
    }

    private FloatBuffer getVertexBuffer(float[] vertices) {
	FloatBuffer vertexBuffer;
	ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
	bb.order(ByteOrder.nativeOrder());
	vertexBuffer = bb.asFloatBuffer();
	vertexBuffer.put(vertices);
	vertexBuffer.position(0);
	return vertexBuffer;
    }

    private ShortBuffer getDrawListBuffer(short[] indices) {
	ShortBuffer drawListBuffer;
	// initialize byte buffer for the draw list
	ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
	dlb.order(ByteOrder.nativeOrder());
	drawListBuffer = dlb.asShortBuffer();
	drawListBuffer.put(indices);
	drawListBuffer.position(0);
	return drawListBuffer;
    }

    private float[] getVertexArray(Rect rect) {
	return new float[] { rect.x1, rect.y2, 0.0f, rect.x1, rect.y1, 0.0f,
		rect.x2, rect.y1, 0.0f, rect.x2, rect.y2, 0.0f };
    }

    private float[] getVertexArray(Rect rect, double dx, double dy,
	    double rad) {
	double sin = Math.sin(rad);
	double cos = Math.cos(rad);// TODO: all cos *rect.x1 etc can be
				   // precomputed
	return new float[] { (float) (cos * rect.x1 + sin * rect.y2 + dx),
		(float) (cos * rect.y2 - sin * rect.x1 + dy), 0.0f,
		(float) (cos * rect.x1 + sin * rect.y1 + dx),
		(float) (cos * rect.y1 - sin * rect.x1 + dy), 0.0f,
		(float) (cos * rect.x2 + sin * rect.y1 + dx),
		(float) (cos * rect.y1 - sin * rect.x2 + dy), 0.0f,
		(float) (cos * rect.x2 + sin * rect.y2 + dx),
		(float) (cos * rect.y2 - sin * rect.x2 + dy), 0.0f };
    }

}
