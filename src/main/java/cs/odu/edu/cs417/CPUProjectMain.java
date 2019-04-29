package cs.odu.edu.cs417;

import java.io.File;

public class CPUProjectMain {

	public static void main(String[] args)
	{
		File tempFile = new File(args[0]);

		TempParser theParser = new TempParser(tempFile);
		
		try
		{
			theParser.parseFile();
		}
		catch (Exception e)
		{
			System.out.println("Error with input file.");
		}
		
		System.out.println("File Parsed Successfully!\n");
		System.out.println(theParser.getTempList().size() + " lines of tempuratures were read in.");
		
		TempSolver theSolver = new TempSolver(theParser.getTempList());
		theSolver.tempInterpolate();
	}

}
