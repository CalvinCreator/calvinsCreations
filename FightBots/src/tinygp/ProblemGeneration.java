package tinygp;

import java.awt.Point;
import java.text.DecimalFormat;

public class ProblemGeneration {
	
	public static void main(String args[]) {
		
		//Find 60 random pairs of points and the angle between them. Output data in
		//a format compatible with TinyGP
		//points can be between (0, 0) and (600, 600)
		
		int populationSize = 5;
		int crossCount = (int)(populationSize * 0.9);
		int mutCount = (int)(populationSize * 0.05);
		int copyCount = populationSize - crossCount - mutCount;

		//90% through crossover
		for(int i = 0; i < crossCount; i++) 
			System.out.println("Cross: " + i);
		
		//5% by copying
		for(int i = crossCount; i < crossCount + copyCount; i++) 
			System.out.println("Copy: " + i);
		
		
		//5% by mutation
		for(int i = crossCount + copyCount; i < populationSize; i++) 
			System.out.println("Mut: " + i);
		
		for(int i = 0; i < 60; i++) {
			
			Point p1 = new Point((int)(Math.random() * 600) , (int)(Math.random() * 600)); //bot's point
			Point p2 = new Point((int)(Math.random() * 600) , (int)(Math.random() * 600)); //enemy point
			while(p2.equals(p1))
				p2 = new Point((int)(Math.random() * 600) , (int)(Math.random() * 600));
			
			float angle = (float) Math.atan2(p2.y - p1.y, p2.x - p1.x);
			DecimalFormat df = new DecimalFormat("0.0000");
			System.out.println(p1.x + " " + p1.y + " " + p2.x + " " + p2.y + " " + df.format(angle));
			
		}
		
	}

}
