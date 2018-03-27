package com.hexabeast.riskisep.gameboard;

import com.hexabeast.riskisep.Main;
import com.hexabeast.riskisep.ressources.TextureManager;

public class Soldier {
	public float x=0;
	public float y=0;
	public int w=0;
	public int h=50;
	
	public int team = 0;
	public int type = 0;
	public int pays = 0;
	
	public Soldier(float x, float y, int pays, int team, int type)
	{
		this.x=x;
		this.y=y;
		this.pays=pays;
		this.team=team;
		this.type=type;
		this.w = this.h*TextureManager.getsoldiertex(team).getWidth()/TextureManager.getsoldiertex(team).getHeight();
	}
	
	public void update()
	{
		if(pays>=0)
		{
			Main.batch.draw(TextureManager.getsoldiertex(team),(int)x-w/2,(int)y,w,h);
		}
	}
}
