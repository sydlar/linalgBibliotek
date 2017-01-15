import java.util.Arrays;
import java.util.Random;

public class ClassificationSample {
    private static int N = 40; // Number of independent variables;
    private static double[] coeffs = generateCoeffs(N); // True coefficients in model
    private static double NOISE_FACTOR = 0.2;
    private static double THRESHOLD = 0.3;

    int size;
    double[][] xValues; // Observations for independent variables
    boolean[] classification; // Observations for dependent variable

    public ClassificationSample(int size){
        this.size = size;
        xValues = Matrices.random(size,N);
        classification = generateClassification();
    }

    public double[][] getXValues(){
        return copy(xValues);
    }

    public boolean[] getClasses(){
        return classification;
    }
    
    private boolean[] generateClassification(){
        Random r = new Random();

        double[] y = Matrices.multiply(xValues,coeffs);
        System.out.println("==============================");
        System.out.println(Arrays.toString(y));
        System.out.println("==============================");

        boolean[] cls = new boolean[size];
        for(int i = 0; i < cls.length; i++) cls[i] = (y[i] > r.nextGaussian()*NOISE_FACTOR+THRESHOLD);
        return cls;
    }

    public String toString(){
        return String.format("Uavhengig variabel:%n%s%nAvhengig variabel:%n%s",Arrays.deepToString(xValues),Arrays.toString(classification));
    }

    public static double[] copy(double[] in){
        double[] out = new double[in.length];
        System.arraycopy(in,0,out,0,in.length);
        return out;
    }

    public static double[][] copy(double[][] in){
        double[][] out = new double[in.length][];
        for(int i = 0; i < in.length;  i++) out[i] = copy(in[i]);
        return out;
    }

    private static double[] generateCoeffs(int n){
        double[] out = new double[n];
        for(int i = 0; i < n; i++) out[i] = 1.0/(i*i+1.0);
        return out;
    }

   


    public static void main(String[] args){
        ClassificationSample s = new ClassificationSample(10);

        System.out.println(s);


    }
}
