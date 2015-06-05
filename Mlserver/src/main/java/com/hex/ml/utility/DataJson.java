package com.hex.ml.utility;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.google.common.base.Joiner;


//This class will provide the stats of the data file to create prediction UI
public class DataJson {
	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
	int [] columnindex;
	StringBuffer selectedValues = new StringBuffer("");

	/**
	 * @param args
	 * @throws Exception 
	 */
	//This method is only for the decision Tree
	public String createNewCsv(Map<Integer,String> columnAndTypes,String filePath) throws Exception {
		columnindex = new int[columnAndTypes.size()];
		Set indexes = columnAndTypes.keySet();
		Iterator it = indexes.iterator();
		int count =0;
		while(it.hasNext()){
			columnindex[count++] = (int)it.next();
		}
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		File file = new File("C:\\per\\UIS\\newFiles.csv");
		BufferedWriter brwr = new BufferedWriter(new FileWriter(file));
		while ((line = br.readLine()) != null) {
		    String[] cols = line.split(cvsSplitBy);
		    
		    for(int i=0;i<columnindex.length;i++){
		    	selectedValues.append(cols[columnindex[i]]);
		    	if(i!=columnindex.length-1){
		    		selectedValues.append(",");
		    	}
		    	
		    }
		    selectedValues.append("\n");
		    brwr.write(selectedValues.toString());
		    selectedValues = new StringBuffer("");
		}
		brwr.close();
		return "C:\\per\\UIS\newFiles.csv";
		 

	}
	
	//This method will be used with all algorithms to generate the stats from data set.As of now last column will be target.
	
