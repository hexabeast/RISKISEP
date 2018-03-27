package com.hexabeast.riskisep.gameboard;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.hexabeast.riskisep.Inputs;

public class GameMaster {
	private ArrayList<Army> armies = new ArrayList<Army>();
	public int njoueurs = 2;
	public int joueuractuel = 0;
	
	public GameMaster()
	{
		
	}
	
	public void beginGame(int njoueurs)
	{
		for(int i=0;i<njoueurs;i++)
		{
			armies.add(new Army(i));
		}
		this.njoueurs = njoueurs;
	}
	
	public void update()
	{
		if(Inputs.instance.leftpress)
		{
			int touche = AllPays.paysTouched();
			if(touche>=0)
			{
				Vector2 pos = AllPays.pays.get(touche).getRandomPos();
				armies.get(joueuractuel).addSoldier(0, pos.x, pos.y, touche);
			}
			
		}
		for(int i=0;i<armies.size();i++)
		{
			armies.get(i).update();
		}
	}

}
