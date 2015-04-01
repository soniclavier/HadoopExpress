package com.hex.ml;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

public class HEMLUtility {
	
	/*Method to set the hadoop configuration. Hard coded now but during the application launch we
	 * will take path from user or from the system directly.*/    
	protected static Configuration getConfiguration() {
		Configuration conf = new Configuration();
		conf.addResource(new Path("/etc/hadoop/conf/core-site.xml"));
		conf.addResource(new Path("/etc/hadoop/conf/hdfs-site.xml"));
		conf.addResource(new Path("/etc/hadoop/conf/yarn-site.xml"));
		return conf;   
    	
    }
    

}
