package com.hex.ml.utility;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

public class HEMLUtility {
	
	/*Method to set the hadoop configuration. Hard coded now but during the application launch we
	 * will take path from user or from the system directly.*/    
	public static Configuration getConfiguration() {
		Configuration conf = new Configuration();
		conf.addResource(new Path("/home/vishnu/installed/hadoop-2.4.1/etc/hadoop/core-site.xml"));
		conf.addResource(new Path("/home/vishnu/installed/hadoop-2.4.1/etc/hadoop/hdfs-site.xml"));
		conf.addResource(new Path("/home/vishnu/installed/hadoop-2.4.1/etc/hadoop/yarn-site.xml"));
		return conf;   
    	
    }
    

}
