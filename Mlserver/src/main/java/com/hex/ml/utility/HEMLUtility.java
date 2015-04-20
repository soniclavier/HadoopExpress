package com.hex.ml.utility;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

public class HEMLUtility {
	
	/*Method to set the hadoop configuration. Hard coded now but during the application launch we
	 * will take path from user or from the system directly.*/    
	public static Configuration getConfiguration() {
		Configuration conf = new Configuration();
		conf.addResource(new Path(Configs.getInstance().getProperty("core-site")));
		conf.addResource(new Path(Configs.getInstance().getProperty("hdfs-site")));
		conf.addResource(new Path(Configs.getInstance().getProperty("yarn-site")));
		return conf;   
    	
    }
    

}
