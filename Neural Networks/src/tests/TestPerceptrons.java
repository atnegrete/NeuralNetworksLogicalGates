package tests;

import gates.AndGate;
import gates.GatesTrainingData;
import gates.NorGate;
import gates.OrGate;

public class TestPerceptrons {
	
	/*
	 * The Perceptron Gates are really easy to use and you can use them if you want. 
	 * They are pretty limited and basically I could only make three gates out of them. 
	 * These gates extend Perceptron thus you can just create as many objects as you want
	 *  and use them as you need. All they can do is Train() and testInputs()
	 */
	public static void main(String args[]){
		AndGate and = new AndGate(1, .1, GatesTrainingData.AND_WEIGHTS,GatesTrainingData.AND_TRAININGSET);
		and.Train(10);
		
		and.testInputs(0, 0);
		and.testInputs(0, 1);
		and.testInputs(1, 0);
		and.testInputs(1, 1);
		
		OrGate or = new OrGate(1, .1, GatesTrainingData.OR_WEIGHTS,GatesTrainingData.OR_TRAININGSET);
		or.Train(10);
		
		or.testInputs(0, 0);
		or.testInputs(0, 1);
		or.testInputs(1, 0);
		or.testInputs(1, 1);
		
		NorGate nor = new NorGate(1, .1, GatesTrainingData.NOR_WEIGHTS,GatesTrainingData.NOR_TRAININGSET);
		nor.Train(50);
		
		nor.testInputs(0, 0);
		nor.testInputs(0, 1);
		nor.testInputs(1, 0);
		nor.testInputs(1, 1);
	}
}
