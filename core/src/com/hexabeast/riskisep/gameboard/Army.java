package com.hexabeast.riskisep.gameboard;

import java.util.ArrayList;

public class Army {
	private ArrayList<Soldier> soldiers = new ArrayList<Soldier>();
	public int team = 0;
	public int newsoldiers = 30;
	
	public Army(int team)
	{
		this.team=team;
	}
	
	public void addSoldiers(int n, int pays)
	{
		if(newsoldiers>=n)
		{
			newsoldiers-=n;
			for(int i=0;i<n;i++)
			{
				Soldier s = new Soldier(pays,team);
				soldiers.add(s);
				AllPays.pays.get(pays).occupants.add(s);
			}
		}
		
	}
	
	public void update()
	{

	}

}
