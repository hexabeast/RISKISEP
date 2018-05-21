package com.hexabeast.riskisep.ia;

import java.util.ArrayList;

import com.hexabeast.riskisep.GameScreen;
import com.hexabeast.riskisep.gameboard.AllPays;
import com.hexabeast.riskisep.gameboard.Pays;

public class BoardState {
	public SimplePays[] pays;
	
	public BoardState()
	{
		pays = new SimplePays[GameScreen.apays.pays.size()];
	}
	
	public BoardState(BoardState state)
	{
		pays = new SimplePays[GameScreen.apays.pays.size()];
		for(int i=0; i<state.pays.length; i++)
		{
			pays[i] = new SimplePays(state.pays[i].id, state.pays[i].team, state.pays[i].nbsoldats);
		}
	}
	
	public void loadCurrentState()
	{
		for(int i=0; i<GameScreen.apays.pays.size(); i++)
		{
			pays[i] = new SimplePays(GameScreen.apays.pays.get(i).id, GameScreen.apays.pays.get(i).team, GameScreen.apays.pays.get(i).totalCost());
		}
	}
	
	public int comptePaysTeam(int t)
	{
		int total=0;
		for(int i=0; i<pays.length; i++)
		{
			if(pays[i].team==t)total+=1;
		}
		return total;
	}
	
	public int compteUnitesTeam(int t)
	{
		int total=0;
		for(int i=0; i<pays.length; i++)
		{
			if(pays[i].team==t)total+=pays[i].nbsoldats;
		}
		return total;
	}
	
	public int compteUnitesPasTeam(int t)
	{
		int total=0;
		for(int i=0; i<pays.length; i++)
		{
			if(pays[i].team!=t)total+=pays[i].nbsoldats;
		}
		return total;
	}
	
	public int getContinentScore(int team)
	{
		int recomp = 0;
		
		for(int i=0;i<GameScreen.apays.continents.size();i++)
		{
			boolean ok = true;
			for(int j=0;j<GameScreen.apays.continents.get(i).pays.length;j++)
			{
				boolean found = false;
				for(int k=0;k<pays.length;k++)
				{
					if(pays[k].team==team && GameScreen.apays.continents.get(i).pays[j] == pays[k].id)found=true;
				}
				if(!found)ok=false;
			}
			if(ok)
			{
				recomp+=GameScreen.apays.continents.get(i).recompense;
			}
		}
		
		return recomp;
	}
	
	public int getContinentScoreEnnemy(int team)
	{
		int recomp = 0;
		
		for(int i=0;i<4;i++)
		{
			if(i != team)
			{
				recomp+=getContinentScore(i);
			}
		}
		
		return recomp;
	}

}
