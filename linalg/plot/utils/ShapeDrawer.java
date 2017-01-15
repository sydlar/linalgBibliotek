package linalg.plot.utils;
import linalg.plot.Plot;
import linalg.plot.utils.shapes.Shape;
import linalg.plot.utils.shapes.Dot;

import javafx.scene.canvas.GraphicsContext;

public class ShapeDrawer extends Drawer {

    Shape shape;
    
    public ShapeDrawer(){
        this(new Dot());
    }

    public ShapeDrawer(Shape shape){
        this.shape = shape;
    }

    void draw(double[] x, double[] y,Plot plot){
        int n = Math.min(x.length,y.length);
        for(int i = 0; i < n ; i++){
            plot.draw(x[i],y[i],shape);
        }
    }

}
