package WorldObjects;

import java.awt.Graphics;

import Auxiliary.Vector2D;
import Auxiliary.CollisionObjectBase;
import Auxiliary.EWorldObjects.Category;
import Auxiliary.IArcImage.Image;
import Graphics.ArcImage;
import Graphics.GameColors;
import Graphics.InverseArcImage;

public class Arc extends ArcBase<Graphics> {

    private final Image image;
    private final int offsetX, offsetY;
	
	public Arc(Vector2D center, double radius, double startAngle, double arcAngle) {
		super(center, radius, startAngle, arcAngle);
		this.image = getImage(radius, startAngle, arcAngle);
		this.offsetX = image != null ? image.getOffsetHorizontal() : 0;
		this.offsetY = image != null ? image.getOffsetVertical() : 0;
	}

	@Override
	public void draw(Graphics gr, double offset) {
		if (image != null) {
		    gr.drawImage(image, (int) (center.X - offset + offsetX),
			    (int) (center.Y + offsetY), null);
		} else {
		    gr.setColor(GameColors.GREY_DARKER);
		    gr.drawArc((int) (center.X - offset - radius),
			    (int) (center.Y - radius), (int) (2 * radius),
			    (int) (2 * radius), (int) startAngle, (int) (arcAngle));
		    gr.fillArc((int) (center.X - offset - radius),
			    (int) (center.Y - radius), (int) (2 * radius),
			    (int) (2 * radius), (int) startAngle, (int) (arcAngle));
		}
	}
	
	 private Image getImage(double radius, double startAngle, double arcAngle) {
			return inverse
				? InverseArcImage.getInstance().getImage(radius, startAngle,
					arcAngle)
				: ArcImage.getInstance().getImage(radius, startAngle, arcAngle);
		    }

	

}
