package amten.ml.examples;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.hexabeast.riskisep.GameScreen;

import amten.ml.NNParams;
import amten.ml.matrix.Matrix;

/**
 * Examples of using NeuralNetwork for classification.
 *
 * @author Johannes Amtén
 */
public class NNTest {
	
	public static amten.ml.NeuralNetwork nn;

    /**
     * Performs classification of Handwritten digits,
     * using a subset (1000 rows) from the Kaggle Digits competition.
     * <br></br>
     * Uses file /example_data/Kaggle_Digits_1000.csv
     *
     * @see <a href="http://www.kaggle.com/c/digit-recognizer">http://www.kaggle.com/c/digit-recognizer</a></a>
     */
	public static void init()
	{
		
		
		NNParams params = new NNParams();
        params.numClasses = 1; // 10 digits to classify
        params.hiddenLayerParams = new NNParams.NNLayerParams[] { new NNParams.NNLayerParams(100),new NNParams.NNLayerParams(100),new NNParams.NNLayerParams(100) };
        params.maxIterations = 100;
        params.learningRate = 0.02;
        params.convergenceThreshold=0.0002;

        //long startTime = System.currentTimeMillis();
        nn = new amten.ml.NeuralNetwork(params);
        
        Matrix m = new Matrix(1,GameScreen.apays.pays.size()*2+1);
        m.fill(0);
        Matrix x = m.getColumns(1, -1);
        Matrix y = m.getColumns(0, 0);
        try {
			nn.train(x, y);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        FileHandle file1 = Gdx.files.local("nn/Layer2");
		if(file1.file().exists())
		{
			nn.load();
		}
			
	}
	
	public static double feed(int[] state)
	{
		double[] dstate = new double[state.length];
		for(int i=0;i<state.length;i++)
		{
			dstate[i] = state[i];
		}
		Matrix datastate = new Matrix(new double[][] {dstate});
		Matrix prediction;
		try {
			prediction = nn.getPredictions(datastate);
			return prediction.get(0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("FEED ERROR");
		return 0;
	}
	
    public static void train(Matrix data) throws Exception {

        // Read data from CSV-file
        //int headerRows = 1;
        //char separator = ',';
        // = MatrixUtils.readCSV("example_data/Kaggle_Digits_1000.csv", separator, headerRows);

        // Split data into training set and crossvalidation set.
        //float crossValidationPercent = 33;
        //Matrix[] split = MatrixUtils.split(data, crossValidationPercent, 0);
        Matrix dataTrain = data;
        //Matrix dataCV = split[1];

        // First column contains the classification label. The rest are the indata.
        Matrix xTrain = dataTrain.getColumns(1, -1);
        Matrix yTrain = dataTrain.getColumns(0, 0);
        //Matrix xCV = dataCV.getColumns(1, -1);
        //Matrix yCV = dataCV.getColumns(0, 0);

       
        nn.train(xTrain, yTrain);
        //System.out.println("\nTraining time: " + String.format("%.3g", (System.currentTimeMillis() - startTime) / 1000.0) + "s");

        /*int[] predictedClasses = nn.getPredictedClasses(xTrain);
        int correct = 0;
        for (int i = 0; i < predictedClasses.length; i++) {
            if (predictedClasses[i] == yTrain.get(i, 0)) {
                correct++;
            }
        }
        System.out.println("Training set accuracy: " + String.format("%.3g", (double) correct/predictedClasses.length*100) + "%");

        
        Matrix predictions = nn.getPredictions(xCV);
        float error = 0;
        for (int i = 0; i < predictions.numRows(); i++) {
            error += Math.round(predictions.get(i, 0)) == yCV.get(i, 0)?0:1;
        }
        error/=predictions.numRows();
        System.out.println("Crossvalidation set root mean squared error: " + String.format("%.4g", error));*/
        System.out.println();	
        FileHandle file1 = Gdx.files.local("nn.json");
		
		nn.save();
    }

    public static void main(String[] args) throws Exception {
        init();
    }
}
