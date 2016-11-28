package OpenGL;

import Auxiliary.Vector2D;
import TextureMap.TextureMap;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.GLBuffers;
import glutil.MatrixStack;
import jglm.Vec3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

public class IntegerRenderer implements IRenderer {



    private GLRenderer.ShaderAndTexture shaderAndTexture;
    private int[] vertexBufferObject = new int[1];
    private int[] indexBufferObject = new int[1];
    private int[] uvBufferObject = new int[1];
    private AtomicReference<float[]> modelMatrixFloatArray = new AtomicReference<float[]>();

    public void init(GL2 gl2, GLRenderer.ShaderAndTexture shaderAndTexture) {
        setPositionAndOrientation(0, 720, 0);

        setInteger(122);
        initializeVertexBuffer(gl2, vertices);
        initializeIndexBuffer(gl2, indices);
        initializeUVBuffer(gl2, uvs);
        this.shaderAndTexture = shaderAndTexture;
    }

    public void Render(GL2 gl2) {

        //initializeVertexBuffer(gl2, vertices);
        //initializeIndexBuffer(gl2, indices);
        //initializeUVBuffer(gl2, uvs);

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
                gl2.glDrawElements(GL2.GL_TRIANGLES, this.indices.length, GL2.GL_UNSIGNED_INT, 0);
                //gl2.glDrawArrays(GL2.GL_QUADS, 0, 12);

                this.shaderAndTexture.texture.disable(gl2);
            }
            gl2.glDisableVertexAttribArray(0);
            gl2.glDisableVertexAttribArray(1);
        }
        this.shaderAndTexture.programObject.unbind(gl2);
    }

    public void setPositionAndOrientation(double dx, double dy, double deg) {
        MatrixStack model = new MatrixStack();
        //
        model.scale(new Vec3(2f / 1280f, 2f / 720f, 1f));
        model.translate(new Vec3((float) (-640f + dx), (float) (360f - dy), 0));
        model.rotateZ((float) (deg));

        modelMatrixFloatArray.set(model.top().toFloatArray());
    }


    public LinkedList<float[]> stack;
    private float[] uvs;
    private float[] vertices;
    private int[] indices;
    private int[] indicesTemplate = new int[]{0, 1, 2, 0, 2, 3};
    private final Dictionary<Integer, float[]> dictUVS;
    private final Vector2D.Rect textureRect;
    private Vector2D.Rect rect;
    private final float offset;
    private final TextureMap.Texture texture;
    private float dx = 0, dy = 0;

    public IntegerRenderer() {
        offset = 8;
        texture = new TextureMap.Texture(0, 1440, 2048);
        rect = new Vector2D.Rect(0, 0, 15, 22);
        textureRect = new Vector2D.Rect(735, 180, 743, 192);
        //textureRect = new Vector2D.Rect(500, 180, 600, 360);
        dictUVS = makeUVSDictionary();
    }

    //public int[] getIndices() {
    //    return indices;
    //}

    public TextureMap.Texture getTexture() {
        return texture;
    }

    //public float[] getUVS() {
    //    return uvs;
    //}

    //public float[] getVertices() {
    //    return vertices;
    //}

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

    private int addElements(float[] array, int index, float[] inArray) {
        for (float value : inArray) {
            array[index++] = value;
        }
        return index;
    }

    private float[] getUVS(int offsetIndex, float offset) {
        float dx = offset * offsetIndex+8;
        return new float[]{
                (textureRect.x1 + 0.5f + dx) / texture.width,
                1f-(textureRect.y2 - 0.5f) / texture.height,
                (textureRect.x1 + 0.5f + dx) / texture.width,
                1f-(textureRect.y1 + 0.5f) / texture.height,
                (textureRect.x2 - 0.5f + dx) / texture.width,
                1f-(textureRect.y1 + 0.5f) / texture.height,
                (textureRect.x2 - 0.5f + dx) / texture.width,
                1f-(textureRect.y2 - 0.5f) / texture.height};

    }

    private float[] getVertices(float dx) {
        return new float[]{(float) (rect.x1 + dx + this.dx),
                (float) (-rect.y2 + this.dy), 0.0f,
                (float) (rect.x1 + this.dx + dx), (float) (-rect.y1 + this.dy),
                0.0f, (float) (rect.x2 + this.dx + dx),
                (float) (-rect.y1 + this.dy), 0.0f,
                (float) (rect.x2 + this.dx + dx), (float) (-rect.y2 + this.dy),
                0.0f};
    }

    private void makeArrays() {
        int size = stack.size();
        this.uvs = new float[8 * size];
        int indexForUVS = 0;
        this.vertices = new float[12 * size];
        int indexForVertices = 0;
        this.indices = new int[6 * size];
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
