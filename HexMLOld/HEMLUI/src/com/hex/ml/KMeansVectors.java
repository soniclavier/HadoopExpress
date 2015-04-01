package com.hex.ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.VectorWritable;


public class KMeansVectors {
	 double[]colvalues = new double[2];
	 int columnLength;
	 String fileSperator;
	 String outputPath="/user/hue/KTest/KmeansVec";
	 Path vecoutput =new Path(outputPath);
	
	
	public String getFileSperator() {
		return fileSperator;
	}


	public void setFileSperator(String fileSperator) {
		this.fileSperator = fileSperator;
	}


	public int getColumnLength() {
		return columnLength;
	}


	public void setColumnLength(int columnLength) {
		this.columnLength = columnLength;
	}


	@SuppressWarnings("deprecation")
	public String getSeqFile(String inputLocation) throws Exception{
	    FileSystem fs = null;
	    SequenceFile.Writer writer;
	    fs = FileSystem.get(HEMLUtility.getConfiguration());	    
	    checkAndCreatePath(inputLocation);
	    writer = new SequenceFile.Writer(fs, HEMLUtility.getConfiguration(), vecoutput, Text.class, VectorWritable.class);
	    VectorWritable vec = new VectorWritable();
	    try {
	        FileReader fr = new FileReader(inputLocation);
	        @SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(fr);
	        String s = null;
	        String key = "";
	        while((s=br.readLine())!=null){
	        	//File Separator could be '/t',',','|' etc.
	            String spl[] = s.split(getFileSperator());
	            //Number of columns in the file
	           // if(spl.length==getColumnLength()){
	            	key = spl[0].toString();
            		Integer val = 0;	           
		            colvalues[val] = Double.parseDouble(spl[10]);
		            val++;
		            colvalues[val] = Double.parseDouble(spl[11]);
		            NamedVector nmv = new NamedVector(new DenseVector(colvalues),key);
		            vec.set(nmv);
		            writer.append(new Text(nmv.getName()), vec);		            
	          //  }            
	          
	            }
	            
	            writer.close();

	        } catch (Exception e) {
	        	System.out.println("ERROR: "+e);
	        }
	    return "/user/hue/KTest"; 
	}
	
	private void checkAndCreatePath(String inputLocation){
		try {
			FileSystem fs = FileSystem.get(HEMLUtility.getConfiguration());
			Path inputDirP = new Path(inputLocation);
			if(!fs.exists(inputDirP)){
				System.out.println("Input path " +inputLocation + "does not exists.");
			System.exit(0);
		}
		else{
			if(fs.exists(vecoutput))
				fs.delete(vecoutput, true);					
		}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	

	

}
