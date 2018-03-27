package com.hexabeast.riskisep.gameboard;

import java.util.ArrayList;

public class Army {
	private ArrayList<Soldier> soldiers = new ArrayList<Soldier>();
	int team = 0;
	
	public Army(int team)
	{
		this.team=team;
	}
	
	public void addSoldier(int type, float x, float y, int pays)
	{
		Soldier s = new Soldier(x,y,pays,team,type);
		soldiers.add(s);
		AllPays.pays.get(pays).occupants.add(s);
	}
	
	/*public void addSoldiers(int n, int type)
	{
		for(int i=0;i<n;i++)addSoldier(type);
	}*/
	
	public void update()
	{
		for(int i=0; i<soldiers.size(); i++)
		{
			soldiers.get(i).update();
		}
	}

}
