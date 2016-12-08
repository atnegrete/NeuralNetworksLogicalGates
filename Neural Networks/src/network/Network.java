package network;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Network {
	static {
		Locale.setDefault(Locale.ENGLISH);
	}

	final boolean isTrained = false;
	final DecimalFormat df;
	final Random rand = new Random();
	final ArrayList<Neuron> input_layer = new ArrayList<Neuron>();
	final ArrayList<Neuron> hidden_layer = new ArrayList<Neuron>();
	final ArrayList<Neuron> output_layer = new ArrayList<Neuron>();
	final Neuron bias = new Neuron();
	final int[] layers;
	final int randomWeightMultiplier = 1;

	final double epsilon = 0.00000000001;
	final double learningRate = 0.9f;
	final double momentum = 0.7f;

	// Initialize with random values.
	double result_outputs[][]; 
	double output[];
	// training_inputs given. 
	double training_inputs[][];
	// Output expected. 
	double training_expected_outputs[][];

	/*
	 * @param int input Number of training_inputs/neurons.
	 * 
	 * @param int hidden Number of hidden nuerons.
	 * 
	 * @param int output Number of outputs/nerons.
	 * 
	 */
	public Network(int inputs, int hidden, int output) {
		this.layers = new int[] { inputs, 4, output };
		df = new DecimalFormat("#.0#");

		/*
		 * Create the Neurons and add to their layers. Then add neuron connections.
		 */
		for (int i = 0; i < layers.length; i++) {
			if (i == 0) {
				for (int j = 0; j < layers[i]; j++) {
					Neuron neuron = new Neuron();
					input_layer.add(neuron);
				}
			} else if (i == 1) {
				for (int j = 0; j < layers[i]; j++) {
					Neuron neuron = new Neuron();
					neuron.addInConnectionsS(input_layer);
					neuron.addBiasConnection(bias);
					hidden_layer.add(neuron);
				}
			}

			else if (i == 2) {
				for (int j = 0; j < layers[i]; j++) {
					Neuron neuron = new Neuron();
					neuron.addInConnectionsS(hidden_layer);
					neuron.addBiasConnection(bias);
					output_layer.add(neuron);
				}
			} 
		}

		// Init random weights.
		for (Neuron neuron : hidden_layer) {
			ArrayList<Connection> connections = neuron.getAllInConnections();
			for (Connection conn : connections) {
				double newWeight = getRandom();
				conn.setWeight(newWeight);
			}
		}
		for (Neuron neuron : output_layer) {
			ArrayList<Connection> connections = neuron.getAllInConnections();
			for (Connection conn : connections) {
				double newWeight = getRandom();
				conn.setWeight(newWeight);
			}
		}

		// Reset counters.
		Neuron.counter = 0;
		Connection.counter = 0;

	}
	
	public void setTrainingInputs(double[][] training_inputs) {
		this.training_inputs = training_inputs;
	}

	public void setTrainingOutputs(double[][] training_expected_outputs) {
		this.training_expected_outputs = training_expected_outputs;
		int sets = training_expected_outputs.length;
		int size = training_expected_outputs[0].length;
		result_outputs = new double[sets][size];
		for(int i = 0; i < training_expected_outputs.length; i++){
			for(int j = 0; j < training_expected_outputs[i].length; j++){
				result_outputs[i][j] = -1;
			}
		}
		
	}

	double getRandom() {
		return randomWeightMultiplier * (rand.nextDouble() * 2 - 1);
	}

	public void applyBackpropagation(double expectedOutput[]) {

		for (int i = 0; i < expectedOutput.length; i++) {
			double d = expectedOutput[i];
			if (d < 0 || d > 1) {
				if (d < 0)
					expectedOutput[i] = 0 + epsilon;
				else
					expectedOutput[i] = 1 - epsilon;
			}
		}

		int i = 0;
		for (Neuron n : output_layer) {
			ArrayList<Connection> connections = n.getAllInConnections();
			for (Connection con : connections) {
				double ak = n.getOutput();
				double ai = con.leftNeuron.getOutput();
				double desiredOutput = expectedOutput[i];

				double partialDerivative = -ak * (1 - ak) * ai * (desiredOutput - ak);
				double deltaWeight = -learningRate * partialDerivative;
				double newWeight = con.getWeight() + deltaWeight;
				con.setDeltaWeight(deltaWeight);
				con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
			}
			i++;
		}

		// Update weights on Hidden Layer.
		for (Neuron n : hidden_layer) {
			ArrayList<Connection> connections = n.getAllInConnections();
			for (Connection con : connections) {
				double aj = n.getOutput();
				double ai = con.leftNeuron.getOutput();
				double sumKoutputs = 0;
				int j = 0;
				for (Neuron out_neu : output_layer) {
					double wjk = out_neu.getConnection(n.id).getWeight();
					double desiredOutput = (double) expectedOutput[j];
					double ak = out_neu.getOutput();
					j++;
					sumKoutputs = sumKoutputs + (-(desiredOutput - ak) * ak * (1 - ak) * wjk);
				}

				double partialDerivative = aj * (1 - aj) * ai * sumKoutputs;
				double deltaWeight = -learningRate * partialDerivative;
				double newWeight = con.getWeight() + deltaWeight;
				con.setDeltaWeight(deltaWeight);
				con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
			}
		}
	}

	public void train(int trainCycles) {
		double error = 1;
		for (int i = 0; i < trainCycles; i++) {
			error = 0;
			for (int p = 0; p < training_inputs.length; p++) {
				setInput(training_inputs[p]);

				activate();

				output = getOutput();
				result_outputs[p] = output;

				for (int j = 0; j < training_expected_outputs[p].length; j++) {
					double err = Math.pow(output[j] - training_expected_outputs[p][j], 2);
					error += err;
				}

				applyBackpropagation(training_expected_outputs[p]);
			}
		}

		printResult();
		System.out.println("Sum of squared errors = " + error);
		System.out.println("##### EPOCH " + trainCycles + "\n");
	}
	
	public void setInput(double training_inputs[]) {
		for (int i = 0; i < input_layer.size(); i++) {
			input_layer.get(i).setOutput(training_inputs[i]);
		}
	}

	public double[] getOutput() {
		double[] outputs = new double[output_layer.size()];
		for (int i = 0; i < output_layer.size(); i++)
			outputs[i] = output_layer.get(i).getOutput();
		return outputs;
	}

	public void activate() {
		for (Neuron n : hidden_layer)
			n.calculateOutput();
		for (Neuron n : output_layer)
			n.calculateOutput();
	}

	void printResult() {
		System.out.println("--Training results.--");
		for (int p = 0; p < training_inputs.length; p++) {
			System.out.print("training_inputs: ");
			for (int x = 0; x < layers[0]; x++) {
				System.out.print(training_inputs[p][x] + " ");
			}

			System.out.print("EXPECTED: ");
			for (int x = 0; x < layers[2]; x++) {
				System.out.print(this.training_expected_outputs[p][x] + " ");
			}

			System.out.print("ACTUAL: ");
			for (int x = 0; x < layers[2]; x++) {
				System.out.print(result_outputs[p][x] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	String weightKey(int neuronId, int conId) {
		return "N" + neuronId + "_C" + conId;
	}
	
	public int[][] calcualteOutput(double[][] inputs){
		int[][] calculated_output = new int[inputs.length][1];
		
		for (int p = 0; p < inputs.length; p++) {
			setInput(inputs[p]);

			activate();

			double[] rounded_outputs = getOutput();

			for(int i = 0; i < rounded_outputs.length; i++){
				if(rounded_outputs[i] > .5){
					calculated_output[p][i] = 1;
				}else{
					calculated_output[p][i] = 0;
				}
			}
			
		}

		return calculated_output;
	}
	
    public void printWeightUpdate() {
        System.out.println("printWeightUpdate, put this i trainedWeights() and set isTrained to true");
        // weights for the hidden layer
        for (Neuron n : hidden_layer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String w = df.format(con.getWeight());
                System.out.println("weightUpdate.put(weightKey(" + n.id + ", "
                        + con.id + "), " + w + ");");
            }
        }
        // weights for the output layer
        for (Neuron n : output_layer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String w = df.format(con.getWeight());
                System.out.println("weightUpdate.put(weightKey(" + n.id + ", "
                        + con.id + "), " + w + ");");
            }
        }
        System.out.println();
    }
	
	public ArrayList<ArrayList<Double>> getAllWeights(boolean show) {
		ArrayList<ArrayList<Double>> weights = new ArrayList<ArrayList<Double>>();
        // Weights for hidden layer Neurons.
		int neuron_count = 0;
        for (Neuron n : hidden_layer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            weights.add(new ArrayList<Double>());
    		if(show) { System.out.print("\n--Hidden Neuron:" + n.id +" --\n"); }
            for (Connection con : connections) {
                double w = con.getWeight();
                weights.get(neuron_count).add(w);
                if(show) { System.out.println("   Connection: " + con.id + " Weight:" + w); }
            }
            neuron_count++;
        }
        // Weights for output layer Neurons.
        for (Neuron n : output_layer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            weights.add(new ArrayList<Double>());
            if(show) { System.out.println("\nOutput Neuron:" + n.id + "--\n"); }
            for (Connection con : connections) {
                double w = con.getWeight();
                weights.get(neuron_count).add(w);
                if(show) { System.out.println("   Connection: " + con.id + " Weight:" + w); }
            }
            neuron_count++;
        }
		return weights;
    }

}