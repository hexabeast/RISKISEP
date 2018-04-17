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
	
	public float fx;
	public float fy;
	
	public float h = 28;
	public float w;
	
	public float centery = 15;
	
	public int scoreactuel = 0;
	public int mvtactuel = 0;
	
	public static float graphicmoveslow = 2;
	public static float graphicmovefast = 150;
	public boolean running = false;
	
	public boolean dead=false;
	public float transparency = 1;
	
	float tw = w;
	float th = h;
	
	public Unite(int id, int pays, int team)
	{
		this(id, pays, team, 0, 0);
		if(pays>=0)randomizePos();
		this.x=this.fx;
		this.y=this.fy;
    }
	
	public Unite(int id, int pays, int team, float x, float y)
	{
		this.id=id;
		this.pays=pays;
		this.team=team;
		this.fx=x;
		this.fy=y;
		this.x=this.fx;
		this.y=this.fy;
    }
	
	public void setPos(float x, float y)
	{
		this.x=x;
		this.y=y;
		this.fx=x;
		this.fy=y;
	}
	
	public void randomizePos()
	{
		Vector2 pos = AllPays.pays.get(pays).getRandomPos();
		this.fx = pos.x;
		this.fy = pos.y;
		running=true;
	}
	
	public void randomizePosChill()
	{
		randomizePos();
		running=false;
	}
	
	public void init()
	{
		w = h*tex.getWidth()/tex.getHeight();
		tw=w;
		pixmap = TextureManager.unitePixmap.get(type);
	}
	
	public void update(int type)
	{
		tw = w;
		th = h;

		if(type<=0)Shaders.setSoldierTeamShader(team,transparency);
		else if(type==1)Shaders.setWhiteTeam(transparency);
		
		if(type>=0)
		{
			float movespeed = graphicmovefast;
			if(!running)movespeed = graphicmoveslow;
			Vector2 distance = new Vector2(this.fx-this.x, this.fy-this.y);
			Vector2 velocity = new Vector2(distance);
			if(velocity.len()>20 || !running)velocity.nor();
			else {
				velocity.scl(1f/20f);
			}
			x+=movespeed*velocity.x*Main.delta;
			y+=movespeed*velocity.y*Main.delta;
			
			if(distance.len()<3)randomizePosChill();
				
		
			if(AllPays.pays.get(pays)==AllPays.selection)
			{
				tw*=1.05f;
				th*=1.05f;
			}
			
			if(dead)
			{
				transparency-=1*Main.delta;
				if(transparency<0)transparency=0;
				float sizechange = -(transparency-1)/2 + 1;
				
				tw*=sizechange;
				th*=sizechange;
			}
		}
		
		Main.batch.draw(tex, x-tw/2, y-centery, tw,th);
		Shaders.setDefaultShader();
	}
	
	public void update()
	{
		update(0);
	}
	
	public void updatehighlight()
	{
		update(1);
	}
	
	public boolean isTouched(float xx, float yy)
	{
		return TextureManager.isPixTouched(x-tw/2,y-centery,tw,th,tex,pixmap,xx,yy);
	}
}
