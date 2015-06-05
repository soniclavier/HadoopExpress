package com.hex.ml.logistic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.common.base.Charsets;
import com.hex.ml.utility.Configs;
import com.hex.ml.utility.DataJson;
import com.hex.ml.utility.HEMLUtility;

public class SGDMLP {
	

	private String hdfsWorkingDir;
	private String localWorkingDir;
	private static Path trainingData;
	private static Path testData;
	private static String traincsv;
	private static String testcsv;
	private static String modelfile;
	private static JSONObject sgdResult = null;

	
	public SGDMLP() {
		Configs conf = Configs.getInstance();
		hdfsWorkingDir = conf.getProperty("hdfs_workingdir");
		localWorkingDir = conf.getProperty("local_workingdir");
		
		trainingData = new Path(hdfsWorkingDir+"train.csv");
		testData = new Path(hdfsWorkingDir+"test.csv");
		traincsv = localWorkingDir+"train.csv";
		testcsv = localWorkingDir+"test.csv";
		
		modelfile = localWorkingDir+"sgdmode.bin";
	}
	
	public static void main(String[] args) {
		SGDMLP mpl = new SGDMLP();
		try {
			mpl.generateFeatures("path","Sepal_length,Sepal_width,Petal_length,Petal_width,Species","Numeric,Numeric,Numeric,Numeric");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String runSGD(String inputLocation, String targetClass,
			String varType,String header) throws JSONException {

		if (null == inputLocation || inputLocation.equals("")) {
			JSONObject error = new JSONObject();
			error.append("error", "Input Location not provided");
			return error.toString();
		} else {
			try {
				int ret = dataPrepration(inputLocation,header);
				if (ret == -1) {
					JSONObject error = new JSONObject();
					error.append("error", "Data preparation failed");
					return error.toString();
				}
				checkLocalPath();
				sgdResult = createSGDModel(traincsv, testcsv,targetClass,varType,header);

			} catch (Exception e) {
				e.printStackTrace();
				JSONObject error = new JSONObject();
				error.append("error","Training failed");
				return error.toString();
				
			}
			JSONObject success = new JSONObject();
			success.append("success", sgdResult);
			
			
			JSONArray features = generateFeatures(inputLocation,header,"Numeric,Numeric,Numeric,Numeric");
			success.put("features", features);
			//.................................. dummy code
			
			return success.toString();
		}

	}

	public int dataPrepration(String fileLocation,String header) throws Exception {
		File f = new File(fileLocation);
		if (!f.exists()) {
			return -1; // file does not exist
		}
		List<String> result = FileUtils.readLines(new File(fileLocation),
				Charsets.UTF_8);

		List<String> raw = result.subList(0, 149);
		Random random = new Random();
		Collections.shuffle(raw, random);
		List<String> train = raw.subList(0, 99);
		List<String> test = raw.subList(100, 149);
		checkAndCreatePath();
		System.out.println(header);
		writeCSV(train, trainingData,header);
		writeCSV(test, testData,header);
		return 0; // success
	}

	public void writeCSV(List<String> list, Path p,String header) throws IOException {
		FileSystem fs = FileSystem.get(HEMLUtility.getConfiguration());
		if (fs.exists(p)) {
			fs.delete(p, true);
		}
		OutputStream os = fs.create(p);
		BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os,
				"UTF-8"));
		br.write(header+"\n");
		for (int i = 0; i < list.size(); i++) {
			br.write(list.get(i) + "\n");
		}
		br.close();
		fs.close();

	}

	public JSONObject createSGDModel(String trainData, String testData,String targetClass,String varType,String header)
			throws Exception {

		String outputLoc = modelfile;
		String numCategories = "3";
		String[] predictors = header.split(",");
		String numPasses = "90";
		String rate = "300";
		String[] trainInput = formTrainInputArr(trainData,outputLoc,targetClass,numCategories,varType,predictors,numPasses,rate);
		TrainLogistic.train(trainInput);		
		System.out.println("training the model");
		
		
		String[] testInput = formTestInputArr(testData,outputLoc,true,true);

		
		JSONObject result = RunLogistic.mainToOutput(testInput,
				new PrintWriter(System.out, true));

		return result;

	}
	
