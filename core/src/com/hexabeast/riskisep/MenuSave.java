package com.hexabeast.riskisep;

public class MenuSave {
	public int njoueurs = 0;
	public String[] noms;
	public int[] types;
	
	public MenuSave()
	{
		
	}
	
	public MenuSave(int njoueurs, String[] noms, int[] types)
	{
		this.noms=noms;
		this.njoueurs=njoueurs;
		this.types=types;
	}
}
