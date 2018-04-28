package com.hexabeast.riskisep.ia;

public class Probability {

	public double[] probs;
	public static int[][] issues = new int[][]{{1,0,0,0,0},{0,1,0,0,0},{0,0,1,0,0},
		                                       {1,1,0,0,0},{0,1,1,0,0},{1,0,1,0,0},
		                                       {0,0,0,1,1},{0,0,0,1,0},{0,0,0,0,1},
		                                       {1,0,0,1,0},{1,0,0,0,1},{0,1,0,1,0},
		                                       {0,1,0,0,1},{0,0,1,1,0},{0,0,1,0,1}};
	
	public Probability(double[] probs)
	{
		this.probs = probs;
	}
	
	public Probability()
	{
		
	}
}
