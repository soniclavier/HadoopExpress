package com.hex.ml;

import java.io.IOException;
import java.util.Map;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.mahout.classifier.ClassifierResult;
import org.apache.mahout.classifier.ConfusionMatrix;
import org.apache.mahout.classifier.ResultAnalyzer;
import org.apache.mahout.classifier.naivebayes.BayesUtils;
import org.apache.mahout.classifier.naivebayes.test.TestNaiveBayesDriver;
import org.apache.mahout.classifier.naivebayes.training.TrainNaiveBayesJob;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.iterator.sequencefile.PathFilters;
import org.apache.mahout.common.iterator.sequencefile.PathType;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileDirIterable;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.text.SequenceFilesFromDirectory;
import org.apache.mahout.vectorizer.SparseVectorsFromSequenceFiles;
import org.apache.mahout.utils.SplitInput;
import org.apache.mahout.math.Vector;




public class NBMLP {
	public static final String COMPLEMENTARY = "class";
	public static String vectorDir="/user/hue/twvec";
	private static Path inputDirP;
	private static Path outputDirP =new Path("/user/hue/twnewsseq");
	private static Path vectorDirP =new Path("/user/hue/twvec");
	private static Path trainingOutputP=new Path("/user/hue/twtrain");
	private static Path testOutputP =new Path("/user/hue/twtest");
	private static Path modelDirP =new Path("/user/hue/twmodel");
	private static Path labelIndexP=new Path("/user/hue/twlabel");
	private static Path finalTestP=new Path("/user/hue/twfinaltest");
	private static Path traintemp = new Path("temp");
	
	public static void main(String args[]) throws Exception{
		NBMLP nb= new NBMLP();
		System.out.println("Starting App");
		String output=nb.createModel(args[0], args[1]);
		System.out.println(output);
		System.out.println("Ending App");
		
		

	}   
	
	/*This method will take the input location of data-set in HDFS and percentage to split the 
	 * data into training and testing
	 * */   
	
    public String createModel(String per,String inputLoction) throws Exception{
    	String percentage;
    	checkAndCreatePath(inputLoction);	
    	if(null==per ||per.equals("")){
    		percentage ="20";
    		
    	}
    	else{
    		percentage =per;
    	}
		
		
	    //Convert document corpse into sequence file
		SequenceFilesFromDirectory sfd = new SequenceFilesFromDirectory();
		sfd.setConf(HEMLUtility.getConfiguration());
		sfd.run(new String[] {"--input",inputDirP.toString(), "--output", outputDirP.toString(), "--chunkSize", "64"});
		
		//Create the vector from sequence file			
		String[] vectorizingParameters = {"-i", outputDirP.toString(), "-o", vectorDirP.toString(), "-lnorm","-nv","-wt","tfidf"};
		SparseVectorsFromSequenceFiles sparseVactorizer = new SparseVectorsFromSequenceFiles();
		sparseVactorizer.setConf(HEMLUtility.getConfiguration());
		sparseVactorizer.run(vectorizingParameters);
		
		//Split the training and test data set
		SplitInput spi = new SplitInput();
		spi.setConf(HEMLUtility.getConfiguration());
		Path vectorDirtf = new Path(vectorDir+"/tfidf-vectors");
		spi.setInputDirectory(vectorDirtf);
		String[] splitInputs = {"--input",vectorDirtf.toString(),"--trainingOutput",trainingOutputP.toString(),"--testOutput",testOutputP.toString(),"--randomSelectionPct",percentage
				,"--overwrite","--sequenceFiles", "-xm", "sequential"};		
		spi.run(splitInputs);
		
		//Run the Naive Bayes Model Trainer to train the model.
		String [] trainingInput ={"-i",trainingOutputP.toString(),"-el","-o",modelDirP.toString(),"-li",labelIndexP.toString(),"-c"};
		TrainNaiveBayesJob tnj = new TrainNaiveBayesJob();
		tnj.setConf(HEMLUtility.getConfiguration());
		tnj.run(trainingInput);
		
		//Test the model using test data set
		String[] testInput={"-i",testOutputP.toString(),"-m",modelDirP.toString(),"-l",labelIndexP.toString(),"-o",finalTestP.toString(),"-c"};
		TestNaiveBayesDriver tnd = new TestNaiveBayesDriver();
		tnd.setConf(HEMLUtility.getConfiguration());
	    tnd.run(testInput);	
	    
        //load the labels
	     Map<Integer, String> labelMap = BayesUtils.readLabelIndex(HEMLUtility.getConfiguration(), new Path(labelIndexP.toString()));
	     //loop over the results and create the confusion matrix
	    // addOption(buildOption("testComplementary", "c", "test complementary?", false, false, String.valueOf(false)));
	    SequenceFileDirIterable<Text, VectorWritable> dirIterable = new SequenceFileDirIterable<Text, VectorWritable>(finalTestP,PathType.LIST,PathFilters.partFilter(), HEMLUtility.getConfiguration());
	    ResultAnalyzer analyzer = new ResultAnalyzer(labelMap.values(), "DEFAULT");
	    analyzeResults(labelMap, dirIterable, analyzer);	  
	    
	    //As of now I am putting it as true. once we will option addOption we will add the method 'hasOption("testComplementary")';
	    
	    // Get the model statistics.	        
	    ConfusionMatrix cm = analyzer.getConfusionMatrix();
	    double acuracy =analyzer.getConfusionMatrix().getAccuracy();
	    double kappa=analyzer.getConfusionMatrix().getKappa();
	    double reliability=analyzer.getConfusionMatrix().getReliability();
	    return("Confusion Matrix: " +cm.toString() + " Accuracy of the model: " +acuracy +" Kappa of the model: " +kappa + " reliability of the model: " +reliability);
		    		       	
	    }
	    
