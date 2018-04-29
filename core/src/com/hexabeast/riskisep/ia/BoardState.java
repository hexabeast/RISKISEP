package com.hexabeast.riskisep.ia;

import com.hexabeast.riskisep.GameScreen;
import com.hexabeast.riskisep.gameboard.AllPays;

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

}
