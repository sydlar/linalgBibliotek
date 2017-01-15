package linalg;

import java.util.Arrays;
import java.util.Random;
import java.util.function.DoubleUnaryOperator;

/*
 * Merknad: Ikke ta feilhåndteringen for seriøst her.
 * 
 * Det ligger i sakens natur at operasjonene ikke alltid
 * er veldefinert. Klassen Matrices kan gjerne fraskrive seg
 * ansvaret.
 *
 * En løsning som sannsynligvis fungerer ok er å finne opp noen 
 * passende unntak som arver fra RuntimeException. (Se nederst)
 *
 */

public class Matrices {


	public static final double EPS = 1e-11;

	public static void main(String[] args){
		System.out.println("SIMPLE TESTING");
		int n = 10;

		System.out.println("A:");
		double[][] A = randomSquare(n);

        double[] v = randomArray(3);
        print(v);
        print(vector2skew(v));
        print(skew2vector(vector2skew(v)));
          

       double[] a = new double[]{1.0,0.0,0.0}; 
       double[] b = new double[]{1.0,1.0,0.0}; 

       print(crossProduct(a,b));
       print(crossProduct(b,a));

		
        /*print(A);

		System.out.println("B:");
		double[][] B = randomSquare(n);
		print(B);
	
		System.out.println("b:");
		double[] b = randomArray(n);
		print(b);

		System.out.print("A*B");
		print(multiply(A,B));
		
		System.out.print("B*A");
		print(multiply(B,A));
		
		System.out.print("b*A");// b er her rad-vektor
		print(multiply(b,A));

		System.out.print("A*b"); // her tolkes b som søyle-vektor
		print(multiply(A,b));


		System.out.print("SYSTEM");
		double[][] S = randomSystem(3);
		print(S);
		Solvers.simpleGaussJordanElimination(S);
		print(S);

		System.out.println("Beregner A-invers");
		double[][] Ainv = inverse(A); // OBS: krasjer hvis A ikke er n x n
		print(Ainv);
		System.out.println("A*Ainv");
		System.out.println("Ainv*");
		print(multiply(Ainv,A));

		System.out.println(det(A)); // Krasjer hvis A ikke er n x n
		System.out.println(det(Ainv));
		System.out.println(det(A)*det(Ainv));*/
		

		System.out.println("\n\nQUICK DETERMINANT:");
		System.out.println(det(A)); // Bruker gauss-eliminasjon
		System.out.println("\n\nBRUTE FORCE DETERMINANT:");
		System.out.println(laplaceDeterminant(A));
	}

	/*
	 * Vector operations
	 */

	public static double[][] add(double[][] A, double[][] B){
		int rows = A.length;
		int cols = A[0].length;

		double[][] out = new double[rows][cols];
		for(int i = 0; i <rows; i++){
			for (int j = 0; j < cols; j++){
				out[i][j] = A[i][j] + B[i][j];
				
				if (Math.abs(out[i][j]) < EPS)
					out[i][j]=0.0;
			}
		}
		return out;
	}
	
	
	public static double[][] subtract(double[][] A, double[][] B){
		int rows = A.length;
		int cols = A[0].length;

		double[][] out = new double[rows][cols];
		for(int i = 0; i <rows; i++){
			for (int j = 0; j < cols; j++){
				out[i][j] = A[i][j] - B[i][j];

				if (Math.abs(out[i][j]) < EPS)
						out[i][j]=0.0;
			}
		}
		return out;
	}

    public static double[] add(double[] A, double[] B){
		int rows = A.length;

		double[] out = new double[rows];
		for(int i = 0; i <rows; i++){
			out[i] = A[i] + B[i];
		}
		return out;
	}

    public static double[] subtract(double[] A, double[] B){
		int rows = A.length;

		double[] out = new double[rows];
		for(int i = 0; i <rows; i++){
    		out[i] = A[i] - B[i];
		}
		return out;
	}
	
	public static double[][] multiply(double[][] A, double scalar){
		int rows = A.length;
		int cols = A[0].length;

		double[][] out = new double[rows][cols];
		for(int i = 0; i <rows; i++)
			for (int j = 0; j < cols; j++)
				out[i][j] = A[i][j]*scalar;
		return out;
	}
	
