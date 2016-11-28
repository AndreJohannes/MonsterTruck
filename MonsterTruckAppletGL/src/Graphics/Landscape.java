package Graphics;

import OpenGL.GLRenderer;

import java.awt.*;

public class Landscape implements IBackground<GLRenderer> {

    private Image image;

    public Landscape() {
    }

    @Override
    public void draw(GLRenderer renderer, double offset) {
        renderer.getLandscape().setOffset(offset/4);
    }

}
