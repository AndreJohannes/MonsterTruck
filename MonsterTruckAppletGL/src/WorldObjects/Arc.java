package WorldObjects;


import Auxiliary.Vector2D;
import OpenGL.GLRenderer;

public class Arc extends ArcBase<GLRenderer> {

	
	public Arc(Vector2D center, double radius, double startAngle, double arcAngle) {
		super(center, radius, startAngle, arcAngle);
	}

	@Override
	public void draw(GLRenderer gr, double offset) {
	}
	

}
