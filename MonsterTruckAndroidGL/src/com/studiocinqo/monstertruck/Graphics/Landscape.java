package com.studiocinqo.monstertruck.Graphics;

import com.studiocinqo.monstertruck.OpenGL.GLRenderer;

import Graphics.IBackground;

public class Landscape implements IBackground<GLRenderer> {

    public Landscape() {
	// TODO Auto-generated constructor stub
    }

    @Override
    public void draw(GLRenderer renderer, double offset) {
	renderer.getLandscape().setOffset(offset/4);
    }

}
