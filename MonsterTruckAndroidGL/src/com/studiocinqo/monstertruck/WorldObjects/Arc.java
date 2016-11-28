package com.studiocinqo.monstertruck.WorldObjects;

import com.studiocinqo.monstertruck.OpenGL.GLRenderer;

import WorldObjects.ArcBase;

public class Arc extends ArcBase<GLRenderer> {

    // private final Paint paint = new Paint();

    public Arc(Auxiliary.Vector2D center, double radius, double startAngle,
	    double arcAngle) {
	super(center, radius, startAngle, arcAngle);
    }

    public void draw(GLRenderer canvas, double offset) {
	// RectF rect = new RectF((float) (center.X - offset - radius), (float)
	// (center.Y - radius),
	// (float) (center.X - offset + radius), (float) (center.Y + radius));
	// canvas.drawArc(rect, (float) (360.d - startAngle), (float) -arcAngle,
	// false, (Paint) paint);
    }

}