	public String[] formTestInputArr(String testData,String modelLoc,boolean auc,boolean confusion) {
		List<String> inputList = new ArrayList<String>();
		inputList.add("--input");
		inputList.add(testData);
		inputList.add("--model");
		inputList.add(modelLoc);
		inputList.add("--auc");
		inputList.add("--confusion");
		String[] input = new String[inputList.size()];
		return inputList.toArray(input);
	}

	public String[] formTrainInputArr(String trainData,String outputLoc,String target,
			String numCategories,String types, String[] predictors, String numPasses,
			String rate) {
		String numFeatures = predictors.length+"";
		String[] input = new String[17 + predictors.length];
		input[0] = "--input";
		input[1] = trainData;
		input[2] = "--output";
		input[3] = outputLoc;
		input[4] = "--target";
		input[5] = target;
		input[6] = "--categories";
		input[7] = numCategories;
		input[8] = "--types";
		input[9] = types;
		input[10] = "--features";
		input[11] = numFeatures;
		input[12] = "--passes";
		input[13] = numPasses;
		input[14] = "--rate";
		input[15] = rate;
		input[16] = "--predictors";
		for (int i = 0; i < predictors.length; i++) {
			input[17 + i] = predictors[i];
		}

		return input;
	}

	/*
	 * Method to check whether input path exists and if yes it will check for
	 * other locations where we wanted to create model, test,training files.
	 */
	private void checkAndCreatePath() {
		try {
			FileSystem fs = FileSystem.get(HEMLUtility.getConfiguration());
			if (fs.exists(trainingData))
				fs.delete(trainingData, true);
			if (fs.exists(testData))
				fs.delete(testData, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void checkLocalPath() {
		File trainFile = new File(traincsv);
		File testFile = new File(testcsv);
		File modelFile = new File(modelfile);

		if (modelFile.exists()) {
			modelFile.delete();
		}

		if (trainFile.exists()) {
			trainFile.delete();
		}

		if (testFile.exists()) {
			testFile.delete();
		}

		try {
			FileSystem fs = FileSystem.get(HEMLUtility.getConfiguration());
			fs.copyToLocalFile(trainingData, new Path(traincsv));
			fs.copyToLocalFile(testData, new Path(testcsv));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/*
	 * Generates the features from the data set. This is used by the predict to populate the max,min for numeric and values for categorical types.
	 */
	
	public JSONArray generateFeatures(String path,String columns,String types) throws JSONException {
		
		System.out.println(columns);
		//String checked = columns;
		//String[] selected= checked.split(",");;
		//get array of column names and create a hash map of the original column list.
		
		String [] columnArr = columns.split(",");
		String[] typeArr = types.split(",");
		
		String[] originalColumnName = new String[columnArr.length-1];
		for(int i=0;i<columnArr.length-1;i++) {
			originalColumnName[i]= columnArr[i];
		}
		
		HashMap<Integer, String> columnNames = new HashMap<Integer, String>();
		for(int i=0;i<originalColumnName.length;i++){
			columnNames.put(i,typeArr[i]);
		}
		
		DataJson dataJson = new DataJson();
		return dataJson.getFeatureJSONArr(columnNames, path, originalColumnName);
		/*JSONObject dummy = new JSONObject();
		dummy.append("name","sepal_color");
		JSONObject dummyCatType = new JSONObject();
		dummyCatType.append("varType","categorical");
		dummyCatType.append("values", "green,yellow");
		dummy.append("type",dummyCatType);
		
		JSONObject dummy2 = new JSONObject();
		dummy2.append("name","sepal_width");
		JSONObject dummyNumType = new JSONObject();
		dummyNumType.append("varType","numeric");
		dummyNumType.append("min", "10");
		dummyNumType.append("max", "100");
		dummy2.append("type",dummyNumType);
		
		features.put(dummy);
		features.put(dummy2);*/
	}


}
