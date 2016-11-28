package OpenGL;

import TextureMap.TextureMap;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.IOException;
import java.net.URL;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.GL_ALPHA_TEST;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

public class GLRenderer extends GLCanvas implements GLEventListener {

    public class ShaderAndTexture {

        public final GLSLProgramObject programObject;
        public final com.jogamp.opengl.util.texture.Texture texture;
        public final int texUnLoc;
        public final int modUnLoc;
        public final int offUnLoc;

        public ShaderAndTexture(GLSLProgramObject programObject, com.jogamp.opengl.util.texture.Texture texture,
                                int texUnLoc, int modUnLoc, int offUnLoc) {
            this.programObject = programObject;
            this.texUnLoc = texUnLoc;
            this.modUnLoc = modUnLoc;
            this.texture = texture;
            this.offUnLoc = offUnLoc;
        }

    }

    private ImageRenderer background = ImageRenderer.backgroundRender;
    private ImageRenderer landscape = ImageRenderer.landscapeRenderer;
    private SpriteRenderer carRenderer = SpriteRenderer.carRender;
    private SpriteRenderer frontWheelRenderer = SpriteRenderer
            .getWheelRenderer();
    private SpriteRenderer rearWheelRenderer = SpriteRenderer
            .getWheelRenderer();
    private CollectiveSpriteRenderer spriteRenderer = CollectiveSpriteRenderer.getInstance();
    private final TextureMap textureMap;

    private ShaderAndTexture shaderAndTexure = null;

    // Our screenresolution
    public final float mScreenWidth = 1280;
    public final float mScreenHeight = 720;

    private GLU glu;

    public GLRenderer() {
        this.textureMap = new TextureMap();
        int FPS = 60;
        this.addGLEventListener(this);
        final FPSAnimator animator = new FPSAnimator(this, FPS, true);
        animator.start();
    }

    public final TextureMap getTextureMap() {
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

    public ShaderAndTexture getShaderAndTexure() {
        return shaderAndTexure;
    }


    // Misc
    long mLastTime;

    private void Render(GL2 gl2) {
        background.Render(gl2);
        landscape.Render(gl2);
        carRenderer.Render(gl2);
        frontWheelRenderer.Render(gl2);
        rearWheelRenderer.Render(gl2);
        spriteRenderer.Render(gl2);

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
        glu = new GLU();                         // get GL Utilities
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        //gl.glEnable(GL_DEPTH_TEST); // enables depth testing
        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
        gl.glEnable(GL_ALPHA);
        gl.glEnable(GL_ALPHA_TEST);
        gl.glEnable(gl.GL_BLEND);
        gl.glEnable(GL2.GL_ALPHA);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL2ES1.GL_COLOR_MATERIAL);
        //gl.glEnable(GL2.GL_CULL_FACE);
        //gl.glCullFace(GL2.GL_FRONT_AND_BACK);
        GLSLProgramObject programObject = buildShader(gl);
        Texture texture = initializeTexture(gl);
        int textureUnLoc = gl.glGetUniformLocation(programObject.getProgramId(), "myTexture");
        int modUnLoc = gl.glGetUniformLocation(programObject.getProgramId(), "model");
        int offsetUnLoc = gl.glGetUniformLocation(programObject.getProgramId(), "offset");
        shaderAndTexure = new ShaderAndTexture(programObject, texture,
                textureUnLoc,
                modUnLoc, offsetUnLoc);

        landscape.init(gl, shaderAndTexure);
        background.init(gl, shaderAndTexure);
        carRenderer.init(gl, shaderAndTexure);
        frontWheelRenderer.init(gl, shaderAndTexure);
        rearWheelRenderer.init(gl, shaderAndTexure);
        spriteRenderer.init(gl, shaderAndTexure);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

        if (height == 0) height = 1;   // prevent divide by zero
        float aspect = (float) width / height;

        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar

        // Enable the model-view transform
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
        //gl.glLoadIdentity();  // reset the model-view matrix

        // ----- Your OpenGL rendering code here (Render a white triangle for testing) -----
        //gl.glTranslatef(-3f, -.9f, -6.0f); // translate into the screen
        //gl.glBegin(GL_TRIANGLES); // draw using triangles
        //gl.glVertex3f(0.0f, 1.0f, 0.0f);
        //gl.glVertex3f(-1.0f, -1.0f, 0.0f);
        //gl.glVertex3f(1.0f, -1.0f, 0.0f);
        //gl.glEnd();
        this.Render(gl);
        //gl.glFlush();
        drawable.swapBuffers();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    private GLSLProgramObject buildShader(GL2 gl2) {

        final String shadersFilepath = "../resources/";

        GLSLProgramObject programObject = new GLSLProgramObject(gl2);
        programObject.attachVertexShader(gl2, shadersFilepath + "Standard_VS.glsl");
        programObject.attachFragmentShader(gl2, shadersFilepath + "TextureAtlas_FS.glsl");
        programObject.initializeProgram(gl2, true);

        return programObject;

    }

    private Texture initializeTexture(GL2 gl2) {

        Texture t = null;

        try {
            URL url = this.getClass().getResource("../resources/texturemap.png");
            t = TextureIO.newTexture(url, false, ".png");
            t.setTexParameteri(gl2, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
            t.setTexParameteri(gl2, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
            t.setTexParameteri(gl2, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
            t.setTexParameteri(gl2, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);

        } catch (IOException | GLException ex) {
            throw new RuntimeException(ex);
            //Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        return t;

    }


}

