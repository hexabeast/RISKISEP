package com.hexabeast.riskisep.gameboard;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hexabeast.riskisep.Main;
import com.hexabeast.riskisep.ressources.Shaders;
import com.hexabeast.riskisep.ressources.TextureManager;

public abstract class Unite {
	public int id;
	public int team = 0;
	public int pays = 0;
	public int type=-1;
	public int cout = 0;
	public int mvt = 0;
	public int att = 0;
	public int def = 0;
	public int puissance = 0;
	public Texture tex;
	public Pixmap pixmap;
	
	public float x;
	public float y;
	
	public float h = 50;
	public float w;
	
	public float centery = 15;
	
	public int scoreactuel = 0;
	public int mvtactuel = 0;
	
	public Unite(int id, int pays, int team)
	{
		this.id=id;
		this.pays=pays;
		this.team=team;
		Vector2 pos = AllPays.pays.get(pays).getRandomPos();
		this.x = pos.x;
		this.y = pos.y;
    }
	
	public void init()
	{
		w = h*tex.getWidth()/tex.getHeight();
		pixmap = TextureManager.unitePixmap.get(type);
	}
	
	public void update()
	{
		Shaders.setSoldierTeamShader(team);
		Main.batch.draw(tex, x-w/2, y-centery, w,h);
		Shaders.setDefaultShader();
	}
	
	public boolean isTouched(float xx, float yy)
	{
		return TextureManager.isPixTouched(x,y,w,h,tex,pixmap,xx,yy);
	}
}
