package cs.odu.edu.cs417;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TempParser 
{
	String fileName;
	File tempFile;
	List<ArrayList<Double>> tempList = new ArrayList<ArrayList<Double>>();
	
	TempParser()
	{
		fileName = "";
		tempFile = null;
	}
	
	TempParser(File file)
	{
		tempFile = file;
	}
	
	public void setFileName(String newFileName)
	{
		fileName = newFileName;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public void setFile(File newFile)
	{
		tempFile = newFile;
	}
	
	public File getFile()
	{
		return tempFile;
	}
	
	public List<ArrayList<Double>> getTempList()
	{
		return tempList;
	}
	
	public void parseFile() throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(tempFile));
		Double timeCounter = 0.0;
		int index = 0;
		
		while (reader.ready() == true)
		{
			tempList.add(new ArrayList<Double>());

			String newLine = reader.readLine();
			
			tempList.get(index).add(timeCounter);
			
			for (int i = 1; i < 5; i++)
			{
				if (i == 4)
				{
					Double newTemp = Double.parseDouble(newLine.substring(1, 4));
					tempList.get(index).add(newTemp);
				}
				else
				{
					Double newTemp = Double.parseDouble(newLine.substring(1, 4));
					tempList.get(index).add(newTemp);
					newLine = newLine.substring(9, newLine.length()-1);
				}
			}
			
			index++;
			timeCounter += 30;
		}
		
		reader.close();
	}
}
