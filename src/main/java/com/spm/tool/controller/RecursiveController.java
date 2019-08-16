package com.spm.tool.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.spm.tool.model.Method;

public class RecursiveController {

	public RecursiveController() {
		
	}
	
	
	public ArrayList<Method> checkRecurtion(ArrayList<Method> methods,ArrayList<String> lines) {
		
		ArrayList<Method> recursiveMethods = new ArrayList<>();
		
		boolean recursiveStatus = false;
		
		for(Method method:methods) {
			
			
			for(int i=method.getStartLine();i<method.getEndLine();i++) {
				
				//if(!lines.get(i).replace(" ", "").substring(0, 2).equals("//")) {
			
				String[] parts = lines.get(i).split("(?<!^)\\b");
	            	
	            	for (String part : parts) {
	            	
	            		String[] spaceSplit = part.split(" ");
	            		
	            		for (String spacePart: spaceSplit) {
	            		
	            			
	            			String word = spacePart.replace(" ", "");
	            			//check for the found method name is a legal method (sometimes there can be a string with method name"
	            			if(recursiveStatus) {
	            				
	            				if(word.contains("(")) {
	            					
            						recursiveMethods.add(method);

	            				}
	            				recursiveStatus = false;
	            			}
	            			for(Method newMethod:methods) {
	    					
	            				//check for the recursive method name
	            				if(word.equals(newMethod.getName())) {
	            					//check for direct recursion
	            					if(newMethod.getName().equals(method.getName())) {
	    							
	            						if(word.contains("(")) {
			            					
			            					recursiveMethods.add(method);
			            				
			            				}else {
			            					
			            					recursiveStatus = true;
			            				
			            				}
	    							//check for indirect recursion	
	    							}else {
	    								
	    								boolean newRecursiveStatus = false;
	    								
	    								for(int x=newMethod.getStartLine();x<newMethod.getEndLine();x++) {
	    								
	    									//if(!lines.get(x).replace(" ", "").substring(0, 2).equals("//")) {
		    								
	    									String[] recursivePart = lines.get(x).split("(?<!^)\\b");
		    					            	
		    					            	for (String rPart : recursivePart) {
		    					            	
		    					            		String[] rSplit = rPart.split(" ");
		    					            		
		    					            		for (String spaceRPart: rSplit) {
		    					            		
		    					            			String rWord = spaceRPart.replace(" ", "");
		    					            
		    					            			if(newRecursiveStatus) {
		    					            				
		    					            				if(rWord.contains("(")) {
		    					            					
		    				            						recursiveMethods.add(method);

		    					            				}
		    					            				newRecursiveStatus = false;
		    					            			}
		    					            			
		    					            			if(rWord.equals(method.getName())) {
		    			    							
		    					            				if(rWord.contains("(")) {
		    					            					
		    					            					recursiveMethods.add(method);
		    					            				
		    					            				}else {
		    					            					
		    					            					newRecursiveStatus = true;
		    					            				
		    					            				}
		    					            				
		    					            			}
		    					            		}
		    					            	}

	    								//}
	    							}
	    						}
	    					}
	            		}
	            	}
	           	}
	        }
			//}
		}
		
		return recursiveMethods;
		
	}
}
