package com.hexabeast.riskisep.ia;

import java.util.ArrayList;
import java.util.Arrays;

import com.hexabeast.riskisep.GameScreen;
import com.hexabeast.riskisep.gameboard.AllPays;

import amten.ml.examples.NNTest;
import amten.ml.matrix.Matrix;

public class BoardState {
	public SimplePays[] pays;
	
	public static ArrayList<BoardState> states = new ArrayList<BoardState>();
	public static ArrayList<float[]> statesf = new ArrayList<float[]>();
	
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
	
	public int[] getState(int team)
	{
		int[] state = new int[pays.length*2];
		for(int i=0;i<pays.length;i++)
		{
			if(pays[i].team==team)state[2*i] = pays[i].nbsoldats;
			else state[2*i+1] = pays[i].nbsoldats;
		}
		return state;
	}
	
	public static void addBoardState()
	{
		BoardState b = new BoardState();
		b.loadCurrentState();
		states.add(b);
	}
	
	public static void convert()
	{
		float[] ft = new float[states.get(0).pays.length*2+1];
		
		for(int i=0; i<states.size();i++)
		{
			for(int j=0;j<GameScreen.master.njoueurs;j++)
			{
				float reward = -1;
				if(GameScreen.master.winner==j)reward = 1;
				reward*=((float)i+1)/states.size();
				int[] line = states.get(i).getState(j);
				for(int k=0;k<line.length;k++)
				{
					ft[k+1] = line[k];
				}
				ft[0] = reward;
				
				statesf.add(ft.clone());
			}
			
		}
		
		states.clear();
	}
	
	public static void train()
	{
		Matrix m = new Matrix(statesf.size(),statesf.get(0).length);
		for(int i=0; i<statesf.size();i++)
		{
			//System.out.println(Arrays.toString(statesf.get(i)));
			for(int j=0;j<statesf.get(i).length;j++)
			{
				m.set(i, j, statesf.get(i)[j]);
			}
			
		}
		try {
			NNTest.train(m);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println(m.toString());
		statesf.clear();
	}

}
