package linalg.plot.utils;

class Utils {

     static double min(double[] array){
        if (array.length == 0)
            return 0.0;

        double val = array[0];
        for(double d : array){
            if(d < val)
                val = d;
        }
        return val;
    }

    static double max(double[] array){
        if (array.length == 0)
            return 0.0;

        double val = array[0];
        for(double d : array){
            if(d > val)
                val = d;
        }
        return val;
    }
}
