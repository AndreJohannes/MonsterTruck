package com.studiocinqo.monstertruck.WorldObjects;

import com.studiocinqo.monstertruck.OpenGL.GLRenderer;

import Auxiliary.Vector2D;
import WorldObjects.CoinBase;

public class Coin extends CoinBase<GLRenderer> {

    public Coin(GLRenderer renderer, String name, Vector2D center) {
	super(center);
    }

    @Override
    public void draw(GLRenderer canvas, double offset) {
	// TODO Auto-generated method stub

    }

}
