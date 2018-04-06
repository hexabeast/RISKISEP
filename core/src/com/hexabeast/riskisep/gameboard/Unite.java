package com.hexabeast.riskisep.gameboard;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hexabeast.riskisep.Main;

public abstract class Unite {
	public int team = 0;
	public int pays = 0;
	public int type=-1;
	public int cout = 0;
	public int mvt = 0;
	public int att = 0;
	public int def = 0;
	public int puissance = 0;
	public Texture tex;
	
	public float x;
	public float y;
	
	public float h = 50;
	public float w;
	
	public float centery = 15;
	
	public Unite(int pays, int team)
	{
		this.pays=pays;
		this.team=team;
		Vector2 pos = AllPays.pays.get(pays).getRandomPos();
		this.x = pos.x;
		this.y = pos.y;
    }
	
	public void init()
	{
		w = h*tex.getWidth()/tex.getHeight();
	}
	
	public void update()
	{
		Main.batch.draw(tex, x-w/2, y-centery, w,h);
	}
}
