package OpenGL;


import com.jogamp.opengl.GL2;

public interface IRenderer {

    void Render(GL2 gl2);

    void init(GL2 gl2, GLRenderer.ShaderAndTexture shaderAndTexture);

}