    public static double[] multiply(double[] A, double scalar){
		int rows = A.length;

		double[] out = new double[rows];
		for(int i = 0; i <rows; i++)
			out[i] = A[i]*scalar;
		return out;
	}

	/*
	 * Inplace vector operations
	 */

	public static void addInplace(double[][] A, double[][] B){
		for(int i = 0; i <A.length; i++)
			for (int j = 0; j < A[0].length; j++)
				A[i][j] += B[i][j];
	}
	
	public static void subtractInplace(double[][] A, double[][] B){
		for(int i = 0; i < A.length; i++)
			for (int j = 0; j < A[0].length; j++)
				A[i][j] -= B[i][j];
	}
	
	public static void multiplyInplace(double[][] A, double scalar){
		for(int i = 0; i < A.length; i++)
			for (int j = 0; j < A[0].length; j++)
				A[i][j]*=scalar;
	}

	/*
	 * Matrix operations
	 */
	public static double[] multiply(double[][] A, double[] b){
		return asVector(multiply(A,asColumnVector(b)));
	}

	public static double[][] multiply(double[] b, double[][] A){
		return multiply(asRowVector(b),A);
	}

    // Brukeren har ansvar for at A,B er kompatible
	public static double[][] multiply(double[][] A, double[][] B){
		int rows = A.length;
		int cols = B[0].length;
		int n = B.length;
		
		double[][] out = new double[rows][cols];

		for (int i = 0; i< rows; i++)
			for (int j = 0; j < cols; j++)
				for (int k = 0; k < n ; k++)
					out[i][j] += A[i][k]*B[k][j];

		return out;
	}

	public static double[][] inverse(double[][] A){
		int n = A.length;
		double[][] augMatrix = concatenate(A,identity(n)); // [A | I]
		Solvers.scaledGaussJordanElimination(augMatrix); // .... ~ [I | Ainv]
		return extract(augMatrix,0,n,n,n); // Henter ut de $n$ siste søylene i auxMatrix -> Ainv
	}

	public static double[][] transpose(double[][] A){
		double[][] output = new double[A[0].length][A.length];
		for (int i = 0; i < A.length; i++)
			for (int j = 0; j < A[0].length; j++)
				output[j][i] = A[i][j];
		return output;
	}

	public static double det(double[][] A){
		return Solvers.detByElim(A);
	}

    // Hårreisende, rekursiv metode.
	public static double laplaceDeterminant(double[][] A){
        if (A.length == 0)
            return 1;
		/*if (A.length == 1) // Grunntilfelle.
			return A[0][0];*/
		
		double determinant = 0.0;
		short sign = 1;
		for (int i = 0; i < A.length; i++){
			determinant += sign*A[0][i]*laplaceDeterminant(getCofactorMatrix(A,0,i));
			sign *= -1;
		}
		return determinant;
	}

    // Vektorprodukter
    public static double scalarProduct(double[] u, double[] v){
        return scalarProduct(asRowVector(u),asColumnVector(v));
    }

    public static double scalarProduct(double[][] row, double[][] column){
        return multiply(row,column)[0][0];
    }

    public static double[][] exteriorProduct(double[][] u, double[][] v){
        return subtract(multiply(v,transpose(u)),multiply(u,transpose(v)));
    }

    public static double[] crossProduct(double[] u, double[] v){
        return skew2vector(exteriorProduct(vector2skew(u),vector2skew(v)));
    }

    public static double[][] vector2skew(double[] u){
        double[][] out = new double[3][3];
        for(int i = 0; i < 3; i++){
            out[(i+1)%3][(i+2)%3] = -u[i];
            out[(i+2)%3][(i+1)%3] = u[i];
        }
        return out;
    }

    public static double[] skew2vector(double[][] skew){
        double[] out = new double[3];
        for(int i = 0; i < 3; i++){
            out[i] = 0.5*(skew[(i+2)%3][(i+1)%3] - skew[(i+1)%3][(i+2)%3]);
        }
        return out;

    }




	/*
	 * Potentially useful tools: copy and print.
	 ********************************************/

	public static double[][] copy(double[][] array){
		double[][] out = new double[array.length][];
		for(int i = 0 ; i < array.length; i++){
				out[i] = Arrays.copyOf(array[i],array[i].length);
		}
		return out;
	}
	
