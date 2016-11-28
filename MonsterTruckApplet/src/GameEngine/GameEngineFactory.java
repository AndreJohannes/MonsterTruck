package GameEngine;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import Graphics.Background;
import Graphics.Landscape;
import Graphics.ScoreBoard;
import Graphics.TextureAtlas;
import VehicleObjects.Vehicle;
import XMLParser.XMLVehicleFileReader;
import XMLParser.XMLWorldFileReader;

public class GameEngineFactory extends GameEngineFactoryBase<Graphics> {

    private final BufferStrategy bufferStrategy;

    public GameEngineFactory(Applet applet, BufferStrategy bufferStrategy,
	    TextureAtlas textureAtlas) {
	super(new XMLVehicleFileReader(textureAtlas), new XMLWorldFileReader(textureAtlas),
		new Background(textureAtlas), new Landscape(textureAtlas),
		new ScoreBoard());
	this.bufferStrategy = bufferStrategy;
    }

    @Override
    protected void _draw(Graphics graphics, double[] state, double[] data) {
	super._draw(graphics, state, data);
	if (!bufferStrategy.contentsLost())
	    bufferStrategy.show();
    }

}