	public String getJsonPath(Map<Integer,String> columnAndTypes, String filePath,String[]columnNames){
		Map<Integer,ArrayList<String>> numericMap = new HashMap<>();
		Map<Integer,Set<String>> categoricalMap = new HashMap<>();
		
		try {	
			System.out.println(filePath);
			br = new BufferedReader(new FileReader(filePath));
			int count =0;
			while ((line = br.readLine()) != null) {	
				if(count==0){
					System.out.println("not null");
					count =1;
					continue;
				}else{
					String[] columns = line.split(cvsSplitBy);
					System.out.println(columns[0]);
					for(int i=0;i<columns.length;i++){
						if(columnAndTypes.get(i)!=null){
							if(columnAndTypes.get(i).equals("Numeric")){
								//put the value in the hash map for each column separately.
								if(numericMap.containsKey(i)){
									numericMap.get(i).add(columns[i]);
								}else{
									ArrayList<String> numValue = new ArrayList<>();
									numValue.add(columns[i]);
									numericMap.put(i, numValue);
								}						
								
							}else{
								//For Categorical
								if(categoricalMap.containsKey(i)){
									categoricalMap.get(i).add(columns[i]);
								}else{
									Set<String> catValue = new HashSet<>();
									catValue.add(columns[i]);
									categoricalMap.put(i, catValue);
								}						
								
							}	
					    }
					}
				}//end of outer else
	 
			}			
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		FileWriter file;
		try {
			file = new FileWriter("C:\\per\\UIS\\filetest.json");
			file.write("{feature:[");
			file.write(printNumericMap(numericMap, columnNames).toString());
			if(printCategoricalMap(categoricalMap, columnNames)!=null){
				file.write(","+printCategoricalMap(categoricalMap, columnNames).toString()+"]}");
			}else{
				
				file.write("]}");
			}			
			
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
        		
		return "C:\\per\\UIS\\filetest.json";
		
	} 
	@SuppressWarnings("unchecked")
	public StringBuffer printNumericMap (Map<Integer,ArrayList<String>> numericMap,String[]columnNames) throws Exception {
	    Iterator<Entry<Integer, ArrayList<String>>> it = numericMap.entrySet().iterator();
	    double maxValue = 0;
	    double minValue = Integer.MIN_VALUE;	 
	    JSONObject arrayElementOne= new JSONObject();
        
        StringBuffer sb= new StringBuffer();
        StringBuffer finalString;
        while (it.hasNext()) {
	    	@SuppressWarnings("rawtypes")
			Map.Entry pair = it.next();
	        ArrayList<String> list = (ArrayList<String>)pair.getValue();
	        minValue = Double.parseDouble(list.get(0));
	        for(int i=0;i<list.size();i++){
	        	
	        	if(maxValue < Double.parseDouble(list.get(i))){
	        		maxValue= Double.parseDouble(list.get(i));
	        	}
	        	if(minValue > Double.parseDouble(list.get(i))){
	        		minValue = Double.parseDouble(list.get(i));
	        	}
	        }
	        //put the stuff into json	        
	        arrayElementOne.put("name", columnNames[(int)pair.getKey()]);
	        arrayElementOne.put("type", "numeric");
	        arrayElementOne.put("max", maxValue);
	        arrayElementOne.put("min", minValue);
	  
	        //vishnu : changed toJSONString() to toString()
	        sb.append(arrayElementOne.toString()+",");     
	        it.remove(); // avoids a ConcurrentModificationException
	    }
        System.out.println("This is sb length: "+sb.length());
        finalString=new StringBuffer(sb.substring(0,sb.length()-1));
        return finalString;      
	    
	}
	
	@SuppressWarnings("unchecked")
	public StringBuffer printCategoricalMap (Map<Integer,Set<String>> categoricalMap,String[]columnNames) throws Exception {
	    Iterator<Entry<Integer, Set<String>>> it = categoricalMap.entrySet().iterator();
	    JSONObject arrayElementOne= new JSONObject();        
        StringBuffer sb= new StringBuffer();
        StringBuffer finalString;
        while (it.hasNext()) {
        	System.out.println("in print cat");
	    	@SuppressWarnings("rawtypes")	    	
			Map.Entry pair = it.next();
	        Set<String> set = (Set<String>)pair.getValue();
	        Iterator<String> setIterator = set.iterator();
	        StringBuffer array = new StringBuffer() ;
	        while(setIterator.hasNext()){
	        	array.append(setIterator.next()+",");
	        	System.out.println("in print cat");
	        }
	        String catValue = array.substring(0,array.length()-1);
	        System.out.println(catValue);
	        //put the stuff into json	        
	        arrayElementOne.put("name", columnNames[(int)pair.getKey()]);
	        arrayElementOne.put("type", "categorical");
	        arrayElementOne.put("value", catValue);
	        
	        //vishnu : changed toJSONString() to toString()
	        sb.append(arrayElementOne.toString()+",");     
	        it.remove(); // avoids a ConcurrentModificationException
	    }
        if(sb.length()!=0){
        	finalString=new StringBuffer(sb.substring(0,sb.length()-1));
        	return finalString;
        }else{
        	return null;
        }
	}
	
	public JSONArray getFeatureJSONArr(Map<Integer,String> columnAndTypes, String filePath,String[] columnNames) throws JSONException{
		
		JSONArray features = new JSONArray();
		Map<Integer,List<Double>> numericMap = new HashMap<>();
		Map<Integer,Set<String>> categoricalMap = new HashMap<>();
		
		try {	
			System.out.println(filePath);
			br = new BufferedReader(new FileReader(filePath));
			int count =0;
			while ((line = br.readLine()) != null) {
				if(count==0){
					System.out.println("not null");
					count =1;
					continue;
				}else{
					if (line.trim().equals(""))
						continue;
					String[] columns = line.split(cvsSplitBy);
					
					for(int i=0;i<columns.length;i++){
						if(columnAndTypes.get(i)!=null){
							if(columnAndTypes.get(i).equals("Numeric")){
								//put the value in the hash map for each column separately.
								if(numericMap.containsKey(i)){
									List<Double> minMax = numericMap.get(i);
									double min = minMax.get(0);
									double max = minMax.get(1);
									double newVal = Double.parseDouble(columns[i]);
									if(newVal < min) {
										min = newVal;
										minMax.set(0,min);
									} else if (newVal > max) {
										max = newVal;
										minMax.set(1,max);
									}
									numericMap.put(i,minMax);
								}else{
									List<Double> numValue = new ArrayList<>();
									numValue.add(Double.parseDouble(columns[i]));
									numValue.add(Double.parseDouble(columns[i]));
									numericMap.put(i, numValue);
								}						
								
							}else{
								//For Categorical
								if(categoricalMap.containsKey(i)){
									categoricalMap.get(i).add(columns[i]);
								}else{
									Set<String> catValue = new HashSet<>();
									catValue.add(columns[i]);
									categoricalMap.put(i, catValue);
								}						
								
							}	
					    }
					}
				}//end of outer else
	 
			}			
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		for(int i=0;i<columnNames.length;i++) {
			
			JSONObject feature = new JSONObject();
			String name = columnNames[i];
			feature.put("name",name);
			System.out.println(name);
			if (numericMap.containsKey(i)) {
				JSONObject catType = new JSONObject();
				catType.put("varType","numeric");
				catType.put("min",numericMap.get(i).get(0));
				catType.put("max",numericMap.get(i).get(1));
				feature.put("type",catType);
			} else if (categoricalMap.containsKey(i)) {
				JSONObject catType = new JSONObject();
				catType.put("varType","categorical");
				String values = Joiner.on(",").join(categoricalMap.get(i));
				catType.put("values",values);
				feature.put("type",catType);
			}
			features.put(feature);
		}
		
		return features;
		
	} 
	
	public static String getHeaders(String file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		return br.readLine();
	}

}
