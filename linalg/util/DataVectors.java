
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class DataVectors {

	public static double[] getData(String fileName){
		return getData(fileName,0);
	}


	public static double[] getData(String fileName,int column){
		int count = 0;
		String[] line;
		ArrayList<Double> outputList = new ArrayList<Double>();
        
        Scanner fileScanner;

		try {
			fileScanner = new Scanner(new File(fileName));
		} catch (Exception e) {
			System.out.println(e);
            return new double[0];
		}

        while(fileScanner.hasNextLine()){
            line = fileScanner.nextLine().trim().split("\\s");

            if ( line.length > column){
                Scanner lineScanner = new Scanner(line[column]);
                if (lineScanner.hasNextDouble() && line.length > column){
                    outputList.add(lineScanner.nextDouble());
                    count++;
                }
            }
        }

		double[] output = new double[count];
		for(int i = 0; i < output.length; i++)
			output[i] = outputList.get(i);

		return output;
	}

    private static double[] sin(int size, int waveNumber){
        return Matrices.multiply(Matrices.applyTo(Matrices.range(0,2*Math.PI*waveNumber,size),Math::sin),Math.sqrt(2.0/size));

    }
    
    private static double[] cos(int size, int waveNumber){
        return Matrices.multiply(Matrices.applyTo(Matrices.range(0,2*Math.PI*waveNumber,size),Math::cos),Math.sqrt(2.0/size));
    }

    public static void main(String[] args){
        double[] oslo = getData("OsloSeries.txt");
        //double[] stockholm = getData("StockholmSeriesSmall.txt");

        int N = oslo.length;
        int M = 200;
    
        
        double[] scalarProducts = new double[M];

        for(int i = 0; i < M; i++){
            scalarProducts[i] = Matrices.scalarProduct(oslo,cos(N,i));
            //System.out.printf("%d %f%n",i,scalarProducts[i]);
        }


        Plot.dots(scalarProducts);
    }
}
