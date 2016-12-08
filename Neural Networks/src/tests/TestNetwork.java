package tests;

import network.Network;

public class TestNetwork {
	
	/*
	 * You shouldn't have to change anything in Neuron or Connection classes, the Network uses
	 * them and you can just use the methods for Network that I use below. 
	 * 
	 * If you want to remove the console SOPs go to Network -> train() -> commment out "printResuilt()" 
	 * and the two SOP's below it.
	 */

	public static void main(String[] args) {
		/*
		 * Initialize the Neural Network
		 * 		input1 : Number of inputs/neurons per test. For this you can see that each 
		 * 				 array or training inputs consists of 3 inputs. Note that you don't have
		 * 				 to train with multiple sets it can be just one (like on test), but this 
		 * 				 way you can train many sets at once.
		 * 		input2 : You don't have to change this, it's number of hidden neurons, but you can.
		 * 		input3 : Similar input1, here specify number of outputs PER set of inputs. I would 
		 * 				 assume for our truth table we only need 1 here, have not tried to see how 
		 * 				 well it trains with more than 1 fyi...
		 */
		Network nn = new Network(3, 4, 1);
		
		// Then set the training inputs & outputs.
		double[][] training_inputs = { {1,1,0},{0,0,1}, {1,1,1}};
		double[][] training_outputs = { {1} , {0} , {1}};
		nn.setTrainingInputs(training_inputs);
		nn.setTrainingOutputs(training_outputs);
		
		// Then train it however many times you want.
		nn.train(5000);
		
		
		// Perform a test to see what it calculates. You can also perform multiple test sets here,
		// it will automatically increase the number of outputs.
		double[][] test = { {1,1,0} };
		int[][] result = nn.calcualteOutput(test);
		
		// Printing results.
		System.out.print("[");
		for(int i = 0; i < result.length; i++){
			System.out.print("[");
			for(int j = 0; j < result[0].length; j++){
				System.out.print(result[i][j]);
			}
			System.out.print("]");
		}
		System.out.print("]");
	}
}
