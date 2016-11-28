package OpenGL;

import Auxiliary.Vector2D.Rect;
import TextureMap.TextureMap.Texture;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.GLBuffers;
import jglm.Mat4;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.atomic.AtomicReference;


public class ImageRenderer {

    private AtomicReference<Float> offset = new AtomicReference<Float>(0f);
    private int[] vertexBufferObject = new int[1];
    private final Texture texture;
    private final float[] vertexData;
    private GLRenderer.ShaderAndTexture shaderAndTexture;
    private static final  float[] modelMatrixFloatArray = new Mat4(1f).toFloatArray();

    public ImageRenderer(Texture texture, Rect outputRect, Rect textureRect) {
        this.texture = texture;
//        vertexData = new float[]{(textureRect.x1 + 0.5f) / texture.width,
//                (textureRect.y1 + 0.5f) / texture.height, 0f, 0f,
//                (textureRect.x1 + 0.5f) / texture.width,
//                (textureRect.y2 - 0.5f) / texture.height, 0f, 0f,
//                (textureRect.x2 - 0.5f) / texture.width,
//                (textureRect.y2 - 0.5f) / texture.height, 0f, 0f,
//                (textureRect.x2 - 0.5f) / texture.width,
//                (textureRect.y1 + 0.5f) / texture.height, 0f, 0f,
//                outputRect.x1, outputRect.y2, 0.0f,
//                outputRect.x1, outputRect.y1, 0.0f,
//                outputRect.x2, outputRect.y1, 0.0f,
//                outputRect.x2, outputRect.y2, 0.0f};

        vertexData = new float[]{1f, 1f, 0.f, 1.0f,
                1f, -1f, 0.f, 1.0f,
                -1f, -1f, 0.f, 1.0f,
                -1f, 1f, 0.f, 1.0f,
                (textureRect.x2 - 0.5f) / texture.width, 1f - (textureRect.y1 + 0.5f) / texture.height,
                (textureRect.x2 - 0.5f) / texture.width, 1f - (textureRect.y2 - 0.5f) / texture.height,
                (textureRect.x1 + 0.5f) / texture.width, 1f - (textureRect.y2 - 0.5f) / texture.height,
                (textureRect.x1 + 0.5f) / texture.width, 1f - (textureRect.y1 + 0.5f) / texture.height
        };
    }

    public static ImageRenderer backgroundRender = new ImageRenderer(
            new Texture(0, 1440, 2048),
            new Rect(0f, 0f, 1280f, 720f),
            new Rect(0f, 180f, 320f, 360f));

    public static ImageRenderer landscapeRenderer = new ImageRenderer(
            new Texture(0, 1440, 2048),
            new Rect(0f, 0f, 1280f, 720f),
            new Rect(0f, 0f, 320f, 180f));

    public void init(GL2 gl2, GLRenderer.ShaderAndTexture shaderAndTexture) {
        initializeVertexBuffer(gl2, vertexData);
        this.shaderAndTexture = shaderAndTexture;
    }

    public void setOffset(double offset) {
        this.offset.set((float) (offset / texture.width));
    }

    public void Render(GL2 gl2) {
        //gl2.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //gl2.glClear(GL2.GL_COLOR_BUFFER_BIT);

        this.shaderAndTexture.programObject.bind(gl2);
        {
            gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertexBufferObject[0]);

            gl2.glEnableVertexAttribArray(0);
            gl2.glEnableVertexAttribArray(1);
            {
                gl2.glActiveTexture(GL2.GL_TEXTURE0);
                this.shaderAndTexture.texture.enable(gl2);
                this.shaderAndTexture.texture.bind(gl2);
                gl2.glUniform1i(this.shaderAndTexture.texUnLoc, 0);

                gl2.glUniformMatrix4fv(this.shaderAndTexture.modUnLoc, 1, false, modelMatrixFloatArray, 0);

                gl2.glUniform1f(this.shaderAndTexture.offUnLoc, offset.get());

                gl2.glVertexAttribPointer(0, 4, GL2.GL_FLOAT, false, 0, 0);
                gl2.glVertexAttribPointer(1, 2, GL2.GL_FLOAT, false, 0, 4 * 4 * 4);

                gl2.glDrawArrays(GL2.GL_QUADS, 0, 4);

                this.shaderAndTexture.texture.disable(gl2);
            }
            gl2.glDisableVertexAttribArray(0);
            gl2.glDisableVertexAttribArray(1);
        }
        this.shaderAndTexture.programObject.unbind(gl2);

    }

    private void initializeVertexBuffer(GL2 gl2, float[] vertexData) {
        gl2.glGenBuffers(1, IntBuffer.wrap(vertexBufferObject));
        gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertexBufferObject[0]);
        {
            FloatBuffer buffer = GLBuffers.newDirectFloatBuffer(vertexData);
            gl2.glBufferData(GL2.GL_ARRAY_BUFFER, vertexData.length * 4, buffer, GL2.GL_STATIC_DRAW);
        }
        gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
    }


}

