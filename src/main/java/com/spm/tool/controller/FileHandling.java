package com.spm.tool.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spm.tool.model.Method;
import com.spm.tool.model.StatementLine;

@RequestMapping("/file")
@RestController
public class FileHandling {

	private static String UPLOAD_DIR = "upload";
	String line = null;
	String[][][] list;
	String Path,fileName;

	
	
	@CrossOrigin("http://localhost:3000")
	@PostMapping("/upload")
	public void upload(@RequestParam("file") MultipartFile file,HttpServletRequest request) {
		ArrayList<String> lineList = new ArrayList<>();
		try {

			
			fileName = file.getOriginalFilename().toString();
			Path = request.getServletContext().getRealPath("")+UPLOAD_DIR+File.separator+fileName;
			saveFile(file.getInputStream(),Path);
			System.out.println(Path);
			
			String line = null;

	        try {
	            // FileReader reads text files in the default encoding.
	            FileReader fileReader = 
	                new FileReader(Path);

	            // Always wrap FileReader in BufferedReader.
	            BufferedReader bufferedReader = 
	                new BufferedReader(fileReader);
	            
	            while((line = bufferedReader.readLine()) != null) {
	        		//add the code lines to the array list
	        		lineList.add(line);
	        	}
	        }catch(Exception ex) {
	        	ex.printStackTrace();
	        }
			
	        
			FindMethodsController findMethods = new FindMethodsController();
			
			RecursiveController recursive = new RecursiveController();
			
			ControlStructureMeasurement structures = new ControlStructureMeasurement(lineList);
			
			
			
//			for(StatementLine m:structures.calculateComplexityByType(findMethods.getMethods(Path, fileName))){
//				System.out.println(m.getLineNumber()+" -------------- "+m.getComplexity());
//			}
			
			for(StatementLine m:structures.calculateComplexityByNestingControlStructure(findMethods.getMethods(Path, fileName))){
				System.out.println(m.getLineNumber()+" -------------- "+m.getComplexity());
			}
			//displayMethods(recursive.checkRecurtion(findMethods.getMethods(Path, fileName), lineList));
			
			
			//return "http://localhost:8080/upload/"+fileName;
		}catch(Exception e) {
			System.out.println(e);
			//return e.getMessage().toString();
		}
	}

	private void saveFile(InputStream inputStream,String path) {
		try {
			OutputStream outputStream= new FileOutputStream(new File(path));
			int read = 0;
			byte[] bytes = new byte[1024];
			while((read = inputStream.read(bytes))!= -1) {
				outputStream.write(bytes,0,read);
			}
			outputStream.flush();
			outputStream.close();
					
		}catch(Exception e) {
			
		}
	}
	
	public void displayMethods(ArrayList<Method> method) {
		for(Method m:method) {
			System.out.println("---"+m.getName()+"-----Starts at: "+m.getStartLine()+"--------Ends at: "+m.getEndLine());
		}
	}
	
	
	
}
