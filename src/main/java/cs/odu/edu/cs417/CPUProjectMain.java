package cs.odu.edu.cs417;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @author Chris Guilfoy
 * 
 * This program will operate on a command line. It takes one parameter, the location
 * of a file of CPU temperature readings, and will perform a piecewise linear
 * interpolation, and a general least squares approximation on the data. The results
 * are output into text files name output_core_<core number>.
 *
 */
public class CPUProjectMain {

	public static void main(String[] args)
	{
		if (args.length > 1)
		{
			System.out.println("This program only takes 1 arguement, are you trying to kill it or something?");
			System.exit(0);
		}
		
		if (args.length <= 0)
		{
			System.out.println("Nothing to see here, move along... no seriously there's nothing here to read");
			System.exit(0);
		}
		
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
		System.out.println(theParser.getTempList().size() + " lines of temperatures were read in.");
		
		TempSolver theSolver = new TempSolver(theParser.getTempList());
		
		try 
		{
			theSolver.tempInterpolate();
			theSolver.tempLeastSquares();
		} 
		catch (IOException e) 
		{
			System.out.println("Error in solving");
		}
		
		System.out.println("\nFile Parsed, Interpolated, and Least Squared successfully. "
				+ "Output is stored in 4 files named output_core_<core number>.");
	}

}
