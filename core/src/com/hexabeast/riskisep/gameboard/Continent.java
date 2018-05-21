package com.hexabeast.riskisep.gameboard;

public class Continent {
	public int[] pays;
	public String nom;
	public int recompense;
	
	public Continent(String nom, int[] p)
	{
		pays=p;
		this.recompense=(int) Math.floor(p.length/2);
	}
}
