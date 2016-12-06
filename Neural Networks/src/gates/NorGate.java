package gates;

import perceptron.Perceptron;

public class NorGate extends Perceptron implements GatesInterface{

	public NorGate(double threshold, double learningRate, double[] weights, int[][][] trainingData) {
		super(threshold, learningRate, weights, trainingData);
	}

	@Override
	public int testInputs(int input1, int input2) {
		int expected = getExpected(input1, input2);
		int bias = 1;
		int[][][] testData = {{{bias, input1, input2},{expected}}};
		return super.testInputs(testData);
	}

	@Override
	public int getExpected(int input1, int input2) {
		int result = 0;
		if(input1 == 0 && input2 == 0){
			result =1;
		}
		return result;
	}

	
}
