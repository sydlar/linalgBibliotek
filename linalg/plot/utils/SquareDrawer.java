package linalg.plot.utils;
import linalg.plot.Plot;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

public class SquareDrawer extends ShapeDrawer {
    private static final double SIZE = 5;

    double size;
    Color color;

    public SquareDrawer(Plot plot, double size, Color color){
        super(plot);
        this.size = size;
        this.color = color;
        this.plot = plot;
    }
    
    public SquareDrawer(Plot plot){this(plot,SIZE,Color.BLACK);}
    public SquareDrawer(Plot plot, double size){this(plot,size,Color.BLACK);}
    public SquareDrawer(Plot plot, Color color){this(plot,SIZE,color);}

    public void draw(double sx,double sy,GraphicsContext g){
        g.setFill(color);
        g.fillRect(sx-0.5*size,sy-0.5*size,size,size);
    }
}
