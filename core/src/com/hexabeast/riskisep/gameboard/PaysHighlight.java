package com.hexabeast.riskisep.gameboard;

import com.hexabeast.riskisep.Main;

public class PaysHighlight {
	public Pays p;
	public float transparency = 0.2f;
	public float decayrate = 0.8f;
	
	public PaysHighlight(Pays p)
	{
		this.p=p;
	}
	
	public void decay()
	{
		transparency-=decayrate*Main.delta;
	}
}
