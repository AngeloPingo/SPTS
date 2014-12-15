package MathMatrix;

final public class Matrix {
    private final int M;             // number of rows
    private final int N;             // number of columns
    private final double[][] data;   // M-by-N array

    // create M-by-N matrix of 0's
    public Matrix(int M, int N) {
        this.M = M;
        this.N = N;
        data = new double[M][N];
    }

    // create matrix based on 2d array
    public Matrix(double[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new double[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                    this.data[i][j] = data[i][j];
    }

    // copy constructor
    private Matrix(Matrix A) { this(A.data); }

    // create and return a random M-by-N matrix with values between 0 and 1
    public static Matrix random(int M, int N) {
        Matrix A = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                A.data[i][j] = Math.random();
        return A;
    }

    // create and return the N-by-N identity matrix
    public static Matrix identity(int N) {
        Matrix I = new Matrix(N, N);
        for (int i = 0; i < N; i++)
            I.data[i][i] = 1;
        return I;
    }

    // swap rows i and j
    public void swap(int i, int j) {
        double[] temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    // create and return the transpose of the invoking matrix
    public Matrix transpose() {
        Matrix A = new Matrix(N, M);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                A.data[j][i] = this.data[i][j];
        return A;
    }
    
    public static Matrix transpose(Matrix matrix) {
		Matrix transposedMatrix = new Matrix(matrix.getNcols(), matrix.getNrows());
		for (int i=0;i<matrix.getNrows();i++) {
			for (int j=0;j<matrix.getNcols();j++) {
				transposedMatrix.setValueAt(j, i, matrix.getValueAt(i, j));
			}
		}
		return transposedMatrix;
	}

    // return C = A + B
    public Matrix plus(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j] + B.data[i][j];
        return C;
    }
    
    public Matrix addVector(double[] B, int row) {
        if (B.length != this.N) throw new RuntimeException("Illegal matrix dimensions.");
            for (int j = 0; j < N; j++)
            	data[row][j] = B[j];
        return this;
    }


    // return C = A - B
    public Matrix minus(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j] - B.data[i][j];
        return C;
    }

    // does A = B exactly?
    public boolean eq(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                if (A.data[i][j] != B.data[i][j]) return false;
        return true;
    }

    // return C = A * B
    public Matrix times(Matrix B) {
        Matrix A = this;
        if (A.N != B.M) throw new RuntimeException("Illegal matrix dimensions" +A.M+"x"+A.N+ " " +B.M+"x"+B.N+".");
        Matrix C = new Matrix(A.M, B.N);
        for (int i = 0; i < C.M; i++)
            for (int j = 0; j < C.N; j++)
                for (int k = 0; k < A.N; k++)
                    C.data[i][j] += (A.data[i][k] * B.data[k][j]);
        return C;
    }
    
    public double determinant() throws Exception {
		if (!this.isSquare())
			throw new Exception("matrix need to be square.");
		if (this.size() == 1){
			return this.getValueAt(0, 0);
		}
			
		if (this.size()==2) {
			return (this.getValueAt(0, 0) * this.getValueAt(1, 1)) - ( this.getValueAt(0, 1) * this.getValueAt(1, 0));
		}
		double sum = 0.0;
		for (int i=0; i<this.getNcols(); i++) {
			sum += changeSign(i) * this.getValueAt(0, i) * createSubMatrix(this, 0, i).determinant();
		}
		return sum;
	}
    
    public Matrix cofactor() throws Exception {
		Matrix mat = new Matrix(this.getNrows(), this.getNcols());
		for (int i=0;i<this.getNrows();i++) {
			for (int j=0; j<this.getNcols();j++) {
				mat.setValueAt(i, j, changeSign(i) * changeSign(j) * createSubMatrix(this, i, j).determinant());
			}
		}
		
		return mat;
	}
    
    public Matrix inverse() throws Exception {
		return (transpose(this.cofactor()).multiplyByConstant(1.0/this.determinant()));
	}


    public static Matrix createSubMatrix(Matrix matrix, int excluding_row, int excluding_col) {
		Matrix mat = new Matrix(matrix.getNrows()-1, matrix.getNcols()-1);
		int r = -1;
		for (int i=0;i<matrix.getNrows();i++) {
			if (i==excluding_row)
				continue;
				r++;
				int c = -1;
			for (int j=0;j<matrix.getNcols();j++) {
				if (j==excluding_col)
					continue;
				mat.setValueAt(r, ++c, matrix.getValueAt(i, j));
			}
		}
		return mat;
	}
    
    public Matrix createSubMatrixInf(int excluding_rows, int excluding_cols) {
		Matrix mat = new Matrix(excluding_rows, excluding_cols);
		for (int i=0;i<excluding_rows;i++) {
			for (int j=0;j<excluding_cols;j++) {
				mat.setValueAt(i, j, this.getValueAt(i, j));
			}
		}
		return mat;
	}
    
    public double trace(int inicial, int end) {
    	double result = 0;
    	for (int i = inicial-1; i < end; i++) {
			for (int j = inicial-1; j < end; j++) {
				if (i==j) {
					result += getValueAt(i, j);
				}
			}
    	}
    	return result;
    }
    
    public Matrix multiplyByConstant(double constant) {
		Matrix mat = new Matrix(getNrows(), getNcols());
		for (int i = 0; i < getNrows(); i++) {
			for (int j = 0; j < getNcols(); j++) {
				mat.setValueAt(i, j, data[i][j] * constant);
			}
		}
		return mat;
	}
    
    public static int changeSign(int i) {
		if (i%2==0)
			return 1;
		return -1;
	}
    
    public void setValueAt(int row, int col, double value) {
		data[row][col] = value;
	}
    
    public double getValueAt(int row, int col) {
		return data[row][col];
	}

    public int getNrows() {
		return this.M;
	}

    public boolean isSquare() {
    	if (this.M==this.N) {
    		return true;
    	}
		return false;
	}

    public int getNcols() {
		return this.N;
	}

    public int size() {
		return this.M*this.N;
	}

	// return x = A^-1 b, assuming A is square and has full rank
    public Matrix solve(Matrix rhs) {
        if (M != N || rhs.M != N || rhs.N != 1)
            throw new RuntimeException("Illegal matrix dimensions.");

        // create copies of the data
        Matrix A = new Matrix(this);
        Matrix b = new Matrix(rhs);

        // Gaussian elimination with partial pivoting
        for (int i = 0; i < N; i++) {

            // find pivot row and swap
            int max = i;
            for (int j = i + 1; j < N; j++)
                if (Math.abs(A.data[j][i]) > Math.abs(A.data[max][i]))
                    max = j;
            A.swap(i, max);
            b.swap(i, max);

            // singular
            if (A.data[i][i] == 0.0) throw new RuntimeException("Matrix is singular.");

            // pivot within b
            for (int j = i + 1; j < N; j++)
                b.data[j][0] -= b.data[i][0] * A.data[j][i] / A.data[i][i];

            // pivot within A
            for (int j = i + 1; j < N; j++) {
                double m = A.data[j][i] / A.data[i][i];
                for (int k = i+1; k < N; k++) {
                    A.data[j][k] -= A.data[i][k] * m;
                }
                A.data[j][i] = 0.0;
            }
        }

        // back substitution
        Matrix x = new Matrix(N, 1);
        for (int j = N - 1; j >= 0; j--) {
            double t = 0.0;
            for (int k = j + 1; k < N; k++)
                t += A.data[j][k] * x.data[k][0];
            x.data[j][0] = (b.data[j][0] - t) / A.data[j][j];
        }
        return x;
   
    }

    // print matrix to standard output
    public void show() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) 
                System.out.print(data[i][j] + "\t");
            System.out.println();
        }
    }



//    // test client
//    public static void main(String[] args) {
//        double[][] d = { { 1, 2, 3 }, { 4, 5, 6 }, { 9, 1, 3} };
//        Matrix D = new Matrix(d);
//        D.show();        
//        System.out.println();
//
//        Matrix A = Matrix.random(5, 5);
//        A.show(); 
//        System.out.println();
//
//        A.swap(1, 2);
//        A.show(); 
//        System.out.println();
//
//        Matrix B = A.transpose();
//        B.show(); 
//        System.out.println();
//
//        Matrix C = Matrix.identity(5);
//        C.show(); 
//        System.out.println();
//
//        A.plus(B).show();
//        System.out.println();
//
//        B.times(A).show();
//        System.out.println();
//
//        // shouldn't be equal since AB != BA in general    
//        System.out.println(A.times(B).eq(B.times(A)));
//        System.out.println();
//
//        Matrix b = Matrix.random(5, 1);
//        b.show();
//        System.out.println();
//
//        Matrix x = A.solve(b);
//        x.show();
//        System.out.println();
//
//        A.times(x).show();
//        
//    }
}

