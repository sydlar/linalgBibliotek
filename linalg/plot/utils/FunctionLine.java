package linalg.plot.utils;
import linalg.plot.Plot;
import linalg.Matrices;
import java.util.function.DoubleUnaryOperator;

public class FunctionLine extends DataSet  {
    DoubleUnaryOperator f;
    int sampleSize = 500;

    public FunctionLine(DoubleUnaryOperator theF){
        super();
        f = theF;
    }

    private void provideData(){
        if (super.xs != null && super.ys != null)
            return;

        super.xs = Matrices.range(-10,10,sampleSize);
        super.ys = Matrices.applyTo(xs,f);
    }

    private void provideData(Plot plot){
        if(super.xs != null &&  super.xs[0] == plot.n2m(0,0) && super.xs[xs.length-1] == plot.n2m(1,0))
            return;

        super.xs = Matrices.range(plot.n2m(0,0),plot.n2m(1,0),sampleSize);
        super.ys = Matrices.applyTo(xs,f);
    }

    public void draw(Plot p){
        this.provideData(p) ;
        super.draw(p);
    }

    public double xMin(){this.provideData(); return Utils.min(super.xs);}
    public double xMax(){this.provideData(); return Utils.max(super.xs);}
    public double yMin(){this.provideData(); return Utils.min(super.ys);}
    public double yMax(){this.provideData(); return Utils.max(super.ys);}


    public void setSampleSize(int n){
        sampleSize = n;
        super.xs = null;
    }
}
