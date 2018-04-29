package com.hexabeast.riskisep.gameboard;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hexabeast.riskisep.GameScreen;
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
	public int mvtactuel = mvt;
	
	public static float graphicmoveslow = 2;
	public static float graphicmovefast = 300;
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
		if(x<-999 || y<-999)
		{
			randomizePos();
			this.x=fx;
			this.y=fy;
		}
		else
		{
			this.x=x;
			this.y=y;
			this.fx=x;
			this.fy=y;
		}
	}
	
	public void randomizePos()
	{
		Vector2 pos = GameScreen.apays.pays.get(pays).getRandomPos();
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
		if(id>=0)mvtactuel=mvt;
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
			if(!GameMaster.noTransition)
			{
				float movespeed = graphicmovefast;
				if(!running)movespeed = graphicmoveslow;
				if(running && GameScreen.master.human[GameScreen.master.teamactuel]==0)movespeed = graphicmovefast*5;
				Vector2 distance = new Vector2(this.fx-this.x, this.fy-this.y);
				Vector2 velocity = new Vector2(distance);
				if(velocity.len()>20 || !running)velocity.nor();
				else {
					velocity.scl(1f/20f);
				}
				
				x+=movespeed*velocity.x*Main.delta;
				y+=movespeed*velocity.y*Main.delta;
				
				if(distance.len()<3)randomizePosChill();
			}
			else
			{
				x=fx;
				y=fy;
			}
			
				
		
			if(GameScreen.apays.pays.get(pays)==GameScreen.apays.selection)
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
		//Shaders.setDefaultShader();
		
	}
	
	public void updatePoints()
	{
		float ballsize=6;
		if(mvtactuel==1)
			{
			if(this.type==2)Main.batch.draw(TextureManager.tex.get("movepoint"), x-3-ballsize/2, y-centery+th, ballsize,ballsize);
			else Main.batch.draw(TextureManager.tex.get("movepoint"), x-ballsize/2, y-centery+th+2, ballsize,ballsize);
			}
		if(mvtactuel==2)
			{
			Main.batch.draw(TextureManager.tex.get("movepoint"), x-5-ballsize/2, y-centery+th+2, ballsize,ballsize);
			Main.batch.draw(TextureManager.tex.get("movepoint"), x+5-ballsize/2, y-centery+th+2, ballsize,ballsize);
			}
		if(mvtactuel==3)
			{
			Main.batch.draw(TextureManager.tex.get("movepoint"), x-ballsize/2, y-centery+th+2, ballsize,ballsize);
			Main.batch.draw(TextureManager.tex.get("movepoint"), x-10-ballsize/2, y-centery+th, ballsize,ballsize);
			Main.batch.draw(TextureManager.tex.get("movepoint"), x+10-ballsize/2, y-centery+th, ballsize,ballsize);
			}
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
		if(!dead)return TextureManager.isPixTouched(x-tw/2,y-centery,tw,th,tex,pixmap,xx,yy);
		return false;
	}
}
