package Auxiliary;

public class EWorldObjects {

    public static enum TokenTypes {
	Coin;
    }

    public static enum DynamicTypes {
	Cylinder, Bridge, Block, Pendulum, DoublePendulum;
    }

    public static enum StaticTypes {
	Bar, Point, Arc;
    }

    public static enum NoninteractTypes {
	Foregound;
    }

    public static enum VehiclePartTypes {
	Bodywork, FrontBumper, RearBumper, FrontWheel, RearWheel;
    }

    public static enum Category {
	Static, Dynamic, Token, Vehicle;
    }

}
