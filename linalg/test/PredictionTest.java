
public class PredictionTest {

    private static int TRAIN_SIZE = 100;
    private static int TEST_SIZE = 500;


    public static void main(String[] args){
        partII();
    }

    public static void partI(){

        System.out.println("First test");
        
        Sample trainingSample = new Sample(TRAIN_SIZE);

        double[] model = train(trainingSample);

        double[] trainingPrediction = predict(trainingSample,model);
        double[] trainingY = trainingSample.getYValues();


        System.out.printf("Gjennomsnittlig kvadratisk avvik (testdata): %f%n",averageSquareDev(trainingPrediction, trainingY));

        Sample testSample = new Sample(TEST_SIZE);
        double[] testPrediction = predict(testSample,model);// Modell fra trening
        double[] testY = testSample.getYValues();

        System.out.printf("Gjennomsnittlig kvadratisk avvik (treningsdata):%f%n",averageSquareDev(testPrediction, testY));
        
    }

    public static void partII(){

        // ITERASJON 2
        // Forbedre metoden med tanke på lavere avvik for test-sett
        //
        // Lissepasning: "tilfeldigvis" spiller x_1 større rolle enn x_2 osv
        // - > mulig fremgangsmåte: Ta kun x_1,...,x_k med i modellen. 
        //
        
        // Forsøk å finne en k-verdi som gir lavest mulig treningsavvik.
        //
        double[] xs = new double[Sample.DEFAULT_N];
        double[] ys = new double[Sample.DEFAULT_N];
         
        Sample train = new Sample(TRAIN_SIZE);
        Sample test = new Sample(TEST_SIZE);

        for(int n = 1; n <= Sample.DEFAULT_N; n++){

            double[][] trainX = train.getXValues();
            double[] trainY = train.getYValues();

            double[][] testX = test.getXValues();
            double[] testY = test.getYValues();

            trainX = getFirstColumns(trainX,n);
            testX = getFirstColumns(testX,n);

            double[] model = leastSquares(trainX,trainY);//TRENING

            double[] testPrediction = Matrices.multiply(testX,model); // PREDIKSJON
            double[] trainPrediction = Matrices.multiply(trainX,model); // PREDIKSJON


            double testGKA = averageSquareDev(testPrediction,testY);
            double trainGKA = averageSquareDev(trainPrediction,trainY);

            System.out.printf("%d\t%f%n",n,testGKA);
            xs[n-1] = n;
            ys[n-1] = testGKA;
        }
        Plot.dots(xs,ys);
    }


    /**
     
     * Returns coefficients for linear model given by
     * least squares fit to training sample. 
     *
     * Model: X a = y,
     * Normal-ligning = XTXa-stjerne = XTy
     *
     * metoden returnerer a-stjerne
     */
    private static double[] train(Sample sample){
        return leastSquares(sample.getXValues(),sample.getYValues());
    }

    private static double[] leastSquares(double[][] A, double[] b){
        double[][] AT = Matrices.transpose(A);
        double[][] ATA = Matrices.multiply(AT,A);
        double[] ATb = Matrices.multiply(AT,b);
        double[][] ATAinv = Matrices.inverse(ATA);
        double[] xAst = Matrices.multiply(ATAinv,ATb);
        return xAst;
    }
    
    private static double[][] getFirstColumns(double[][] X,int n){
        return Matrices.extract(X,0,X.length,0,n);
    }
    

    private static double[] predict(Sample s, double[] model){
        return Matrices.multiply(s.getXValues(),model);
    }

    public static double averageSquareDev(double[] a, double[] b){
        double accumulator = 0.0;
        for(int i = 0; i < a.length; i++) accumulator += (a[i]-b[i])*(a[i]-b[i]);
       return accumulator/a.length;
    }
}
