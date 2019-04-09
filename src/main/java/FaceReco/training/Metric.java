package FaceReco.training;

import FaceReco.jama.Matrix;

public interface Metric {
	double getDistance(Matrix a, Matrix b);
}
