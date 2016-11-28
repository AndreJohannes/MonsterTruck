package com.studiocinqo.monstertruck.OpenGL;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import TextureMap.TextureMap;
import TextureMap.TextureMap.Texture;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

public class GLRenderer implements Renderer {

    // Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    private ImageRenderer background = ImageRenderer.backgroundRender;
    private ImageRenderer landscape = ImageRenderer.landscapeRender;
    private SpriteRenderer carRenderer = SpriteRenderer.carRender;
    private SpriteRenderer frontWheelRenderer = SpriteRenderer
	    .getWheelRenderer();
    private SpriteRenderer rearWheelRenderer = SpriteRenderer
	    .getWheelRenderer();
    private CollectiveSpriteRenderer spriteRenderer = new CollectiveSpriteRenderer(
	    new Texture(0, 2048, 1440));
    private final TextureMap textureMap;

    // Our screenresolution
    public final float mScreenWidth = 1280;
    public final float mScreenHeight = 720;

    // Misc
    Context mContext;
    long mLastTime;
    int mProgram;

    public GLRenderer(Context c) {
	mContext = c;
	this.textureMap = new TextureMap();
	mLastTime = System.currentTimeMillis() + 100;
    }

    public final TextureMap getTextureMap(){
	return textureMap;
    }
    
    public ImageRenderer getLandscape() {
	return landscape;
    }

    public SpriteRenderer getVehicle() {
	return carRenderer;
    }

    public SpriteRenderer getFrontWheel() {
	return frontWheelRenderer;
    }

    public SpriteRenderer getRearWheel() {
	return rearWheelRenderer;
    }

    public CollectiveSpriteRenderer getSpriteRenderer() {
	return spriteRenderer;
    }

    public void onPause() {
	/* Do stuff to pause the renderer */
    }

    public void onResume() {
	/* Do stuff to resume the renderer */
	mLastTime = System.currentTimeMillis();
    }

    @Override
    public void onDrawFrame(GL10 unused) {

	long now = System.currentTimeMillis();

	if (mLastTime > now)
	    return;

	long elapsed = now - mLastTime;

	Render(mtrxProjectionAndView);

	mLastTime = now;

    }

    private void Render(float[] m) {

	GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

	background.Render(m);
	landscape.Render(m);
	carRenderer.Render(m);
	frontWheelRenderer.Render(m);
	rearWheelRenderer.Render(m);
	spriteRenderer.Render(m);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

	// mScreenWidth = width;
	// mScreenHeight = height;

	GLES20.glViewport(0, 0, (int) mScreenWidth, (int) mScreenHeight);
	for (int i = 0; i < 16; i++) {
	    mtrxProjection[i] = 0.0f;
	    mtrxView[i] = 0.0f;
	    mtrxProjectionAndView[i] = 0.0f;
	}
	Matrix.orthoM(mtrxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight,
		0, 50);
	Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
	Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView,
		0);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

	SetupImage();
	GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

	GLES20.glEnable(GLES20.GL_BLEND);
	GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

	int vertexShader = GLShaders.loadShader(GLES20.GL_VERTEX_SHADER,
		GLShaders.vs_SolidColor);
	int fragmentShader = GLShaders.loadShader(GLES20.GL_FRAGMENT_SHADER,
		GLShaders.fs_SolidColor);

	GLShaders.sp_SolidColor = GLES20.glCreateProgram();
	GLES20.glAttachShader(GLShaders.sp_SolidColor, vertexShader);
	GLES20.glLinkProgram(GLShaders.sp_SolidColor);

	vertexShader = GLShaders.loadShader(GLES20.GL_VERTEX_SHADER,
		GLShaders.vs_Image);
	fragmentShader = GLShaders.loadShader(GLES20.GL_FRAGMENT_SHADER,
		GLShaders.fs_Image);

	GLShaders.sp_Image = GLES20.glCreateProgram();
	GLES20.glAttachShader(GLShaders.sp_Image, vertexShader);
	GLES20.glAttachShader(GLShaders.sp_Image, fragmentShader);
	GLES20.glLinkProgram(GLShaders.sp_Image);
	GLES20.glUseProgram(GLShaders.sp_Image);
    }

    public void SetupImage() {

	int[] texturenames = new int[1];
	GLES20.glGenTextures(1, texturenames, 0);

	Bitmap bmp = null;
	try {
	    bmp = BitmapFactory
		    .decodeStream(mContext.getAssets().open("texturemap.png"));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);

	GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
		GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
		GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

	GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

	bmp.recycle();

    }

}
