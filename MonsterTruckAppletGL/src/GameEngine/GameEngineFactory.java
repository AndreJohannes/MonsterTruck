package GameEngine;

import Graphics.Background;
import Graphics.Landscape;
import Graphics.ScoreBoard;
import OpenGL.GLRenderer;
import XMLParser.XMLVehicleFileReader;
import XMLParser.XMLWorldFileReader;

import java.applet.Applet;

public class GameEngineFactory extends GameEngineFactoryBase<GLRenderer> {


    public GameEngineFactory(Applet applet, GLRenderer renderer) {
        super(new XMLVehicleFileReader(), new XMLWorldFileReader(renderer),
        	new Background(), new Landscape(),
        	new ScoreBoard(renderer));
    }

	@Override
	protected void _draw(GLRenderer graphics, double[] state, double[] data) {
		super._draw(graphics, state, data);
		graphics.getSpriteRenderer().setAndSeal();
	}

}