	public static double[] copy(double[] array){
		return Arrays.copyOf(array,array.length);
	}

    public static double[] asVector(double[][] array){
        double[] out = new double[array.length*array[0].length];
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[0].length; j++){
                out[i*array[0].length+j] = array[i][j];   
            }
        }
        return out;
    }

	public static double[][] asColumnVector(double[] array){
		double[][] out = new double[array.length][1];
		for(int i = 0; i < array.length; i++)
			out[i][0] = array[i];
		return out;
	}

	public static double[][] asRowVector(double[] array){
		double[][] out = new double[1][];
		out[0] = Arrays.copyOf(array,array.length);
		return out;
	}

	public static double[][] concatenate(double[][] A, double[][] B) {
		int rows = A.length;
		int cols = A[0].length + B[0].length;
		
		double[][] out = new double[rows][cols];

		for(int i = 0; i < rows; i++){
			int offset = A[0].length;
			for (int j = 0; j < offset; j++)
				out[i][j] = A[i][j];
			for (int j = 0; j < B[0].length; j++)
				out[i][j+offset] = B[i][j];
		}
		return out;
	}
	
	public static double[][] concatenate(double[][] A, double[] b) {
		int rows = A.length;
		int cols = A[0].length + 1;
		
		double[][] out = new double[rows][cols];

		for(int i = 0; i < rows; i++){
			int j = 0; 
			while (j < A[0].length){
				out[i][j] = A[i][j];
				j++;
			}
			out[i][j] = b[i];
		}
		return out;
	}

    public static double[] concatenate(double[] u, double[] v){
        double[] out = new double[u.length+v.length];
        System.arraycopy(u,0,out,0,u.length);
        System.arraycopy(v,0,out,u.length,v.length);
        return out;
    }

	public static double[][] extract(double[][] A,int iStart,int iCount, int jStart, int jCount){
		double[][] out = new double[iCount][jCount];
		for(int i = 0; i < iCount; i++)
			for (int j= 0; j < jCount; j++)
				out[i][j] = A[iStart+i][jStart+j];
		return out;
	}

    public static double[] extract(double[] array,int lo, int hi){
        double[] out = new double[hi-lo];
        for(int i = 0; i < out.length; i++)
            out[i] = array[lo+i];
        return out;
    }
    
	public static double[][] getCofactorMatrix(double[][] A,int i,int j){
		double[][] out = new double[A.length-1][A[0].length-1];
		
		for (int k = 0, l = 0; l < out.length; k++,l++){
			if (k == i)
				k++;
			for (int m = 0, n = 0 ; n < out[0].length; m++,n++){
				if (m == j) 
					m++;
				out[l][n]=A[k][m];
			}
		}
		return out;
	}



	public static void print(double[][] array) {
		System.out.println();
		for (double[] row : array)
			print(row);
		System.out.println();
	}
	
	public static void print(double[] array) {
		System.out.print("[");
		for ( int i = 0; i < array.length; i++)
			System.out.printf("  %2.2e ",array[i]);
		System.out.println("]");
	}

	public static void printGnuplotData( double[] y){
		printGnuplotData(new double[][]{y});
	}

	public static void printGnuplotData(double[] x, double[] y){
		printGnuplotData(new double[][]{x,y});
	}
	
	public static void printGnuplotData(double[][] vectors){
		System.out.print("#AUTO-GENERATED PLOTTING DATA\n#");
		System.out.print(" x \t");
		for(int i = 1; i < vectors.length; i++)
			System.out.printf("\ty_%d\t",i);
		printRawData(vectors);
	}

	private static void printRawData(double[][] vectors){
		for(int i = 0; i < vectors[0].length; i++){
			System.out.println();
			for (int j = 0; j < vectors.length; j++)
				System.out.printf("%e\t",vectors[j][i]);
		}
	}
	

	/*
	 * Creation of random arrays
	 *******************************************/

