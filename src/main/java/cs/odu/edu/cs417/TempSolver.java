package cs.odu.edu.cs417;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TempSolver 
{
	ArrayList<ArrayList<String>> outputStrings = new ArrayList<ArrayList<String>>();
	List<ArrayList<Double>> tempList = new ArrayList<ArrayList<Double>>();
	
	public TempSolver()
	{
		
	}
	
	public TempSolver(List<ArrayList<Double>> newTempList)
	{
		tempList = newTempList;
	}
	
	public void tempInterpolate()
	{
	    double c0 = 0;
		double c1 = 0;
		double x0 = 0;
		double f0 = 0;
		double x1 = 0;
		double f1 = 0;
		DecimalFormat df = new DecimalFormat("#.##");
		
		for (int i = 1; i < 5; i++)
		{
			outputStrings.add(new ArrayList<String>());
			
			for (int j = 0; j < tempList.size()-1; j++)
			{
				x0 = tempList.get(j).get(0);
				x1 = tempList.get(j+1).get(0);
				f0 = tempList.get(j).get(i);
				f1 = tempList.get(j+1).get(i);
				
				c0 = f0 - ((f1 - f0) / (x1 - x0)) * x0;
				c1 = ((f1 - f0) / (x1 - x0));
				
				String formattedc0 = df.format(c0);
				String formattedc1 = df.format(c1);
				
				String outputString = String.format("%-60s%-30s", x0 + " <= x < " + x1 + " ; y sub " + x1 + 
						" = " + formattedc0 + " + " + formattedc1 + "x ", ": Interpolation");
				
				outputStrings.get(i-1).add(x0 + " <= x < " + x1 + " ; y sub " + x1 + 
						" = " + formattedc0 + " + " + formattedc1 + "x : Interpolation");
				
				System.out.println(outputString);
			}
		}
	}
}
