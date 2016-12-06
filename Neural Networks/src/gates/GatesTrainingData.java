package gates;

public class GatesTrainingData {
	
	public static final double[] AND_WEIGHTS = {0.0, 0.0};
	public static final double[] OR_WEIGHTS = {0.0, 0.0};
	public static final double[] NOR_WEIGHTS = {0.0, 0.0, 0.0};
	
	public static final int[][][] AND_TRAININGSET = {
											            {{0, 0}, {0}},
											            {{0, 1}, {0}},
											            {{1, 0}, {0}},
											            {{1, 1}, {1}},
											        };
	
	public static final int[][][] OR_TRAININGSET = {
											            {{0, 0}, {0}},
											            {{0, 1}, {1}},
											            {{1, 0}, {1}},
											            {{1, 1}, {1}},
											        };
	
	public static final int[][][] NOR_TRAININGSET = {
											            {{1, 0, 0}, {1}},
											            {{1, 0, 1}, {0}},
											            {{1, 1, 0}, {0}},
											            {{1, 1, 1}, {0}},
											        };


}
