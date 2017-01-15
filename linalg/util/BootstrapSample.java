import java.util.Random;

// IDE TIL ARKITEKTUR FOR BOOTSTRAP-TESTING.

public class BootstrapSample extends Sample {
    private Sample population;
    int[] sample;

    public BootstrapSample(Sample s,int n){
        population = s;
        sample = new int[n];
        for(int i = 0; i < n; i++)
            sample[i] = Random.nextInt(n);
    }

    public double[] getXValues(){
        // TODO
    }

    public double[] getYValyes(){

    }
}
