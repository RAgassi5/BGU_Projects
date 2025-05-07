package assignment2;

public class GaussianElimination {
    /**
     * this function transforms a given matrix using Gaussian Elimination
     *
     * @param matrix a given matrix
     */
    public static void gaussianElimination(double[][] matrix) {
        int h = 0;
        int k = 0;

        while ((k < matrix.length) && (h < matrix[k].length)) {
            int maxI = findRowIndex(matrix, h, k);

            //if the last value in the row equals 0, continue to the next row
            if (matrix[maxI][k] == 0) {
                k++;
            }
            //if the last value in the row is not 0, the swapRows swaps between the rows
            else {
                swapRows(matrix, h, maxI);

                //for each value in the given matrix, a calculation is made based on the Gaussian Elimination method
                //the "for" loop keeps iterating until it reaches the final value in the matrix
                for (int i = h + 1; i < matrix.length; i++) {
                    double FirstDivision = matrix[i][k] / matrix[h][k];
                    matrix[i][k] = 0;
                    for (int j = k + 1; j < matrix[k].length; j++) {
                        matrix[i][j] -= matrix[h][j] * FirstDivision;
                    }
                }
                h++;
                k++;
            }

        }
    }

    /**
     * this function finds the index with the maximum value
     *
     * @param matrix the given matrix
     * @param h      the current row index
     * @param k      the column row index
     */
    public static int findRowIndex(double[][] matrix, int h, int k) {
        int maxI = h;
        int TotalRows = matrix.length;
        double maxValue = Math.abs(matrix[h][k]);
        for (int i = h + 1; i < TotalRows; i++) {
            if (Math.abs(matrix[i][k]) > maxValue) {
                maxI = i;
                maxValue = Math.abs(matrix[i][k]);
            }
        }

        return maxI;
    }

    /**
     * this function swaps between two given rows in the matrix
     *
     * @param matrix    the given matrix
     * @param firstRow  the first row that needs to be swapped
     * @param secondRow the first row that needs to be swapped
     */
    public static void swapRows(double[][] matrix, int firstRow, int secondRow) {
        double[] storedArray = matrix[firstRow];
        matrix[firstRow] = matrix[secondRow];
        matrix[secondRow] = storedArray;
    }
}
