package com.hexabeast.riskisep.gameboard;

import java.util.ArrayList;

public class Army {
	public ArrayList<Soldier> soldiers = new ArrayList<Soldier>();
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
			addSoldiersForce( n, pays);
		}
		
	}
	
	public void addSoldiersForce(int n, int pays)
	{
		for(int i=0;i<n;i++)
		{
			Soldier s = new Soldier(pays,team);
			soldiers.add(s);
			AllPays.pays.get(pays).occupants.add(s);
			AllPays.pays.get(pays).team=team;
		}
	}
	
	public void removeSoldiers(int n, int pays)
	{
		for(int i=0;i<n;i++)
		{
			
			Soldier s = AllPays.pays.get(pays).occupants.get(0);
			soldiers.remove(s);
			AllPays.pays.get(pays).occupants.remove(0);
			if(AllPays.pays.get(pays).occupants.size()==0)AllPays.pays.get(pays).team=-1;
		}
	}
	
	public void update()
	{

	}
	
	public ArrayList<Pays> getCountries()
	{
		ArrayList<Pays> c = new ArrayList<Pays>();
		for(int i=0;i<AllPays.pays.size();i++)
		{
			if(AllPays.pays.get(i).team==team)c.add(AllPays.pays.get(i));
		}
		return c;
	}

}
