package com.hexabeast.riskisep.gameboard;

import java.util.ArrayList;

import com.hexabeast.riskisep.Inputs;
import com.hexabeast.riskisep.Main;
import com.hexabeast.riskisep.ressources.Shaders;
import com.hexabeast.riskisep.ressources.TextureManager;

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
		
		Shaders.setWaveShader();
		Main.batch.draw(TextureManager.tex.get("background"),-90-226,130-85);
		Shaders.setDefaultShader();
		Main.batch.setColor(0.2f, 0.2f, 0.25f, 1);
		Main.batch.draw(TextureManager.tex.get("panneau"),-20,-90);
		Main.batch.draw(TextureManager.tex.get("panneauv"),1750,-90);
		Main.batch.setColor(1,1,1,1);
		
		AllPays.update();
		
		if(Inputs.instance.leftmousedown)
		{
			int touche = AllPays.paysTouched();
			if(touche>=0)
			{
				armies.get(joueuractuel).addSoldiers(1, touche);
			}
			
		}
		for(int i=0;i<armies.size();i++)
		{
			armies.get(i).update();
		}
	}

}
