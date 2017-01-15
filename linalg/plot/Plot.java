package linalg.plot;

import linalg.Matrices;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.shape.ArcType;

import java.util.LinkedList;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

public class Plot extends Canvas {

    private static boolean serverDown = true;

    private static void provideServer(){
        if (serverDown)
            new Thread(() -> javafx.application.Application.launch(PlotApp.class)).start();
        serverDown = false;
    }

    public static void main(String[] args){
        Plot plot = new Plot();
        plot.addElement(Matrices.range(-2,6,30),Matrices.range(-1,2,30),Plot.DOTS,Plot.CURVE);
        plot.addElement(Matrices.range(-0,4,50),Matrices.multiply(Matrices.random(31),0.3));
        plot.addElement(Matrices.multiply(Matrices.random(31),1.5),Plot.CURVE);
        plot.addElement(Matrices.range(0,31.4,1000),Math::cos,Plot.CURVE,Plot.DOTS);
        plot.addElement(Matrices.multiply(Matrices.random(30),0.5),Plot.CURVE);
        plot.setPngOutput("plot");

        plot.show();

        Plot plII = new Plot();
        plII.addElement(Matrices.multiply(Matrices.random(30),1.0),Plot.CURVE,Plot.DOTS);
        plII.addElement(Matrices.multiply(Matrices.random(30),0.1),Plot.CURVE);
        plII.setTitle("TITTELEN");
        plII.setPngOutput("plII");

        plII.addElement((double x) -> Math.sin(0.3*x) + Math.cos(0.1*x)+2*Math.cos(5*x),1000);

        plII.show();
    }

    public static final DrawFunction CURVE = (Plot p) -> p.CURVE_DRAWER;
    public static final DrawFunction DOTS = (Plot p) -> p.DOT_DRAWER;


    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 800;
    private static final String DEFAULT_TITLE = "Plot";

    private static final int MARGIN = 20;
    private static final int TOP = MARGIN;
    private static final int BOTTOM = MARGIN+20;
    private static final int LEFT = MARGIN+60;
    private static final int RIGHT = MARGIN;

    private static final double TICK_WIDTH = 1.0;
    private static final double TICK_LEN = 5.0;
    private static final double BORDER_WIDTH = 1.0;
    private static final double CURVE_WIDTH = 1.0;

    public final DataDrawer CURVE_DRAWER = new LineDrawer();
    public final DataDrawer DOT_DRAWER = new DotDrawer();

    private double width;
    private double height;
    private double top = TOP;
    private double bottom = BOTTOM;
    private double left = LEFT;
    private double right = RIGHT;

    private double[] min = new double[]{-1.0,-1.0};
    private double[] max = new double[]{2.0,2.0};

    private boolean variableRange = true;
    private boolean defaultRange = true;

    private GraphicsContext gc;

    private String name = null;
    private String title = null;

    private LinkedList<Line> lines = new LinkedList<>();
    
