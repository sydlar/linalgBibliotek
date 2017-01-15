import linalg.plot.Plot;

public class Test {
    public static void main(String[] args){
        Plot plot = new Plot();
        double k = 1.08;
        double b = 0.074;
        plot.addLine(Math::sin,Math::cos,0.0,2*Math.PI,600);
        plot.setTitle("NICE PLOT");
        plot.setPngOutput("test");
           
        plot.show();
    }
}
