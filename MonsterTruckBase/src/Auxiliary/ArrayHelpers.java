package Auxiliary;

import java.util.List;

public class ArrayHelpers {

    public static void ArrayCopyDoubles(double[] src, double[] dest) {
	if (src.length != dest.length)
	    throw new RuntimeException("Arrays dont have the same length");
	for (int i = 0; i < src.length; i++) {
	    dest[i] = src[i];
	}
    }

    public static float[] toFloatArray(List<Float> list) {
	float[] retArray = new float[list.size()];
	int i = 0;
	for (Float value : list) {
	    retArray[i++] = value;
	}
	;
	return retArray;
    }

    public static short[] toShortArray(List<Short> list) {
   	short[] retArray = new short[list.size()];
   	int i = 0;
   	for (Short value : list) {
   	    retArray[i++] = value;
   	}
   	;
   	return retArray;
       }
    
}
