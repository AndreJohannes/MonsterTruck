package OpenGL;

import Auxiliary.Vector2D;
import TextureMap.TextureMap;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.GLBuffers;
import glutil.MatrixStack;
import jglm.Mat4;
import jglm.Vec3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.atomic.AtomicReference;

public class MeshRenderer implements IRenderer {

    //private final TextureMap.Texture texture;
    private GLRenderer.ShaderAndTexture shaderAndTexture;
    private float[] uvData;
    private float[] vertexData;
    private int[] indexData;
    private int[] vertexBufferObject = new int[1];
    private int[] indexBufferObject = new int[1];
    private int[] uvBufferObject = new int[1];
    private AtomicReference<float[]> modelMatrixFloatArray = new AtomicReference<float[]>();

    public MeshRenderer(float[] uvData, int[] indexData) {
        modelMatrixFloatArray.set(new Mat4(1f).toFloatArray());
        this.uvData = uvData;
        this.indexData = indexData;
    }

    public void setPositionAndOrientation(double dx, double dy, double deg) {
        MatrixStack model = new MatrixStack();
        //
        model.scale(new Vec3(2f / 1280f, 2f / 720f, 1f));
        model.translate(new Vec3((float) (-640f + dx), (float) (360f - dy), 0));
        model.rotateZ((float) (deg));

        modelMatrixFloatArray.set(model.top().toFloatArray());
    }

    public void init(GL2 gl2, GLRenderer.ShaderAndTexture shaderAndTexture) {
        setPositionAndOrientation(0, 720, 0);
        //initializeVertexBuffer(gl2, vertexData);
        initializeIndexBuffer(gl2, indexData);
        initializeUVBuffer(gl2, uvData);
        this.shaderAndTexture = shaderAndTexture;
    }

    public void setVertices(float[] vertexData) {
        this.vertexData = vertexData;
    }

    public void Render(GL2 gl2) {

        this.initializeVertexBuffer(gl2, this.vertexData);

        this.shaderAndTexture.programObject.bind(gl2);
        {


            gl2.glEnableVertexAttribArray(0);
            gl2.glEnableVertexAttribArray(1);
            {
                gl2.glActiveTexture(GL2.GL_TEXTURE0);
                this.shaderAndTexture.texture.enable(gl2);
                this.shaderAndTexture.texture.bind(gl2);
                gl2.glUniform1i(this.shaderAndTexture.texUnLoc, 0);

                gl2.glUniformMatrix4fv(this.shaderAndTexture.modUnLoc, 1, false, modelMatrixFloatArray.get(), 0);

                gl2.glUniform1f(this.shaderAndTexture.offUnLoc, 0);

                gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertexBufferObject[0]);
                gl2.glVertexAttribPointer(0, 3, GL2.GL_FLOAT, false, 0, 0);

                gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, uvBufferObject[0]);
                gl2.glVertexAttribPointer(1, 2, GL2.GL_FLOAT, false, 0, 0);

                gl2.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);
                gl2.glDrawElements(GL2.GL_TRIANGLES, this.indexData.length, GL2.GL_UNSIGNED_INT, 0);
                //gl2.glDrawArrays(GL2.GL_QUADS, 0, 12);

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

    private void initializeIndexBuffer(GL2 gl2, int[] indexData) {

        gl2.glGenBuffers(1, IntBuffer.wrap(indexBufferObject));

        gl2.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);
        {
            IntBuffer buffer = GLBuffers.newDirectIntBuffer(indexData);
            gl2.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, indexData.length * 4, buffer, GL2.GL_STATIC_DRAW);
        }
        gl2.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);

    }

    private void initializeUVBuffer(GL2 gl2, float[] uvData) {

        gl2.glGenBuffers(1, IntBuffer.wrap(uvBufferObject));
        gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, uvBufferObject[0]);
        {
            FloatBuffer buffer = GLBuffers.newDirectFloatBuffer(uvData);
            gl2.glBufferData(GL2.GL_ARRAY_BUFFER, uvData.length * 4, buffer, GL2.GL_STATIC_DRAW);
        }
        gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);

    }


}
