import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
	// create a seam carver object based on the given picture
	private double energy[][];
	private int rgb[][];

	public SeamCarver(Picture picture) {
		if (picture == null) {
			throw new java.lang.IllegalArgumentException();
		}


		rgb = new int[picture.height()][picture.width()];
		for (int i=0; i<picture.height(); i++) {
			for (int j=0; j<picture.width(); j++) {
				rgb[i][j] = picture.getRGB(j, i);
			}
		}

		calcEnergyMatrix();
	}

	// current picture
	public Picture picture() {
		Picture p = new Picture(rgb[0].length, rgb.length);
		for (int i=0; i<height(); i++) {
			for (int j=0; j<width(); j++) {
				p.setRGB(j, i, rgb[i][j]);
			}
		}
		return p;
	}

	// width of current picture
	public int width() {
		return rgb[0].length;
	}

	// height of current picture
	public int height() {
		return rgb.length;
	}

	private double getDelta(int a, int b) {
		int ra = (a >> 16) & 0xFF;
		int ga = (a >>  8) & 0xFF;
		int ba = (a >>  0) & 0xFF;

		int rb = (b >> 16) & 0xFF;
		int gb = (b >>  8) & 0xFF;
		int bb = (b >>  0) & 0xFF;

		return Math.pow(ra - rb,2) + Math.pow(ga - gb,2) + Math.pow(ba - bb,2);
	}

	private double calcEnergy(int row, int col) {
		if (row == 0 || col == 0 || row == height()-1 || col == width()-1) {
			return 1000;
		}

		int l = rgb[row][col-1];
		int r = rgb[row][col+1];
		int u = rgb[row-1][col];
		int d = rgb[row+1][col];
		Double delta1 = getDelta(l, r);
		Double delta2 = getDelta(u, d);

		return Math.sqrt(delta1 + delta2);
	}

	// energy of pixel at column x and row y
	public double energy(int x, int y) {
		if (x < 0 || y < 0 || x >= width() || y >= height()) {
			throw new java.lang.IllegalArgumentException();
		}
		return energy[y][x];
	}

	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		transposeRGB();
		transposeEnergy();
		int id[] = findVerticalSeam();
		transposeRGB();
		transposeEnergy();
		return id;
	}

	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		double[][] minSumEnengy = new double[height()][width()];
		int[][] link = new int[height()][width()];

		for (int i=0; i<height(); i++) {
			for (int j=0; j<width(); j++) {
				minSumEnengy[i][j] = Double.MAX_VALUE;
			}
		}

		for (int i=0; i<height()-1; i++) {
			for (int j=0; j<width(); j++) {
				if (i==0) {
					minSumEnengy[i][j] = energy[i][j];
				}

				for (int k=-1; k<=1; k++) {
					if (j+k<0 || j+k>=width()) {
						continue;
					}

					double min = minSumEnengy[i][j] + energy[i+1][j+k];
					if (min < minSumEnengy[i+1][j+k]) {
						minSumEnengy[i+1][j+k] = min;
						link[i+1][j+k] = j;
					}
				}
			}
		}

		double min = Double.MAX_VALUE;
		int minIndex = 0;
		for (int j=0; j<width(); j++) {
			if (minSumEnengy[height()-1][j] < min) {
				min = minSumEnengy[height()-1][j];
				minIndex = j;
			}
		}

		int [] id = new int[height()];
		for (int i=height()-1; i>=0; i--) {
			id[i] = minIndex;
			minIndex = link[i][minIndex];
		}
		return id;
	}

	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		if (seam == null || seam.length != width()) {
			throw new java.lang.IllegalArgumentException();
		}

		checkSeamDistance(seam);

		transposeRGB();
		removeVerticalSeam(seam);
		transposeRGB();
		transposeEnergy();
	}

	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		if (seam == null || seam.length != height()) {
			throw new java.lang.IllegalArgumentException();
		}
		checkSeamDistance(seam);

		int [][] newRgb = new int [height()][width()-1];

		for (int i=0; i<height(); i++) {
			int k=0;
			for (int j=0; j<width(); j++) {
				if (seam[i] <0 || seam[i] >= width()) {
					throw new java.lang.IllegalArgumentException();
				}

				if (j==seam[i]) {
					continue;
				}

				newRgb[i][k++] = rgb[i][j];
			}
		}

		rgb = newRgb;
		calcEnergyMatrix();
	}

	private void transposeEnergy() {
		double [][] newEnergy = new double [energy[0].length][energy.length];
		for (int i=0; i<energy.length; i++) {
			for (int j=0; j<energy[0].length; j++) {
				newEnergy[j][i] = energy[i][j];
			}
		}

		energy = newEnergy;
	}

	private void transposeRGB() {
		int [][] newRgb = new int [width()][height()];
		for (int i=0; i<height(); i++) {
			for (int j=0; j<width(); j++) {
				newRgb[j][i] = rgb[i][j];
			}
		}

		rgb = newRgb;
	}

	private void calcEnergyMatrix() {
		energy = new double[height()][width()];
		for (int i=0; i<height(); i++) {
			for (int j=0; j<width(); j++) {
				energy[i][j] = calcEnergy(i, j);
			}
		}
	}

	private void checkSeamDistance(int[] seam) {
		for (int i=0; i<seam.length-1; i++) {
			if (Math.abs(seam[i]-seam[i+1]) > 1) {
				throw new java.lang.IllegalArgumentException();
			}
		}
	}
}