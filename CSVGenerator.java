package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class CSVGenerator {

	public static String BASE_FILE_LOCATION=System.getProperty("user.home") + "/Desktop/base";
	public static String INPUT_FILE_LOCATION=System.getProperty("user.home") + "/Desktop/input";
	public static String OUTPUT_FILE_LOCATION=System.getProperty("user.home") + "/Desktop/output";
	public static String HEADER ="";

	public static void main(String[] args) {
		
		Map<Integer, String> baseFileContent = getFileContent(BASE_FILE_LOCATION,false);
		Map<Integer, String> inputFileContent = getFileContent(INPUT_FILE_LOCATION,false);

		clearDirectory(OUTPUT_FILE_LOCATION);
		
		for(Integer key : inputFileContent.keySet()){
			String installNum = inputFileContent.get(key);
			writeContentToCSV(installNum,OUTPUT_FILE_LOCATION,baseFileContent);
		}
	}
	
	private static void clearDirectory(String location){
		File file = new File(location);
		if(file.isDirectory()){
			for(File fileToBeDeleted : file.listFiles()){
				fileToBeDeleted.delete();
			}
		}
	}
	
	private static String readFileNameFromDirectory(String location){
		File folder = new File(location);
		if(folder.isDirectory()){
			File file = folder.listFiles()[0];
			if(file.isFile()){
				return file.getAbsolutePath();
			}
		}
		return "";
	}

	private static void writeContentToCSV(String installNum,String fileLocation, Map<Integer, String> baseFileContent){
		
		FileWriter fileWriter = null;
		
		try {
			
			String fileName = fileLocation+"/"+installNum+".csv";
			fileWriter = new FileWriter(fileName);

			//Write the CSV file header
			//fileWriter.append(HEADER.toString());

			//Add a new line separator after the header
			//fileWriter.append("\n");

			int instNoIndex = 1;
			//int valueIndex = getColumnIndex(HEADER.split(","),"Value");

			for(Integer key : baseFileContent.keySet()){
				String rowData = baseFileContent.get(key);
				String[] tokens = rowData.split(",");
				if (tokens.length > 0) {
					int index=0;
					for(String token : tokens){
						if(index==instNoIndex){
							fileWriter.append(installNum);
							fileWriter.append(",");
						}else{
							fileWriter.append(token);
							fileWriter.append(",");
						}
						index++;
					}
				}
				fileWriter.append("\n");
			}
			System.out.println(fileName +" Created Successfully.");

		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}
		}
	}

	private static Map<Integer, String> getFileContent(String fileLocation, boolean captureHeader) {
		Map<Integer, String> baseFileContent = new TreeMap<Integer, String>();
		BufferedReader fileReader = null;
		
		String fileName = readFileNameFromDirectory(fileLocation);
		try {
			String line = "";

			fileReader = new BufferedReader(new FileReader(fileName));

			if(captureHeader){
				HEADER = fileReader.readLine();
			}

			int row = 0;
			while ((line = fileReader.readLine()) != null) {
				if (line.length() > 0) {
					baseFileContent.put(row, line);
				}
				row++;
			}
		} 
		catch (Exception e) {
			System.out.println("Error while processing Base File !!!");
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				System.out.println("Error while closing fileReader !!!");
				e.printStackTrace();
			}
		}
		return baseFileContent;
	}

}