/**
 * Produces a random n x (n+1)-matrix that is useful 
 * for testing Gauss Elimination methods.
 *
 * The entries are supposed to follow the Gauss-distribution.
 */
	public static double[][] randomSystem(int n){
		Random r = new Random();
		double[][] array = new double[n][n+1];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n+1; j++)
				array[i][j] = r.nextGaussian();
		return array;
	}



    /*
     * Create useful arrays
     * ************************************************/
    // values lo,lo+(hi-lo)/(n-1), ... ,hi
    // I.e: With endpoints!
    public static double[] range(double lo, double hi,int n){
        double[] out = new double[n];
        double h = 1.0/(n-1);
        for(int i = 0; i < n; i++) out[i] = h*((n-1-i)*lo + i*hi);
        return out;
    }

    public static double[] range(double lo, double hi){return range(lo,hi,100);}

    public static double[] range(double lo, double hi, double h){
        int n = (int)Math.floor((hi-lo)/h);
        return range(lo,lo+h*n,n+1);
    }
    
    public static double[] range(int lo, int hi){
        double[] out = new double[hi-lo];
        for(int i = lo; i < hi; i++)
            out[i-lo] = i;
        return out;
    }

    public static double[] range(int n){return range(0,n);}



    public static double[] applyTo(double[] array, DoubleUnaryOperator op){
        double[] out = new double[array.length];
        for(int i = 0; i < array.length ; i++){
            out[i] = op.applyAsDouble(array[i]);
        }
        return out;
    }

/**
 * Produces a random n x n-matrix that is well suited
 * for Jacobi iteration and Gauss-Seidel iteration.
 *
 * The random choice is based on Gauss-distributions,
 * but the results are tuned in order to make the diagonal 
 * elements sufficiently dominant.
 */
	public static double[][] randomSquare(int n){
		Random r = new Random();
		double[][] array = new double[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				array[i][j] = r.nextGaussian()/(1+(n+1)*Math.pow(i-j,2));
		return array;
	}
	
	
	public static double[][] random(int n,int m){
		Random r = new Random();
		double[][] array = new double[n][m];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < m; j++)
				array[i][j] = r.nextGaussian();
		return array;
	}
	
    public static double[] random(int n){
		Random r = new Random();
		double[] array = new double[n];
		for (int i = 0; i < n; i++)
			array[i] = r.nextGaussian(); 
		return array;
	}

/**
 * Produces random array of length n.
 *
 * The elements are supposed to follow the Gauss-distribution.
 */
	public static double[] randomArray(int n){
		Random r = new Random();
		double[] array = new double[n];
		for (int i = 0; i < n; i++)
			array[i] = r.nextGaussian();
		return array;
	}


	/*
	 * Create special matrices:
	 */

	public static double[][] identity(int n){
		double[][] out = new double[n][n];
		for(int i = 0;i < n ; i++)
			out[i][i] = 1.0;
		return out;
	}

	public static double[] elementaryVector(int n,int i){
		double[] out = new double[n];
		out[i]=1;
		return out;
	}
}




class Solvers {

    public static boolean rowReduce(double[][] matrix){
        return scaledGaussJordanElimination(matrix);
    }

	/*
	 * Input: Coefficient matrix of linear system (the augemented matrix)
	 * Performs in-place GaussJordanElimination
	 * Output: true or false depending on sucess or failure.
	 */
	public static boolean simpleGaussJordanElimination(double[][] matrix){
		int rows = matrix.length;

		for (int row = 0; row < rows; row++){
			for (int i = 0; i < rows; i ++){
				if ( i == row)
					continue;
				eliminateVariableFromRow(matrix,row,i);
			}
		}
		normalizeRows(matrix);
		return true;
	}

	private static void eliminateVariableFromRow(double[][] matrix, int constantRow, int changingRow) {
		double multiplicator = matrix[changingRow][constantRow] / matrix[constantRow][constantRow];
		applyRow(matrix, constantRow,changingRow,multiplicator);
	}

	private static void applyRow(double[][] matrix, int row, int changingRow, double multiplicator){
		for (int j = 0; j < matrix[0].length; j++)
			matrix[changingRow][j] -= multiplicator*matrix[row][j];
	}

	private static void normalizeRows(double[][] matrix){
		for (int i = 0; i < matrix.length; i++){
			double multiplicator = 1.0/matrix[i][i];
			for (int j = 0; j < matrix[0].length; j++)
				matrix[i][j]*=multiplicator;
		}
	}


	/*
	 * Input: Coefficient matrix of linear system (the augemented matrix)
	 * Performs in-place GaussJordanElimination
	 * Output: true or false depending on sucess or failure.
	 */
	public static boolean scaledGaussJordanElimination(double[][] matrix){
		ScaledGaussJordanEliminator G = new ScaledGaussJordanEliminator(matrix);
		G.eliminate();
		G.normalizeRows();
		return G.success;
	}


