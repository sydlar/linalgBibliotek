package linalg.plot.utils;
import linalg.plot.Plot;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color;

public class Axes {
    
    private static final double TICK_WIDTH = 1.0;
    private static final double TICK_LEN = 5.0;
    private static final double BORDER_WIDTH = 2.0;
    private static final double AXIS_WIDTH = 1.0;
    
    Plot plot;
    GraphicsContext g;

    public Axes(Plot thePlot){
        this.plot = thePlot;
        this.g = plot.getCanvas().getGraphicsContext2D();
    }

    public void draw(){
        drawBorder();
        drawAxes();
    }

    /*
     * AXES
     ******************************/
    private void xTick(double mathX){
       double size = 12;
       double len = TICK_LEN;
       double sx = plot.m2s(mathX,0);
       double sy = plot.height()-plot.bottom();
       String txt = String.format("%1.1E",mathX);
       g.setTextAlign(TextAlignment.CENTER);
       g.fillText(txt,sx,sy+size+len+3); 
       g.strokeLine(sx,sy,sx,sy+len);
    }
    
    private void yTick(double mathY){
       double size = 12;
       double len = TICK_LEN;
       double sy = plot.m2s(mathY,1);
       double sx = plot.left();
       String txt = String.format("%1.1E",mathY);
       g.setTextAlign(TextAlignment.RIGHT);
       g.fillText(txt,sx-len-3,sy+0.5*size); 
       g.setLineWidth(TICK_WIDTH);
       g.strokeLine(sx,sy,sx-len,sy);
    }

    private void tick(double val, int i){
        if(i ==0)
            xTick(val);
        else
            yTick(val);
    }

    private void drawAxisLine(int i){
        double width = g.getCanvas().getWidth();
        double height = g.getCanvas().getHeight();

        g.setLineWidth(AXIS_WIDTH);
        if(i == 0)
            g.strokeLine(plot.m2s(0.0,0),plot.height()-plot.bottom(),plot.m2s(0.0,0),plot.top());
        else
            g.strokeLine(plot.width()-plot.right(),plot.m2s(0.0,1),plot.left(),plot.m2s(0.0,1));
    }

    private double calculateStep(double min, double max,int N){
        double rangeSize = max-min;
        int numberOfDigits = (int)Math.floor(Math.log10(rangeSize));
        int eksp = -numberOfDigits+1;
        return Math.floor((max-min)/N*Math.pow(10,eksp))*Math.pow(10,-eksp);
    }

    private void drawAxes(){
        drawAxis(0);
        drawAxis(1);
    }

    private void drawAxis(int i){
        int N = 5;
        double step = calculateStep(plot.n2m(0,i),plot.n2m(1,i),N);
        double start = Math.floor(plot.n2m(0,i)/step)*step;

        g.setLineWidth(AXIS_WIDTH);
        if(plot.isInside(0,i)) drawAxisLine(i);

        for (int j = 1; j <= N; j++) tick(start+j*step,i);
        
    }
    
    private void drawBorder(){
        g.setLineWidth(BORDER_WIDTH);
        g.setStroke(Color.BLACK);
        double xMin = plot.n2s(0.0,0);
        double yMin = plot.n2s(0.0,1);
        double xMax = plot.n2s(1.0,0);
        double yMax = plot.n2s(1.0,1);
        g.strokeRect(xMin,yMax,xMax-xMin,yMin-yMax);
    }
}
