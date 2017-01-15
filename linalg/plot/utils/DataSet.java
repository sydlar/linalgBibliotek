package linalg.plot.utils;
import linalg.plot.Plot;
import java.util.LinkedList;

import javafx.scene.paint.Color;


public class DataSet {

    private static int hue = 240;
    private static int hue(){return (hue = (hue + 210) & 360);}

    Color color;
    
    LinkedList<Drawer> drawers;
    double[] xs = null;
    double[] ys = null;
    
    public double xMax, yMax, xMin, yMin;
    
    public DataSet(Color color){
        this.drawers = new LinkedList<>();
        this.color = color;
    }

    public DataSet(){this(null);}

    public DataSet(double[] xs, double[] ys){
        this();
        this.xs = xs;
        this.ys = ys;
        xMax = Utils.max(xs);
        yMax = Utils.max(ys);
        xMin = Utils.min(xs);
        yMin = Utils.min(ys);
    }

    public void draw(Plot p){

        if(color== null) p.setColor(Color.hsb(hue(),1.0,0.7));
        else p.setColor(color);

        p.setLineWidth(1.0);
        for(Drawer d: drawers){
            d.draw(xs,ys,p);
        }
    }

    public void useDrawer(Drawer d){
        drawers.clear();
        drawers.add(d);
    }
    public void addDrawer(Drawer d){
        drawers.add(d);
    }

    public double xMax(){return xMax;}
    public double yMax(){return yMax;}
    public double xMin(){return xMin;}
    public double yMin(){return yMin;}
}