	/*
	 * Input: Matrix.
	 * Output: Determinant.
	 *
	 * Method: 
	 * (i) Scaled Gauss-Jordan elimination without rescaling of rows. 
	 * (ii) Multiplication of diagonal elements.
	 *
	 */
	public static double detByElim(double[][] matrix){
		ScaledGaussJordanEliminator G = new ScaledGaussJordanEliminator(matrix);
		G.eliminate();
		if (!G.success)
			return 0.0;
		
		double result = 1.0;
		for(int i = 0; i < matrix.length; i++)
			result*=matrix[i][i];
		return result;
	}

	/*
	 *
	 * Solves Ax=b by Gauss-Seidel iteration. 
	 * NOTE: Convergence is NOT guaranteed at all.
	 *
	 * Input: 
	 *  - Matrix (double[][]) A
	 *  - Vector (double[]) b
	 *  - Vector (double[]) x: Solution. The iteration takes place in-place in x.
	 *
	 *  Output: true or false depending on sucess or failure.
	 *
	 */
	public static boolean gaussSeidel(double[][] A, double[] b, double[] x) {

			try {
				GaussSeidel solver = new GaussSeidel(A,b,x);
				solver.solve();
				return true;
			} catch (GSException e){
				System.out.println(e);
				e.printStackTrace();
				return false;
			}
	}

}
	

class GaussJordanEliminator {
	static final int EPS = (int)1e-9;
	static final int INVALID_VARIABLE = -1;
	
	double[][] matrix;
	int rows,cols;
	boolean success;
	
	public GaussJordanEliminator(double[][] theMatrix){
		matrix = theMatrix;
		rows = matrix.length;
		cols = matrix[0].length;
		success = false;
	}

	public void eliminate(){
		success = true;
		for(int i = 0; i < cols;  i++){
			eliminateVariable(i);
		}
		rearrange();
	}


	void eliminateVariable(int var ){
		int row = chooseRow(var);
		if (row == INVALID_VARIABLE)
			return;
		
		for(int j = 0; j < rows; j++){
			if (j == row)
				continue;
			try {
				eliminateVariableBetweenRows(var,row,j);
			} catch (GJException e){
				success = false;
			}
		}
	}

	void eliminateVariableBetweenRows(int var, int invariantRow, int changingRow){
		double multiplicator = matrix[changingRow][var]/matrix[invariantRow][var];
		testValidity(multiplicator);

		for(int i = 0; i < cols; i++)
			matrix[changingRow][i] = matrix[changingRow][i] - matrix[invariantRow][i]*multiplicator;
		
		matrix[changingRow][var] = 0.0; // to prevent roundoff errors here.
	}

	void normalizeRows(){
		for(int i = 0; i < rows; i++){
			normalizeRow(i);
		}
	}

	void normalizeRow(int row){
		double multiplicator = 0.0;
		for(int i = 0; i < cols; i++){
			if (multiplicator == 0.0) {
				if(Math.abs(matrix[row][i]) > EPS){
					multiplicator = 1.0/matrix[row][i];
					matrix[row][i] = 1.0;
				}
			} else {
				matrix[row][i] *= multiplicator;
			}

		}
	}
	
	void rearrange(){
		return;
	}

	int chooseRow(int var){
		if(var >= rows)
			return INVALID_VARIABLE; 
		return var;
	}

	static void testValidity(double d){
		if ( d == Double.NaN || d == Double.POSITIVE_INFINITY || d == Double.NEGATIVE_INFINITY)
			throw new GJException();
	}
}



class ScaledGaussJordanEliminator extends GaussJordanEliminator{
	boolean[] rowIsUsed;
	int[] permutationRecord;

	ScaledGaussJordanEliminator(double[][] array){
		super(array);
		rowIsUsed = new boolean[rows];
		permutationRecord = new int[cols];
	}

