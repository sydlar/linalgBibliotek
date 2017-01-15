import java.util.Arrays;

public class Sample {
    public static int DEFAULT_N = 40; // Number of independent variables;
    private static double NOISE_FACTOR = 0.2;

    double[][] xValues; // Observations for independent variables
    double[] yValues; // Observations for dependent variable
    double[] predictedYValues; // The "gold standard prediction"
    double[] noise; // Added noise
    double[] coeffs; // True coefficients in model

    int size;
    int N;

    public Sample(int size,int numberOfVars){
        this.size = size;
        N = numberOfVars;
        coeffs = generateCoeffs(N); // True coefficients in model
        xValues = Matrices.random(size,N);
        predictedYValues = Matrices.multiply(xValues,coeffs);
        noise = Matrices.multiply(Matrices.random(size),NOISE_FACTOR);
        yValues = Matrices.add(predictedYValues,noise);
    }

    public Sample(int size){this(size,DEFAULT_N);};

    public double[][] getXValues(){
        return copy(xValues);
    }

    public double[] getYValues(){
        return copy(yValues);
    }
    
    public double[] predictedYValues(){
        return copy(predictedYValues);
    }
    


    public String toString(){
        return String.format("Uavhengig variabel:%n%s%nAvhengig variabel:%n%s",Arrays.deepToString(xValues),Arrays.toString(yValues));
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

    private double[] generateCoeffs(int n){
        double[] out = new double[n];
        for(int i = 0; i < n; i++) out[i] = 1.0/(i*i+1.0);
        return out;
    }

   


    public static void main(String[] args){
        Sample s = new Sample(60);

        System.out.println(s);

        Plot.dots(s.predictedYValues,s.yValues);

    }
}
