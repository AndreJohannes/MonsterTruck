package com.studiocinqo.monstertruck.GameEngine;

import com.studiocinqo.monstertruck.Graphics.Background;
import com.studiocinqo.monstertruck.Graphics.Landscape;
import com.studiocinqo.monstertruck.Graphics.ScoreBoard;
import com.studiocinqo.monstertruck.OpenGL.GLRenderer;
import com.studiocinqo.monstertruck.XMLParser.XMLVehicleFileReader;
import com.studiocinqo.monstertruck.XMLParser.XMLWorldFileReader;

import android.content.Context;

import GameEngine.GameEngineFactoryBase;
import GameEngine.Solver.SolutionContainer;

public class GameEngineFactory extends GameEngineFactoryBase<GLRenderer> {

    private static GameEngineFactory ourInstance = null;

    public static GameEngineFactory getInstance(Context context,
	    GLRenderer renderer) {
	if (ourInstance == null)
	    ourInstance = new GameEngineFactory(context, renderer);
	return ourInstance;
    }

    public static GameEngineFactory getInstance() {
	if (ourInstance == null)
	    throw new RuntimeException("Need to make an instance first");
	return ourInstance;
    }

    private GameEngineFactory(Context context, GLRenderer renderer) {
	super(new XMLVehicleFileReader(context),
		new XMLWorldFileReader(context.getAssets(), renderer),
		new Background(context), new Landscape(),
		new ScoreBoard(renderer));
    }

    @Override
    protected void _draw(GLRenderer graphics, double[] state, double[] data) {
	super._draw(graphics, state, data);
	graphics.getSpriteRenderer().setAndSealBuffers();
    }

}
