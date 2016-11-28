package TextureMap;

import java.util.HashMap;

import Auxiliary.Vector2D.Rect;

public class TextureMap {

    public static class Landscape {
        public final Rect area;
        public final double scale;

        public Landscape(Rect area, double scale) {
            this.area = area;
            this.scale = scale;
        }
    }

    public static class Vehicle {
        public final Rect bodywork;
        public final Rect wheel;

        public Vehicle(Rect bodywork, Rect wheel) {
            this.bodywork = bodywork;
            this.wheel = wheel;
        }
    }

    public static class ScoreBoard {
        public final Rect text;
        public final Rect digits;

        public ScoreBoard(Rect text, Rect digits) {
            this.text = text;
            this.digits = digits;
        }

    }

    public static class Texture {

        public final int lot;
        public final int height;
        public final int width;

        public Texture(int lot, int height, int width) {
            this.lot = lot;
            this.height = height;
            this.width = width;
        }
    }

    public final Texture texture;
    public final Rect background;
    public final Landscape landscape;
    public final Rect healthBar;
    public final ScoreBoard scoreBoard;
    public final HashMap<String, Rect> textureByName;
    public final HashMap<String, Vehicle> vehicleByName;

    public TextureMap() {
        // TODO: the field values as defined below should be populated via an
        // xml reader
        this.texture = new Texture(0, 1440, 2048);
        this.background = new Rect(0f, 180f, 320f, 360f);
        this.landscape = new Landscape(new Rect(0f, 0f, 320f, 180f), 4.0d);
        this.healthBar = new Rect(500f, 180f, 600f, 280f);
        this.scoreBoard = new ScoreBoard(new Rect(700f, 180f, 740f, 192f),
                null);
        this.textureByName = new HashMap<String, Rect>();
        this.vehicleByName = new HashMap<String, Vehicle>();
        textureByName.put("mainbridge", new Rect(850, 180, 1350, 355));
        textureByName.put("leftbridge", new Rect(1400, 180, 1600, 355));
        textureByName.put("rightbridge", new Rect(1600, 180, 1400, 355));
        textureByName.put("pillar", new Rect(325f, 39f, 330f, 133f));
        textureByName.put("tomb", new Rect(950f, 180f, 980f, 230f));
        textureByName.put(null, new Rect(0, 0, 2048, 1440));
        //textureByName.put("cludgel", new Rect());
        //textureByName.put("beam", new Rect());
        vehicleByName.put("niva", new Vehicle(new Rect(200f, 360f, 396f, 431f),
                new Rect(200f, 450f, 253f, 503f)));
    }

}
