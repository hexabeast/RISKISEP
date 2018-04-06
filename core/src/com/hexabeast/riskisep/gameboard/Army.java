package com.hexabeast.riskisep.gameboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.hexabeast.riskisep.GameScreen;

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
			if(type==0)s = new Soldat(GameScreen.master.curid, pays,team);
			else if(type==1)s = new Cheval(GameScreen.master.curid, pays,team);
			else s = new Cannon(GameScreen.master.curid, pays,team);
			soldiers.add(s);
			GameScreen.master.soldiersmap.put(String.valueOf(GameScreen.master.curid), s);
			AllPays.pays.get(pays).occupants.add(s);
			AllPays.pays.get(pays).team=team;
			GameScreen.master.curid++;
		}
	}
	
	public void removeSoldier(int id)
	{
		Unite s = GameScreen.master.soldiersmap.get(String.valueOf(id));
		Pays pays = AllPays.pays.get(s.pays); //Pays
		GameScreen.master.soldiersmap.remove(String.valueOf(s.id));
		soldiers.remove(s);
		pays.occupants.remove(s);
		if(pays.occupants.size()==0)pays.team=-1;
	}
	
	public void transferSoldiers(int id, int paysdest)
	{
		if(GameScreen.master.soldiersmap.containsKey(String.valueOf(id)))
		{
			Unite s = GameScreen.master.soldiersmap.get(String.valueOf(id));
			s.mvtactuel-=1;
			Pays pays = AllPays.pays.get(s.pays); //Pays
			pays.occupants.remove(s);
			AllPays.pays.get(paysdest).occupants.add(s);
			AllPays.pays.get(paysdest).team=team;
			if(AllPays.pays.get(paysdest).occupants.size()==0)AllPays.pays.get(paysdest).team=-1;
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
