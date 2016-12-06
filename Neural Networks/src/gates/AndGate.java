package gates;

import perceptron.Perceptron;

public class AndGate extends Perceptron implements GatesInterface{

	public AndGate(double threshold, double learningRate, double[] weights, int[][][] trainingData) {
		super(threshold, learningRate, weights, trainingData);
	}

	@Override
	public int testInputs(int input1, int input2) {
		int expected = getExpected(input1, input2);
		int[][][] testData = {{{input1, input2},{expected}}};
		return super.testInputs(testData);
	}

	@Override
	public int getExpected(int input1, int input2) {
		int result = 0;
		if(input1 == 1 && input2 == 1){
			result = 1;
		}
		return result;
	}

}
