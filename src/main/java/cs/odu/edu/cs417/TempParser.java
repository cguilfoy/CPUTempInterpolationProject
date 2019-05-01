package cs.odu.edu.cs417;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Chris Guilfoy
 * 
 * TempParser takes a file consisting of times, and temperatures,
 * and parses them out into individual core readings, then puts
 * those readings into a 2D ArrayList for later use
 *
 */
public class TempParser 
{
	File tempFile;
	List<ArrayList<Double>> tempList = new ArrayList<ArrayList<Double>>();
	
	/**
	 * Default Constructor
	 */
	TempParser()
	{
		tempFile = null;
	}
	
	/**
	 * Constructor takes a file and sets it to its own stored file tempFile
	 * @param file
	 */
	TempParser(File file)
	{
		tempFile = file;
	}
	
	/**
	 * Sets a file to one provided in the parameters
	 * @param newFile
	 */
	public void setFile(File newFile)
	{
		tempFile = newFile;
	}
	
	/**
	 * 
	 * @return tempFile
	 */
	public File getFile()
	{
		return tempFile;
	}
	
	/**
	 * 
	 * @return 2D ArrayList tempList
	 */
	public List<ArrayList<Double>> getTempList()
	{
		return tempList;
	}
	
	/**
	 * Takes a file and loads it into a BufferedReader, and reads
	 * line by line, parsing out temp readings for 4 different cores.
	 * Stores those values into the 2D ArrayList tempList
	 * 
	 * @throws IOException if file not found
	 */
	public void parseFile() throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(tempFile));
		Double timeCounter = 0.0;
		int index = 0;
		
		//While there is something to read in the file
		while (reader.ready() == true)
		{
			tempList.add(new ArrayList<Double>());

			String newLine = reader.readLine();
			
			tempList.get(index).add(timeCounter);
			
			//Loops for each of the cores
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
					newLine = newLine.substring(newLine.indexOf(" ")+1, newLine.length()-1);
				}
			}
			
			index++;
			timeCounter += 30;
		}
		
		reader.close();
	}
}
