package com.spm.tool.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.spm.tool.model.Method;

public class FindMethodsController {

	public FindMethodsController() {
		
	}
	public ArrayList<Method> getMethods(String path,String filename){
		String line = null;
		ArrayList<Method> methodList = new ArrayList<>();
		ArrayList<String> lineList = new ArrayList<>();


        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(path);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            int i=1;
            boolean dataTypeStatus = false;
            boolean methodStatus = false;
            boolean foundMethod = false;
            boolean arrayMethod = false;

            String previousWord = "";
            String methodName = "";
            int scopeBracketCount=0;
            int startLine = 0;
            int endLine = 0;
            
            
            
            while((line = bufferedReader.readLine()) != null) {
            		//add the code lines to the array list
            		lineList.add(line);
            		//split the line in to small parts
	            	String[] parts = line.split("(?<!^)\\b");

	            	for (String part : parts) {
	            	
	            		String[] spaceSplit = part.split(" ");
	            		
	            		for (String spacePart: spaceSplit) {
	            			
	            			String word = spacePart.replace(" ", "");
	            			boolean dataType;
	            			
	            			if(arrayMethod) {
	            				dataType = false;
	            			}else {
	            				//check for data type
	            				dataType = this.search(word);
	            			}
	            			if(dataType) {		            	
	            				dataTypeStatus = true;	            		
	            			}else if(arrayMethod){
	            				//check for the '>' in the methods that return ArrayList or List
	            				if(previousWord.contains(">")) {
	            					arrayMethod = false;
	            					methodStatus = true;
	            				}
	            			}else {	            		
	            				if(dataTypeStatus) {
	            					//check for the '<' in the methods that return ArrayList or List
	            					if (word.contains("<")) {
	        							arrayMethod = true;
	        						}else {
	        							methodStatus = true;
	            					}	            		
	            				}else {	            			
	            					if(methodStatus) {		
	            						//check for the '(' to find the method name because always there should be a '(' after the method name
	            						if(word.contains("(")) {
	            							methodName = previousWord;
	            							startLine = i;
	            							foundMethod = true;
	            						}
	            						methodStatus = false;
		            				}
		            			}            					            			
	            				dataTypeStatus = false;
		            		}
	            			
	            			if(foundMethod) {
	            				//checking the start of the scope
		            			if(word.contains("{")) {
		            						            				
		            				scopeBracketCount++;	            				
		            			}
		            			//check the end of the scope
		            			if(word.contains("}")){	            				
		            				scopeBracketCount--;
		            				//check the end of the method
		            				if(scopeBracketCount==0) {
		            					foundMethod = false;
		            					endLine = i;
		            					//create a Method type object
		            					Method method = new Method(methodName,startLine,endLine);
		            					//add the method to the array list
		            					methodList.add(method);
		            					
		            					}
		            			}
	            			}            			
		            		previousWord = word;	            		
		            	}            		           	   
	            	}	         
	                i++;
//            	}
            }   

            // Always close files.
            bufferedReader.close();

        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                filename + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + filename + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        return methodList;
        
	}
	
	//search the data types
		public boolean search(String word) {
			String names[] = {"String","void","boolean","double","int","float","loan","short","char","ArrayList","List","HashMap"};
		
			for(String str: names) {
				
			    if(str.toLowerCase().equals(word.toLowerCase())) {
			    
			    	//System.out.println("Data type :"+word);
			        return true;
			    }
			}
			return false;
		}
}
