/*
 * Example from oppg9.pdf. (2016)
 * Oppgave 2
 */
public class MatrixExample {
    
    public static void main(String[] args){
        double[][] A = new double[][]{
            {-4.0,1.0},
            {-2.0,1.0},
            {0.0,1.0},
            {2.0,1.0},
            {4.0,1.0},
        };

        double[] b = new double[]{
            -2.0,
            0.0,
            3.0,
            4.0,
            5.0
        };
        
        // Løse Ax = b ved å bruke normalligningen. (AT*A x = AT b)
        
        double[][] AT = Matrices.transpose(A);

        double[][] ATA = Matrices.multiply(AT,A);

        double[] ATb = Matrices.multiply(AT,b);

        double[][] ATAinv = Matrices.inverse(ATA);

        double[] xAst = Matrices.multiply(ATAinv,ATb);

        Matrices.print(A);

        Matrices.print(b);

        Matrices.print(AT);

        Matrices.print(ATA);

        Matrices.print(ATb);

        Matrices.print(ATAinv);

        Matrices.print(xAst);


        double[] bAst = Matrices.multiply(A,xAst);

        System.out.println("Original y-values:");
        Matrices.print(b);
        System.out.println("Predicted y-values:");
        Matrices.print(bAst);

        Plot.dots(b,bAst);
    }
}
