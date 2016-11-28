package Graphics;

import Auxiliary.Vector2D;
import OpenGL.CollectiveSpriteRenderer;
import OpenGL.GLRenderer;
import OpenGL.IntegerRenderer;


public class ScoreBoard implements IScoreBoard<GLRenderer> {

    private static int positionScoreX = 25;
    private static int positionScoreY = 33;
    private static int positionHealthX = 25;
    private static int positionHealthY = 75;
    private static int heightHealth = 10;
    private static int widthHealth = 100;

    private final CollectiveSpriteRenderer.CSpriteRenderer healthSprite;
    private final CollectiveSpriteRenderer.CSpriteRenderer scoreSprite;
    private final IntegerRenderer scoreRenderer;

    public ScoreBoard(GLRenderer renderer) {
        TextureMap.TextureMap textureMap = renderer.getTextureMap();
        healthSprite = new CollectiveSpriteRenderer.CSpriteRenderer(
                textureMap.texture, new Vector2D.Rect((float) 0, (float) 0,
                (float) (widthHealth), (float) (heightHealth)),
                textureMap.healthBar);
        scoreSprite = new CollectiveSpriteRenderer.CSpriteRenderer(
                textureMap.texture,
                new Vector2D.Rect((float) 0, (float) -22, (float) (80), (float) (0)),
                textureMap.scoreBoard.text);
        scoreRenderer = new IntegerRenderer();
        CollectiveSpriteRenderer.getInstance().register(scoreRenderer);
    }


    @Override
    public void draw(GLRenderer renderer, int score, double health) {
        float dx = (1f - (float) health) * 100f;
        healthSprite.setPositionAndOrientation(positionHealthX, positionHealthY, 0);
        //healthSprite.setUVS(500f + dx, 180f, 600f + dx, 280f);
        renderer.getSpriteRenderer().queueRenderer(healthSprite);

        scoreRenderer.setPositionAndOrientation(120, positionScoreY,0);
        scoreRenderer.setInteger(122);

        scoreSprite.setPositionAndOrientation(positionScoreX, positionScoreY, 0);
        renderer.getSpriteRenderer().queueRenderer(scoreSprite);
        renderer.getSpriteRenderer().queueRenderer(scoreRenderer);

    }

}
