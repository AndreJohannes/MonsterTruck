package com.studiocinqo.monstertruck.WorldObjects;


import com.studiocinqo.monstertruck.OpenGL.GLRenderer;

import WorldObjects.BarBase;

public class Bar extends BarBase<GLRenderer> {

	public Bar(Auxiliary.Vector2D A, Auxiliary.Vector2D B) {
		super(A, B);
	}

	//private final Paint paint = new Paint();

	@Override
	public void draw(GLRenderer canvas, double offset) {
		//canvas.drawLine((float) (A.X - offset), (float) A.Y, (float) (B.X - offset), (float) B.Y, paint);

	}

}
