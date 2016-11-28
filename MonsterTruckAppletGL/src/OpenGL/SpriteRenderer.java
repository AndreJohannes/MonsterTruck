package OpenGL;

import Auxiliary.Vector2D.Rect;
import TextureMap.TextureMap.Texture;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.GLBuffers;
import glutil.MatrixStack;
import jglm.Mat4;
import jglm.Vec3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.atomic.AtomicReference;

public class SpriteRenderer implements IRenderer{

    private final Texture texture;
    private final Rect outputRect;
    private GLRenderer.ShaderAndTexture shaderAndTexture;
    private final float[] vertexData;
    private int[] vertexBufferObject = new int[1];
    private AtomicReference<float[]> modelMatrixFloatArray = new AtomicReference<float[]>();

    public SpriteRenderer(Texture texture, Rect outputRect, Rect textureRect) {
        this.texture = texture;
        this.outputRect = outputRect;
        modelMatrixFloatArray.set(new Mat4(1f).toFloatArray());
        this.vertexData = new float[]{outputRect.x2, outputRect.y2, 0.f, 1.0f,
                outputRect.x2, outputRect.y1, 0.f, 1.0f,
                outputRect.x1, outputRect.y1, 0.f, 1.0f,
                outputRect.x1, outputRect.y2, 0.f, 1.0f,
                (textureRect.x2 - 0.5f) / texture.width, 1f - (textureRect.y1 + 0.5f) / texture.height,
                (textureRect.x2 - 0.5f) / texture.width, 1f - (textureRect.y2 - 0.5f) / texture.height,
                (textureRect.x1 + 0.5f) / texture.width, 1f - (textureRect.y2 - 0.5f) / texture.height,
                (textureRect.x1 + 0.5f) / texture.width, 1f - (textureRect.y1 + 0.5f) / texture.height
        };

    }

    public static SpriteRenderer carRender = new SpriteRenderer(
            new Texture(0, 1440, 2048), new Rect(-98f, -36f, 98f, 36f),
            new Rect(200f, 360f, 396f, 431f));

    public static SpriteRenderer getWheelRenderer() {
        return new SpriteRenderer(new Texture(0, 1440, 2048),
                new Rect(-26f, -26f, 26f, 26f),
                new Rect(200f, 450f, 253f, 503f));
    }

    public void setPositionAndOrientation(double dx, double dy, double deg) {
        MatrixStack model = new MatrixStack();
        //
        model.scale(new Vec3(2f/1280f, 2f/720f, 1f));
        model.translate(new Vec3((float) (-640f+dx), (float) (360f-dy), 0));
        model.rotateZ((float) (deg));

        modelMatrixFloatArray.set(model.top().toFloatArray());
    }


    public void init(GL2 gl2, GLRenderer.ShaderAndTexture shaderAndTexture) {
        initializeVertexBuffer(gl2, vertexData);
        this.shaderAndTexture = shaderAndTexture;
    }

    public void Render(GL2 gl2) {
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

                gl2.glUniformMatrix4fv(this.shaderAndTexture.modUnLoc, 1, false, modelMatrixFloatArray.get(), 0);

                gl2.glUniform1f(this.shaderAndTexture.offUnLoc, 0);

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