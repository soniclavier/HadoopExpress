package com.hex.ml;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.cli2.util.HelpFormatter;
import org.apache.mahout.math.DenseMatrix;
import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.cf.taste.impl.common.FullRunningAverageAndStdDev;
import org.apache.mahout.cf.taste.impl.common.RunningAverageAndStdDev;
import org.apache.mahout.classifier.sgd.CsvRecordFactory;
import org.apache.mahout.classifier.sgd.LogisticModelParameters;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;


import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.util.Locale;

public final class RunLogistic2 {

 private static String inputFile;
 private static String modelFile;
 private static boolean showAuc;
 private static boolean showScores;
 private static boolean showConfusion;
 private static StringBuilder retrunString = new StringBuilder();
 //I am putting the length as hard coded now but we will take the lables from the set of categories.
 private static Matrix confusionMatrix = new DenseMatrix(3, 3);
  private static int total=0;

 private RunLogistic2() {
 }

 public static void main(String[] args) throws Exception {
   mainToOutput(args, new PrintWriter(System.out, true));
 }

 static String mainToOutput(String[] args, PrintWriter output) throws Exception {
   if (parseArgs(args)) {
     if (!showAuc && !showConfusion && !showScores) {
       showAuc = true;
       showConfusion = true;
       showScores =true;
     }

          
     LogisticModelParameters lmp = LogisticModelParameters.loadFrom(new File(modelFile));

     CsvRecordFactory csv = lmp.getCsvRecordFactory();
     OnlineLogisticRegression lr = lmp.createRegression();
     BufferedReader in = TrainLogistic2.open(inputFile);
     String line = in.readLine();
     csv.firstLine(line);
     line = in.readLine();
     if (showScores) {
       output.printf(Locale.ENGLISH, "\"%s\",\"%s\",\"%s\"\n", "target", "model-output", "log-likelihood");
     }
     int k =3;
     int[][] confusion = new int[k][k];
     while (line != null) {
       Vector v = new SequentialAccessSparseVector(lmp.getNumFeatures());
       int target = csv.processLine(line, v);
      System.out.println("Target value is: "+target);
       
      int classResult = lr.classifyFull(v).maxValueIndex();
       System.out.println("classresult: " + classResult );
      if (target == classResult) {
    	  confusion[classResult][classResult]++;
      } else {
    	  confusion[target][classResult]++;
      }
      
     
      confusionMatrix.set(target, classResult,confusionMatrix.get(target, classResult)+1);
      line = in.readLine();
     }
     in.close();
     
    String[][] confStr = new String[k+1][k+1];
    confStr[0][1] = "Iris-Setosa";
    confStr[0][2] = "Iris-Versicolor";
    confStr[0][3] = "Iris-Virginica";
    
    confStr[1][0] = "Iris-Setosa";
    confStr[2][0] = "Iris-Versicolor";
    confStr[3][0] = "Iris-Virginica";
    
    for(int i=0;i<k;i++) {
    	for(int j=0;j<k;j++) {
    		confStr[i+1][j+1] = confusion[i][j]+"";
    	}
    }
    
    for(int i=0;i<k+1;i++) {
    	System.out.println();
    	retrunString.append("\n");
    	for(int j=0;j<k+1;j++) {
    		System.out.print(confStr[i][j]+" ");
    		retrunString.append(confStr[i][j]+" ");
    	}
    }
    
    
    
    

     /*if (showAuc) {
       output.printf(Locale.ENGLISH, "AUC = %.2f\n", collector.auc());
     }
     if (showConfusion) {
       Matrix m = collector.confusion();
       output.printf(Locale.ENGLISH, "confusion: [[%.1f, %.1f], [%.1f, %.1f]]\n",
         m.get(0, 0), m.get(1, 0), m.get(0, 1), m.get(1, 1));
       m = collector.entropy();
       output.printf(Locale.ENGLISH, "entropy: [[%.1f, %.1f], [%.1f, %.1f]]\n",
         m.get(0, 0), m.get(1, 0), m.get(0, 1), m.get(1, 1));
     }*/
    if (showConfusion) {
        output.printf(Locale.ENGLISH, "confusion: [[%.1f, %.1f,%.1f], [%.1f, %.1f,%.1f], [%.1f, %.1f,%.1f]]\n",confusionMatrix.get(0, 0), confusionMatrix.get(0, 1), 
        		confusionMatrix.get(0, 2), confusionMatrix.get(1, 0),confusionMatrix.get(1, 1),confusionMatrix.get(1, 2),
        		confusionMatrix.get(2, 0),confusionMatrix.get(2, 1),confusionMatrix.get(2, 2));
        output.printf(Locale.ENGLISH,"kappa:[%.5f]",kappa());
    }
   }
   retrunString.append(confusionMatrix);
   retrunString.append(" Kappa ");
   retrunString.append(kappa());
   retrunString.append(" StandardDeviation ");
   retrunString.append(getNormalizedStats().getStandardDeviation());
   retrunString.append(" Average ");
   retrunString.append(getNormalizedStats().getAverage());
   return retrunString.toString();
 }
 