        /*Support method to get the different probabilities against different class*/
	    private static void analyzeResults(Map<Integer, String> labelMap, SequenceFileDirIterable<Text, VectorWritable> dirIterable, ResultAnalyzer analyzer) {
	    	for (Pair<Text, VectorWritable> pair : dirIterable) {
	    		int bestIdx = Integer.MIN_VALUE;
	    		double bestScore = Long.MIN_VALUE;
	    		for (Vector.Element element: pair.getSecond().get().all()) {	
	    			if (element.get() > bestScore) {
	    				bestScore = element.get();
	    				bestIdx = element.index();
	    			}
	    		}
	    		if (bestIdx != Integer.MIN_VALUE) {
	    			ClassifierResult classifierResult = new ClassifierResult(labelMap.get(bestIdx), bestScore);
	    			analyzer.addInstance(pair.getFirst().toString(), classifierResult);
   		        }
            }
       }
   
	   /*Method to check whether input path exists and if yes it will check for other locations
	    * where we wanted to create model, test,training files.
	    * */ 
	    private void checkAndCreatePath(String inputLocation){
			try {
				FileSystem fs = FileSystem.get(HEMLUtility.getConfiguration());
				inputDirP = new Path(inputLocation);
				if(!fs.exists(inputDirP)){
					System.out.println("Input path " +inputLocation + "does not exists.");
				System.exit(0);
			}
			else{
				if(fs.exists(outputDirP))
					fs.delete(outputDirP, true);
				if(fs.exists(vectorDirP))
					fs.delete(vectorDirP, true);
				if(fs.exists(trainingOutputP))
					fs.delete(vectorDirP, true);
				if(fs.exists(testOutputP))
					fs.delete(testOutputP, true);
				if(fs.exists(modelDirP))
					fs.delete(modelDirP, true);
				if(fs.exists(labelIndexP))
					fs.delete(labelIndexP, true);
				if(fs.exists(finalTestP))
					fs.delete(finalTestP, true);	
				if(fs.exists(traintemp))
					fs.delete(traintemp, true);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		
	}
    

    
	    
		

	

}
