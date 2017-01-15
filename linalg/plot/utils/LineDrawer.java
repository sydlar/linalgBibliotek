package linalg.plot.utils;
import linalg.plot.Plot;


public class LineDrawer extends Drawer {


    void draw(double[] x, double[] y,Plot plot){
        int n = Math.min(x.length,y.length);
        if (n == 0) return;
        for(int i = 1; i < n; i++) plot.drawLine(x[i-1],y[i-1],x[i],y[i]);
    }
}
