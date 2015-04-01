package com.hex.ml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Progressable;
import com.google.common.base.Charsets;

public class SGDMLP {
	private static Path trainingData = new Path("/user/hue/irisdataTrain/train.csv");
	private static Path testData =new Path("/user/hue/irisdataTest/test.csv");
	private static String traincsv ="/tmp/irisdataTrain/train.csv";
	private static String testcsv="/tmp/irisdataTest/test.csv";
	private static String modelfile ="/tmp/IrisDataModel/sgdmodel.bin";
	private static String result;
	
	
	
	public String runSGD(String inputLocation,String targetClass,String varType){
		
		if(null==inputLocation||inputLocation.equals("")){
			return "input location  not provided";
		}else{
			//checkAndCreatePath();
			try {
				dataPrepration(inputLocation);
				checkLocalPath();
				result=createSGDModel(traincsv, testcsv);
				
			} catch (Exception e) {				
				e.printStackTrace();
			}
			
			
			return result;
		}
		
	}
	
	public void dataPrepration(String fileLocation) throws Exception {
		List<String> result =FileUtils.readLines(new File(fileLocation),Charsets.UTF_8);
		  
		  List<String> raw = result.subList(0, 149);
		  Random random = new Random();
		  Collections.shuffle(raw, random);
		  List<String> train = raw.subList(0, 99);
		  List<String> test = raw.subList(100, 149);	
		  checkAndCreatePath();
		  writeCSV(train,trainingData);
		  writeCSV(test,testData);		 	  
	}
	
	public void writeCSV(List<String> list ,Path p) throws IOException{
		FileSystem fs = FileSystem.get(HEMLUtility.getConfiguration());	
		if ( fs.exists( p )) { 
			  fs.delete( p, true ); 
	    } 
		OutputStream os = fs.create( p, new Progressable() {
		          public void progress() {
		            
		          } });
		  BufferedWriter br = new BufferedWriter( new OutputStreamWriter( os, "UTF-8" ) );
		  br.write("Sepal_length"+","+"Sepal_width"+","+"Petal_length"+","+"Petal_width"+","+"Species"+"\n");
			 for(int i=0;i< list.size();i++){
		    	  br.write(list.get(i)+"\n");
		      }
		  br.close();
		  fs.close();
		
	}
	
	public String createSGDModel(String trainData,String testData) throws Exception{		
		
		String []trainLogisticInputs ={"--input",trainData,"--output","/tmp/IrisDataModel/sgdmodel.bin","--target","Species","--categories","3","--predictors"
				,"Sepal_length", "Sepal_width", "Petal_length" ,"Petal_width", "Species","--types","numeric","--features", "4", "--passes", "90", "--rate", "300"};
		TrainLogistic2.main(trainLogisticInputs);
		System.out.println("training the model");
		String [] testLogisticInput ={"--input",testData,"--model","/tmp/IrisDataModel/sgdmodel.bin","--auc", "--confusion"};
		String resultString=RunLogistic2.mainToOutput(testLogisticInput, new PrintWriter(System.out, true));
	//	System.out.println("testing the model");
		return resultString.toString();
		
	}
	
	/*Method to check whether input path exists and if yes it will check for other locations
	    * where we wanted to create model, test,training files.
	    * */ 
	    private void checkAndCreatePath(){
			try {
				FileSystem fs = FileSystem.get(HEMLUtility.getConfiguration());
				if(fs.exists(trainingData))
					fs.delete(trainingData, true);
				if(fs.exists(testData))
					fs.delete(testData, true);				
				} catch (IOException e) {
					e.printStackTrace();
		   }
			
	    }
	    
	    private void checkLocalPath(){
	    	File  trainFile= new File(traincsv);
	    	File testFile = new File(testcsv);
	    	File tmpDir1 = new File("/tmp/irisdataTrain");
	    	File tmpDir2 = new File("/tmp/irisdataTest");
	    	File modelFile = new File(modelfile);
	    	
	    	if(modelFile.exists()){
	    		modelFile.delete();
	    	}
	    			
	    	if(trainFile.exists()){
	    		trainFile.delete();
	    	}
	    	
	    	if(testFile.exists()){
	    		testFile.delete();
	    	}
	    	
	    	if(tmpDir1.exists()){
	    		tmpDir1.delete();
	    		tmpDir1.mkdir();
	    	}
	    	
	    	if(tmpDir2.exists()){
	    		tmpDir2.delete();
	    		tmpDir2.mkdir();
	    	}
	    	try {
				FileSystem fs = FileSystem.get(HEMLUtility.getConfiguration());
				fs.copyToLocalFile(trainingData,new Path(traincsv));
				fs.copyToLocalFile(testData,new Path(testcsv));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	
	    	
	    }
				

}