 public static double kappa(){
	 double a = 0.0;
	   double b = 0.0;
	   for (int i = 0; i < confusionMatrix.rowSize(); i++) {
	     a += confusionMatrix.get(i,i);
	     double br = 0,bc=0;
	     for (int j = 0; j < confusionMatrix.rowSize(); j++) {
	       br += confusionMatrix.get(i,j);
	       bc+=confusionMatrix.get(j, i);
	     }
	     b += br * bc;
	   }
	   return (getTotal() * a - b) / (getTotal() * getTotal() - b);
	 
 }
 
 private static int getTotal(){
	  
	 for(int i=0;i<confusionMatrix.rowSize();i++){
		
		 for(int j=0;j<confusionMatrix.rowSize();j++){
			 total+=confusionMatrix.get(i,j);
		 }
	 }
	 return total;
	
 }
 //I will implement this later.
/* public Matrix entropy() {
	 
     double p = (0.5 + confusionMatrix.get(confusionMatrix.rowSize()-1, confusionMatrix.rowSize()-1)) / (1 + confusionMatrix.get(0, 0) + confusionMatrix.get(confusionMatrix.rowSize()-1, confusionMatrix.rowSize()-1));
     for(int i=0;i<confusionMatrix.rowSize();i++){
    	 
     }
     entropy.set(0, 0, confusionMatrix.get(0, 0) * Math.log1p(-p));
     entropy.set(0, 1, confusionMatrix.get(0, 1) * Math.log(p));
     entropy.set(1, 0, confusionMatrix.get(1, 0) * Math.log1p(-p));
     entropy.set(1, 1, confusionMatrix.get(1, 1) * Math.log(p));
   }
   return entropy;
 }*/
 
 public static  RunningAverageAndStdDev  getNormalizedStats() {
	   RunningAverageAndStdDev summer = new FullRunningAverageAndStdDev();
	   for (int d = 0; d < confusionMatrix.rowSize(); d++) {
	     double total = 0;
	     for (int j = 0; j < confusionMatrix.rowSize(); j++) {
	       total += confusionMatrix.get(d,j);
	     }
	     summer.addDatum(confusionMatrix.get(d,d) / (total + 0.000001));
	   }
	   
	   return summer;
	 }

 private static boolean parseArgs(String[] args) {
   DefaultOptionBuilder builder = new DefaultOptionBuilder();

   Option help = builder.withLongName("help").withDescription("print this list").create();

   Option quiet = builder.withLongName("quiet").withDescription("be extra quiet").create();

   Option auc = builder.withLongName("auc").withDescription("print AUC").create();
   Option confusion = builder.withLongName("confusion").withDescription("print confusion matrix").create();

   Option scores = builder.withLongName("scores").withDescription("print scores").create();

   ArgumentBuilder argumentBuilder = new ArgumentBuilder();
   Option inputFileOption = builder.withLongName("input")
           .withRequired(true)
           .withArgument(argumentBuilder.withName("input").withMaximum(1).create())
           .withDescription("where to get training data")
           .create();

   Option modelFileOption = builder.withLongName("model")
           .withRequired(true)
           .withArgument(argumentBuilder.withName("model").withMaximum(1).create())
           .withDescription("where to get a model")
           .create();

   Group normalArgs = new GroupBuilder()
           .withOption(help)
           .withOption(quiet)
           .withOption(auc)
           .withOption(scores)
           .withOption(confusion)
           .withOption(inputFileOption)
           .withOption(modelFileOption)
           .create();

   Parser parser = new Parser();
   parser.setHelpOption(help);
   parser.setHelpTrigger("--help");
   parser.setGroup(normalArgs);
   parser.setHelpFormatter(new HelpFormatter(" ", "", " ", 130));
   CommandLine cmdLine = parser.parseAndHelp(args);

   if (cmdLine == null) {
     return false;
   }

   inputFile = getStringArgument(cmdLine, inputFileOption);
   modelFile = getStringArgument(cmdLine, modelFileOption);
   showAuc = getBooleanArgument(cmdLine, auc);
   showScores = getBooleanArgument(cmdLine, scores);
   showConfusion = getBooleanArgument(cmdLine, confusion);

   return true;
 }

 private static boolean getBooleanArgument(CommandLine cmdLine, Option option) {
   return cmdLine.hasOption(option);
 }

 private static String getStringArgument(CommandLine cmdLine, Option inputFile) {
   return (String) cmdLine.getValue(inputFile);
 }

}