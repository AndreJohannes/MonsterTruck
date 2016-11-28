package com.studiocinqo.monstertruck.OpenGL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.atomic.AtomicReference;

import TextureMap.TextureMap.Texture;
import Auxiliary.Vector2D.Rect;
import android.opengl.GLES20;

public class ImageRenderer implements IRenderer {

    private final short[] indices;
    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final FloatBuffer uvBuffer;
    private AtomicReference<Float> offset = new AtomicReference<Float>(0f);
    private final Texture texture;

    public ImageRenderer(Texture texture, Rect outputRect, Rect textureRect) {
	this.texture = texture;
	float[] uvs = new float[] { (textureRect.x1 + 0.5f) / texture.width,
		(textureRect.y1 + 0.5f) / texture.height,
		(textureRect.x1 + 0.5f) / texture.width,
		(textureRect.y2 - 0.5f) / texture.height,
		(textureRect.x2 - 0.5f) / texture.width,
		(textureRect.y2 - 0.5f) / texture.height,
		(textureRect.x2 - 0.5f) / texture.width,
		(textureRect.y1 + 0.5f) / texture.height };
	uvBuffer = getUVBuffer(uvs);
	float[] vertices = new float[] { outputRect.x1, outputRect.y2, 0.0f,
		outputRect.x1, outputRect.y1, 0.0f, outputRect.x2,
		outputRect.y1, 0.0f, outputRect.x2, outputRect.y2, 0.0f };
	vertexBuffer = getVertexBuffer(vertices);
	indices = new short[] { 0, 1, 2, 0, 2, 3 };
	drawListBuffer = getDrawListBuffer(indices);
    }

    public static ImageRenderer backgroundRender = new ImageRenderer(
	    new Texture(0, 1440, 2048), new Rect(0f, 0f, 1280f, 720f),
	    new Rect(0f, 180f, 320f, 360f));

    public static ImageRenderer landscapeRender = new ImageRenderer(
	    new Texture(0, 1440, 2048), new Rect(0f, 0f, 1280f, 720f),
	    new Rect(0f, 0f, 320f, 180f));

    public void setOffset(double offset) {
	this.offset.set((float) (offset / texture.width));
    }

    public void Render(float[] m) {

	// get handle to vertex shader's vPosition member
	int mPositionHandle = GLES20.glGetAttribLocation(GLShaders.sp_Image,
		"vPosition");

	// Enable generic vertex attribute array
	GLES20.glEnableVertexAttribArray(mPositionHandle);

	// Prepare the triangle coordinate data
	GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
		0, vertexBuffer);

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

	GLES20.glUniform1f(mOffset, offset.get());

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

}
