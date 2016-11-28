package WorldObjects;

import Auxiliary.Vector2D;
import OpenGL.GLRenderer;
import OpenGL.MeshRenderer;
import TextureMap.TextureMap;

public class Bridge extends BridgeBase<GLRenderer> {

    private final MeshRenderer mesh;
    private final TextureMap.Texture texture;
    private final float[] uvs;
    private final int[] indices;

    public Bridge(GLRenderer renderer, String name, Vector2D startPoint, Vector2D endPoint, int numberOfLinks,
                  WorldObjects.BridgeBase.Suspension suspension) {
        super(startPoint, endPoint, numberOfLinks, suspension);
        TextureMap textureMap = renderer.getTextureMap();
        Vector2D.Rect textureRect = textureMap.textureByName.get(name);
        texture = textureMap.texture;
        indices = makeIndices();
        uvs = makeUVS(texture, textureRect);
        mesh = new MeshRenderer(uvs, indices);
        renderer.getSpriteRenderer().register(mesh);
    }

    @Override
    public void draw(GLRenderer renderer, double[] state, double offsetX) {
        mesh.setVertices(makeVertices(offsetX, state));
        renderer.getSpriteRenderer().queueRenderer(mesh);
    }

    private float[] makeUVS(TextureMap.Texture texture, Vector2D.Rect rect) {
        float[] retUVS = new float[4 * (numberOfLinks + 1)];
        double max = Math.max(suspension.function(0), suspension.function(1));
        for (int i = 0; i <= (numberOfLinks); i++) {
            retUVS[4 * i] = (rect.x1
                    + (rect.x2 - rect.x1) * (i / (float) numberOfLinks))
                    / texture.width;
            retUVS[4 * i + 1] = 1-rect.y2 / texture.height;
            retUVS[4 * i + 2] = (rect.x1
                    + (rect.x2 - rect.x1) * (i / (float) numberOfLinks))
                    / texture.width;
            retUVS[4 * i
                    + 3] = 1-(((float) (rect.y2
                    + ((rect.y1 - rect.y2) / max
                    * suspension.function(
                    i / (double) numberOfLinks)))
                    / texture.height));
        }
        return retUVS;
    }

    private float[] makeVertices(double xOffset, double[] state) {
        float[] retVertices = new float[6 * (numberOfLinks + 1)];
        retVertices[0] = (float) startPoint.X - (float) xOffset;
        retVertices[1] = 720f - (float) startPoint.Y;
        retVertices[2] = 0f;
        retVertices[3] = (float) startPoint.X - (float) xOffset;
        retVertices[4] = 720f - (float) startPoint.Y
                 +(float) suspension.function(0d);
        retVertices[5] = 0f;
        retVertices[6 * numberOfLinks] = (float) endPoint.X - (float) xOffset;
        retVertices[6 * numberOfLinks + 1] = 720f - (float) endPoint.Y;
        retVertices[6 * numberOfLinks + 2] = 0f;
        retVertices[6 * numberOfLinks + 3] = (float) endPoint.X
                - (float) xOffset;
        retVertices[6 * numberOfLinks + 4] = 720f - (float) endPoint.Y
                + (float) suspension.function(1d);
        retVertices[6 * numberOfLinks + 5] = 0f;
        int offset = this.offset;
        for (int i = 1; i < (numberOfLinks); i++) {
            retVertices[6 * i] = (float) state[offset] - (float) xOffset;
            retVertices[6 * i + 1] = 720f - (float) state[offset + 1];
            retVertices[6 * i + 2] = 0f;
            retVertices[6 * i + 3] = (float) state[offset] - (float) xOffset;
            retVertices[6 * i + 4] = 720f - (float) state[offset + 1]
                    + (float) suspension
                    .function(i / (double) (numberOfLinks + 0));
            retVertices[6 * i + 5] = 0f;
            offset += 4;
        }
        return retVertices;
    }
    private int[] makeIndices() {
        int[] retIndices = new int[6 * numberOfLinks];
        for (int i = 0; i < numberOfLinks; i++) {
            retIndices[6 * i] =  (2 * i);
            retIndices[6 * i + 1] =  (2 * i + 1);
            retIndices[6 * i + 2] =  (2 * i + 2);
            retIndices[6 * i + 3] =  (2 * i + 2);
            retIndices[6 * i + 4] =  (2 * i + 1);
            retIndices[6 * i + 5] =  (2 * i + 3);
        }
        return retIndices;
    }


}