	int chooseRow(int var){ // Hvilken rad er mest velegnet til å eliminere søyle 'var'?
		int bestOption = 0;
		double bestScore = 0.0, currentScore = 0.0;

		for (int i = 0; i < rows ; i++){
			if(rowIsUsed[i])
				continue;

			currentScore = scalingScore(i,var);

			if (currentScore > bestScore && currentScore !=Double.POSITIVE_INFINITY){
				bestOption = i;
				bestScore = currentScore;
			}
		}
		if (bestScore == 0.0)
			return INVALID_VARIABLE; // Overtradisjonell feilhåndtering. Anbefalt: Exception

		rowIsUsed[bestOption] = true;
		permutationRecord[var] = bestOption; // Husk på at vi vi brukte raden.
		return bestOption;
	}


	double scalingScore(int row,int var){
		if (Math.abs(matrix[row][var]) < EPS)
			return 0.0;

		double maxValue = 0.0;

        // Søker største mulige verdi i raden
		for (int i = 0; i < cols ; i ++){
			double currentValue = Math.abs( matrix[row][i]);

			if (currentValue > maxValue)
				maxValue = currentValue;
		}

		return Math.abs(matrix[row][var]/maxValue); // størrelsen til foreslått pivotelement, relativt til største elementet i raden.
	}

	void rearrange(){//Brutal algoritm: Uses a copy of the matrix.
        double[][] tmpMatrix = Arrays.copyOf(matrix,rows);
		for(int var = 0; var < cols && var < rows ; var++) matrix[var] = tmpMatrix[permutationRecord[var]];
	}
}





class GaussSeidel {
	private static final double EPS = 1e-6;
	private static final double TOL = 1e-6;

	double[][] A;
	double[] b;
	double[] x;
	double[] AdiagInv;

	double errorIndicator = Double.POSITIVE_INFINITY; // This is NOT a valid error estimate.

	int n;
	boolean success = false;
	int count = 0;

	GaussSeidel(double[][] theA, double[] theB, double[] theX){
		A = theA;
		b = theB;
		x = theX;
		n = b.length;
		testSize();
		AdiagInv = new double[n];
		calculateInverseDiagonalElements();
	}

	void simpleStep(){
		for (int i = 0; i < n ; i++){
			x[i] = b[i];
			for (int j = 0 ;j < i ; j++){
				x[i] -= A[i][j]*x[j];
			}
			for (int j = i+1 ;j < n ; j++){
				x[i] -= A[i][j]*x[j];
			}
			x[i]*=AdiagInv[i];
		}
		count++;
	}

	void stepWithTest(){
		double accumulator;
		double error = 0;
		success = true; // A hypothesis to test.
		for (int i = 0; i < n ; i++){
			accumulator = b[i];
			for ( int j = 0 ; j < n ; j++){
				accumulator -= A[i][j]*x[j];
			}
			x[i] = AdiagInv[i]*(accumulator + A[i][i]*x[i]);
			accumulator = Math.abs(accumulator);
			if ( accumulator > TOL)
				success = false;
			error += accumulator;
		}
		if ( error > 10*errorIndicator || error == Double.POSITIVE_INFINITY || error == Double.NaN) {
			System.out.printf("ERROR %E after %d iterations%n%n",error,count);
			throw new GSException.Convergence();
		}
		errorIndicator = error;
		count++;
	}

	void solve(){
		while(!success){
			for ( int i = 0; i < 0; i++)
				simpleStep();
			stepWithTest();
		}
	}

	private void testSize(){
		if (A.length != n || x.length != n)
			throw new GSException.Dimensions();
		for(int i = 0; i < n ; i++) {
			if (A[i].length != n)
				throw new GSException.Dimensions();
		}
	}

	private void calculateInverseDiagonalElements(){
		for(int i = 0 ; i < n ; i++){
			if (Math.abs(A[i][i]) < EPS)
				throw new GSException.Convergence();
			else
				AdiagInv[i] = 1/A[i][i];
		}
	}

}


class GJException extends RuntimeException {
	GJException(){
		super("Unsuccessful Gauss-elimination");
	}
}

class GSException extends RuntimeException {
	
	GSException(String message){
		super("Unsuccessful Gauss-Seidel-iteration: " + message);
	}

	GSException(){
		super("Unsuccessful Gauss-Seidel-iteration.");
	}

	static class Convergence extends GSException {
		Convergence(){
			super("Method does not converge");
		}
	}
	
	static class Dimensions extends GSException {
		Dimensions(){
			super("Incompatible dimensions");
		}
	}



}
