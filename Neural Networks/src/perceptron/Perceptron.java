package perceptron;

import java.util.Arrays;

public class Perceptron {
	
	private double threshold;
	private double learningRate;
	private double[] weights;
	private int[][][] trainingData;
	
	public Perceptron(double threshold, double learningRate, double[] weights, int[][][] trainingData) {
		this.threshold = threshold;
		this.learningRate = learningRate;
		this.weights = weights;
		this.trainingData = trainingData;
	}
	
	public void Train(int trainingCount){
		int round = 1;
		 while(round <= trainingCount){
			 	System.out.println("---Round " + round + "---");
	            // Loop over training data
	            for(int i=0; i < trainingData.length; i++){
	                System.out.println("Starting weights: " + Arrays.toString(weights));
	                // Calculate weighted input
	                double weightedSum = 0;
	                for(int ii=0; ii < trainingData[i][0].length; ii++) {
	                    weightedSum += trainingData[i][0][ii] * weights[ii];
	                }

	                // Calculate output
	                int output = 0;
	                if(threshold <= weightedSum){
	                    output = 1;
	                }

	                System.out.println("Target output: " + trainingData[i][1][0] + ", "
	                        + "Actual Output: " + output);
	                                
	                // Calculate error
	                int error = trainingData[i][1][0] - output;
	                
	                // Update weights
	                for(int ii=0; ii < trainingData[i][0].length; ii++) {
	                    weights[ii] += learningRate * error * trainingData[i][0][ii];
	                }

	                System.out.println("New weights: " + Arrays.toString(weights));
	                System.out.println();
	            }
                round++;
	    }
         System.out.println("Final Training Weights: " + Arrays.toString(weights));
         System.out.println("--- Completed Training ---");
         
	}
	
	protected int testInputs(int[][][] testData){
		
        System.out.println("\n[Test Inputs] Starting weights: " + Arrays.toString(weights));
        
        double weightedSum = 0;
        for(int ii=0; ii < testData[0][0].length; ii++) {
            weightedSum += testData[0][0][ii] * weights[ii];
        }

        int output = 0;
        if(threshold <= weightedSum){
            output = 1;
        }

        System.out.println("Target output: " + testData[0][1][0] + ", "
                + "Actual Output: " + output);
                        
        int error = testData[0][1][0] - output;
        
        for(int ii=0; ii < testData[0][0].length; ii++) {
            weights[ii] += learningRate * error * testData[0][0][ii];
        }

        System.out.println("New weights: " + Arrays.toString(weights));
        System.out.println();
		
		return output;
	}

}
