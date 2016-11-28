package VehicleObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Rect;
import Graphics.TextureAtlas;
import VehicleObjects.VehicleBase.Geometry;

public class Vehicle extends VehicleBase<Graphics> {

    private final Image body;
    private final Image wheel;

    public Vehicle(TextureAtlas textureAtlas, Geometry geometry) {
	super(geometry);
	int width = (int) (2 * geometry.geometricCentre.X);
	int height = (int) (2 * geometry.geometricCentre.Y);
	body = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	Rect vehicleRect = textureAtlas.vehicleByName.get("niva").bodywork;
	Graphics2D graphics = ((BufferedImage) body).createGraphics();
	graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	graphics.drawImage(textureAtlas.getTextureImage(), 0, 0, width, height,
		(int) vehicleRect.x1, (int) vehicleRect.y1,
		(int) vehicleRect.x2, (int) vehicleRect.y2, null);
	height = (int)(2*geometry.radiusWheel);
	width =height;
	wheel = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	Rect wheelRect = textureAtlas.vehicleByName.get("niva").wheel;
	graphics = ((BufferedImage) wheel).createGraphics();
	graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	graphics.drawImage(textureAtlas.getTextureImage(), 0, 0, width, height,
		(int) wheelRect.x1, (int) wheelRect.y1,
		(int) wheelRect.x2, (int) wheelRect.y2, null);
    }

    @Override
    public void draw(Graphics graphics, double positionCM_x,
	    double positionCM_y, double angle, double angleFrontWheel,
	    double angleRearWheel, double displacementFrontWheelX,
	    double displacementFrontWheelY, double displacementRearWheelX,
	    double displacementRearWheelY) {
	Vector2D offset = geometry.centerOfMass.add(geometry.geometricCentre);
	AffineTransform trans = new AffineTransform();
	double margin = 100;
	trans.rotate(Math.toRadians(angle), offset.X + margin,
		offset.Y + margin);
	trans.translate((int) (margin), (margin));
	AffineTransformOp affineTransformOp = new AffineTransformOp(trans,
		AffineTransformOp.TYPE_BILINEAR);
	((Graphics2D) graphics).drawImage(
		affineTransformOp.filter((BufferedImage) body, null),
		(int) (positionCM_x - offset.X - margin),
		(int) (positionCM_y - offset.Y - margin), null);

	double radius = geometry.radiusWheel + 0 * 1;
	trans = new AffineTransform();
	trans.translate(positionCM_x + displacementFrontWheelX,
		positionCM_y + displacementFrontWheelY);
	trans.rotate(Math.toRadians(angle));
	trans.translate(geometry.CMtoFrontWheel.X - radius,
		geometry.CMtoFrontWheel.Y - radius);
	trans.rotate(Math.toRadians(-angleFrontWheel), radius, radius);
	((Graphics2D) graphics).drawImage(wheel, trans, null);

	trans = new AffineTransform();
	trans.translate(positionCM_x + displacementRearWheelX,
		positionCM_y + displacementRearWheelY);
	trans.rotate(Math.toRadians(angle));
	trans.translate(geometry.CMtoRearWheel.X - radius,
		geometry.CMtoRearWheel.Y - radius);
	trans.rotate(Math.toRadians(-angleRearWheel), radius, radius);
	((Graphics2D) graphics).drawImage(wheel, trans, null);

    }

}