    public Plot(){
        this(DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }

    public Plot(int width, int height){
        super(width,height);
        this.width = width;
        this.height = height;
        this.gc = this.getGraphicsContext2D();
    }


    public void show(String title){
        this.drawFigure();
        provideServer();
        PlotApp.show(this);
    }

    public void show(){
        show(DEFAULT_TITLE);
    }

    public void drawFigure() {
        drawBorder();
        drawAxes();
        for(Line line: lines) line.draw();
    }

    public String name(){
        return name;
    }
    public void setPngOutput(String theName){
        name = theName;
    }
    
    public String title(){
        return title;
    }

    public void setTitle(String theTitle){
        title = theTitle;
    }
    
    public void setRange(double xmin, double ymin, double xmax, double ymax){
        min[0] = xmin;
        min[1] = ymin;
        max[0] = xmax;
        max[1] = ymax;
        variableRange = false;
        defaultRange = false;
    }

    private void setRangeInternal(double xmin, double ymin, double xmax, double ymax){
        double A = -0.03;
        double B = 1-A;
        min[0] = B*xmin+A*xmax;
        min[1] = B*ymin+A*ymax;
        max[0] = B*xmax+A*xmin;
        max[1] = B*ymax+A*xmin;
        defaultRange = false;
    }
    
    private void extendRangeInternal(double xmin, double ymin, double xmax, double ymax){
        setRangeInternal(Math.min(xmin,min[0]), Math.min(ymin,min[1]), Math.max(xmax,max[0]), Math.max(ymax,max[1]));
    }

    public void unsetRange(){
        variableRange = true;
    }

    private void adjustRange(Line line){
        if (defaultRange)
           setRangeInternal(line.xMin(),line.yMin(),line.xMax(),line.yMax());
        else if (variableRange) 
           extendRangeInternal(line.xMin(),line.yMin(),line.xMax(),line.yMax());
    }

    public void adjustRanges(){
        unsetRange();
        for(Line line: lines)
            adjustRange(line);
    }

    /*
     * Add lines
     **********************/
    public void addElement(double[] xs, double[] ys,DrawFunction... drawers){
        Line line = new Line(xs,ys);
        adjustRange(line);
        if (drawers.length == 0) line.useDrawer(DOT_DRAWER);
        for(Function<Plot,DataDrawer> d: drawers) line.addDrawer(d.apply(this));
        lines.add(line);
    }


    public void addElement(double[] ys,DrawFunction... drawers){
        double[] xs = Matrices.range(0,ys.length,ys.length);
        addElement(xs,ys,drawers);
    }

    public void addElement(double[] xs, DoubleUnaryOperator f,DrawFunction... drawers){
        addElement(xs,Matrices.applyTo(xs,f),drawers);
    }
    
    public void addElement(double[] ts, DoubleUnaryOperator x, DoubleUnaryOperator y,DrawFunction... drawers){
        if(drawers.length != 0)
            addElement(Matrices.applyTo(ts,x),Matrices.applyTo(ts,y),drawers);
        else
            addElement(Matrices.applyTo(ts,x),Matrices.applyTo(ts,y),CURVE);
    }

    public void addElement(DoubleUnaryOperator x, DoubleUnaryOperator y, double t0, double t1, int n, DrawFunction... drawers){
        double[] ts = Matrices.range(t0,t1,n);
        addElement(ts,x,y,drawers);
    }

    public void addElement(DoubleUnaryOperator x, DoubleUnaryOperator y, double t0, double t1, DrawFunction... drawers){
        addElement(x,y,t0,t1,100,drawers);
    }

    public void addElement(DoubleUnaryOperator f, int sampleSize){
        FunctionLine line = new FunctionLine(f);
        line.setSampleSize(sampleSize);
        adjustRange(line);
        line.useDrawer(CURVE_DRAWER);
        lines.add(line);
    }

    public void addElement(DoubleUnaryOperator f){
        addElement(f,500);
    }

    /*
     * Border
     *************************/
    private void drawBorder(){
        gc.setLineWidth(BORDER_WIDTH);
        gc.setStroke(Color.BLACK);
        double xMin = n2s(0.0,0);
        double yMin = n2s(0.0,1);
        double xMax = n2s(1.0,0);
        double yMax = n2s(1.0,1);
        gc.strokeRect(xMin,yMax,xMax-xMin,yMin-yMax);
    }

    /*
     * AXES
     ******************************/
    private void xTick(double mathX){
       double size = 12;
       double len = TICK_LEN;
       double sx = m2s(mathX,0);
       double sy = height-bottom;
       String txt = String.format("%1.1E",mathX);
       gc.setTextAlign(TextAlignment.CENTER);
       gc.fillText(txt,sx,sy+size+len+3); 
       gc.strokeLine(sx,sy,sx,sy+len);
    }
    
    private void yTick(double mathY){
       double size = 12;
       double len = TICK_LEN;
       double sy = m2s(mathY,1);
       double sx = left;
       String txt = String.format("%1.1E",mathY);
       gc.setTextAlign(TextAlignment.RIGHT);
       gc.fillText(txt,sx-len-3,sy+0.5*size); 
       gc.setLineWidth(TICK_WIDTH);
       gc.strokeLine(sx,sy,sx-len,sy);
    }

    private void tick(double val, int var){
        if(var ==0)
            xTick(val);
        else
            yTick(val);
    }

    private void drawAxisLine(int var){
        if(var == 0)
            gc.strokeLine(m2s(0.0,0),height-bottom,m2s(0.0,0),top);
        else
            gc.strokeLine(width-right,m2s(0.0,1),left,m2s(0.0,1));


    }

    private double calculateStep(double min, double max,int N){
        double rangeSize = max-min;
        int numberOfDigits = (int)Math.floor(Math.log10(rangeSize));
        int eksp = -numberOfDigits+1;
        return Math.floor((max-min)/N*Math.pow(10,eksp))*Math.pow(10,-eksp);
    }

    private void drawAxes(){
        int[] N = new int[]{5,5};
        double[] step = new double[2];
        double[] start = new double[2];
        for(int i = 0; i < 2; i++){
            step[i] = calculateStep(min[i],max[i],N[i]);
            start[i] = min[i];
            if(max[i] > 0 && min[i] < 0){
                start[i] = Math.floor(start[i]/step[i])*step[i];
                drawAxisLine(i);
            }
            for (int j = 1; j <= N[i]; j++){
                tick(start[i]+j*step[i],i);
            }

        }
    }

    /*
     * Plot dataset
     *****************************/
    private class Line {
        LinkedList<DataDrawer> drawers;
        double[] xs = null;
        double[] ys = null;
        
        public double xMax, yMax, xMin, yMin;
        
        public Line(){
            drawers = new LinkedList<DataDrawer>();
        }

        public Line(double[] xs, double[] ys){
            this();
            this.xs = xs;
            this.ys = ys;
            xMax = max(xs);
            yMax = max(ys);
            xMin = min(xs);
            yMin = min(ys);
        }

        public void draw(){
            for(DataDrawer d: drawers){
                d.put(xs,ys);
            }
        }

        public void useDrawer(DataDrawer d){
            drawers.clear();
            drawers.add(d);
        }
        public void addDrawer(DataDrawer d){
            drawers.add(d);
        }

        public double xMax(){return xMax;}
        public double yMax(){return yMax;}
        public double xMin(){return xMin;}
        public double yMin(){return yMin;}

    }

    private class FunctionLine extends Line  {
        DoubleUnaryOperator f;
        int sampleSize = 500;

        public FunctionLine(DoubleUnaryOperator theF){
            super();
            f = theF;
        }

        private void provideData(){
            if(super.xs != null &&  super.xs[0] == min[0] && super.xs[xs.length-1] == max[0])
                return;

            super.xs = Matrices.range(min[0],max[0],sampleSize);
            super.ys = Matrices.applyTo(xs,f);
        }

        public void draw(){
            this.provideData();
            super.draw();
        }

        public double xMax(){return max[0];}
        public double xMin(){return min[0];}
        public double yMin(){this.provideData(); return min(super.ys);}
        public double yMax(){this.provideData(); return max(super.ys);}


        public void setSampleSize(int n){
            sampleSize = n;
            super.xs = null;
        }


    }


    /*
     * Different line styles
     */

    public interface DrawFunction extends Function<Plot,Plot.DataDrawer>{} 

    public static abstract class DataDrawer {
        abstract void put(double[] x, double[] y);
    }

    private class LineDrawer extends DataDrawer {

        void put(double[] x, double[] y){
            int n = Math.min(x.length,y.length);
            if (n == 0)
                return;

            gc.setLineWidth(1.0);
            gc.setStroke(Color.BLACK);

            double currentX = m2s(x[0],0);
            double currentY = m2s(y[0],1);
            for(int i = 1; i < n; i++){
                double nextX = m2s(x[i],0);
                double nextY = m2s(y[i],1);
                if (isInside(x[i-1],y[i-1]) && isInside(x[i],y[i]))
                    gc.strokeLine(currentX,currentY,nextX,nextY);
                currentX = nextX;
                currentY = nextY;
            }
        }
    }

    private abstract class ShapeDrawer extends DataDrawer {

        abstract void draw(double screenX, double screenY);

        void put(double mathX, double mathY){
            double sx = m2s(mathX,0);
            double sy = m2s(mathY,1);
           if (isInside(mathX,mathY))
                draw(sx,sy);
        }

        void put(double[] x, double[] y){
            int n = Math.min(x.length,y.length);
            for(int i = 0; i < n ; i++){
                put(x[i],y[i]);
            }
        }

    }


    private class DotDrawer extends ShapeDrawer {
        private static final double SIZE = 3.5;

        double size;
        Color color;

        DotDrawer(double size, Color color){
            this.size = size;
            this.color = color;
        }
        
        DotDrawer(){this(SIZE,Color.BLACK);}
        DotDrawer(double size){this(size,Color.BLACK);}
        DotDrawer(Color color){this(SIZE,color);}

        public void draw(double sx,double sy){
            gc.setFill(color);
            gc.fillOval(sx-0.5*size,sy-0.5*size,size,size);
        }
    }


    /*
     * Helper functions
     * *****************************************/

    private boolean isInside(double val, int var){
        final double EPS = 1e-8;
        return val > min[var]-EPS && val < max[var] + EPS;
    }

    private boolean isInside(double x, double y){
        return isInside(x,0) && isInside(y,1);
    }
        
    private double m2n(double val, int var){
        double scale = 1.0/(max[var]-min[var]);
        return (val-min[var])*scale;
    }

    private double n2s(double val,int var){
        if (var == 0)
            return (width-right)*val+(1-val)*left;
        else if (var == 1)
            return top*val+(height-bottom)*(1-val);
        else 
            throw new ArrayIndexOutOfBoundsException("norm2screen: invalid variable id");
    }

    private double m2s(double val, int var){
        return n2s(m2n(val,var),var);
    }


    /*
     * Static helper functions
     * *************************************/
    private double min(double[] array){
        if (array.length == 0)
            return 0.0;

        double val = array[0];
        for(double d : array){
            if(d < val)
                val = d;
        }
        return val;
    }

    private double max(double[] array){
        if (array.length == 0)
            return 0.0;

        double val = array[0];
        for(double d : array){
            if(d > val)
                val = d;
        }
        return val;
    }


    /*
     * Accesssors
     ***********************/
    //public double getWidth(){return width;}
    //public double getHeight(){return height;}

}
