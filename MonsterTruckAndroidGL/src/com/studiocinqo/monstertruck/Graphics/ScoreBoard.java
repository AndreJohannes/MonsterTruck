package com.studiocinqo.monstertruck.Graphics;

import com.studiocinqo.monstertruck.OpenGL.CollectiveSpriteRenderer;
import com.studiocinqo.monstertruck.OpenGL.GLRenderer;
import com.studiocinqo.monstertruck.OpenGL.IntegerRenderer;

import TextureMap.TextureMap;
import TextureMap.TextureMap.Texture;
import Auxiliary.Vector2D.Rect;
import Graphics.IScoreBoard;

public class ScoreBoard implements IScoreBoard<GLRenderer> {

    private static int positionScoreX = 25;
    private static int positionScoreY = 33;
    private static int positionHealthX = 25;
    private static int positionHealthY = 75;
    private static int heightHealth = 10;
    private static int widthHealth = 100;

    private final CollectiveSpriteRenderer.SpriteForRenderer healthSprite;
    private final CollectiveSpriteRenderer.SpriteForRenderer scoreSprite;
    private final IntegerRenderer scoreRenderer;

    public ScoreBoard(GLRenderer renderer) {
	TextureMap textureMap = renderer.getTextureMap();
	healthSprite = new CollectiveSpriteRenderer.SpriteForRenderer(
		textureMap.texture, new Rect((float) 0, (float) 0,
			(float) (widthHealth), (float) (heightHealth)),
		textureMap.healthBar);
	scoreSprite = new CollectiveSpriteRenderer.SpriteForRenderer(
		textureMap.texture,
		new Rect((float) 0, (float) 0, (float) (80), (float) (22)),
		textureMap.scoreBoard.text);
	scoreRenderer = new IntegerRenderer();
    }

    @Override
    public void draw(GLRenderer renderer, int score, double health) {

	float dx = (1f - (float) health) * 100f;
	healthSprite.setPosition(renderer, positionHealthX, positionHealthY);
	healthSprite.setUVS(500f + dx, 180f, 600f + dx, 280f);
	renderer.getSpriteRenderer().registerSprite(healthSprite);

	scoreRenderer.setPosition(renderer, 120, positionScoreY);
	scoreRenderer.setInteger(122);

	scoreSprite.setPosition(renderer, positionScoreX, positionScoreY);
	renderer.getSpriteRenderer().registerSprite(scoreSprite);
	renderer.getSpriteRenderer().registerSprite(scoreRenderer);
    }

}
