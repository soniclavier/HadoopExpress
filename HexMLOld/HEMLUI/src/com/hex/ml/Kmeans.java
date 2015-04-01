package com.hex.ml;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;

public class Kmeans {
	String Kmeansoutputlocation="/user/hue/KmeansOut";
	String clusterinLocation="/user/hue/Kmeanscluster";
	Path clustersIn =  new Path(clusterinLocation);
	Path KmeansCluster = new Path(clusterinLocation+"/clusters-0-final");
	Path KMeanOutput = new Path(Kmeansoutputlocation);
	
	public static void main(String args[]) throws Exception{
		Kmeans km= new Kmeans();
		km.runKmeans("/user/hue/KTest", "0.5", "10", "org.apache.mahout.common.distance.EuclideanDistanceMeasure");
		
	}

	
	public String runKmeans(String inputLocation,String convergenceDelta,String maxIterations,String distanceMeasure) throws Exception{
		
    	checkAndCreatePath(inputLocation);
    	CanopyDriver.run(HEMLUtility.getConfiguration(), new Path(inputLocation),clustersIn, new EuclideanDistanceMeasure(), 7, 4, true, 0.7, false);
		KMeansDriver.run(HEMLUtility.getConfiguration(), new Path(inputLocation),KmeansCluster,KMeanOutput, Double.parseDouble(convergenceDelta), Integer.parseInt(maxIterations), true, 0.7, false);
	    /*We will use canopy clustering here to determine the number of cluster*/
	/*	String[]inputs ={"--input",inputLocation,"--output",Kmeansoutputlocation,"--clusters","clusterinLocation","--numClusters","5","--distanceMeasure",distanceMeasure
				,"--maxIter",maxIterations,"-cd",convergenceDelta,"--method", "mapreduce", "--clustering"};
		
		KMeansDriver.main(inputs);*/
		return Kmeansoutputlocation;
		
	//	--input /user/hue/20newsdatavec/tfidf-vectors/ --output /user/hue/Kmeansout --clusters /user/hue/kmeanscenter --numClusters 10 --distanceMeasure org.apache.mahout.common.distance.EuclideanDistanceMeasure --maxIter 20 --method mapreduce --clustering
	}
	
	 /*Method to check whether input path exists and if yes it will check for other locations
	    * where we wanted to create model, test,training files.
	    * */ 
	    private void checkAndCreatePath(String inputLocation){
			try {
				FileSystem fs = FileSystem.get(HEMLUtility.getConfiguration());
				Path inputDirP = new Path(inputLocation);
				if(!fs.exists(inputDirP)){
					System.out.println("Input path " +inputLocation + "does not exists.");
				System.exit(0);
			}
			else{
				if(fs.exists(clustersIn)){
					fs.delete(clustersIn, true);
					
				}
				
				if(fs.exists(KMeanOutput))
					fs.delete(KMeanOutput, true);
				
			}
			
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }

}
