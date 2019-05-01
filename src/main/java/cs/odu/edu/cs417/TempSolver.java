package cs.odu.edu.cs417;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Chris Guilfoy
 * TempSolver does the calculates on the temperature lists to find
 * linear interpolation between temps and a general least squares
 * approximation for the entire list of temps @ time x
 *
 */
public class TempSolver 
{
	//outputStrings holds that list of temps and formulae to be output to a txt
	ArrayList<ArrayList<String>> outputStrings = new ArrayList<ArrayList<String>>();
	
	//tempList holds a list of temps and a list of corresponding times
	List<ArrayList<Double>> tempList = new ArrayList<ArrayList<Double>>();
	
	/**
	 * Default Constructor
	 */
	public TempSolver()
	{
		
	}
	
	/**
	 * 
	 * @param newTempList takes a list and stores it in tempList
	 */
	public TempSolver(List<ArrayList<Double>> newTempList)
	{
		tempList = newTempList;
	}
	
	/**
	 * Does a piecewise linear interpolation between each x value
	 * and its corresponding tempurate value
	 * x = time
	 * y = temp
	 * 
	 * @throws IOException if a file isn't found
	 */
	public void tempInterpolate() throws IOException
	{
	    double c0 = 0;
		double c1 = 0;
		double x0 = 0;
		double f0 = 0;
		double x1 = 0;
		double f1 = 0;
		
		DecimalFormat df = new DecimalFormat("#.##");
		
		//Loops through each cores data
		for (int i = 1; i < 5; i++)
		{
			outputStrings.add(new ArrayList<String>());
			Path outputFile = Paths.get("output_core_" + i + ".txt");
			
			//Loops through each temp in a certain core
			for (int j = 0; j < tempList.size()-1; j++)
			{
				x0 = tempList.get(j).get(0);
				x1 = tempList.get(j+1).get(0);
				f0 = tempList.get(j).get(i);
				f1 = tempList.get(j+1).get(i);
				
				//Calculates the needed data for the line equation
				c0 = f0 - ((f1 - f0) / (x1 - x0)) * x0;
				c1 = ((f1 - f0) / (x1 - x0));
				
				String formattedc0 = df.format(c0);
				String formattedc1 = df.format(c1);
				
				String outputString = String.format("%-60s%-30s", x0 + " <= x < " + x1 + " ; y  = " + 
						formattedc0 + " + " + formattedc1 + "x ", ": interpolation");
				
				outputStrings.get(i-1).add(outputString);
			}
			
			Files.write(outputFile, outputStrings.get(i-1), Charset.forName("UTF-8"), StandardOpenOption.CREATE);
		}
	}
	
