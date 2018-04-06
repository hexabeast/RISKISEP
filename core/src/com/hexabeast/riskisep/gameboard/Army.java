package com.hexabeast.riskisep.gameboard;

import java.util.ArrayList;

public class Army {
	public ArrayList<Unite> soldiers = new ArrayList<Unite>();
	public int team = 0;
	public int newsoldiers = 10;
	
	public Army(int team)
	{
		this.team=team;
	}
	
	public void addSoldiers(int type, int n, int pays)
	{
		if(newsoldiers>=n)
		{
			newsoldiers-=n;
			addSoldiersForce(type, n, pays);
		}
	}
	
	public void addSoldiersForce(int type, int n, int pays)
	{
		for(int i=0;i<n;i++)
		{
			Unite s;
			if(type==0)s = new Soldat(pays,team);
			else if(type==1)s = new Cheval(pays,team);
			else s = new Cannon(pays,team);
			soldiers.add(s);
			AllPays.pays.get(pays).occupants.add(s);
			AllPays.pays.get(pays).team=team;
		}
	}
	
	public void removeSoldiers(int n, int pays)
	{
		for(int i=0;i<n;i++)
		{
			
			Unite s = AllPays.pays.get(pays).occupants.get(0);
			soldiers.remove(s);
			AllPays.pays.get(pays).occupants.remove(0);
			if(AllPays.pays.get(pays).occupants.size()==0)AllPays.pays.get(pays).team=-1;
		}
	}
	
	public void update()
	{
		for(int i=0;i<soldiers.size();i++)
		{
			soldiers.get(i).update();
		}
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
