package linalg.util;

public class Util {
	public static final double EPS = 1e-11;


    boolean isZero(double x){
        return Math.abs(x) < EPS;
    }

    boolean isZero(double[] array){
        for(int i = 0; i < array.length; i++)
            if(!isZero(array[i]))
                return false;
        return true;
    }

    boolean isZero(double[][] array){
        for(int i = 0; i < array.length; i++)
            if(!isZero(array[i]))
                return false;
        return true;
    }

    boolean equals(double x, double y){
        return isZero(x-y);
    }

    void trimZeros(double[] array){
        for(int i = 0; i < array.length; i++){
            if(isZero(array[i]))
                array[i] = 0.0;
        }
    }

    void trimZeros(double[][] array){
        for(int i = 0; i < array.length; i++){
            trimZeros(array[i]);
        }
    }
}
