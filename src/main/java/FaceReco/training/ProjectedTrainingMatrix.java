package FaceReco.training;

import FaceReco.jama.Matrix;

public class ProjectedTrainingMatrix {
	Matrix matrix;
	String label;
	double distance = 0;

	public ProjectedTrainingMatrix(Matrix m, String l) {
		this.matrix = m;
		this.label = l;
	}
}