	/**
	 * Calculates a least squares approximation for the entire data set
	 * 
	 * @throws IOException If file not found
	 */
	public void tempLeastSquares() throws IOException
	{	
		//Variables to keep a running tally of dot products
		Double xTx00 = 0.0;
		Double xTx01 = 0.0;
		Double xTx10 = 0.0;
		Double xTx11 = 0.0;
		Double xTy00 = 0.0;
		Double xTy01 = 0.0;
		//2D ArrayList for the x matrix
		ArrayList<ArrayList<Double>> x = new ArrayList<ArrayList<Double>>();
		x.add(new ArrayList<Double>());
		x.add(new ArrayList<Double>());
		
		//2D ArrayList for the x Transpose matrix
		ArrayList<ArrayList<Double>> xTranspose = new ArrayList<ArrayList<Double>>();
		
		//2D ArrayList for the x Transpose * x matrix
		ArrayList<ArrayList<Double>> xTransposeX = new ArrayList<ArrayList<Double>>();
		xTransposeX.add(new ArrayList<Double>());
		xTransposeX.add(new ArrayList<Double>());
		xTransposeX.get(0).add(xTx00);
		xTransposeX.get(0).add(xTx01);
		xTransposeX.get(1).add(xTx10);
		xTransposeX.get(1).add(xTx11);
		
		//2D ArrayList for the x Transpose * y matrix
		ArrayList<ArrayList<Double>> xTransposeY = new ArrayList<ArrayList<Double>>();
		xTransposeY.add(new ArrayList<Double>());
		xTransposeY.get(0).add(xTy00);
		xTransposeY.get(0).add(xTy01);
		
		DecimalFormat df = new DecimalFormat("#.######");
		
		//Loops through each of the cores data
		for (int i = 1; i < 5; i++)
		{
			Path outputFile = Paths.get("output_core_" + i + ".txt");
			
			//Loops through the temperatures for each of the cores
			for (int j = 0; j < tempList.size(); j++)
			{
				//If this is the first time through the outer loop, we only need to
				//calculate the values for x, x transpose, and x transpose * x one time
				//Doing this for every iteration would waste time
				if (i == 1)
				{	
					//x matrix creation one row at a time
					x.get(i-1).add(1.0);
					x.get(i).add(tempList.get(j).get(i-1));
					
					//x transpose creation one column at a time
					xTranspose.add(new ArrayList<Double>());
					xTranspose.get(j).add(1.0);
					xTranspose.get(j).add(tempList.get(j).get(i-1));
					
					//calculation of the x transpose * x matrix
					xTx00 += 1.0;
					xTx01 = xTx01 + (xTranspose.get(j).get(0) * x.get(1).get(j));
					xTx10 = xTx10 + (xTranspose.get(j).get(1) * x.get(0).get(j));
					xTx11 = xTx11 + (xTranspose.get(j).get(1) * x.get(1).get(j));
				}
				
				//calculation of the x transpose * y matrix
				xTy00 = xTy00 + (xTranspose.get(j).get(0) * tempList.get(j).get(i));
				xTy01 = xTy01 + (xTranspose.get(j).get(1) * tempList.get(j).get(i));
			}
			
			//sets calculated dot products to correct spot in x transpose * x and
			//x Transpose * y matrices
			xTransposeX.get(0).set(0, xTx00);
			xTransposeX.get(0).set(1, xTx01);
			xTransposeX.get(1).set(0, xTx10);
			xTransposeX.get(1).set(1, xTx11);
			
			xTransposeY.get(0).set(0, xTy00);
			xTransposeY.get(0).set(1, xTy01);
			
			//Solves the matrix so that x transpose * x is upper triangular and the 
			//resulting x transpose * y matrix has the data for a line
			while (xTransposeX.get(0).get(0) != 1 && xTransposeX.get(0).get(1) != 0 && 
					xTransposeX.get(1).get(0) != 0 && xTransposeX.get(1).get(1) != 1)
			{
				//System.out that visualizes the matrix in its current form for testing
				/*System.out.println(" [ " + xTransposeX.get(0).get(0) + "   " + 
						xTransposeX.get(0).get(1) + "  |   " + xTransposeY.get(0).get(0) + " ]");
				System.out.println(" [ " + xTransposeX.get(1).get(0) + "   " + 
						xTransposeX.get(1).get(1) + "  |   " + xTransposeY.get(0).get(1) + " ]");*/
				
				//Solve for Top Left = 1
				if (xTransposeX.get(0).get(0) != 1)
				{
					xTransposeX.get(0).set(1, xTransposeX.get(0).get(1) / xTransposeX.get(0).get(0));
					xTransposeY.get(0).set(0, xTransposeY.get(0).get(0) / xTransposeX.get(0).get(0));
					xTransposeX.get(0).set(0, 1.0);
				}
				
				//Solve for Bottom Left = 0
				if (xTransposeX.get(1).get(0) != 0)
				{
					xTransposeX.get(1).set(1, xTransposeX.get(1).get(1) - (xTransposeX.get(0).get(1) * xTransposeX.get(1).get(0)));
					xTransposeY.get(0).set(1, xTransposeY.get(0).get(1) - (xTransposeX.get(1).get(0) * xTransposeY.get(0).get(0)));
					xTransposeX.get(1).set(0, 0.0);
				}
			
				//Solve for Bottom Right = 1
				if (xTransposeX.get(1).get(1) != 1)
				{
					xTransposeY.get(0).set(1, xTransposeY.get(0).get(1) / xTransposeX.get(1).get(1));
					xTransposeX.get(1).set(1, 1.0);
				}
			
				//Solve for Top Right = 0
				if (xTransposeX.get(0).get(1) != 0)
				{
					xTransposeY.get(0).set(0, xTransposeY.get(0).get(0) - (xTransposeX.get(0).get(1) * xTransposeY.get(0).get(1)));
					xTransposeX.get(0).set(1, 0.0);
				}
			}
			
			//This section handles the output to text file, appends to the file created
			//by the interpolation method
			List<String> outputList = new ArrayList<String>();
			String formattedY1 = df.format(xTransposeY.get(0).get(0));
			String formattedY2 = df.format(xTransposeY.get(0).get(1));
			
			String outputString = String.format("%-60s%-30s", 0 + " <= x < " + tempList.get(tempList.size()-1).get(0) + " ; y = " + 
					formattedY1 + " + " + formattedY2 + "x ", ": least-squares");
			
			outputList.add(outputString);
			
			Files.write(outputFile, outputList, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
			
			//reset the x transpose * y matrix
			xTransposeY.get(0).set(0, 0.0);
			xTransposeY.get(0).set(1, 0.0);
			
			//reset the dot product variables for x transpose * y
			xTy00 = 0.0;
			xTy01 = 0.0;
			
		}
	}
}
